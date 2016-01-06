package joebruckner.lastpick

import com.bumptech.glide.DrawableRequestBuilder
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import retrofit.Call
import retrofit.Callback
import retrofit.Response
import retrofit.Retrofit

public infix fun <T> Call<T>.enqueue(callback: (Response<T>, Retrofit) -> Unit) {
    enqueue(object : Callback<T> {
        override fun onResponse(response: Response<T>, retrofit: Retrofit)
                = callback.invoke(response, retrofit)

        override fun onFailure(t: Throwable)
                = t.printStackTrace()
    })
}