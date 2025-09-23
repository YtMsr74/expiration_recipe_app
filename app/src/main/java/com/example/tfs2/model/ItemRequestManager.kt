package com.example.tfs2.model

import com.example.tfs2.view.listener.ItemInfoListener
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

class ItemRequestManager {
    private val retrofit: Retrofit by lazy {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val newRequest = originalRequest.newBuilder()
                    .header("User-Agent", "TFS2/1.0 (danil.portenko@gmail.com)")
                    .build()
                chain.proceed(newRequest)
            }
            .build()

        Retrofit.Builder()
            .baseUrl("https://world.openfoodfacts.net/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    fun getItemByCode(listener: ItemInfoListener, code: String) {
        val callItemInfo: CallItemInfo = retrofit.create(CallItemInfo::class.java)
        val call: Call<ItemCodeResponse> = callItemInfo.callItemInfo(code)

        call.enqueue(object: Callback<ItemCodeResponse> {
            override fun onResponse(
                call: Call<ItemCodeResponse>,
                response: Response<ItemCodeResponse>
            ) {
                if (!response.isSuccessful) {
                    listener.didError(response.message())
                    return
                }
                listener.didFetch(response.body(), response.message())
            }

            override fun onFailure(call: Call<ItemCodeResponse>, t: Throwable) {
                t.message?.let { listener.didError(it) }
            }
        })
    }

    private interface CallItemInfo {
        @GET("api/v2/product/{code}.json")
        fun callItemInfo(@Path("code") code: String): Call<ItemCodeResponse>
    }
}