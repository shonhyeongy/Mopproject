package com.example.sonhyeongi.bus_v1.Frontend.Ui.Function.Find;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.sonhyeongi.bus_v1.Frontend.Ui.User_interface.Loading;
import com.example.sonhyeongi.bus_v1.Function.Data.Data_;
import com.example.sonhyeongi.bus_v1.R;

import org.jsoup.Jsoup;

public class Last_bus extends AppCompatActivity {

    private TextView textView;
    private int userlocation ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.last_bus);

        textView = (TextView)findViewById(R.id.last);
        if (Data_.getAhead().equals("1")){
            userlocation = Data_.getUser_location_s(); //  공덕동 쪽으로 나감
        }
        else {
            userlocation = Data_.getUser_location_b();  // 국민대 쪽으로 돌아옴
        }
        String userTime = new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date());

        String url = "http://13.209.133.231:8080/demo_war/Lastbus?busnumber=";
        url = url.concat(Data_.getBus_number()).concat("&")
                .concat("busid=").concat(Data_.getBus_id()).concat("&")
                .concat("bus_location=").concat(String.valueOf(userlocation)).concat("&")
                .concat("time=").concat(userTime);

        Log.d("Time", userTime);

        Log.d("URL :", url);
        final background background = new background(url);
        final Intent intent = new Intent(this, Loading.class);

        new Thread(){
            @Override
            public void run() {
                super.run();
                startActivity(intent);


                try {
                    sleep(250);
                    background.start();
                    background.join();
                }catch (Exception e){
                    e.printStackTrace();
                }

                Loading loading = (Loading) Loading.getloading();
                loading.finish();


            }
        }.start();





    }





    class background extends Thread implements Runnable{
        private String url = "";
        private String json = null;
        private Handler mhandler;

        public background(String url){
            this.url = url;
            mhandler = new Handler();
        }
        @Override
        public void run() {
            super.run();
            try{
                json = Jsoup.connect(url).ignoreContentType(true).execute().body();

            }catch (Exception e){
                e.printStackTrace();
            }
            mhandler.post(new Runnable(){
                @Override
                public void run() {
                    textView.setText(json);
                }
            });
        }


    }



}
