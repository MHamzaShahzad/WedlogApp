package com.example.wedlogapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.xmlpull.v1.XmlPullParserException;

import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class Common {

    public static void volleyErrorFormatter(Context mContext, VolleyError error) {
        if (error instanceof NoConnectionError) {
            ConnectivityManager cm = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = null;
            if (cm != null) {
                activeNetwork = cm.getActiveNetworkInfo();
            }
            if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                Toast.makeText(mContext, "Server is not connected to internet.",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "Your device is not connected to internet.",
                        Toast.LENGTH_SHORT).show();
            }
        } else if (error instanceof NetworkError || error.getCause() instanceof ConnectException
                || (error.getCause() != null && error.getCause().getMessage() != null
                && error.getCause().getMessage().contains("connection"))) {
            Toast.makeText(mContext, "Your device is not connected to internet.",
                    Toast.LENGTH_SHORT).show();
        } else if (error.getCause() instanceof MalformedURLException) {
            Toast.makeText(mContext, "Bad Request.", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError || error.getCause() instanceof IllegalStateException
                || error.getCause() instanceof JSONException
                || error.getCause() instanceof XmlPullParserException) {
            Toast.makeText(mContext, "Parse Error (because of invalid json or xml).",
                    Toast.LENGTH_SHORT).show();
        } else if (error.getCause() instanceof OutOfMemoryError) {
            Toast.makeText(mContext, "Out Of Memory Error.", Toast.LENGTH_SHORT).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(mContext, "server couldn't find the authenticated request.",
                    Toast.LENGTH_SHORT).show();
        } else if (error instanceof ServerError || error.getCause() instanceof ServerError) {
            Toast.makeText(mContext, "Server is not responding.", Toast.LENGTH_SHORT).show();
        } else if (error instanceof TimeoutError || error.getCause() instanceof SocketTimeoutException
                || error.getCause() instanceof ConnectTimeoutException
                || error.getCause() instanceof SocketException
                || (error.getCause().getMessage() != null
                && error.getCause().getMessage().contains("Connection timed out"))) {
            Toast.makeText(mContext, "Connection timeout error",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "An unknown error occurred.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
