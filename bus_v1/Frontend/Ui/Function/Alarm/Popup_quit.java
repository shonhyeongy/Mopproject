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

public class Popup_quit extends Activity {

    private TextView txtText;
    private Intent intent;
    private HashMap<String,Integer> Station_s ;
    private HashMap <String,Integer> Station_b ;
    private String bus_number;
    private String destination;
    private String Ahead;
    private String Bus_id;
    private int flag;
    private Background background;
    private MediaPlayer audioPlayer;
    private ArrayList All_station;
    private String json = null;
    private Message message ;
    private Context context;
    private int Check;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_alarm);


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


        audioPlayer = MediaPlayer.create(Popup_quit.this,R.raw.maple);
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
            String URL = "http://13.209.133.231:8080/demo_war/alarm?busid=";
            URL = URL.concat(Data_.getBus_id()).concat("&")
                    .concat("mybusnumber=").concat(bus_number).concat("&")
                    .concat("destination=").concat(destination).concat("&")
                    .concat("busgo=").concat(String.valueOf(flag));
            while (status){
                try {
                    try {
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
                        else if (Check==1){   // 알림추가
                            message = new Message();
                            message.arg1 = -1;
                            message.obj = "도착하였습니다.";
                            message.what=0;
                            mHandler.sendMessage(message);
                            audioPlayer.start();
                            //Log.d("MSG",String.valueOf(Check));
                            break;
                        }
                        else if (Check==403){
                            message = new Message();
                            message.arg1 = -2;
                            message.obj = "이미 도착할 정거장입니다. \n또는 탑승위치와 하차위치가 오류가있습니다.";
                            message.what=0;
                            mHandler.sendMessage(message);
                            break;
                        }
                        else if (Check==404){
                            message = new Message();
                            message.arg1 = -2;
                            message.obj = "올바른 접속이 아닙니다.";
                            message.what=0;
                            mHandler.sendMessage(message);
                            break;
                        }
                        else{
                            message = new Message();
                            message.obj = " 개의 정거장 전에 있습니다.";
                            message.arg1 = Check;
                            message.what=0;
                            mHandler.sendMessage(message);
                        }

                    }catch (Exception e){
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
