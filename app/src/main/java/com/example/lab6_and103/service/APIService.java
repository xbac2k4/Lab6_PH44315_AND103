package com.example.lab6_and103.service;

import com.example.lab6_and103.Model.Distributor;
import com.example.lab6_and103.Model.Fruit;
import com.example.lab6_and103.Model.Response;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface APIService {
//    192.168.1.118 10.24.16.121 192.168.100.6
    String ipv4 = "192.168.1.118";
    String DOMAIN = "http://"+ ipv4 +":3000/api/";

    @GET("get-distributor")
    Call<Response<ArrayList<Distributor>>> getDistributor();
    @GET("get-fruit")
    Call<Response<ArrayList<Fruit>>> getFruits();
    @DELETE("delete-fruit-by-id/{id}")
    Call<Response<Void>> deleteFruit(@Path("id") String id);
    //lab 6
    @Multipart
    @POST("add-fruit-with-file-image")
    Call<Response<Fruit>> addFruitWithFileImage(@PartMap Map<String, RequestBody> requestBodyMap,
                                                @Part ArrayList<MultipartBody.Part> ds_hinh);
    @Multipart
    @PUT("update-fruit-by-id/{id}")
    Call<Response<Void>> updateFruitWithFileImage(@Path("id") String id,
                                                  @PartMap Map<String, RequestBody> requestBodyMap,
                                                  @Part ArrayList<MultipartBody.Part> ds_hinh);
    @GET("get-fruit-authenticate-token")
    Call<Response<ArrayList<Fruit>>> getFruitToken(@Header("Authorization") String token);
}
