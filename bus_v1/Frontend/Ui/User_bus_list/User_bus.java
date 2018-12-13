package com.example.sonhyeongi.bus_v1.Frontend.Ui.User_bus_list;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sonhyeongi.bus_v1.Function.Device.Data_base;
import com.example.sonhyeongi.bus_v1.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class User_bus extends Data_base {

    protected RecyclerView recyclerView;
    protected RecyclerView.Adapter adapter;
    private Button button;
    protected ArrayList<Listitem_user_bus> listitemList;
    private ArrayList all_station ;
    private HashMap<String,Integer> Station_s;
    private HashMap<String,Integer> Station_b;
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_bus_list);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        button = (Button)findViewById(R.id.button7);
        listitemList = new ArrayList<>();
        adapter = new User_bus_adapter(listitemList , this.getApplicationContext());
        recyclerView.setAdapter(adapter);

        makelist();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePreferencesAll();
                Toast.makeText(getApplicationContext(),"모든 버스가 삭제 되었습니다.",Toast.LENGTH_SHORT).show();
                makelist();
            }
        });
    }
    public void convert_hashmap (String input , HashMap<String, Integer> data){
        StringTokenizer stringTokenizer1 = new StringTokenizer(input,",");
        while (stringTokenizer1.hasMoreTokens()){
            StringTokenizer stringTokenizer2 = new StringTokenizer(stringTokenizer1.nextToken(),"=");
            while (stringTokenizer2.hasMoreTokens()){
                data.put(stringTokenizer2.nextToken().trim(),Integer.valueOf(stringTokenizer2.nextToken().trim()));
            }
        }
    }

    public void convert_arraylist (String input, ArrayList data){
        StringTokenizer stringTokenizer = new StringTokenizer(input, ",");
        while (stringTokenizer.hasMoreTokens()){
            data.add(stringTokenizer.nextToken().trim());
        }
    }


    public void makelist() {
        int count =  Integer.valueOf(getPreferences("total"));
        StringTokenizer stringTokenizer;
     //   RecyclerView.Adapter adapter;
        listitemList.clear();
        if (count==0){
            Listitem_user_bus listitem = new Listitem_user_bus(
                    "","","추가된 버스가 없습니다.",all_station,Station_s,Station_b,0
            );
            listitemList.add(listitem);
        }
        else{
            for (int i = 1; i <= count; ++i) {
                if (getPreferences(String.valueOf(i)).equals("0")){
                    continue;
                }
                String input = getPreferences(String.valueOf(i));
                String bus_id = null;
                String bus_number =null;
                String bus_info =null;
                all_station = new ArrayList();
                Station_s= new HashMap<>();
                Station_b= new HashMap<>();

                String a = null;
                String b = null;
                String c = null;
                int d = 0;

                stringTokenizer = new StringTokenizer(input,"&"); // 버스아이디&버스넘버 및 지역 &버스정보&전체정류장&정류장S&정류장B&turning
                while (stringTokenizer.hasMoreTokens()){
                    bus_id = stringTokenizer.nextToken();
                    bus_info = stringTokenizer.nextToken();
                    bus_number = stringTokenizer.nextToken();
                    a = stringTokenizer.nextToken();
                    a = a.substring(1,a.length()-1);
                    b = stringTokenizer.nextToken();
                    b = b.substring(1,b.length()-1);
                    c = stringTokenizer.nextToken();
                    c = c.substring(1,c.length()-1);
                    d = Integer.valueOf(stringTokenizer.nextToken());

                }
                convert_arraylist(a,all_station);
                convert_hashmap(b,Station_s);
                convert_hashmap(c,Station_b);

                Listitem_user_bus listitem = new Listitem_user_bus(
                        bus_id,bus_number,bus_info,all_station,Station_s,Station_b,d
                );

                listitemList.add(listitem);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        makelist();
        adapter.notifyDataSetChanged();
    }
}
