package com.clinicsanddoctors.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.clinicsanddoctors.data.entity.UserClient;
import com.google.gson.Gson;

/**
 * Created by Daro on 30/08/2017.
 */

public class AppPreference {

    private static final String PREFS_NAME = "ClinicsPrefrence";
    private static final int MODE = Context.MODE_PRIVATE;

    private static final String USER_CLIENT = "USER_CLIENT";

    public static UserClient getUser(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS_NAME, MODE);
        String json = prefs.getString(USER_CLIENT, "");
        Gson gson = new Gson();
        return gson.fromJson(json, UserClient.class);
    }

    public static void setUser(Context ctx, UserClient userClient) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS_NAME, MODE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userClient);
        editor.putString(USER_CLIENT, json);
        editor.commit();
    }
}
