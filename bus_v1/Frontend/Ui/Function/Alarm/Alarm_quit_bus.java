package com.example.sonhyeongi.bus_v1.Frontend.Ui.Function.Alarm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sonhyeongi.bus_v1.Frontend.Ui.User_interface.Loading;
import com.example.sonhyeongi.bus_v1.Function.Data.Data_;
import com.example.sonhyeongi.bus_v1.Function.Device.Net_Check;
import com.example.sonhyeongi.bus_v1.R;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Alarm_quit_bus extends AppCompatActivity{

    private Button noneAlarm;
    private Button pushAlarm;

    private EditText Destination;
    private Intent intent;
    private String bus_number = null;
    private String destination;
    private String Ahead;
    private ArrayList All_station;
    private ArrayList All_bus_number;
    private Get_bus_numbers get_bus_numbers;
    private String json = null;
    private Context context;
    private Loading loading;

    class Get_bus_numbers extends Thread{
        int Ahead;
        public void setAhead(int ahead) {
            Ahead = ahead;
        }
        @Override
        public void run() {
            super.run();
            try {
                String URL = "http://13.209.133.231:8080/demo_war/busnumber?bus_id=";
                URL = URL.concat(Data_.getBus_id()).concat("&")
                        .concat("ahead=").concat(String.valueOf(Ahead)).concat("&")
                        .concat("turn=").concat(String.valueOf(Data_.getTurnning_station())).concat("&")
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
        setContentView(R.layout.alram_bus);

       // Bus_number = (EditText)findViewById(R.id.editText3);
        Destination = (EditText)findViewById(R.id.editText4);
        noneAlarm = (Button)findViewById(R.id.button8);
        pushAlarm = (Button)findViewById(R.id.button10);

        All_station = new ArrayList();
        All_bus_number = new ArrayList();
        context = this.getApplicationContext();

        intent = getIntent();
        get_bus_numbers = new Get_bus_numbers();

        loading = (Loading)Loading.getloading();


        final HashMap<String,Integer> S_s = Data_.getStation_s();
        final HashMap<String,Integer> S_b = Data_.getStation_b();
        Iterator iterator2 = S_s.keySet().iterator();
        Iterator iterator1 = S_b.keySet().iterator();


        if (Data_.getAhead().equals("1")){
             // 공덕동 쪽으로 나감

            while (iterator2.hasNext()){
                String data = iterator2.next().toString();
                if (Data_.getStation_s().get(data) > Data_.getUser_location_s()){
                    data = data.replace("\"","");
                    All_station.add(data);
                }
            }

            while (iterator1.hasNext()){
                String data = iterator1.next().toString();
                if (Data_.getStation_b().get(data) > Data_.getUser_location_b()
                        && !All_station.contains(data.replace("\"",""))){
                    data = data.replace("\"","");
                    All_station.add(data);
                }
            }
            get_bus_numbers.setAhead(1);

        }
        else{
            //국민대 쪽으로 들어옴

            while (iterator1.hasNext()){
                String data = iterator1.next().toString();
                if (Data_.getStation_b().get(data) > Data_.getUser_location_b()){
                    data = data.replace("\"","");
                    All_station.add(data);
                }
            }


            get_bus_numbers.setAhead(2);
        }
        get_bus_numbers.start();

        final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.editText4);
        autoCompleteTextView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, All_station));


        noneAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if(!Net_Check.isNetworkAvailable(context)){
                    Toast.makeText(getApplicationContext(),"인터넷이 연결되어 있지 않습니다.",Toast.LENGTH_SHORT).show();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    return;
                }
                destination = Destination.getText().toString();
                if ((!S_s.containsKey("\""+destination+"\"")) && (!S_b.containsKey("\""+destination+"\""))){
                    Toast.makeText(getApplicationContext(),"내리실 정거장 명칭에 오류가 있습니다.",Toast.LENGTH_SHORT).show();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    return;
                }
                Intent intent1 = new Intent(context, Popup_quit.class);
                intent1.putExtra("Ahead",Ahead);
                intent1.putExtra("My_Busnumber",bus_number);
                intent1.putExtra("Destination",destination);
                startActivity(intent1);
            }
        });

        pushAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if(!Net_Check.isNetworkAvailable(context)){
                    Toast.makeText(getApplicationContext(),"인터넷이 연결되어 있지 않습니다.",Toast.LENGTH_SHORT).show();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    return;
                }
                destination = Destination.getText().toString();
                if ((!S_s.containsKey("\""+destination+"\"")) && (!S_b.containsKey("\""+destination+"\""))){
                    Toast.makeText(getApplicationContext(),"내리실 정거장 명칭에 오류가 있습니다.",Toast.LENGTH_SHORT).show();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    return;
                }
                Intent intent1 = new Intent(context, Popup_quit_push.class);
                intent1.putExtra("Ahead",Ahead);
                intent1.putExtra("My_Busnumber",bus_number);
                intent1.putExtra("Destination",destination);
                startActivity(intent1);
            }
        });


    }
    @Override
    protected void onResume() {
        super.onResume();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
