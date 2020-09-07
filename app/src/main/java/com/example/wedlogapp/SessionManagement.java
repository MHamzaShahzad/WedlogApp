package com.example.wedlogapp;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagement {

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private final String PREFERENCE = "app_preferences";
    private final String TOKEN = "token";
    private final String USERNAME = "username";

    public SessionManagement(Context context){
        sharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }



    public void setUserName(String username){
        editor.putString(USERNAME, username).apply();
    }

    public void setToken(String token){
        editor.putString(TOKEN, token).apply();
    }

    public String getToken(){
        return sharedPreferences.getString(TOKEN, null);
    }

    public String getUserName(){
        return sharedPreferences.getString(USERNAME, null);
    }

    public void clearPreferences(){
        editor.clear().apply();
    }

}
