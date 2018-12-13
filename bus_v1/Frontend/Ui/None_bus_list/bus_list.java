
package com.example.sonhyeongi.bus_v1.Frontend.Ui.None_bus_list;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.sonhyeongi.bus_v1.Background.Server.Naver.Get_bus_information;
import com.example.sonhyeongi.bus_v1.Frontend.Ui.Main.MainActivity;
import com.example.sonhyeongi.bus_v1.Frontend.Ui.User_interface.Loading;
import com.example.sonhyeongi.bus_v1.Frontend.Ui.User_interface.Loading_thread;
import com.example.sonhyeongi.bus_v1.Function.Data.Data_;
import com.example.sonhyeongi.bus_v1.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class bus_list extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private Intent intent;
    private List<Listitem_none_bus> listitemList;
    private HashMap a;
    private ArrayList Bus_id = new ArrayList();
    private ArrayList Bus_info = new ArrayList();
    private ArrayList Bus_number = new ArrayList();
    private int count;
    private String key;
    Handler mHandler;
    private Context context;
    private ProgressDialog progressDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_list);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        context = this.getApplicationContext();

        listitemList = new ArrayList<>();

        final Intent intent_test = new Intent(this, Loading.class);

        intent = getIntent();
        mHandler = new Handler();


        final Loading_thread loading_thread = new Loading_thread();
        final getinfo getinfo = new getinfo();
       // getinfo.start();

        new Thread(){
            @Override
            public void run() {
                super.run();
                startActivity(intent_test);
                loading_thread.start();
                try {
                    loading_thread.join();
                    sleep(250);
                }catch (Exception e){
                    e.printStackTrace();
                }
                getinfo.start();
                try {
                    getinfo.join();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                Loading loading = (Loading) Loading.getloading();
                loading.finish();
            }
        }.start();
















    }

    class getinfo extends Thread{
        public void run() {
            try {

                a = Get_bus_information.get_bus_number(Data_.getLocation() + Data_.getBus_number());
                count = 0;
                Iterator iterator = a.keySet().iterator();
                while (iterator.hasNext()) {
                    key = iterator.next().toString();
                    StringTokenizer stringTokenizer = new StringTokenizer(a.get(key).toString(), "&");

                    Bus_number.add(stringTokenizer.nextToken());

                    Bus_info.add(stringTokenizer.nextToken());
                    Bus_id.add(key);

                    count++;

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < count; i++) {
                        Listitem_none_bus listitem = new Listitem_none_bus(
                                Bus_number.get(i).toString(),//Bus_number.get(i),
                                Bus_info.get(i).toString(),
                                Bus_id.get(i).toString());

                        listitemList.add(listitem);
                        adapter = new None_bus_adapter(listitemList, this, context);
                        recyclerView.setAdapter(adapter);
                    }
                }
            });
        }
    }
}
