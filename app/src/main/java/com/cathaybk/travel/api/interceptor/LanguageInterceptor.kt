package com.cathaybk.travel.api.interceptor

import com.cathaybk.travel.api.annotation.LanguageSupport
import com.cathaybk.travel.storage.LanguageDataStore
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.annotation.Factory
import retrofit2.Invocation

@Factory
class LanguageInterceptor(
    private val languageDataStore: LanguageDataStore,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (request.method.equals("GET", ignoreCase = true).not()) {
            return chain.proceed(request)
        }

        val method = request.tag(Invocation::class.java)?.method()
        if (method == null || method.getAnnotation(LanguageSupport::class.java) == null) {
            return chain.proceed(request)
        }
        val currentLang = runBlocking { languageDataStore.getLanguage() }

        val originalUrl = request.url
        val firstPathSegment = originalUrl.pathSegments.firstOrNull() ?: ""
        val newPathSegments =
            listOf(firstPathSegment, currentLang) + originalUrl.pathSegments.drop(1)

        val newUrl = originalUrl.newBuilder()
            .encodedPath("/" + newPathSegments.joinToString("/"))
            .build()

        return chain.proceed(request.newBuilder().url(newUrl).build())
    }
}