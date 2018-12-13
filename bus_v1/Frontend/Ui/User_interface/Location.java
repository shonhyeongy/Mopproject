package com.example.sonhyeongi.bus_v1.Frontend.Ui.User_interface;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.sonhyeongi.bus_v1.Frontend.Ui.User_interface.Ahead_bus;
import com.example.sonhyeongi.bus_v1.Function.Data.Data_;
import com.example.sonhyeongi.bus_v1.Function.Device.Net_Check;
import com.example.sonhyeongi.bus_v1.R;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Location extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteTextView;
    private Button button;
    private List<String> arr ;
    private Intent intent;
    private Context context;

    String find_next_station(int user_location_ ,Iterator iterator_ , int flag){
        String next_station_ = null;
        HashMap<String , Integer> data = null;
        if (flag ==0){
            data = Data_.getStation_s();
        }
        else if (flag ==1){
            data = Data_.getStation_b();
        }

        while (iterator_.hasNext()){
            String new_station = iterator_.next().toString();
            if (user_location_+1 == data.get(new_station)){
                next_station_ = new_station;
            }
        }
        return  next_station_;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);
        context = this.getApplicationContext();

        button = (Button)findViewById(R.id.button);

        arr = Data_.getAll_station();
        Log.d("here",arr.toString());

        final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView2);
        autoCompleteTextView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, arr));

        intent = new Intent( this,Ahead_bus.class);


        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Net_Check.isNetworkAvailable(context)){
                    Toast.makeText(getApplicationContext(),"인터넷이 연결되어 있지 않습니다.",Toast.LENGTH_SHORT).show();
                    return;
                }
                String station = autoCompleteTextView.getText().toString();
                if (!arr.contains(station)){
                    Toast.makeText(getApplicationContext(),"현재 정거장 명칭에 오류가 있습니다.",Toast.LENGTH_SHORT).show();
                    return;
                }
                Data_.setUser_location(station);

                station = "\"" + station + "\"";

                if (Data_.getStation_s().containsKey(station)){
                    int user_location = Data_.getStation_s().get(station);
                    Data_.setUser_location_s(user_location);
                    String next_station;
                    Iterator iterator = Data_.getStation_s().keySet().iterator();
                    next_station = find_next_station(user_location,iterator, 0);
                    intent.putExtra("Station_s", "현재 정거장 : "+ station.replaceAll("\"","") +"\n"+ "다음 정거장 : "+ next_station.replaceAll("\"",""));
                }
                else{
                    intent.putExtra("Station_s", "NO");
                }

                if (Data_.getStation_b().containsKey(station)){
                    int user_location = Data_.getStation_b().get(station);
                    Data_.setUser_location_b(user_location);
                    String next_station;
                    Iterator iterator = Data_.getStation_b().keySet().iterator();
                    next_station = find_next_station(user_location,iterator, 1);
                    intent.putExtra("Station_b", "현재 정거장 : "+ station.replaceAll("\"","") +"\n"+ "다음 정거장 : "+ next_station.replaceAll("\"",""));
                }
                else{
                    intent.putExtra("Station_b", "NO");
                }
                startActivity(intent);
            }
        });
    }
}
