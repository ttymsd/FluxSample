package jp.bglb.bonboru.fluxsample.flux

import jp.bglb.bonboru.flux.kotlin.Dispatcher
import jp.bglb.bonboru.flux.kotlin.middleware.Middleware
import jp.bglb.bonboru.flux.reducer.Reducer
import jp.bglb.bonboru.flux.store.Store
import jp.bglb.bonboru.fluxsample.flux.MyActionType

/**
 * Created by Tetsuya Masuda on 2016/12/24.
 */
class MyDispatcher<T>(reducer: Reducer<T, MyActionType>, store: Store<T>, vararg middlewares: Middleware<T, MyActionType> = arrayOf()) : Dispatcher<T, MyActionType>(
    reducer, store, *middlewares)
