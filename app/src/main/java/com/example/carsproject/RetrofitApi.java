package com.example.carsproject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface RetrofitApi {
    @POST("Cars")
    Call<Cars> createPost(@Body Cars dataModal);
    @DELETE("Cars/")
    Call<Cars> createDelete(@Query("id") int id);
    @PUT("Cars/")
    Call<Cars> createPut(@Body Cars dataModal, @Query("ID") int id);}
