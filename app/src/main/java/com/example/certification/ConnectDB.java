package com.example.certification;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;

public interface ConnectDB {

    String IP_ADDRESS = "http://13.124.225.218/certifi/";

    @POST("test.php")
    Call<List<Recycler_title>> check_test_data();
}
