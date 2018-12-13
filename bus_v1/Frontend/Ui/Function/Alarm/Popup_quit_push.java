package com.example.sonhyeongi.bus_v1.Frontend.Ui.Function.Alarm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sonhyeongi.bus_v1.Frontend.Ui.User_interface.Loading;
import com.example.sonhyeongi.bus_v1.Function.Data.Data_;
import com.example.sonhyeongi.bus_v1.Function.Device.Net_Check;
import com.example.sonhyeongi.bus_v1.R;
import com.google.firebase.iid.FirebaseInstanceId;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.HashMap;

public class Popup_quit_push extends Activity {

    private TextView txtText;
    private Intent intent;
    private String bus_number;
    private String destination;
    private int flag;
    private Background background;
    private Context context;
    private String Check;
    private Boolean check1 = false;
    private Boolean check2 = false;
    private AlertDialog.Builder builder;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_alarm_push);


        txtText = (TextView)findViewById(R.id.txtText);
        intent = getIntent();
        destination = intent.getStringExtra("Destination");
        bus_number = intent.getStringExtra("My_Busnumber");
        context = getApplicationContext();

        if (Data_.getAhead().equals("1")) {
            flag = 0; // 국민대 쪽으로 돌아옴
        }
        else {
            flag = -1;  // 공덕동 쪽으로 나감
        }
        builder = new AlertDialog.Builder(this);

        background = new Background();
        background.start();
    }

    class Background extends Thread implements Runnable{

        Handler handler = new Handler();

        public void run() {
            super.run();

            String URL1 = "http://13.209.133.231:8080/demo_war/pushregister?token="+ FirebaseInstanceId.getInstance().getToken();
            Boolean check = false;
            try {
                check = Boolean.valueOf(Jsoup.connect(URL1).ignoreContentType(true).execute().body());
            }catch (Exception e){
                e.printStackTrace();
            }
            if (check){
                final String text = setalarm();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            txtText.setText(text);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }
                    });
            }
            else{
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        builder.setMessage("기존의 알람 설정이 있습니다. 삭제하시겠습니까?").setPositiveButton("네", dialogClickListener)
                                .setNegativeButton("아니요", dialogClickListener).show();
                    }
                });
            }
        }
    }
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    final String URL = "http://13.209.133.231:8080/demo_war/pushremove?token="+ FirebaseInstanceId.getInstance().getToken();
                    final String URL2 = "http://13.209.133.231:8080/demo_war/stopalarm?token="+ FirebaseInstanceId.getInstance().getToken();
                    final Handler handler = new Handler();
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            try {
                                if (!Boolean.valueOf(Jsoup.connect(URL).ignoreContentType(true).execute().body()))
                                {
                                    Toast.makeText(context,"시스템에 문제가 있습니다.",Toast.LENGTH_SHORT).show();
                                    finish();
                                    return;
                                }
                                if (!Boolean.valueOf(Jsoup.connect(URL).ignoreContentType(true).execute().body())){
                                    Toast.makeText(context,"시스템에 문제가 있습니다.",Toast.LENGTH_SHORT).show();
                                    finish();
                                    return;
                                }
                                else{
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            txtText.setText("기존 알람이 취소 되었습니다.\n 알람을 다시 설정해주세요");
                                        }
                                    });
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }.start();

                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    txtText.setText("예전 알람 설정이 지속됩니다.");
                    Log.d("NO","NO");

                    break;
            }
        }
    };

    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()== MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    public void mOnClose(View v){
        setResult(RESULT_OK, intent);
        finish();
    }

    public String setalarm(){
        String URL = "http://13.209.133.231:8080/demo_war/alarmpush?busid=";
        String result = "알람이 설정 되었습니다. \n 인터넷 연결이 끊기면 알람이 안울립니다.";

        URL = URL.concat(Data_.getBus_id()).concat("&")
                .concat("mybusnumber=").concat(bus_number).concat("&")
                .concat("destination=").concat(destination).concat("&")
                .concat("busgo=").concat(String.valueOf(flag).concat("&"))
                .concat("token=").concat(FirebaseInstanceId.getInstance().getToken()).concat("&")
                .concat("usernumber=").concat(Data_.getDevice_value());

        Log.d("URL :     ",URL);


        try {

            int a = Integer.valueOf(Jsoup.connect(URL).ignoreContentType(true).ignoreContentType(true).timeout(2000).execute().body());
            if (a==403){
                final String URL_ = "http://13.209.133.231:8080/demo_war/pushremove?token="+ FirebaseInstanceId.getInstance().getToken();
                if (Boolean.valueOf(Jsoup.connect(URL_).ignoreContentType(true).execute().body())){
                    result = "이미 도착할 정거장입니다. \n또는 탑승위치와 하차위치가 오류가있습니다.";
                }
            }
            else if (a==404){
                final String URL_ = "http://13.209.133.231:8080/demo_war/pushremove?token="+ FirebaseInstanceId.getInstance().getToken();
                if (Boolean.valueOf(Jsoup.connect(URL_).ignoreContentType(true).execute().body())){
                    result = "올바른 접속이 아닙니다.";
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }





}
