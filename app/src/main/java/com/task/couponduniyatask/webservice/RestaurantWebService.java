package com.task.couponduniyatask.webservice;


import retrofit.client.Response;
import retrofit.http.GET;

/**
 * Created by getnow on 11/12/15.
 */
public interface RestaurantWebService {
    @GET("/task.txt")
    Response getRestaurantList();
}
