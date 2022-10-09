package com.thebengalstudio.tbsauth;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import static com.thebengalstudio.authentication.Service.LogInCheck.*;
import com.thebengalstudio.authentication.SignUpProcess.*;


public class MainActivity extends AppCompatActivity {

    Context context = MainActivity.this;

    int logInReqCode = 200;
    int logOutReqCode = 100;

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

            if (requestCode == logInReqCode) {;

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


            }else if (requestCode == logOutReqCode){

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