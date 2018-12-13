package com.example.sonhyeongi.bus_v1.Frontend.Ui.None_bus_list;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sonhyeongi.bus_v1.Function.Data.Data_;
import com.example.sonhyeongi.bus_v1.Function.Device.Data_base;
import com.example.sonhyeongi.bus_v1.Function.Device.Net_Check;
import com.example.sonhyeongi.bus_v1.R;
import com.example.sonhyeongi.bus_v1.Frontend.Ui.User_interface.Location;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class None_bus_adapter extends RecyclerView.Adapter<None_bus_adapter.ViewHolder> {

    private List<Listitem_none_bus> listItems;
    private Context context;
    private ArrayList All_station;
    private String json = null;


    public None_bus_adapter(List<Listitem_none_bus> listItems , Runnable runnable , Context context) {
        this.listItems = listItems;
        this.context = context;
    }


    public void insert(Iterator iterator_){
        while (iterator_.hasNext()){
            String input_data = iterator_.next().toString();
            input_data = input_data.replaceAll("\"","");
            if (!All_station.contains(input_data)){
                All_station.add(input_data);
            }
        }

    }

    public HashMap<String, Integer> json_to_hashmap (JSONObject input){
        HashMap<String, Integer> data = new HashMap<>();
        Iterator iterator = input.keys();
        while (iterator.hasNext()){
            String key = iterator.next().toString();
            try {
                int value = Integer.parseInt(input.get(key).toString());
                data.put(key,value);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return data;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)  {
        final Listitem_none_bus listitem = listItems.get(position);

        holder.textViewhead.setText(listitem.getHead());
        holder.textViewdesc.setText(listitem.getDesc());

        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(!Net_Check.isNetworkAvailable(context)){
                    Toast.makeText(context,"인터넷이 연결되어 있지 않습니다.",Toast.LENGTH_SHORT).show();
                    return false;
                }
                final Context context = v.getContext();
                final Intent intent = new Intent(context ,Data_base.class);
                Data_.setBus_id(listitem.getId());
                new Thread(){
                    public void run(){
                            try {
                                String URL = "http://13.209.133.231:8080/demo_war/station?busid=";
                                final String url = URL.concat(Data_.getBus_id());
                                json = Jsoup.connect(url).ignoreContentType(true).execute().body();
                                //Log.d("JSON", json);
                                JSONObject jsonObject = new JSONObject(json);

                                All_station = new ArrayList();

                                Data_.setStation_s(json_to_hashmap((JSONObject) jsonObject.get("Station_s")));
                                Data_.setStation_b(json_to_hashmap((JSONObject) jsonObject.get("Station_b")));

                                Data_.setTurnning_station(jsonObject.getInt("Turning_position"));

                                Iterator iterator = Data_.getStation_s().keySet().iterator();
                                insert(iterator);
                                iterator = Data_.getStation_b().keySet().iterator();
                                insert(iterator);

                                Data_.setAll_station(All_station);
                                intent.putExtra("Bus_info",listitem.getDesc());
                                intent.putExtra("Bus_number",listitem.getHead());
                                context.startActivity(intent);
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                    }
                }.start();
                return true;
            }
        });


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Net_Check.isNetworkAvailable(context)){
                    Toast.makeText(context,"인터넷이 연결되어 있지 않습니다.",Toast.LENGTH_SHORT).show();
                    return;
                }
                final Context context = view.getContext();
                Log.d("MSG1",listitem.getDesc());
                final Intent intent2 = new Intent(context, Location.class);
                Data_.setBus_id(listitem.getId());

                    new Thread(){
                        public void run(){
                            try {
                                    All_station = new ArrayList();
                                    String URL = "http://13.209.133.231:8080/demo_war/station?busid=";
                                    final String url = URL.concat(Data_.getBus_id());
                                    json = Jsoup.connect(url).ignoreContentType(true).execute().body();
                                    JSONObject jsonObject = new JSONObject(json);

                                    Data_.setStation_s(json_to_hashmap((JSONObject) jsonObject.get("Station_s")));
                                    Data_.setStation_b(json_to_hashmap((JSONObject) jsonObject.get("Station_b")));
                                    Data_.setTurnning_station(jsonObject.getInt("Turning_position"));


                                    Iterator iterator = Data_.getStation_s().keySet().iterator();
                                    insert(iterator);
                                    iterator = Data_.getStation_b().keySet().iterator();
                                    insert(iterator);

                                    Data_.setAll_station(All_station);
                                    context.startActivity(intent2);



                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    }.start();
            }
        });

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewhead;
        public TextView textViewdesc;
        public LinearLayout linearLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            textViewhead = (TextView) itemView.findViewById(R.id.textView1);
            textViewdesc = (TextView) itemView.findViewById(R.id.textView2);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);

        }
    }
}
