package com.example.sonhyeongi.bus_v1.Background.Server.Naver;
import android.util.Log;

import com.example.sonhyeongi.bus_v1.Function.Data.Data_;
import com.example.sonhyeongi.bus_v1.Background.Server.AWS.Update_info;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;

public class Get_bus_information  {


    public static HashMap<String, String> get_bus_number (String bus_num) throws IOException{

        String update_info = "";
        String URl = "https://m.search.naver.com/search.naver?query=";

        String Bus_number = bus_num + "번";
        String URL = URl + Bus_number ;

        Document dc = Jsoup.connect(URL).timeout(6000).get();

        HashMap<String, String> Bus_information = new HashMap<>();

        Elements only_bus_id_data = dc.select("#cs_bus_realinfo > div.bus_area > div.tab_area.v2").select("div.sct_station");

        if (only_bus_id_data.isEmpty()){
            only_bus_id_data = dc.select(" #cs_bus_realinfo > div.bus_area > div.tab_area").select("div.sct_station");
        }

        int count1 = 0;
        String bus_number;
        String bus_id;
        String bus_info;

        while (count1<only_bus_id_data.size()){
            bus_id = only_bus_id_data.get(count1).attr("data-routeid");
            bus_number = only_bus_id_data.get(count1).select("span.bus_number").text();
            bus_info = only_bus_id_data.get(count1).select("div.course").text();
            bus_info = bus_number + "&" + bus_info;
            update_info = update_info.concat(bus_id+"X" + bus_info.replaceAll("&","Y") + "Z");
            count1++;
            Bus_information.put(bus_id,bus_info);
        }
        Log.d("DATA1",update_info);
        update_info = update_info.replaceAll(" ","T");


        if (!only_bus_id_data.isEmpty()){
            try {
                if(Update_info.conn(Data_.getBus_number(),Data_.getLocation(),update_info)){
                    Log.d("UPDATA RESULT : ", "TRUE");
                }
                else{
                    Log.d("UPDATA RESULT : ", "FALSE");
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }



        Log.d("DATA2",update_info);
        return Bus_information;
    }
}
