//package com.example.sonhyeongi.bus_v1.Frontend.Ui;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.media.MediaPlayer;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.Window;
//import android.widget.ArrayAdapter;
//import android.widget.AutoCompleteTextView;
//import android.widget.TextView;
//
//import com.example.sonhyeongi.bus_v1.Function.Data.Data_;
//
//import com.example.sonhyeongi.bus_v1.R;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//public class Popup2 extends Activity {
//
//    private TextView txtText;
//    private Intent intent;
//    private HashMap<String,Integer> Station_s ;
//    private HashMap <String,Integer> Station_b ;
//    private String bus_number;
//    private String destination;
//    private String Ahead;
//    private String Bus_id;
//    private int flag;
//
//    private MediaPlayer audioPlayer;
//    private ArrayList All_station;
//    private AutoCompleteTextView autoCompleteTextView;
//    private String[] data;
//
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.popup_add);
//
//
//        txtText = (TextView) findViewById(R.id.txtText);
////        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
//
////        data = Data_.getAll_station().toArray(new String[Data_.getAll_station().size()]);
//
////        final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
//        autoCompleteTextView.setAdapter(new ArrayAdapter<String>(this,
//                android.R.layout.simple_dropdown_item_1line, Data_.getAll_station()));
//
//
//        try {
//            Get_bus_station.Bus_Staion(Data_.getBus_id());
//            Log.d("data",Data_.getData().toString());
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
//    }
//
//    public boolean onTouchEvent(MotionEvent event) {
//        //바깥레이어 클릭시 안닫히게
//        if(event.getAction()== MotionEvent.ACTION_OUTSIDE){
//            return false;
//        }
//        return true;
//    }
//    @Override
//    public void onBackPressed() {
//        return;
//    }
//
//    public void mOnClose(View v){
//
//        setResult(RESULT_OK, intent);
//        finish();
//    }
//}
