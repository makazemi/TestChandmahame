package com.chandmahame.testchandmahame.api
import androidx.lifecycle.LiveData
import com.chandmahame.testchandmahame.model.DairyResponse
import com.chandmahame.testchandmahame.model.NotificationResponse
import com.chandmahame.testchandmahame.util.Constant.SECRET_KEY
import com.chandmahame.testchandmahame.util.GenericApiResponse
import retrofit2.http.GET
import retrofit2.http.Header
import javax.inject.Singleton

@Singleton
interface ApiService {

    @GET("5ed788d079382f568bd24eed/1")
    fun getListDairy(
        @Header("secret-key") token: String=SECRET_KEY
    ): LiveData<GenericApiResponse<DairyResponse>>


    @GET("5ed78cd160775a5685875f53")
    suspend fun getNotification(
        @Header("secret-key") token: String=SECRET_KEY
    ):GenericApiResponse<NotificationResponse>
}