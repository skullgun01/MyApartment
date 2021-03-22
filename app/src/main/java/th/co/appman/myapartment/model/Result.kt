package th.co.appman.myapartment.model

sealed class Result<out R> {
    data class Success<out T>(val data: T): Result<T>()
    data class Fail(val exception: String): Result<Nothing>()
}