package jp.bglb.bonboru.fluxsample.util

import rx.Observable
import rx.Observable.Transformer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by Tetsuya Masuda on 2016/12/24.
 */
class ScheduleTransformer<T>() : Transformer<T, T> {

  override fun call(t: Observable<T>): Observable<T> {
    return t.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
  }
}
