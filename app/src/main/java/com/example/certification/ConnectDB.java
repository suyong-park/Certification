package com.example.certification;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ConnectDB {

    String IP_ADDRESS = "http://15.164.49.117/certifi/";

    @POST("certification_category_data.php")
    Call<List<Recycler_category>> certification_category_data();

    @POST("certification_data.php")
    Call<List<Recycler_certification>> certification_data();

    @FormUrlEncoded
    @POST("search.php")
    Call<List<Recycler_onething>> search(
            @Field("query") String query
    );
}
