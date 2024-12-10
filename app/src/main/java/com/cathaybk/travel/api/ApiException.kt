package com.cathaybk.travel.api

open class ApiException(override val message: String? = null) : Exception(message)
class BadRequest(override val message: String?) : ApiException(message)
class Unauthorized(override val message: String?) : ApiException(message)
class Forbidden(override val message: String?) : ApiException(message)
class NotFound(override val message: String?) : ApiException(message)
class InternalServerError(override val message: String?) : ApiException(message)
class ResponseBodyNull(override val message: String?) : ApiException(message)
class ConnectionError(override val message: String?) : ApiException(message)
class ConnectionTimeout(override val message: String?) : ApiException(message)
class Unknown(override val message: String?, val code: Int) : ApiException(message)