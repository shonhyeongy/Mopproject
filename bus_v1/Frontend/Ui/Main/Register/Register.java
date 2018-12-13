package com.example.sonhyeongi.bus_v1.Frontend.Ui.Main.Register;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sonhyeongi.bus_v1.Background.Server.Mysql.Connect_mysql;
import com.example.sonhyeongi.bus_v1.Function.Data.Data_;
import com.example.sonhyeongi.bus_v1.R;

public class Register extends Activity {

    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private EditText editText4;
    private EditText editText5;
    private boolean result;
    private Handler handler;

    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_register);

        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        editText3 = (EditText) findViewById(R.id.editText3);
        editText4 = (EditText) findViewById(R.id.editText4);
        editText5 = (EditText) findViewById(R.id.editText5);
        button = (Button) findViewById(R.id.button1);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = editText1.getText().toString();
                final String pw = editText2.getText().toString();
                final String name = editText3.getText().toString();
                final String age = editText4.getText().toString();
                final String number = editText5.getText().toString();


                if (id.equals("") || pw.equals("") || name.equals("") || age.equals("") || number.equals("")) {
                    Toast.makeText(getApplicationContext(), "모든 사항을 적어주세요", Toast.LENGTH_SHORT).show();
                } else {
                    final Connect_mysql connect_mysql = new Connect_mysql();
                    handler = new Handler();
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            result = connect_mysql.conn(id, pw, name, age, Data_.getDevice_value(),number);

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (result) {
                                        Toast.makeText(getApplicationContext(), "가입완료", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "가입실패", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }.start();


                }
            }
        });


    }
}

