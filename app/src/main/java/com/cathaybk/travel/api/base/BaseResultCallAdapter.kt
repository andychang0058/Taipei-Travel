package com.cathaybk.travel.api.base

import com.cathaybk.travel.api.BadRequest
import com.cathaybk.travel.api.ConnectionError
import com.cathaybk.travel.api.ConnectionTimeout
import com.cathaybk.travel.api.Forbidden
import com.cathaybk.travel.api.InternalServerError
import com.cathaybk.travel.api.NotFound
import com.cathaybk.travel.api.ResponseBodyNull
import com.cathaybk.travel.api.Unauthorized
import com.cathaybk.travel.api.Unknown
import com.cathaybk.travel.extensions.cloneString
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.reflect.Type
import java.net.SocketTimeoutException
import kotlin.text.orEmpty

abstract class BaseResultCallAdapter<T>(
    private val responseType: Type,
) : CallAdapter<T, Call<Result<T>>> {

    abstract fun convertErrorBody(
        code: Int,
        errorString: String,
        message: String? = null,
    ): Exception?

    final override fun responseType() = responseType

    final override fun adapt(call: Call<T>): Call<Result<T>> {
        /*
         If we use a suspend fun in an interface, Retrofit will automatically convert the response
         to Response<T>, so we need to wrap it into Result<T> manually.
         */
        return object : Call<Result<T>> {
            override fun enqueue(callback: Callback<Result<T>>) {
                val newCall = this
                call.enqueue(object : Callback<T> {
                    override fun onResponse(call: Call<T>, response: Response<T>) {
                        val resultBody = if (response.isSuccessful) {
                            convertSuccessBody(response)
                        } else {
                            convertErrorBody(response)
                        }
                        return callback.onResponse(newCall, Response.success(resultBody))
                    }

                    override fun onFailure(call: Call<T>, t: Throwable) {
                        val finalThrowable = when (t) {
                            is SocketTimeoutException -> ConnectionTimeout(t.message)
                            is IOException -> ConnectionError(t.message)
                            else -> t
                        }
                        callback.onResponse(
                            newCall,
                            Response.success(Result.failure(finalThrowable))
                        )
                    }
                })
            }

            override fun isExecuted() = call.isExecuted

            override fun clone() = this

            override fun isCanceled() = call.isCanceled

            override fun cancel() = call.cancel()

            override fun execute(): Response<Result<T>> {
                val response = call.execute()
                return Response.success(
                    if (response.isSuccessful) {
                        convertSuccessBody(response)
                    } else {
                        convertErrorBody(response)
                    }
                )
            }

            override fun request() = call.request()

            override fun timeout() = call.timeout()
        }
    }

    private fun convertSuccessBody(response: Response<T>): Result<T> {
        val body: T? = response.body()
        if (body is Unit) {
            return Result.success(Unit as T)
        }

        if (body == null) {
            return Result.failure(
                ResponseBodyNull("Declared type is $responseType but received body is null")
            )
        }

        return Result.success(body)
    }

    private fun convertErrorBody(response: Response<T>): Result<T> {
        val message = response.message()
        val code = response.code()
        val errorBody = response.errorBody()

        val resultErrorBody =
            errorBody?.let { convertErrorBody(code, it.cloneString().orEmpty(), message) }

        if (resultErrorBody != null) {
            return Result.failure(resultErrorBody)
        }

        return when (code) {
            400 -> Result.failure(BadRequest(message))
            401 -> Result.failure(Unauthorized(message))
            403 -> Result.failure(Forbidden(message))
            404 -> Result.failure(NotFound(message))
            500 -> Result.failure(InternalServerError(message))
            else -> Result.failure(Unknown(message, code))
        }
    }
}