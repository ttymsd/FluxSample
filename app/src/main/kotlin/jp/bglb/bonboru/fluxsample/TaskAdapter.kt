package jp.bglb.bonboru.fluxsample

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.bindView
import jp.bglb.bonboru.fluxsample.TaskAdapter.TaskVH
import jp.bglb.bonboru.fluxsample.domain.dtos.Task
import java.util.ArrayList

/**
 * Created by Tetsuya Masuda on 2016/12/24.
 */
class TaskAdapter(context: Context) : RecyclerView.Adapter<TaskVH>() {

  private val layoutInflater = LayoutInflater.from(context)
  var listener: TaskClickListener? = null
  var taskList: List<Task> = ArrayList()

  private val onClickListener = View.OnClickListener {
    val position = it.getTag(R.string.tag_key_position) as Int
    listener?.onClickListener(taskList[position])
  }

  private val onLongClickListener = View.OnLongClickListener {
    val position = it.getTag(R.string.tag_key_position) as Int
    listener?.onLongClickListener(taskList[position])
    true
  }

  override fun onBindViewHolder(holder: TaskVH, position: Int) {
    val task = taskList[position]
    holder.itemView.setTag(R.string.tag_key_position, position)
    holder.name.text = task.name
  }

  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TaskVH {
    val view = layoutInflater.inflate(R.layout.list_item_task, parent, false)
    view.setOnClickListener(onClickListener)
    view.setOnLongClickListener(onLongClickListener)
    return TaskVH(view)
  }

  override fun getItemCount(): Int = taskList.size

  inner class TaskVH(view: View) : RecyclerView.ViewHolder(view) {
    val name by bindView<TextView>(R.id.name)
  }

  interface TaskClickListener {
    fun onClickListener(task: Task)

    fun onLongClickListener(task: Task)
  }
}
