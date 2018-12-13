package com.example.sonhyeongi.bus_v1.Frontend.Ui.Function.Alarm;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sonhyeongi.bus_v1.Function.Data.Data_;
import com.example.sonhyeongi.bus_v1.Function.Device.Net_Check;
import com.example.sonhyeongi.bus_v1.R;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.HashMap;

public class Popup_boarding extends Activity {


        private TextView txtText;
        private int flag;

        private Background background;
        private MediaPlayer audioPlayer;
        private String json = null;
        private Message message ;
        private Context context;
        private int Check;
        private String bus_number = null;
        private String destination = null;
        private  Intent intent;
        private String Ahead = null;
        private boolean Audio_flag = false;

        Alarm_boarding_bus alarm_boarding_bus;
        Getbusnumber obj;

        class Getbusnumber extends Thread{
            int Ahead;
            public void setAhead(int ahead) {
                Ahead = ahead;
            }
            @Override
            public void run() {
                super.run();
                try {
                    String URL = "http://13.209.133.231:8080/demo_war/busnumber2?bus_id=";
                    URL = URL.concat(Data_.getBus_id()).concat("&")
                            .concat("ahead=").concat(String.valueOf(Ahead)).concat("&")
                            .concat("ps=").concat(String.valueOf(Data_.getUser_location_s())).concat("&")
                            .concat("pb=").concat(String.valueOf(Data_.getUser_location_b()));

                    json = Jsoup.connect(URL).ignoreContentType(true).execute().body();
                    bus_number = json.replaceAll("\"","");
                    Log.d("Turning" , String.valueOf(Data_.getTurnning_station()));
                    Log.d("BUS NUMBER : ", bus_number);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }


        }


        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.popup_alarm2);
            intent = getIntent();


            txtText = (TextView)findViewById(R.id.txtText);
            bus_number = intent.getStringExtra("busnumber");
            destination = Data_.getUser_location();
            Ahead = Data_.getAhead();



            if (Ahead.equals("1")){
                flag=0; // 국민대 쪽으로 돌아옴
            }
            else{
                flag=-1;  // 공덕동 쪽으로 나감
            }

            context = getApplicationContext();
            alarm_boarding_bus = new Alarm_boarding_bus();

            audioPlayer = MediaPlayer.create(Popup_boarding.this,R.raw.maple);

            background = new Background();
            background.start();
        }

        class Background extends Thread implements Runnable{
            boolean status;

            Handler mHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == 0){
                        if (msg.arg1==-1){
                            txtText.setText(msg.obj.toString());
                        }
                        else if(msg.arg1==-2){
                            Toast.makeText(getApplicationContext(),msg.obj.toString(),Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else {
                            txtText.setText(msg.arg1-1 + "개의 정거장 전에 있습니다.");
                        }
                    }
                }
            };

            public void run() {
                super.run();
                String URL1 = "http://13.209.133.231:8080/demo_war/alarm?busid=";
                while (status){
                    try {
                        try {
                            String URL = URL1.concat(Data_.getBus_id()).concat("&")
                                    .concat("mybusnumber=").concat(bus_number).concat("&")
                                    .concat("destination=").concat(destination).concat("&")
                                    .concat("busgo=").concat(String.valueOf(flag));

                            boolean flag = true;
                            if(Net_Check.isNetworkAvailable(context)){
                                Check = Integer.valueOf(Jsoup.connect(URL).ignoreContentType(true).execute().body());
                                Log.d("URL", URL);
                                Log.d("JSON",String.valueOf(Check));
                                flag = false;
                            }

                            if (flag){
                                Log.d("Internet", "NONO");
                                message = new Message();
                                message.obj = "인터넷이 연결되어 있지 않습니다.";
                                message.arg1 = -1;
                                message.what=0;
                                mHandler.sendMessage(message);
                                notificationcall();
                            }
                            else if (Check==-1 || Check==-403){   // 알림추가
                                synchronized (this){
                                    obj = new Getbusnumber();
                                    obj.setAhead(Integer.valueOf(Ahead));
                                    obj.start();
                                    try{
                                        obj.join();
                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                                if (!Audio_flag){
                                    audioPlayer.start();
                                    Audio_flag=true;
                                }
                                Check = 0-Check;
                                message = new Message();
                                message.arg1 = -1;
                                message.obj = "한 정거장 전에 있습니다.";
                                message.what=0;
                                mHandler.sendMessage(message);
                            }
                            else{
                                if (Audio_flag){
                                    audioPlayer.stop();
                                    Audio_flag = false;
                                }
                                message = new Message();
                                Check = 0-Check;
                                message.obj = " 개의 정거장 전에 있습니다.";
                                message.arg1 = Check;
                                message.what=0;
                                mHandler.sendMessage(message);
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            if (Data_.getAll_station().contains(destination.replaceAll("\"",""))){
                                message = new Message();
                                message.arg1 = -2;
                                message.obj = "현재 버스의 정보가 없습니다.";
                                message.what=0;
                                mHandler.sendMessage(message);
                                break;
                            }
                            else{
                                message = new Message();
                                message.arg1 = -2;
                                message.obj = "정거장명칭에 오타가 있습니다.";
                                message.what=0;
                                mHandler.sendMessage(message);
                                break;
                            }
                        }
                        Thread.sleep(30000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
            public Background(){
                status = true;
            }

            public void StopThread(){
                status = false;
            }
        }

        public boolean onTouchEvent(MotionEvent event) {
            //바깥레이어 클릭시 안닫히게
            if(event.getAction()== MotionEvent.ACTION_OUTSIDE){
                return false;
            }
            return true;
        }
        @Override
        public void onBackPressed() {
            return;
        }

        public void mOnClose(View v){

            setResult(RESULT_OK, intent);
            audioPlayer.stop();
            background.StopThread();
            background.interrupt();

            finish();
        }


        public void notificationcall(){

            int notify_id =1;

            NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setContentTitle("Notify")
                    .setChannelId("my_channel_01")
                    .setContentText("인터넷 연결이 끊겼습니다.");
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(notify_id, notificationBuilder.build());
        }


    }