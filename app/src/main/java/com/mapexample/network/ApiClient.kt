package com.mapexample.network


import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.jetbrains.annotations.NotNull
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiClient {

    @NotNull
    private var retrofit:Retrofit

    init {
        val gson = GsonBuilder()
                .setLenient()
                .create()

        val clientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder.addInterceptor(loggingInterceptor)

        retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }

    companion object {
        private const val BASE_URL = "https://fake-poi-api.mytaxi.com"
        private var retrofit: Retrofit? = null
        val instance: Retrofit get() {
                if (retrofit == null) {
                    retrofit = ApiClient().retrofit
                }
                return retrofit!!
            }
    }
}
