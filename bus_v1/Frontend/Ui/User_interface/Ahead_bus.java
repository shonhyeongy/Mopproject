package com.example.sonhyeongi.bus_v1.Frontend.Ui.User_interface;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.sonhyeongi.bus_v1.Frontend.Ui.Function.Alarm.Alarm_boarding_bus;
import com.example.sonhyeongi.bus_v1.Frontend.Ui.Function.Alarm.Alarm_quit_bus;
import com.example.sonhyeongi.bus_v1.Frontend.Ui.Function.Find.Find_bus;
import com.example.sonhyeongi.bus_v1.Frontend.Ui.Function.Find.Last_bus;
import com.example.sonhyeongi.bus_v1.Function.Data.Data_;
import com.example.sonhyeongi.bus_v1.Function.Device.Net_Check;
import com.example.sonhyeongi.bus_v1.R;

import java.util.ArrayList;
import java.util.Iterator;

public class Ahead_bus extends AppCompatActivity {

    private Button Find_option;
    private Button Alarm_option;
    private Button Last_option;
    private Button Alarm2_option;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioGroup radioGroup;
    private Intent intent;
    private Handler mHandler;
    private String Ahead;
    private ArrayList Autotext_data1;
    private int flag=-2;
    private Iterator iterator1;
    private Iterator iterator2;
    private Context context;
    private String station_s;
    private String station_b;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ahead_bus);

        context = this.getApplicationContext();

        Find_option = (Button)findViewById(R.id.button3);
        Alarm_option = (Button)findViewById(R.id.button4);
        Last_option = (Button)findViewById(R.id.button13);
        Alarm2_option = (Button)findViewById(R.id.button14);


        radioButton1 = (RadioButton) findViewById(R.id.radioButton);
        radioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup2);

        intent = getIntent();

        station_s = intent.getStringExtra("Station_s");
        station_b = intent.getStringExtra("Station_b");

        mHandler = new Handler();

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!station_s.equals("NO")){
                            station_s = "1: "+ station_s  +" 방향";
                        }
                        else{
                            station_s = "경로가 없습니다.";
                        }

                        if (!station_b.equals("NO")) {
                            station_b = "2: "+ station_b +" 방향";
                        }
                        else{
                            station_b = "경로가 없습니다.";
                        }

                        radioButton1.setText(station_s);   // 공덕동으로 향하는 방향
                        radioButton2.setText(station_b);   // 국민대로 돌아오는 방향
                    }
                });


        Find_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton)findViewById(id);

                if (!func_check(rb)){
                    return;
                }

                Intent intent1 = new Intent(context, Find_bus.class);
                startActivity(intent1);
            }
        });
        Alarm_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton)findViewById(id);

                if (!func_check(rb)){
                    return;
                }

                Autotext_data1 = new ArrayList();

                Intent intent1 = new Intent(context, Alarm_quit_bus.class);
                intent1.putExtra("Flag",flag);
                startActivity(intent1);
            }
        });
        Last_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton)findViewById(id);

                if (!func_check(rb)){
                    return;
                }

                Intent intent1 = new Intent(context, Last_bus.class);
                startActivity(intent1);


            }
        });

        Alarm2_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton)findViewById(id);

                if (!func_check(rb)){
                    return;
                }

                Intent intent1 = new Intent(context, Alarm_boarding_bus.class);
                startActivity(intent1);

            }
        });
    }

    public Boolean func_check(RadioButton rb1){
        if(!Net_Check.isNetworkAvailable(context)){
            Toast.makeText(getApplicationContext(),"인터넷이 연결되어 있지 않습니다.",Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            Ahead = rb1.getText().toString().substring(0,1);
            Data_.setAhead(Ahead);
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),"방향을 선택해주세요",Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!rb1.getText().toString().contains(":")){
            Toast.makeText(getApplicationContext(),"경로가 없습니다. 다시 선택해주세요.",Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }



}
