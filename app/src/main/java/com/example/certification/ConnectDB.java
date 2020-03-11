package com.example.certification;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;

public interface ConnectDB {

    String IP_ADDRESS = "http://13.124.225.218/certifi/";

    @POST("certification_category_data.php")
    Call<List<Recycler_category>> certification_category_data();

    @POST("certification_data.php")
    Call<List<Recycler_certification>> certification_data();

    @POST("job_category_data.php")
    Call<List<Recycler_category>> job_category_data();

    @POST("job_data.php")
    Call<List<Recycler_job>> job_data();
}
