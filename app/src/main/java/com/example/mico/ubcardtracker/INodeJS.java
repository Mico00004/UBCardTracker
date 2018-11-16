package com.example.mico.ubcardtracker;


import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface INodeJS {
    @POST("SendMessage")
    @FormUrlEncoded
    Observable<String> sendMessage(@Field("address") String address,
                                   @Field("amount") String amount,
                                   @Field("message") String message);
}
