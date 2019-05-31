package com.example.biologic;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

interface GeoportalConnect {
    @GET("/dataset/list")
    Call<ResponseBody> getStruct(@Query("f") String f, @Query("iDisplayStart") int iDisplayStart, @Query("iDisplayLength") int iDisplayLength, @Query("s_fields") String s_fields, @Query("f_id") String f_id);
    @GET("/dataset/list")
    Call<ResponseBody> getData(@Query("f") String f, @Query("iDisplayStart") int iDisplayStart, @Query("iDisplayLength") int iDisplayLength, @Query("s_fields") String s_fields);
    @FormUrlEncoded
    @POST("/dataset/add")
    Call<ResponseBody> sendData(@Field("f") String f, @Field("document") String document);
    @FormUrlEncoded
    @POST("/dataset/update")
    Call<ResponseBody> updateData(@Field("f") String f, @Field("document") String document);
    @FormUrlEncoded
    @POST("/dataset/delete")
    Call<ResponseBody> deleteData(@Field("f") String f, @Field("document") String document);
    @FormUrlEncoded
    @POST("/geothemes/login")
    Call<ResponseBody> regIn(@Field("username") String username,@Field("password") String password);
}
