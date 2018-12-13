package com.example.sonhyeongi.bus_v1.Frontend.Ui.Function.Alarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.example.sonhyeongi.bus_v1.Function.Data.Data_;
import com.example.sonhyeongi.bus_v1.R;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;



public class Alarm_boarding_bus extends AppCompatActivity {

    private TextView textView ;
    private Button button;
    protected String busnumber = null;
    private Intent intent ;

    public String getBusnumber() {
        return busnumber;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alram2_bus);

        button = (Button)findViewById(R.id.button9);
        textView = (TextView)findViewById(R.id.textView);
        Get_bus_number get_bus_number ;

        if (Data_.getAhead().equals("1")){
            // 국민대 쪽으로 돌아옴
            get_bus_number = new Get_bus_number(1);

        }
        else{
            // 공덕동 쪽으로 나감
            get_bus_number = new Get_bus_number(2);
        }
        get_bus_number.start();
        intent = new Intent(this, Popup_boarding.class);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent.putExtra("busnumber",busnumber);
                textView.setText(Data_.getUser_location());
                startActivity(intent);
            }
        });

    }

    public class Get_bus_number extends Thread {
        HashMap<String, Integer> data;
        int Ahead;
        String json = null;
        int user_location ;


        public Get_bus_number(int ahead){
            Ahead = ahead;
            data = new HashMap<>();
        }

        @Override
        public void run() {
            super.run();
            String URL = "http://13.209.133.231:8080/demo_war/realtime?busid=";
            URL = URL.concat(Data_.getBus_id());

            if (Ahead==1){
                user_location = Data_.getUser_location_s();
            }
            else{
                user_location = Data_.getUser_location_b();
            }

            try {
                json = Jsoup.connect(URL).ignoreContentType(true).execute().body();
                JSONObject jsonObject = new JSONObject(json);
                Iterator iterator = jsonObject.keys();
                int small = 1000;
                while (iterator.hasNext()){
                    String data1 = iterator.next().toString();
                    int bus_location = jsonObject.getInt(data1);

                    if (bus_location < user_location &&
                            small > Math.abs(bus_location-user_location)){

                        small = Math.abs(bus_location-user_location);
                        busnumber = data1;
                    }
                }
                Log.d("Thread","end");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
