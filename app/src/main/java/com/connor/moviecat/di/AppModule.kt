package com.connor.moviecat.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.connor.moviecat.BuildConfig
import com.connor.moviecat.model.DetailRepository
import com.connor.moviecat.model.Repository
import com.connor.moviecat.model.room.MovieDataBase
import com.connor.moviecat.viewmodel.DetailViewModel
import com.connor.moviecat.viewmodel.MainViewModel
import com.drake.net.okhttp.setDebug
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.compression.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import okhttp3.Cache
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val appModule = module {

    single { MovieDataBase.getDataBase(androidApplication()) }
    single { get<MovieDataBase>().moviesDao() }

    single { Repository(get()) }
    single { DetailRepository(get(), get()) }
    single { client(get()) }
    //single { (path: String) -> RepoPagingSource(get(), path) }
    viewModel { MainViewModel(get()) }
    viewModel { DetailViewModel(get()) }

}

fun client(context: Context) = HttpClient(OkHttp) {
    defaultRequest {
        url {
            protocol = URLProtocol.HTTPS
            host = "api.themoviedb.org"
            path("3/")
            parameters.append(ApiKey.API_KEY, ApiKey.API_KEY_VALUE)
           // parameters.append("language", "en")
        }
    }
    engine {
        threadsCount = 16
        clientCacheSize = 1024 * 1204 * 128
        config {
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            cache(Cache(context.cacheDir, 1024 * 1204 * 128))
            setDebug(BuildConfig.DEBUG)
           // addInterceptor(LogRecordInterceptor(BuildConfig.DEBUG))
           // cookieJar(PersistentCookieJar(context))
            if (BuildConfig.DEBUG) {
                addInterceptor(
                    ChuckerInterceptor.Builder(context)
                        .collector(ChuckerCollector(context))
                        .maxContentLength(250000L)
                        .redactHeaders(emptySet())
                        .alwaysReadResponseBody(false)
                        .build()
                )
            }
        }
    }
    install(ContentEncoding) {
        deflate(1.0F)
        gzip(0.9F)
    }
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            prettyPrint = true
            isLenient = true
        })
    }
}