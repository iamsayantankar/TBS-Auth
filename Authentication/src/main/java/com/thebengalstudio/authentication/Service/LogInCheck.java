package com.thebengalstudio.authentication.Service;

import static com.thebengalstudio.authentication.Service.API.*;
import static com.thebengalstudio.authentication.Service.SharePref.*;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.thebengalstudio.authentication.R;
import com.thebengalstudio.beautifultoast.BeautifulToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LogInCheck {

    private static Context context;
    private static Activity activity;
    private static boolean returnValue = true;


    public static boolean LogInCheck(Context ib_blockcontext){
        context = ib_blockcontext;
        if(((LoadSharePref(context, "auth_uid").isEmpty()) || (LoadSharePref(context, "auth_uid").equals("null")))){
            returnValue = false;
            return returnValue;
        }else{
            app_uid = LoadSharePref(context, "app_uid");
            app_passcode = LoadSharePref(context, "app_passcode");
            auth_uid = LoadSharePref(context, "auth_uid");
            auth_password = LoadSharePref(context, "auth_password");
            verify_password = LoadSharePref(context, "verify_password");
            if (!network.getInstance(context).isOnline()) {
                returnValue = true;
                return returnValue;
            }
            StringRequest createLogIn = new StringRequest(Request.Method.POST, login_check_api,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONObject jsonObject = new JSONObject(response);

                                String success = jsonObject.getString("code");
                                String message = jsonObject.getString("message");

                                if (success.equals("200")) {
                                    returnValue = true;

                                } else  {
                                    returnValue = false;
                                    LogOut(ib_blockcontext);
                                    BeautifulToast.makeText(context, message, Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                                }

                            } catch (JSONException e) {
                                returnValue = false;
                                LogOut(ib_blockcontext);

                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null) {
                        BeautifulToast.makeText(context, "No Internet found...", Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                    } else {
                        BeautifulToast.makeText(context, "Internal server error...", Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                    }

                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<String, String>();

                    params.put("app_passcode", app_passcode);
                    params.put("app_uid", app_uid);
                    params.put("auth_uid", auth_uid);
                    params.put("auth_password", auth_password);
                    params.put("verify_password", verify_password);
                    return params;
                }
            };

            RequestQueue requestQueue_createLogIn = Volley.newRequestQueue(context);
            requestQueue_createLogIn.add(createLogIn);

        }
        return returnValue;
    }

    private static String app_uid, app_passcode, auth_uid,auth_password,verify_password;


    public static boolean LogOut(Context ib_blockcontext){
        context = ib_blockcontext;
        app_uid = LoadSharePref(context, "app_uid");
        app_passcode = LoadSharePref(context, "app_passcode");
        auth_uid = LoadSharePref(context, "auth_uid");
        auth_password = LoadSharePref(context, "auth_password");
        verify_password = LoadSharePref(context, "verify_password");
        if(((LoadSharePref(context, "auth_uid").isEmpty()) || (LoadSharePref(context, "auth_uid").equals("null")))){
            return false;
        }else{
            StringRequest createLogIn = new StringRequest(Request.Method.POST, logout_api,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONObject jsonObject = new JSONObject(response);

                                String success = jsonObject.getString("code");
                                String message = jsonObject.getString("message");

                                if (success.equals("200")) {

                                    //finishing activity

                                } else if (success.equals("201")) {

                                } else if (success.equals("202")) {

                                } else if (success.equals("404") || success.equals("401")) {

                                }

                            } catch (JSONException e) {

                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null) {
                        BeautifulToast.makeText(context, "No Internet found...", Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                    } else {
                        BeautifulToast.makeText(context, "Internal server error...", Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                    }
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<String, String>();

                    params.put("app_passcode", app_passcode);
                    params.put("app_uid", app_uid);
                    params.put("auth_uid", auth_uid);
                    params.put("auth_password", auth_password);
                    params.put("verify_password", verify_password);
                    return params;
                }
            };

            RequestQueue requestQueue_createLogIn = Volley.newRequestQueue(context);
            requestQueue_createLogIn.add(createLogIn);
        }
        RemoveSharePref(context);
        return true;

    }


    public static boolean LogOutAll(Context ib_blockcontext){
        context = ib_blockcontext;
        app_uid = LoadSharePref(context, "app_uid");
        app_passcode = LoadSharePref(context, "app_passcode");
        auth_uid = LoadSharePref(context, "auth_uid");
        auth_password = LoadSharePref(context, "auth_password");
        verify_password = LoadSharePref(context, "verify_password");
        if(((LoadSharePref(context, "auth_uid").isEmpty()) || (LoadSharePref(context, "auth_uid").equals("null")))){
            return false;
        }else{
            StringRequest createLogIn = new StringRequest(Request.Method.POST, logout_all_api,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONObject jsonObject = new JSONObject(response);

                                String success = jsonObject.getString("code");
                                String message = jsonObject.getString("message");

                                if (success.equals("200")) {

                                    //finishing activity

                                } else if (success.equals("201")) {

                                } else if (success.equals("202")) {

                                } else if (success.equals("404") || success.equals("401")) {

                                }

                            } catch (JSONException e) {

                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null) {
                        BeautifulToast.makeText(context, "No Internet found...", Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                    } else {
                        BeautifulToast.makeText(context, "Internal server error...", Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                    }
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<String, String>();

                    params.put("app_passcode", app_passcode);
                    params.put("app_uid", app_uid);
                    params.put("auth_uid", auth_uid);
                    params.put("auth_password", auth_password);
                    params.put("verify_password", verify_password);
                    return params;
                }
            };

            RequestQueue requestQueue_createLogIn = Volley.newRequestQueue(context);
            requestQueue_createLogIn.add(createLogIn);
        }
        RemoveSharePref(context);
        return true;

    }



}
