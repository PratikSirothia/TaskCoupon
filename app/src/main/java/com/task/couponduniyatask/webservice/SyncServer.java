package com.task.couponduniyatask.webservice;

import android.content.Context;

import com.task.couponduniyatask.utility.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import retrofit.RestAdapter;
import retrofit.client.Response;


public class SyncServer {

    private static final String TAG = "SyncServer";
    private Context context;

    public SyncServer(Context context) {
        this.context = context;

    }


    public String getRestaurantData() throws Exception {
        StringBuilder sb = new StringBuilder();
        try {

            RestAdapter restAdapter = Utils.getRestAdaptor(Utils.BASE_SERVER_URL);
            RestaurantWebService service = restAdapter.create(RestaurantWebService.class);

            Response response = service.getRestaurantList();
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody()
                    .in()));

            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            throw e;
        }


        return sb.toString();
    }


}
