package com.thebengalstudio.authentication.Service;

public class API {
    private static final String website_url = "https://auth.skosao.com";

    //TODO: User Auth Api
    private static final String auth_api = website_url+"";
    public static final String signup_api = auth_api+"/signup";                     //set
    public static final String signup_resend_api = auth_api+"/signup_resend";       //set
    public static final String signup_verify_api = auth_api+"/signup_verify";       //set
    public static final String forget_api = auth_api+"/forget";                     //set
    public static final String forget_resend_api = auth_api+"/forget_resend";       //set
    public static final String forget_verify_api = auth_api+"/forget_verify";       //set
    public static final String login_api = auth_api+"/login";                       //Set
    public static final String login_verify_api = auth_api+"/login_verify";         //set
    public static final String login_resend_api = auth_api+"/login_resend";         //set


    public static final String login_check_api = auth_api+"/login_check";           //Unset
    public static final String logout_api = auth_api+"/logout";                     //Unset
    public static final String logout_all_api = auth_api+"/logout_all";                     //Unset


//    public static final String login_api = auth_api+"/login";
//    public static final String login__otp_api = auth_api+"/login_otp";
//    public static final String login_resend_otp_api = auth_api+"/login_resend_otp";
//    public static final String forgatepassword_email_otp_api = auth_api+"/forgatepassword_email_otp";
//    public static final String forgatepassword_resend_email_otp_api = auth_api+"/forgatepassword_resend_email_otp";
//    public static final String forgatepassword_confirm_api = website_url+"/forgatepassword_confirm";



}
