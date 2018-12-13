package com.example.sonhyeongi.bus_v1.Frontend.Ui.User_interface;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.sonhyeongi.bus_v1.Frontend.Ui.None_bus_list.Listitem_none_bus;
import com.example.sonhyeongi.bus_v1.Frontend.Ui.None_bus_list.bus_list;
import com.example.sonhyeongi.bus_v1.Function.Data.Data_;
import com.example.sonhyeongi.bus_v1.Function.Device.Net_Check;
import com.example.sonhyeongi.bus_v1.R;

import java.util.ArrayList;
import java.util.HashMap;

public class bus_search extends AppCompatActivity {

    private Button button1;
    private EditText editText;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioGroup radioGroup;
    private Context context;
    private HashMap a;
    private ArrayList Bus_id ;
    private ArrayList Bus_info ;
    private ArrayList Bus_number_arr;
    private int count;
    private String key;

    private String Location;
    private String Bus_number;
    private Handler mHandler;
    private Intent intent;
    private Listitem_none_bus listitem;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_search_m);

        button1 = (Button) findViewById(R.id.button_s);
        radioButton1 = (RadioButton) findViewById(R.id.radioButton_s);
        radioButton2 = (RadioButton) findViewById(R.id.radioButton_i);
        editText = (EditText) findViewById(R.id.editText);

        final RadioGroup rg = (RadioGroup)findViewById(R.id.radioGroup1);

        intent = new Intent(this, bus_list.class);

        context = this.getApplicationContext();

        button1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Net_Check.isNetworkAvailable(context)) {
                    Toast.makeText(getApplicationContext(), "인터넷이 연결되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                int id = rg.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(id);
                try {
                    Location = rb.getText().toString();

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "지역을 선택해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                Bus_number = editText.getText().toString();
                if (Bus_number.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "버스 번호를 입력해주세", Toast.LENGTH_SHORT).show();
                    return;
                }
                Bus_number = Bus_number.replaceAll("[a-zA-Z_()]", "");

                Data_.setLocation(Location);
                Data_.setBus_number(Bus_number);
                startActivity(intent);

            }
        });
    }
}

