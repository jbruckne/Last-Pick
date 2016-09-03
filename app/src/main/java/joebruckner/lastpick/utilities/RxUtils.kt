package joebruckner.lastpick.utils

import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

fun <T> applySchedulers(): Observable.Transformer<T, T> {
    return Observable.Transformer { observable ->
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}