package com.example.sonhyeongi.bus_v1.Frontend.Ui.User_bus_list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sonhyeongi.bus_v1.R;

public class User_bus_select_delete extends User_bus {
    private Button button;
    private Intent intent;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
//
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_delete);

        button = (Button)findViewById(R.id.button1);

        // removePreferencesAll();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = getIntent();
                Context context = v.getContext();
                String bus_id = intent.getStringExtra("position");
                String key = findbuskey(bus_id);
                removePreferences(key,bus_id);
                Toast.makeText(getApplicationContext(), "삭제 완료" ,Toast.LENGTH_SHORT).show();
                listitemList.clear();
                onResume();
                finish();

            }
        });
    }



    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()== MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }



}
