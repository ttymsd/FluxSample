package jp.bglb.bonboru.fluxsample.flux

import jp.bglb.bonboru.flux.action.ActionData

/**
 * Created by Tetsuya Masuda on 2016/12/24.
 */
open class MyActionData<T>(type: MyActionType, data: T) : ActionData<T, MyActionType>(data, type)
