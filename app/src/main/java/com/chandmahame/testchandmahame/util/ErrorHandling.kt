package com.chandmahame.testchandmahame.util


class ErrorHandling{

    companion object{
        const val UNABLE_TO_RESOLVE_HOST = "Unable to resolve host"
        const val UNABLE_TODO_OPERATION_WO_INTERNET = "اتصال به اینترنت برقرار نمی باشد!"
        const val ERROR_CHECK_NETWORK_CONNECTION = "اتصال به اینترنت خود را چک کنید."
        const val ERROR_UNKNOWN = "خطای نامشخص"
        const val ERROR_SERVER_CONNECTION="خطا در ارتباط با سرور"


        fun isNetworkError(msg: String): Boolean = msg.contains(UNABLE_TO_RESOLVE_HOST)
    }

}