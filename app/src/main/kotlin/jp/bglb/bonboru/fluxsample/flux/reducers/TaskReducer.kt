package jp.bglb.bonboru.fluxsample.flux.reducers

import jp.bglb.bonboru.flux.action.ActionData
import jp.bglb.bonboru.flux.data.ObservableList
import jp.bglb.bonboru.flux.reducer.Reducer
import jp.bglb.bonboru.fluxsample.domain.dtos.exceptions.ValidationError
import jp.bglb.bonboru.fluxsample.flux.MyActionType
import jp.bglb.bonboru.fluxsample.flux.MyActionType.ADD_TASK
import jp.bglb.bonboru.fluxsample.flux.MyActionType.DELETE_TASK
import jp.bglb.bonboru.fluxsample.flux.MyActionType.INITIALIZE
import jp.bglb.bonboru.fluxsample.flux.states.TaskList
import jp.bglb.bonboru.fluxutility.types.RequestStatus.STILL

/**
 * Created by Tetsuya Masuda on 2016/12/24.
 */
class TaskReducer() : Reducer<TaskList, MyActionType>() {

  override fun received(state: TaskList, action: ActionData<TaskList, MyActionType>): TaskList {
    return when (action.type) {

      INITIALIZE -> action.data

      ADD_TASK -> {
        val list = ObservableList(state.tasks.list)
        list.addAll(action.data.tasks.list)
        state.tasks = list
        state.requestStatus = STILL
        state
      }

      DELETE_TASK -> {
        val list = ObservableList(state.tasks.list)
        action.data.tasks.list.forEach {
          list.remove(it)
        }
        state.requestStatus = STILL
        state.tasks = list
        state
      }

      else -> state
    }
  }

  override fun onError(state: TaskList, error: Throwable): TaskList {
    if (error is ValidationError) {
      state.error = error.type
      state.requestStatus = STILL
      return state
    } else {
      return state
    }
  }
}
