package com.example.sonhyeongi.bus_v1.Background.Server.Mysql;




import android.util.Log;

import com.example.sonhyeongi.bus_v1.Frontend.Ui.User_interface.Loading;
import com.google.firebase.iid.FirebaseInstanceId;

import org.jsoup.Jsoup;


//&Userpw=ddd&Username=ddd&Userage=10&Usernumber=22

public class Connect_mysql{

    final static private String VERSION_NUMBER = "20180716";



//register?userid=212312&userpw=1&username=1&userage=1&usernumber=1
    private String json = null;
    public boolean conn(String id, String pw, String name, String age, String number , String phonenumber) {
        String URL = "http://13.209.133.231:8080/demo_war/register?userid=";
        boolean flag = false;
        final String url = URL.concat(id).concat("&")
                .concat("userpw=").concat(pw).concat("&")
                .concat("username=").concat(name).concat("&")
                .concat("userage=").concat(age).concat("&")
                .concat("usernumber=").concat(number).concat("&")
                .concat("phonenumber=").concat(phonenumber).concat("&")
                .concat("token=").concat(FirebaseInstanceId.getInstance().getToken());


        Log.d("URL :      ", url);

        try {
            json = Jsoup.connect(url).ignoreContentType(true).execute().body();

            if (json.trim().equals("true")){
                flag = true;
            }
            else{
                flag = false;
            }

            

        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    static public boolean Check(String device_number){
        boolean flag = true;  // 회원가입 되어있는 상태
        String URL = "http://13.209.133.231:8080/demo_war/login?device=";
        final String URL_ = URL.concat(device_number);

        try {
            String json = Jsoup.connect(URL_).ignoreContentType(true).execute().body();
            if (!json.trim().equals("true")){
                flag = false;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    static  public  int version(){
        int flag = 0; // false
        String URL = "http://13.209.133.231:8080/demo_war/Version?ver=";
        final String URL_ = URL.concat(VERSION_NUMBER);

        try {
            String json = Jsoup.connect(URL_).ignoreContentType(true).timeout(3000) .execute().body();
            if (json.trim().equals("true")){
                flag = 1;//true
            }
        }
        catch (Exception e){
            flag = 2;
            e.printStackTrace();
        }
        return flag;
    }
}
