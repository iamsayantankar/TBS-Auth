package com.thebengalstudio.tbsauth;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import static com.thebengalstudio.authentication.Service.API.signup_api;
import static com.thebengalstudio.authentication.Service.LogInCheck.*;
import static com.thebengalstudio.authentication.Service.ProgressDialog.cancel_loader;
import static com.thebengalstudio.authentication.Service.StoreValue.app_passcode;
import static com.thebengalstudio.authentication.Service.StoreValue.app_uid;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.thebengalstudio.authentication.SignUpProcess.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    Context context = MainActivity.this;

    int logInReqCode = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!LogInCheck(context)){
            Intent intent = new Intent(context, LogIn.class);
            intent.putExtra("app_uid", "app_uid0123" );
            intent.putExtra("app_passcode", "app_passcode2352" );
            startActivityForResult(intent,logInReqCode); // Activity is started with requestCode 2
        }else {
            Toast.makeText(context, "sign in", Toast.LENGTH_SHORT).show();
        }

    }


    // Call Back method  to get the Message form other Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK)
        {

            if (requestCode == logInReqCode) {

                String app_passcode = data.getStringExtra("app_passcode");
                String tbs_uid = data.getStringExtra("tbs_uid");
                String app_uid = data.getStringExtra("app_uid");
                String auth_uid = data.getStringExtra("auth_uid");
                String app_packge_name = data.getStringExtra("app_packge_name");
                String app_status = data.getStringExtra("app_status");
                String auth_password = data.getStringExtra("auth_password");
                String verify_password = data.getStringExtra("verify_password");

                String tooo = "app_passcode: "+app_passcode+"\ntbs_uid: "+tbs_uid+"app_uid: "+app_uid+"\nauth_uid: "+auth_uid+
                        "app_packge_name: "+app_packge_name+"\napp_status: "+app_status+
                        "auth_password: "+auth_password+"\nverify_password: "+verify_password;
                Toast.makeText(context, tooo, Toast.LENGTH_SHORT).show();

                StringRequest createSignUp = new StringRequest(Request.Method.POST, "https://auth.skosao.com/get_user_details", response -> {
                    Log.w("MyAuth", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        switch (success) {
                            case "200":
                                String dataS = jsonObject.getString("data");
                                JSONObject jsonDataObject = new JSONObject(dataS);
                                String name =  jsonDataObject.getString("name");
                                String email =  jsonDataObject.getString("email");
                                String username =  jsonDataObject.getString("username");
                                String phone_number =  jsonDataObject.getString("phone_number");

                                String tooo2 = "name: "+name+"\nemail: "+email+"username: "+username+"\nphone_number: "+phone_number;
                                Toast.makeText(context, tooo2, Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {

                }) {
                    @Override
                    protected Map<String, String> getParams() {

                        Map<String, String> params = new HashMap<>();

                        params.put("app_passcode", app_passcode);
                        params.put("app_uid", app_uid);
                        params.put("tbs_uid", tbs_uid);
                        params.put("auth_password", auth_password);
                        params.put("verify_password", verify_password);

                        return params;
                    }
                };
                RequestQueue requestQueue_createLogIn = Volley.newRequestQueue(context);
                requestQueue_createLogIn.add(createSignUp);

            }
        }



    }

    public void logout(View view) {
        if(LogOut(context)){
            Toast.makeText(context, "Log Out Successful", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Already log out", Toast.LENGTH_SHORT).show();
        }
    }

    public void logout_all(View view) {
        if(LogOutAll(context)){
            Toast.makeText(context, "Log Out Successful", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Already log out", Toast.LENGTH_SHORT).show();
        }
    }
}