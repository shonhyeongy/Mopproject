package com.example.sonhyeongi.bus_v1.Frontend.Ui.Function.Find;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sonhyeongi.bus_v1.Frontend.Ui.User_interface.Loading;
import com.example.sonhyeongi.bus_v1.Function.Data.Data_;
import com.example.sonhyeongi.bus_v1.Function.Device.Net_Check;
import com.example.sonhyeongi.bus_v1.R;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Find_bus extends AppCompatActivity {

    TextView textView;
    Handler mHandler;
    private int flag;
    private String json = null;
    private Context context;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_bus);

        mHandler = new Handler();
        textView = (TextView)findViewById(R.id.textView5);

        context =this.getApplicationContext();

        if (Data_.getAhead().equals("1")){
            flag=-1; //  공덕동 쪽으로 나감
        }
        else {
            flag = 0;  // 국민대 쪽으로 돌아옴

        }
        final find_thread find_thread = new find_thread();
        final Intent intent = new Intent(this, Loading.class);

        new Thread(){
            @Override
            public void run() {
                super.run();
                startActivity(intent);
                try {
                    sleep(250);
                    find_thread.start();
                    find_thread.join();
                }catch (Exception e){
                    e.printStackTrace();
                }
                Loading loading = (Loading) Loading.getloading();
                loading.finish();
            }
        }.start();
    }

    class find_thread extends Thread{
        public void run(){
            if(!Net_Check.isNetworkAvailable(context)){
                Toast.makeText(getApplicationContext(),"인터넷이 연결되어 있지 않습니다.",Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                try {
                    String URL = "http://13.209.133.231:8080/demo_war/find?busid=";
                    URL = URL.concat(Data_.getBus_id()).concat("&")
                            .concat("busstation=").concat(Data_.getUser_location()).concat("&")
                            .concat("busgo=").concat(String.valueOf(flag));
                    json = Jsoup.connect(URL).ignoreContentType(true).execute().body();

                    Log.d("JSON",URL);

                }catch (NullPointerException e){
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (Data_.getAll_station().contains(Data_.getUser_location())){
                                Toast.makeText(getApplicationContext(),"현재 버스의 정보가 없습니다.",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"정거장명칭에 오타가 있습니다.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    return;
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    textView.setText(json);
                }
            });
        }
    }


}