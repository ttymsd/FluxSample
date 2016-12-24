package jp.bglb.bonboru.fluxsample.flux.actions

import jp.bglb.bonboru.fluxsample.domain.dtos.Task
import jp.bglb.bonboru.fluxsample.domain.dtos.exceptions.ValidationError
import jp.bglb.bonboru.fluxsample.flux.MyActionData
import jp.bglb.bonboru.fluxsample.flux.MyActionType
import jp.bglb.bonboru.fluxsample.flux.states.TaskList
import java.util.concurrent.TimeUnit

/**
 * Created by Tetsuya Masuda on 2016/12/24.
 */
class TaskActionCreator() {

  fun initialize(): () -> MyActionData<TaskList> = {
    Thread.sleep(TimeUnit.SECONDS.toMillis(5))
    val taskList = TaskList()
    taskList.tasks.add(Task("buy milk"))
    taskList.tasks.add(Task("read a book"))
    taskList.tasks.add(Task("study"))
    taskList.tasks.add(Task("watch movie"))
    MyActionData(MyActionType.INITIALIZE, taskList)
  }

  fun addTask(name: String): () -> MyActionData<TaskList> = {
    if ("" == name) {
      throw ValidationError(jp.bglb.bonboru.fluxsample.domain.dtos.ValidationError.FieldEmpty)
    }
    Thread.sleep(TimeUnit.SECONDS.toMillis(5))
    val taskList = TaskList()
    taskList.tasks.add(Task("buy milk"))
    taskList.tasks.add(Task(name))
    MyActionData(MyActionType.ADD_TASK, taskList)
  }

  fun deleteTask(task: Task): () -> MyActionData<TaskList> = {
    Thread.sleep(TimeUnit.SECONDS.toMillis(5))
    val taskList = TaskList()
    taskList.tasks.add(task)
    MyActionData(MyActionType.DELETE_TASK, taskList)
  }

}
