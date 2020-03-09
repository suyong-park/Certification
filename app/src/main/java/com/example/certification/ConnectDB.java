package com.example.certification;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;

public interface ConnectDB {

    String IP_ADDRESS = "http://13.124.225.218/certifi/";

    @POST("title_name.php")
    Call<List<Recycler_certifi>> title_data();

    @POST("detail_certifi.php")
    Call<List<Recycler_certifidetail>> certification_data();

    @POST("job_name.php")
    Call<List<Recycler_job>> category_data();

    @POST("detail_job.php")
    Call<List<Recycler_jobdetail>> job_data();

    @POST("git_parser.php")
    Call<String> parsing_data();
}
