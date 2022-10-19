package com.thebengalstudio.authentication.SignUpProcess;

import static com.thebengalstudio.authentication.Service.API.*;
import static com.thebengalstudio.authentication.Service.ProgressDialog.*;
import static com.thebengalstudio.authentication.Service.SharePref.*;
import static com.thebengalstudio.authentication.Service.StoreValue.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.thebengalstudio.authentication.R;
import com.thebengalstudio.authentication.Service.*;
import com.thebengalstudio.beautifultoast.BeautifulToast;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LogIn extends AppCompatActivity {

    Context context = LogIn.this;

    LinearLayout logInPanel, signUpPanel, forgetPasswordPanel;
    EditText lUserName, lPassword, sName, sPhoneNumber, sEmail, sPassword, sConfirmPassword, fUserName;
    Button lLogIn, lCheck, lSignUp, sSignUp, sCheck, sLogIn, fSendLink, fCheck, fLogIn, fSignUp;
    TextView lResendMail, lForgetPassword, sResendMail, sChangeEmail, fResendMail, fChangeUsername, watch_time;

    String name, email, password, cpassword, phone_number;

    TextView lLinkSendMsg, fLinkSendMsg, sLinkSendMsg;

    String tbs_verify_uid = "", tbs_uid = "";

    int countDownTimer_statusInt = 0;
    CountDownTimer countDownTimer;
    ConstraintLayout time_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Bundle extras = getIntent().getExtras();

        if (extras.getString("app_passcode") != null) {
            app_passcode = extras.getString("app_passcode");
        }

        if (extras.getString("app_uid") != null) {
            app_uid = extras.getString("app_uid");
        }

        time_show = findViewById(R.id.time_show);
        watch_time = findViewById(R.id.watch_time);

        logInPanel = findViewById(R.id.logInPanel);
        signUpPanel = findViewById(R.id.signUpPanel);
        forgetPasswordPanel = findViewById(R.id.forgetPasswordPanel);

        lUserName = findViewById(R.id.lUserName);
        lPassword = findViewById(R.id.lPassword);
        lLinkSendMsg = findViewById(R.id.lLinkSendMsg);
        sEmail = findViewById(R.id.sEmail);
        sName = findViewById(R.id.sName);
        sPassword = findViewById(R.id.sPassword);
        sConfirmPassword = findViewById(R.id.sConfirmPassword);
        sPhoneNumber = findViewById(R.id.sPhoneNumber);
        fLinkSendMsg = findViewById(R.id.fLinkSendMsg);
        fUserName = findViewById(R.id.fUserName);
        sLinkSendMsg = findViewById(R.id.sLinkSendMsg);

        lLogIn = findViewById(R.id.lLogIn);
        lCheck = findViewById(R.id.lCheck);
        lSignUp = findViewById(R.id.lSignUp);
        sSignUp = findViewById(R.id.sSignUp);
        sCheck = findViewById(R.id.sCheck);
        sLogIn = findViewById(R.id.sLogIn);
        fSendLink = findViewById(R.id.fSendLink);
        fCheck = findViewById(R.id.fCheck);
        fLogIn = findViewById(R.id.fLogIn);
        fSignUp = findViewById(R.id.fSignUp);

        lResendMail = findViewById(R.id.lResendMail);
        lForgetPassword = findViewById(R.id.lForgetPassword);
        sResendMail = findViewById(R.id.sResendMail);
        sChangeEmail = findViewById(R.id.sChangeEmail);
        fResendMail = findViewById(R.id.fResendMail);
        fChangeUsername = findViewById(R.id.fChangeUsername);

        time_show.setVisibility(View.GONE);

        logInPanel.setVisibility(View.VISIBLE);
        signUpPanel.setVisibility(View.GONE);
        forgetPasswordPanel.setVisibility(View.GONE);

        lUserName.setVisibility(View.VISIBLE);
        lPassword.setVisibility(View.VISIBLE);
        lLogIn.setVisibility(View.VISIBLE);
        lLinkSendMsg.setVisibility(View.GONE);
        lCheck.setVisibility(View.GONE);
        lResendMail.setVisibility(View.GONE);
        lForgetPassword.setVisibility(View.VISIBLE);

        fUserName.setVisibility(View.VISIBLE);
        fSendLink.setVisibility(View.VISIBLE);
        sLinkSendMsg.setVisibility(View.GONE);
        fCheck.setVisibility(View.GONE);
        fResendMail.setVisibility(View.GONE);
        fChangeUsername.setVisibility(View.GONE);

        sEmail.setVisibility(View.VISIBLE);
        sName.setVisibility(View.VISIBLE);
        sPassword.setVisibility(View.VISIBLE);
        sConfirmPassword.setVisibility(View.VISIBLE);
        sPhoneNumber.setVisibility(View.VISIBLE);
        sSignUp.setVisibility(View.VISIBLE);
        fLinkSendMsg.setVisibility(View.GONE);
        sCheck.setVisibility(View.GONE);
        sResendMail.setVisibility(View.GONE);
        sChangeEmail.setVisibility(View.GONE);

        if (LoadSharePref(context, "auth_uid").isEmpty()) {
            SaveInSharePref(context, "auth_uid", "null");
        }
    }

    public void lLogIn(View view) {
        email = lUserName.getText().toString();
        password = lPassword.getText().toString();
        cpassword = "";
        phone_number = "";

        if (email.isEmpty()) {
            BeautifulToast.makeText(context, "Enter Email", Toast.LENGTH_LONG, R.style.toaststyle_info).show();

        } else if (password.isEmpty()) {
            BeautifulToast.makeText(context, "Enter Password", Toast.LENGTH_LONG, R.style.toaststyle_info).show();

        } else if (!network.getInstance(context).isOnline()) {
            BeautifulToast.makeText(context, "No network connections...", Toast.LENGTH_LONG, R.style.toaststyle_info).show();
            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        } else {
            Show_loader(context);
            StringRequest createLogIn = new StringRequest(Request.Method.POST, login_api, response -> {
                cancel_loader();
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String success = jsonObject.getString("code");
                    String message = jsonObject.getString("message");

                    switch (success) {
                        case "200":
                            String data = jsonObject.getString("data");
                            JSONObject jsonDataObject = new JSONObject(data);

                            BeautifulToast.makeText(context, message, Toast.LENGTH_LONG, R.style.toaststyle_success).show();

                            SaveInSharePref(context, "verify_password", jsonDataObject.getString("verify_password"));
                            SaveInSharePref(context, "auth_password", jsonDataObject.getString("auth_password"));
                            SaveInSharePref(context, "app_status", jsonDataObject.getString("app_status"));
                            SaveInSharePref(context, "app_packge_name", jsonDataObject.getString("app_packge_name"));
                            SaveInSharePref(context, "auth_uid", jsonDataObject.getString("auth_uid"));
                            SaveInSharePref(context, "app_uid", jsonDataObject.getString("app_uid"));
                            SaveInSharePref(context, "tbs_uid", jsonDataObject.getString("tbs_uid"));
                            SaveInSharePref(context, "app_passcode", jsonDataObject.getString("app_passcode"));

                            Intent intent = new Intent();
                            intent.putExtra("app_passcode", jsonDataObject.getString("app_passcode"));
                            intent.putExtra("tbs_uid", jsonDataObject.getString("tbs_uid"));
                            intent.putExtra("app_uid", jsonDataObject.getString("app_uid"));
                            intent.putExtra("auth_uid", jsonDataObject.getString("auth_uid"));
                            intent.putExtra("app_packge_name", jsonDataObject.getString("app_packge_name"));
                            intent.putExtra("app_status", jsonDataObject.getString("app_status"));
                            intent.putExtra("auth_password", jsonDataObject.getString("auth_password"));
                            intent.putExtra("verify_password", jsonDataObject.getString("verify_password"));
                            setResult(RESULT_OK, intent);
                            finish();
                            break;
                        case "201":
                            String response_code = jsonObject.getString("response_code");
                            if (response_code.equals("4")) {

//                                            Send Mail
                                tbs_verify_uid = jsonObject.getString("tbs_verify_uid");
                                tbs_uid = jsonObject.getString("tbs_uid");

                                lUserName.setVisibility(View.GONE);
                                lPassword.setVisibility(View.GONE);
                                lLogIn.setVisibility(View.GONE);
                                lLinkSendMsg.setVisibility(View.VISIBLE);
                                lCheck.setVisibility(View.VISIBLE);
                                lResendMail.setVisibility(View.GONE);
                                lForgetPassword.setVisibility(View.GONE);
                                BeautifulToast.makeText(context, "Verify link send via mail.", Toast.LENGTH_LONG, R.style.toaststyle_success).show();
                                time_show.setVisibility(View.VISIBLE);

                                time_show.setVisibility(View.VISIBLE);
                                countDownTimer = new CountDownTimer(30000, 1000) {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        countDownTimer_statusInt = (int) millisUntilFinished / 1000;
                                        watch_time.setText("Resend mail after " + countDownTimer_statusInt + " Sec");
                                    }

                                    @Override
                                    public void onFinish() {
                                        lResendMail.setVisibility(View.VISIBLE);
                                        time_show.setVisibility(View.GONE);
                                    }
                                }.start();

                            } else {
                                BeautifulToast.makeText(context, message, Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                            }

                            break;
                        case "202":
                            BeautifulToast.makeText(context, message, Toast.LENGTH_LONG, R.style.toaststyle_info).show();
                            break;
                        case "404":
                        case "401":
                            BeautifulToast.makeText(context, message, Toast.LENGTH_LONG, R.style.toaststyle_error).show();

                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    BeautifulToast.makeText(context, "Your data is not loaded", Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                }

            }, error -> {
                cancel_loader();
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    BeautifulToast.makeText(context, "No Internet found...", Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                } else {
                    BeautifulToast.makeText(context, "Internal server error...", Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<>();

                    params.put("app_passcode", app_passcode);
                    params.put("app_uid", app_uid);
                    params.put("email", email);
                    params.put("password", password);
                    return params;
                }
            };
            RequestQueue requestQueue_createLogIn = Volley.newRequestQueue(context);
            requestQueue_createLogIn.add(createLogIn);
        }
    }

    public void lCheck(View view) {
        if (!network.getInstance(context).isOnline()) {
            BeautifulToast.makeText(context, "No network connections...", Toast.LENGTH_LONG, R.style.toaststyle_info).show();
            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        } else {
            Show_loader(context);
            StringRequest createLogIn = new StringRequest(Request.Method.POST, login_verify_api, response -> {
                cancel_loader();
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String success = jsonObject.getString("code");
                    String message = jsonObject.getString("message");

                    switch (success) {
                        case "200":
                            String data = jsonObject.getString("data");
                            JSONObject jsonDataObject = new JSONObject(data);

                            BeautifulToast.makeText(context, message, Toast.LENGTH_LONG, R.style.toaststyle_success).show();


                            SaveInSharePref(context, "verify_password", jsonDataObject.getString("verify_password"));
                            SaveInSharePref(context, "auth_password", jsonDataObject.getString("auth_password"));
                            SaveInSharePref(context, "app_status", jsonDataObject.getString("app_status"));
                            SaveInSharePref(context, "app_packge_name", jsonDataObject.getString("app_packge_name"));
                            SaveInSharePref(context, "auth_uid", jsonDataObject.getString("auth_uid"));
                            SaveInSharePref(context, "app_uid", jsonDataObject.getString("app_uid"));
                            SaveInSharePref(context, "tbs_uid", jsonDataObject.getString("tbs_uid"));
                            SaveInSharePref(context, "app_passcode", jsonDataObject.getString("app_passcode"));

                            Intent intent = new Intent();
                            intent.putExtra("app_passcode", jsonDataObject.getString("app_passcode"));
                            intent.putExtra("tbs_uid", jsonDataObject.getString("tbs_uid"));
                            intent.putExtra("app_uid", jsonDataObject.getString("app_uid"));
                            intent.putExtra("auth_uid", jsonDataObject.getString("auth_uid"));
                            intent.putExtra("app_packge_name", jsonDataObject.getString("app_packge_name"));
                            intent.putExtra("app_status", jsonDataObject.getString("app_status"));
                            intent.putExtra("auth_password", jsonDataObject.getString("auth_password"));
                            intent.putExtra("verify_password", jsonDataObject.getString("verify_password"));
                            setResult(RESULT_OK, intent);
                            finish();
                            //finishing activity

                            break;

                        case "202":
                            BeautifulToast.makeText(context, message, Toast.LENGTH_LONG, R.style.toaststyle_info).show();
                            break;
                        case "201":
                        case "404":
                        case "401":
                            BeautifulToast.makeText(context, message, Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    BeautifulToast.makeText(context, "Your data is not loaded", Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                }

            }, error -> {
                cancel_loader();
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    BeautifulToast.makeText(context, "No Internet found...", Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                } else {
                    BeautifulToast.makeText(context, "Internal server error...", Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<>();

                    params.put("app_passcode", app_passcode);
                    params.put("app_uid", app_uid);
                    params.put("tbs_uid", tbs_uid);
                    params.put("tbs_verify_uid", tbs_verify_uid);

                    return params;
                }
            };
            RequestQueue requestQueue_createLogIn = Volley.newRequestQueue(context);
            requestQueue_createLogIn.add(createLogIn);
        }
    }

    public void lResendMail(View view) {
        if (!network.getInstance(context).isOnline()) {
            BeautifulToast.makeText(context, "No network connections...", Toast.LENGTH_LONG, R.style.toaststyle_info).show();
            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        } else {
            Show_loader(context);
            StringRequest createLogIn = new StringRequest(Request.Method.POST, login_resend_api, response -> {
                cancel_loader();
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("code");
                    String message = jsonObject.getString("message");

                    if (success.equals("200")) {
                        lResendMail.setVisibility(View.GONE);
                        time_show.setVisibility(View.VISIBLE);

                        BeautifulToast.makeText(context, "Verify link send via mail.", Toast.LENGTH_LONG, R.style.toaststyle_success).show();

                        time_show.setVisibility(View.VISIBLE);
                        countDownTimer = new CountDownTimer(30000, 1000) {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onTick(long millisUntilFinished) {
                                countDownTimer_statusInt = (int) millisUntilFinished / 1000;
                                watch_time.setText("Resend mail after " + countDownTimer_statusInt + " Sec");
                            }

                            @Override
                            public void onFinish() {
                                lResendMail.setVisibility(View.VISIBLE);
                                time_show.setVisibility(View.GONE);
                            }
                        }.start();

                    } else if (success.equals("404") || success.equals("401")) {
                        BeautifulToast.makeText(context, message, Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    BeautifulToast.makeText(context, "Your data is not loaded", Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                }
            }, error -> {
                cancel_loader();
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    BeautifulToast.makeText(context, "No Internet found...", Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                } else {
                    BeautifulToast.makeText(context, "Internal server error...", Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<>();

                    params.put("app_passcode", app_passcode);
                    params.put("app_uid", app_uid);
                    params.put("tbs_uid", tbs_uid);
                    params.put("tbs_verify_uid", tbs_verify_uid);

                    return params;
                }
            };
            RequestQueue requestQueue_createLogIn = Volley.newRequestQueue(context);
            requestQueue_createLogIn.add(createLogIn);
        }
    }


    public void lForgetPassword(View view) {
        forgatePasswordShow();
    }

    public void lSignUp(View view) {
        signUpShow();
    }

    public void fSendLink(View view) {
        email = fUserName.getText().toString();
        if (email.isEmpty()) {
            BeautifulToast.makeText(context, "Enter Email", Toast.LENGTH_LONG, R.style.toaststyle_info).show();

        } else if (!network.getInstance(context).isOnline()) {
            BeautifulToast.makeText(context, "No network connections...", Toast.LENGTH_LONG, R.style.toaststyle_info).show();
            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        } else {
            Show_loader(context);
            StringRequest createLogIn = new StringRequest(Request.Method.POST, forget_api, response -> {
                cancel_loader();
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String success = jsonObject.getString("code");
                    String message = jsonObject.getString("message");
                    if (success.equals("200")) {
                        tbs_verify_uid = jsonObject.getString("tbs_verify_uid");
                        tbs_uid = jsonObject.getString("tbs_uid");

                        fUserName.setVisibility(View.GONE);
                        fSendLink.setVisibility(View.GONE);
                        fLinkSendMsg.setVisibility(View.VISIBLE);
                        fCheck.setVisibility(View.VISIBLE);
                        fResendMail.setVisibility(View.GONE);
                        fChangeUsername.setVisibility(View.VISIBLE);

                        BeautifulToast.makeText(context, "Verify link send via mail.", Toast.LENGTH_LONG, R.style.toaststyle_success).show();

                        time_show.setVisibility(View.VISIBLE);

                        time_show.setVisibility(View.VISIBLE);
                        countDownTimer = new CountDownTimer(30000, 1000) {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onTick(long millisUntilFinished) {
                                countDownTimer_statusInt = (int) millisUntilFinished / 1000;
                                watch_time.setText("Resend mail after " + countDownTimer_statusInt + " Sec");
                            }

                            @Override
                            public void onFinish() {
                                fResendMail.setVisibility(View.VISIBLE);
                                time_show.setVisibility(View.GONE);
                            }
                        }.start();

                    } else if (success.equals("404") || success.equals("401")) {
                        BeautifulToast.makeText(context, message, Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    BeautifulToast.makeText(context, "Your data is not loaded", Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                }
            }, error -> {
                cancel_loader();
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    BeautifulToast.makeText(context, "No Internet found...", Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                } else {
                    BeautifulToast.makeText(context, "Internal server error...", Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<>();

                    params.put("app_passcode", app_passcode);
                    params.put("app_uid", app_uid);
                    params.put("email", email);

                    return params;
                }
            };

            RequestQueue requestQueue_createLogIn = Volley.newRequestQueue(context);
            requestQueue_createLogIn.add(createLogIn);
        }
    }

    public void fCheck(View view) {
        if (!network.getInstance(context).isOnline()) {
            BeautifulToast.makeText(context, "No network connections...", Toast.LENGTH_LONG, R.style.toaststyle_info).show();
            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        } else {
            Show_loader(context);
            StringRequest createLogIn = new StringRequest(Request.Method.POST, forget_verify_api, response -> {
                cancel_loader();
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String success = jsonObject.getString("code");
                    String message = jsonObject.getString("message");

                    switch (success) {
                        case "200":
                            BeautifulToast.makeText(context, message, Toast.LENGTH_LONG, R.style.toaststyle_success).show();
                            logInShow();
                            break;
                        case "201":
                        case "404":
                        case "401":
                            BeautifulToast.makeText(context, message, Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    BeautifulToast.makeText(context, "Your data is not loaded", Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                }
            }, error -> {
                cancel_loader();
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    BeautifulToast.makeText(context, "No Internet found...", Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                } else {
                    BeautifulToast.makeText(context, "Internal server error...", Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<>();

                    params.put("app_passcode", app_passcode);
                    params.put("app_uid", app_uid);
                    params.put("tbs_uid", tbs_uid);
                    params.put("tbs_verify_uid", tbs_verify_uid);

                    return params;
                }
            };

            RequestQueue requestQueue_createLogIn = Volley.newRequestQueue(context);
            requestQueue_createLogIn.add(createLogIn);
        }
    }

    public void fResendMail(View view) {
        if (!network.getInstance(context).isOnline()) {
            BeautifulToast.makeText(context, "No network connections...", Toast.LENGTH_LONG, R.style.toaststyle_info).show();
            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        } else {
            Show_loader(context);
            StringRequest createLogIn = new StringRequest(Request.Method.POST, forget_resend_api, response -> {
                cancel_loader();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String success = jsonObject.getString("code");
                    String message = jsonObject.getString("message");

                    if (success.equals("200")) {

                        lResendMail.setVisibility(View.GONE);
                        time_show.setVisibility(View.VISIBLE);

                        BeautifulToast.makeText(context, "Verify link send via mail.", Toast.LENGTH_LONG, R.style.toaststyle_success).show();

                        time_show.setVisibility(View.VISIBLE);
                        countDownTimer = new CountDownTimer(30000, 1000) {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onTick(long millisUntilFinished) {
                                countDownTimer_statusInt = (int) millisUntilFinished / 1000;
                                watch_time.setText("Resend mail after " + countDownTimer_statusInt + " Sec");
                            }

                            @Override
                            public void onFinish() {
                                fResendMail.setVisibility(View.VISIBLE);
                                time_show.setVisibility(View.GONE);
                            }
                        }.start();

                    } else if (success.equals("404") || success.equals("401")) {
                        BeautifulToast.makeText(context, message, Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    BeautifulToast.makeText(context, "Your data is not loaded", Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                }
            }, error -> {
                cancel_loader();
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    BeautifulToast.makeText(context, "No Internet found...", Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                } else {
                    BeautifulToast.makeText(context, "Internal server error...", Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<>();

                    params.put("app_passcode", app_passcode);
                    params.put("app_uid", app_uid);
                    params.put("tbs_uid", tbs_uid);
                    params.put("tbs_verify_uid", tbs_verify_uid);

                    return params;
                }
            };
            RequestQueue requestQueue_createLogIn = Volley.newRequestQueue(context);
            requestQueue_createLogIn.add(createLogIn);
        }
    }

    public void fChangeUsername(View view) {
        forgatePasswordShow();
    }


    public void fLogIn(View view) {
        logInShow();
    }

    public void fSignUp(View view) {
        signUpShow();
    }

    public void sSignUp(View view) {

        name = sName.getText().toString();
        email = sEmail.getText().toString();
        password = sPassword.getText().toString();
        cpassword = sConfirmPassword.getText().toString();
        phone_number = sPhoneNumber.getText().toString();

        if (name.isEmpty()) {
            BeautifulToast.makeText(context, "Enter Name.", Toast.LENGTH_LONG, R.style.toaststyle_info).show();
        } else if (email.isEmpty()) {
            BeautifulToast.makeText(context, "Enter Email Id.", Toast.LENGTH_LONG, R.style.toaststyle_info).show();
        } else if (password.isEmpty()) {
            BeautifulToast.makeText(context, "Enter password.", Toast.LENGTH_LONG, R.style.toaststyle_info).show();
        } else if (cpassword.isEmpty()) {
            BeautifulToast.makeText(context, "Enter confirm password.", Toast.LENGTH_LONG, R.style.toaststyle_info).show();
        } else if (!password.equals(cpassword)) {
            BeautifulToast.makeText(context, "Both password not matched..", Toast.LENGTH_LONG, R.style.toaststyle_error).show();
        } else if (phone_number.isEmpty()) {
            BeautifulToast.makeText(context, "Enter Phone phone_number.", Toast.LENGTH_LONG, R.style.toaststyle_info).show();
        } else if (!network.getInstance(context).isOnline()) {
            BeautifulToast.makeText(context, "No network connections...", Toast.LENGTH_LONG, R.style.toaststyle_info).show();
            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        } else {
            Show_loader(context);
            StringRequest createSignUp = new StringRequest(Request.Method.POST, signup_api, response -> {
                cancel_loader();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("code");
                    String message = jsonObject.getString("message");
                    switch (success) {
                        case "200":
                            tbs_verify_uid = jsonObject.getString("tbs_verify_uid");
                            tbs_uid = jsonObject.getString("tbs_uid");

                            sName.setVisibility(View.GONE);
                            sEmail.setVisibility(View.GONE);
                            sPassword.setVisibility(View.GONE);
                            sConfirmPassword.setVisibility(View.GONE);
                            sPhoneNumber.setVisibility(View.GONE);
                            sSignUp.setVisibility(View.GONE);
                            fLinkSendMsg.setVisibility(View.VISIBLE);
                            sCheck.setVisibility(View.VISIBLE);
                            sResendMail.setVisibility(View.GONE);
                            sChangeEmail.setVisibility(View.VISIBLE);

                            BeautifulToast.makeText(context, "Verify link send via mail.", Toast.LENGTH_LONG, R.style.toaststyle_success).show();
                            time_show.setVisibility(View.VISIBLE);
                            countDownTimer_statusInt = 30;

                            new CountDownTimer(30000, 1000) {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    countDownTimer_statusInt--;
                                    watch_time.setText("Resend mail after " + countDownTimer_statusInt + " Sec");
                                }

                                @Override
                                public void onFinish() {
                                    sResendMail.setVisibility(View.VISIBLE);
                                    time_show.setVisibility(View.GONE);

                                }
                            }.start();

                            break;
                        case "201":
                            BeautifulToast.makeText(context, message, Toast.LENGTH_LONG, R.style.toaststyle_info).show();
                            break;
                        case "404":
                        case "401":
                            BeautifulToast.makeText(context, message, Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    BeautifulToast.makeText(context, "Your data is not loaded", Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                }

            }, error -> {
                cancel_loader();
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    BeautifulToast.makeText(context, "No Internet found...", Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                } else {
                    BeautifulToast.makeText(context, "Internal server error...", Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<>();

                    params.put("app_passcode", app_passcode);
                    params.put("app_uid", app_uid);
                    params.put("email", email);
                    params.put("name", name);
                    params.put("password", password);
                    params.put("phone_number", phone_number);

                    return params;
                }
            };
            RequestQueue requestQueue_createLogIn = Volley.newRequestQueue(context);
            requestQueue_createLogIn.add(createSignUp);
        }
    }

    public void sCheck(View view) {
        if (!network.getInstance(context).isOnline()) {
            BeautifulToast.makeText(context, "No network connections...", Toast.LENGTH_LONG, R.style.toaststyle_info).show();
            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        } else {
            Show_loader(context);
            StringRequest createLogIn = new StringRequest(Request.Method.POST, signup_verify_api, response -> {
                cancel_loader();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("code");
                    String message = jsonObject.getString("message");

                    switch (success) {
                        case "200":
                            String data = jsonObject.getString("data");
                            JSONObject jsonDataObject = new JSONObject(data);

                            BeautifulToast.makeText(context, message, Toast.LENGTH_LONG, R.style.toaststyle_success).show();

                            SaveInSharePref(context, "verify_password", jsonDataObject.getString("verify_password"));
                            SaveInSharePref(context, "auth_password", jsonDataObject.getString("auth_password"));
                            SaveInSharePref(context, "app_status", jsonDataObject.getString("app_status"));
                            SaveInSharePref(context, "app_packge_name", jsonDataObject.getString("app_packge_name"));
                            SaveInSharePref(context, "auth_uid", jsonDataObject.getString("auth_uid"));
                            SaveInSharePref(context, "app_uid", jsonDataObject.getString("app_uid"));
                            SaveInSharePref(context, "tbs_uid", jsonDataObject.getString("tbs_uid"));
                            SaveInSharePref(context, "app_passcode", jsonDataObject.getString("app_passcode"));

                            Intent intent = new Intent();
                            intent.putExtra("app_passcode", jsonDataObject.getString("app_passcode"));
                            intent.putExtra("tbs_uid", jsonDataObject.getString("tbs_uid"));
                            intent.putExtra("app_uid", jsonDataObject.getString("app_uid"));
                            intent.putExtra("auth_uid", jsonDataObject.getString("auth_uid"));
                            intent.putExtra("app_packge_name", jsonDataObject.getString("app_packge_name"));
                            intent.putExtra("app_status", jsonDataObject.getString("app_status"));
                            intent.putExtra("auth_password", jsonDataObject.getString("auth_password"));
                            intent.putExtra("verify_password", jsonDataObject.getString("verify_password"));
                            setResult(RESULT_OK, intent);
                            finish();
                            break;
                        case "201":
                            BeautifulToast.makeText(context, message, Toast.LENGTH_LONG, R.style.toaststyle_info).show();
                            break;
                        case "404":
                        case "401":
                            BeautifulToast.makeText(context, message, Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    BeautifulToast.makeText(context, "Your data is not loaded", Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                }
            }, error -> {
                cancel_loader();
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    BeautifulToast.makeText(context, "No Internet found...", Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                } else {
                    BeautifulToast.makeText(context, "Internal server error...", Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<>();
                    params.put("app_passcode", app_passcode);
                    params.put("app_uid", app_uid);
                    params.put("tbs_uid", tbs_uid);
                    params.put("tbs_verify_uid", tbs_verify_uid);
                    return params;
                }
            };
            RequestQueue requestQueue_createLogIn = Volley.newRequestQueue(context);
            requestQueue_createLogIn.add(createLogIn);
        }
    }

    public void sResendMail(View view) {
        if (!network.getInstance(context).isOnline()) {
            BeautifulToast.makeText(context, "No network connections...", Toast.LENGTH_LONG, R.style.toaststyle_info).show();
            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        } else {
            Show_loader(context);
            StringRequest createLogIn = new StringRequest(Request.Method.POST, signup_resend_api, response -> {
                cancel_loader();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String success = jsonObject.getString("code");
                    String message = jsonObject.getString("message");

                    switch (success) {
                        case "200":
                            tbs_verify_uid = jsonObject.getString("tbs_verify_uid");
                            tbs_uid = jsonObject.getString("tbs_uid");

                            fUserName.setVisibility(View.GONE);
                            fSendLink.setVisibility(View.GONE);
                            fLinkSendMsg.setVisibility(View.VISIBLE);
                            fCheck.setVisibility(View.VISIBLE);
                            fResendMail.setVisibility(View.GONE);
                            fChangeUsername.setVisibility(View.VISIBLE);

                            BeautifulToast.makeText(context, "Verify link send via mail.", Toast.LENGTH_LONG, R.style.toaststyle_success).show();

                            time_show.setVisibility(View.VISIBLE);

                            time_show.setVisibility(View.VISIBLE);
                            countDownTimer = new CountDownTimer(30000, 1000) {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onTick(long millisUntilFinished) {
//                                        countDownTimer_statusInt = millisUntilFinished / 1000;
                                    watch_time.setText("Resend mail after " + countDownTimer_statusInt + " Sec");
                                }

                                @Override
                                public void onFinish() {
                                    fResendMail.setVisibility(View.VISIBLE);
                                    time_show.setVisibility(View.GONE);

                                }
                            }.start();

                            break;
                        case "201":
                            BeautifulToast.makeText(context, message, Toast.LENGTH_LONG, R.style.toaststyle_info).show();
                            break;
                        case "404":
                        case "401":
                            BeautifulToast.makeText(context, message, Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    BeautifulToast.makeText(context, "Your data is not loaded", Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                }

            }, error -> {
                cancel_loader();
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    BeautifulToast.makeText(context, "No Internet found...", Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                } else {
                    BeautifulToast.makeText(context, "Internal server error...", Toast.LENGTH_LONG, R.style.toaststyle_error).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<>();

                    params.put("app_passcode", app_passcode);
                    params.put("app_uid", app_uid);
                    params.put("tbs_uid", tbs_uid);
                    params.put("tbs_verify_uid", tbs_verify_uid);

                    return params;
                }
            };
            RequestQueue requestQueue_createLogIn = Volley.newRequestQueue(context);
            requestQueue_createLogIn.add(createLogIn);
        }
    }

    public void sChangeEmail(View view) {
        signUpShow();
    }

    public void sLogIn(View view) {
        logInShow();
    }

    private void signUpShow() {
        logInPanel.setVisibility(View.GONE);
        signUpPanel.setVisibility(View.VISIBLE);
        forgetPasswordPanel.setVisibility(View.GONE);

        allElementShow();
    }

    private void forgatePasswordShow() {
        logInPanel.setVisibility(View.GONE);
        signUpPanel.setVisibility(View.GONE);
        forgetPasswordPanel.setVisibility(View.VISIBLE);

        allElementShow();
    }

    private void logInShow() {
        logInPanel.setVisibility(View.VISIBLE);
        signUpPanel.setVisibility(View.GONE);
        forgetPasswordPanel.setVisibility(View.GONE);
        allElementShow();
    }

    private void allElementShow() {
        email = "";
        password = "";
        cpassword = "";
        phone_number = "";

        lUserName.setVisibility(View.VISIBLE);
        lPassword.setVisibility(View.VISIBLE);
        lLogIn.setVisibility(View.VISIBLE);
        lLinkSendMsg.setVisibility(View.GONE);
        lCheck.setVisibility(View.GONE);
        lResendMail.setVisibility(View.GONE);
        lForgetPassword.setVisibility(View.VISIBLE);


        fUserName.setVisibility(View.VISIBLE);
        fSendLink.setVisibility(View.VISIBLE);
        fLinkSendMsg.setVisibility(View.GONE);
        fCheck.setVisibility(View.GONE);
        fResendMail.setVisibility(View.GONE);
        fChangeUsername.setVisibility(View.GONE);

        sEmail.setVisibility(View.VISIBLE);
        sName.setVisibility(View.VISIBLE);
        sPassword.setVisibility(View.VISIBLE);
        sConfirmPassword.setVisibility(View.VISIBLE);
        sPhoneNumber.setVisibility(View.VISIBLE);
        sSignUp.setVisibility(View.VISIBLE);
        sLinkSendMsg.setVisibility(View.GONE);
        sCheck.setVisibility(View.GONE);
        sResendMail.setVisibility(View.GONE);
        sChangeEmail.setVisibility(View.GONE);
    }
}