package com.iteach.taxi.api
import com.iteach.taxi.fragment.login.base.LoginModel
import com.iteach.tezyordam.base.BaseResponse
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*
interface Api {
    @POST(Api_urls.LOGIN_URL)
    @FormUrlEncoded
    fun getLogin(
        @Field("username") username: String,
        @Field("password") pass: String): Observable<BaseResponse>


    @GET("/api/order/update")
    fun getupdate(
        @Query("id") id: Int,
        @Query("doctor_id") doctor_id: Int,
        @Query("arrived_at") arrived_at: String,
        @Query("finished_at") finished_at: String,
    ): Observable<BaseResponse>



}

