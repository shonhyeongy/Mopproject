package com.example.sonhyeongi.bus_v1.Function.Device;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sonhyeongi.bus_v1.Function.Data.Data_;
import com.example.sonhyeongi.bus_v1.R;

public class Data_base extends Activity{

    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editor;
    private EditText editText;
    private Button button;
    private Intent intent;
    private int total;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_add);

        sharedPreferences = getSharedPreferences("pref",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        button = (Button)findViewById(R.id.button1);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = getIntent();
                String info = intent.getStringExtra("Bus_info");
                String bus_number = intent.getStringExtra("Bus_number");
                total = 1;
                if (getPreferences("total")!="0"){
                    total = total + Integer.valueOf(getPreferences("total"));
                }

                info =  Data_.getBus_id()+"&"+ bus_number +"&" + info +"&" +Data_.getAll_station()+"&"+
                        Data_.getStation_s()+"&"+Data_.getStation_b()+"&"+Data_.getTurnning_station();
                if(!savePreferences(String.valueOf(total),info , Data_.getBus_id())){
                    Toast.makeText(getApplicationContext(), "추가완료" ,Toast.LENGTH_SHORT).show();
                }
                mOnClose(v);
            }
        });
    }

    public String getPreferences(String busid){
        return sharedPreferences.getString(busid,"0");
    }


     //info = 버스아이디&버스넘버 및 지역 &버스정보&전체정류장&정류장S&정류장B&turning
    public boolean savePreferences(String bus_total , String info_ , String bus_id){
        boolean flag = false; // 정보가 없을때
        if (getPreferences(bus_id)!="0"){
            Toast.makeText(getApplicationContext(), "이미 추가된 버스 입니다." ,Toast.LENGTH_SHORT).show();
            return true;
        }
        editor.putString(bus_total , info_);
        editor.putString(bus_id,"1");
        editor.putString("total",bus_total);
        editor.commit();
        return flag;
    }

    public void removePreferences(String key , String busid_ ){
        int total = Integer.valueOf(getPreferences("total"));
        String info = getPreferences(String.valueOf(total));
        editor.putString(key,info);
        editor.remove(String.valueOf(total));
        editor.putString("total",String.valueOf(total-1));
        editor.remove(busid_);
        editor.commit();
    }

    public void removePreferencesAll(){
        editor.clear();
        editor.commit();
    }

    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()== MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    public String findbuskey(String busid_){
        String result="0";
        int total = Integer.valueOf(getPreferences("total"));
        for (int i =1; i<=total; ++i){
            String busid = getPreferences(String.valueOf(i)).split("&")[0];
            if (busid.equals(busid_)){
                result = String.valueOf(i);
            }
        }

        return result;
    }

    public void mOnClose(View v){

        setResult(RESULT_OK);


        finish();
    }

    public boolean login(){
        boolean flag1 = true;
        sharedPreferences = getSharedPreferences("pref",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String flag  = sharedPreferences.getString("register","0");
        Log.d("FLAG",flag);

        if (flag.equals("0")){
            flag1 = false;
        }
        return flag1;

    }
    public void register(){
        sharedPreferences = getSharedPreferences("pref",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("register", "1");
        editor.commit();
    }




}
