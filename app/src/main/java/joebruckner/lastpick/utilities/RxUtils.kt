package joebruckner.lastpick.utilities

import rx.Observable.Transformer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

fun <T> applySchedulers(): Transformer<T, T> {
    return Transformer { observable ->
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}