package com.task.couponduniyatask.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;


public class Utils {
    public static final String BASE_SERVER_URL = "http://staging.couponapitest.com";
    private static final String TAG = "Utils";
    private static Toast mToast;

    public static void showLongToast(Context context, String message) {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
        mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        mToast.show();
    }

    public static void showShortToast(Context context, String message) {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
        mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        mToast.show();
    }





    public static boolean isNullString(String object) {
        boolean isValid = false;
        try {
            if (object == null) {
                isValid = true;
            } else {
                if (object.trim().isEmpty()) {
                    isValid = true;
                }

                if (object.trim().equalsIgnoreCase("null")) {
                    isValid = true;
                }

            }
        } catch (Exception e) {
            System.err.println("Error in isNullString:" + e.getMessage());
        }
        return isValid;
    }

    public static boolean isNull(Object object) {
        boolean isValid = false;
        try {
            if (object == null) {
                isValid = true;
            } else {
                if (object.equals("")) {
                    isValid = true;
                }
            }
        } catch (Exception e) {
            System.err.println("Error in isNull:" + e.getMessage());
        }
        return isValid;
    }


    public static boolean isNetwrokAvial(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


    /**
     * @param - server url
     * @return RestAdapter
     */
    public static RestAdapter getRestAdaptor(String serverURL) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL).setEndpoint(serverURL)
                .setClient(new OkClient(new OkHttpClient())).build();
        return restAdapter;
    }

}
