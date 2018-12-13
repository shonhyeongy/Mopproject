package com.example.sonhyeongi.bus_v1.Frontend.Ui.Main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.sonhyeongi.bus_v1.Background.Server.Mysql.Connect_mysql;
import com.example.sonhyeongi.bus_v1.Frontend.Ui.User_interface.Loading;
import com.example.sonhyeongi.bus_v1.Frontend.Ui.User_interface.Loading_thread;
import com.example.sonhyeongi.bus_v1.Function.Data.Data_;
import com.example.sonhyeongi.bus_v1.Function.Device.Data_base;
import com.example.sonhyeongi.bus_v1.Function.Device.Device_value;
import com.example.sonhyeongi.bus_v1.Function.Device.Net_Check;
import com.example.sonhyeongi.bus_v1.R;
import com.example.sonhyeongi.bus_v1.Frontend.Ui.Main.Register.Register;
import com.example.sonhyeongi.bus_v1.Frontend.Ui.User_bus_list.User_bus;
import com.example.sonhyeongi.bus_v1.Frontend.Ui.User_interface.bus_search;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import static android.content.ContentValues.TAG;

public class MainActivity extends Data_base {

    private Button button1;
    private Button button2;
    private Intent intent;
    private Intent intent2;
    private Intent intent3;
    private Context context;
    private Activity activity;
    private boolean Version = false;
    private static Loading loading = null;
    private int test =0;
    Boolean Register_flag;

    Intent intent_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = (Button)findViewById(R.id.button1);  // 버스검색
        button2 = (Button)findViewById(R.id.button2);  // 자주타는버스
        activity = this;

        intent = new Intent(this, bus_search.class);
        intent2 = new Intent(this, User_bus.class);
        intent3 = new Intent(this, Register.class);
        context = this.getApplicationContext();

        intent_test = new Intent(this, Loading.class);

        String data = Device_value.getDeviceUniqueID(this);

        Data_.setDevice_value(data);

        FirebaseInstanceId.getInstance().getToken();

        if (FirebaseInstanceId.getInstance().getToken() != null) {
            Log.d(TAG, "token = " + FirebaseInstanceId.getInstance().getToken());
        }
       // FirebaseMessaging.getInstance().subscribeToTopic("ALL");



        button1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!Net_Check.isNetworkAvailable(context)){
                    Toast.makeText(getApplicationContext(),"인터넷이 연결되어 있지 않습니다.",Toast.LENGTH_SHORT).show();
                    return;
                }
               // final thread1 thread1 = new thread1();
                final Background background = new Background(0);
                final Loading_thread loading_thread = new Loading_thread();

                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        startActivity(intent_test);
                        loading_thread.start();
                        try {
                            loading_thread.join();
                            sleep(250);
                            Log.d("JOIN","JOIN");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        background.start();
                        try {
                            background.join();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }.start();

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Net_Check.isNetworkAvailable(context)){
                    Toast.makeText(getApplicationContext(),"인터넷이 연결되어 있지 않습니다.",Toast.LENGTH_SHORT).show();
                    return;
                }

                final Background background = new Background(1);
                final Loading_thread loading_thread = new Loading_thread();

                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        startActivity(intent_test);
                        loading_thread.start();
                        try {
                            loading_thread.join();
                            sleep(250);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        background.start();
                        try {
                            background.join();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
    }

    class Background extends Thread {
        private Handler handler;
        private Intent intent1;
        public Background(int input) {
            handler = new Handler();
            if (input == 0) { // intent
                intent1 = intent;
            } else if (input == 1) {
                intent1 = intent2;
            }
        }

        public void run() {
            super.run();
            final int Version_flag = Connect_mysql.version();
            Register_flag = false;
            if (Version_flag!=0 && Version_flag!=2){
                Register_flag = Connect_mysql.Check(Data_.getDevice_value());
            }

            Log.d("Version flag :" , String.valueOf(Version_flag));

            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (Version_flag==0){
                        Toast.makeText(getApplicationContext(), "업데이트 후 사용해 주시기 바랍니다." ,Toast.LENGTH_SHORT).show();
                        loading = (Loading)Loading.getloading();
                        loading.finish();
                        finish();
                        return;
                    }
                    if (Version_flag==2){
                        Toast.makeText(getApplicationContext(), "서버 접속 오류\n잠시후 시도해주세요." ,Toast.LENGTH_SHORT).show();
                        loading = (Loading)Loading.getloading();
                        loading.finish();
                        finish();
                        return;
                    }

                    if (Register_flag) {
                        loading = (Loading)Loading.getloading();
                        loading.finish();
                        startActivity(intent1);
                    } else {
                        loading = (Loading)Loading.getloading();
                        loading.finish();
                        startActivity(intent3);
                    }
                }
            });


        }
    }
}
