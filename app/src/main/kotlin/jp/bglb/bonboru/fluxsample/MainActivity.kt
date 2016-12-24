package jp.bglb.bonboru.fluxsample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import butterknife.bindView
import jp.bglb.bonboru.flux.data.ObservableList
import jp.bglb.bonboru.fluxsample.domain.dtos.Task
import jp.bglb.bonboru.fluxsample.domain.dtos.ValidationError
import jp.bglb.bonboru.fluxsample.domain.dtos.ValidationError.FieldEmpty
import jp.bglb.bonboru.fluxsample.flux.MyActionType
import jp.bglb.bonboru.fluxsample.flux.MyDispatcher
import jp.bglb.bonboru.fluxsample.flux.actions.TaskActionCreator
import jp.bglb.bonboru.fluxsample.flux.reducers.TaskReducer
import jp.bglb.bonboru.fluxsample.flux.states.TaskList
import jp.bglb.bonboru.fluxsample.flux.states.TaskListStore
import jp.bglb.bonboru.fluxsample.util.ScheduleTransformer
import jp.bglb.bonboru.fluxutility.middlewares.AsyncMiddleware
import jp.bglb.bonboru.fluxutility.types.RequestStatus
import jp.bglb.bonboru.fluxutility.types.RequestStatus.REQUESTING
import rx.lang.kotlin.plusAssign
import rx.subscriptions.CompositeSubscription

/**
 * Created by Tetsuya Masuda on 2016/12/24.
 */
class MainActivity() : AppCompatActivity(), TaskAdapter.TaskClickListener {

  // views
  val progress by bindView<ProgressBar>(R.id.progress)
  val taskList by bindView<RecyclerView>(R.id.task_list)
  val name by bindView<EditText>(R.id.name)
  val add by bindView<Button>(R.id.submit)
  lateinit var adapter: TaskAdapter

  // flux
  val reducer = TaskReducer()
  val store = TaskListStore()
  val dispatcher: MyDispatcher<TaskList> = MyDispatcher(reducer, store,
      AsyncMiddleware<TaskList, MyActionType>())
  val actions = TaskActionCreator()

  lateinit var subscriptions: CompositeSubscription

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    adapter = TaskAdapter(this)
    adapter.listener = this
    taskList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    taskList.adapter = adapter
    add.setOnClickListener {
      val taskName = name.text.toString()
      dispatcher.dispatch(actions.addTask(taskName))
    }
  }

  override fun onStart() {
    super.onStart()
    subscriptions = CompositeSubscription()
    subscriptions += store.error.compose(ScheduleTransformer<ValidationError>())
        .subscribe {
          when (it) {
            FieldEmpty -> Toast.makeText(this, "タスク名が入力されていません", Toast.LENGTH_LONG).show()

            else -> {
            }
          }
        }
    subscriptions += store.requestStatus.compose(ScheduleTransformer<RequestStatus>())
        .subscribe {
          when (it) {
            REQUESTING -> {
              add.isEnabled = false
              progress.visibility = View.VISIBLE
            }

            else -> {
              add.isEnabled = true
              progress.visibility = View.GONE
            }
          }
        }

    subscriptions += store.tasks.compose(ScheduleTransformer<ObservableList<Task>>())
        .subscribe {
          name.setText("")
          val oldList = adapter.taskList
          val diffResult = DiffUtil.calculateDiff(DiffCallback(oldList, it.list), true)
          adapter.taskList = it.list
          diffResult.dispatchUpdatesTo(adapter)
        }

    dispatcher.dispatch(actions.initialize())
  }

  override fun onStop() {
    super.onStop()
    subscriptions.unsubscribe()
  }

  override fun onClickListener(task: Task) {
  }

  override fun onLongClickListener(task: Task) {
    dispatcher.dispatch(actions.deleteTask(task))
  }

  inner class DiffCallback(val oldList: List<Task>, val newList: List<Task>) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
      return oldList[oldItemPosition].name == newList[newItemPosition].name
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
      return oldList[oldItemPosition] == newList[newItemPosition]
    }
  }
}
