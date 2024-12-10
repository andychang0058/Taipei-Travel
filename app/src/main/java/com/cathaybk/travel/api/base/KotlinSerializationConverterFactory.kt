package com.cathaybk.travel.api.base

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.jvm.java

class KotlinSerializationConverterFactory(json: Json) : Converter.Factory() {

    private val delegateFactory: Converter.Factory =
        json.asConverterFactory("application/json".toMediaType())

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        if (getRawType(type) != Result::class.java) {
            return delegateFactory.responseBodyConverter(type, annotations, retrofit)
        }

        val wrappedType = (type as ParameterizedType).actualTypeArguments[0]
        val delegate = delegateFactory.responseBodyConverter(wrappedType, annotations, retrofit)
            ?: return null

        return Converter<ResponseBody, Any> { body ->
            delegate.convert(body)
        }
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        return delegateFactory.requestBodyConverter(
            type,
            parameterAnnotations,
            methodAnnotations,
            retrofit
        )
    }
}