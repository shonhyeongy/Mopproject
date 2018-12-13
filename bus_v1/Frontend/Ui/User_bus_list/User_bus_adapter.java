package com.example.sonhyeongi.bus_v1.Frontend.Ui.User_bus_list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sonhyeongi.bus_v1.Function.Data.Data_;
import com.example.sonhyeongi.bus_v1.Function.Device.Net_Check;
import com.example.sonhyeongi.bus_v1.R;
import com.example.sonhyeongi.bus_v1.Frontend.Ui.User_interface.Location;

import java.util.ArrayList;
import java.util.List;

public class User_bus_adapter extends RecyclerView.Adapter<User_bus_adapter.ViewHolder> {
    private List<Listitem_user_bus> list;
    private Context context;


    public User_bus_adapter(List<Listitem_user_bus> list, Context context) {
        this.list = list;
        this.context = context;
        ArrayList<Listitem_user_bus> list2 = new ArrayList<>();
        Listitem_user_bus listitem = new Listitem_user_bus(
                "", "", "추가된 버스가 없습니다.", null, null, null,0
        );
        list2.add(listitem);
        if (this.list == null) {
            this.list = list2;
        }
    }

    @NonNull
    @Override
    public User_bus_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        final Listitem_user_bus listitem = list.get(position);
        holder.textViewhead.setText(listitem.getBus_info());
        holder.textViewdesc.setText(listitem.getBus_number());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Net_Check.isNetworkAvailable(context)){
                    Toast.makeText(context,"인터넷이 연결되어 있지 않습니다.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (listitem.getBus_id().equals("")){
                    return;
                }
                Data_.setBus_id(listitem.getBus_id());
                Data_.setStation_s(listitem.getStation_s());
                Data_.setStation_b(listitem.getStation_b());
                Data_.setAll_station(listitem.getAll_station());
                Data_.setTurnning_station(listitem.getTurning());
                Context context = v.getContext();
                Intent intent = new Intent(context , Location.class);
                context.startActivity(intent);

            }
        });

        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listitem.getBus_id().equals("")){
                    return false;
                }
                Context context = v.getContext();
                Intent intent = new Intent(context,User_bus_select_delete.class);
                intent.putExtra("position",listitem.getBus_id());
                context.startActivity(intent);
                return true;

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

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

    class getresult extends Activity{
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }
        public void start_activity (Context context ,Class input , String input2) {
            Intent intent = new Intent(context,input);
            intent.putExtra("position",input2);
            startActivityForResult(intent,1);

        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 1) {
                if(resultCode == Activity.RESULT_OK){
                    String result=data.getStringExtra("result");
                    notifyDataSetChanged();
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    return;
                }
            }
        }
    }

}


