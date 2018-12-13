package com.example.sonhyeongi.bus_v1.Frontend.Ui.User_interface;

import android.util.Log;

import com.example.sonhyeongi.bus_v1.Function.Data.Data_;

public class Loading_thread extends Thread {
        public void run() {
            super.run();
            while (Data_.getLoading()==null){
                Data_.setLoading((Loading)Loading.getloading());
                try {
                    sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            //Log.d("END","END");
        }
}
