package com.myfittinglife.equipmentinfoproject.ip

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * @Author LD
 * @Time 2021/11/30 15:20
 * @Describe
 * @Modify
 */
interface IPService {
    @GET
    fun getIP(@Url url: String): Call<ResponseBody>
}