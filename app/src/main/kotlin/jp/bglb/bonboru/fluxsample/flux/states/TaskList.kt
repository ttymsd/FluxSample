package jp.bglb.bonboru.fluxsample.flux.states

import jp.bglb.bonboru.flux.data.ObservableList
import jp.bglb.bonboru.flux.processor.annotation.ObservableClass
import jp.bglb.bonboru.flux.processor.annotation.ObservableField
import jp.bglb.bonboru.fluxsample.domain.dtos.Task
import jp.bglb.bonboru.fluxsample.domain.dtos.ValidationError
import jp.bglb.bonboru.fluxsample.domain.dtos.ValidationError.NoError
import jp.bglb.bonboru.fluxutility.dtos.markers.AsyncLoadable
import jp.bglb.bonboru.fluxutility.types.RequestStatus
import jp.bglb.bonboru.fluxutility.types.RequestStatus.STILL

/**
 * Created by Tetsuya Masuda on 2016/12/24.
 */
@ObservableClass
data class TaskList(
    @ObservableField var error: ValidationError = NoError,
    @ObservableField var requestStatus: RequestStatus = STILL,
    @ObservableField var tasks: ObservableList<Task> = ObservableList()
) : AsyncLoadable {

  override fun updateRequestStatus(status: RequestStatus) {
    this.requestStatus = status
  }

}
