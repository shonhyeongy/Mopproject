//package com.example.sonhyeongi.bus_v1.Function;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import org.jsoup.Jsoup;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Set;
//
//public class Get_bus_station {
//
//    public static ArrayList Bus_Staion(String Busid) throws IOException{
//        String URL3 = "https://map.naver.com/pubtrans/getBusRouteInfo.nhn?busID=";
//
//        String BusID = Busid;
//        String URL4 = URL3 + BusID;
//
//        String json2 = Jsoup.connect(URL4).ignoreContentType(true).execute().body();
//
//        JsonParser parser = new JsonParser();
//        JsonObject jsonObject1 = (JsonObject) parser.parse(json2);
//
//        JsonElement a = jsonObject1.getAsJsonObject().get("result");
//
//        String Station_name = "";
//
//        HashMap <String,Integer> Station_s = new HashMap<String,Integer>();
//        HashMap <String,Integer> Station_b = new HashMap<String,Integer>();
//
//        ArrayList result = new ArrayList();
//
//        int Total_station = jsonObject1.getAsJsonObject().get("result")
//                .getAsJsonObject().get("station")
//                .getAsJsonArray().size();
//
//        int Turning_station = Integer.valueOf(jsonObject1.getAsJsonObject().get("result")
//                .getAsJsonObject().get("turningPointIdx").toString());
//
//       // Data_.setTurnning_station(Turning_station);
//
//        String Start_position = (jsonObject1.getAsJsonObject().get("result")
//                .getAsJsonObject().get("busStartPoint")).toString();
//
//        String End_position = (jsonObject1.getAsJsonObject().get("result")
//                .getAsJsonObject().get("busEndPoint")).toString();
//
//        for (int count2 = 0; count2<Total_station; count2++){
//            Station_name = jsonObject1.getAsJsonObject().get("result")
//                    .getAsJsonObject().get("station")
//                    .getAsJsonArray().get(count2)
//                    .getAsJsonObject().get("stationName").toString();
//            if (count2==Turning_station){
//                Station_s.put(Station_name,count2);
//                Station_b.put(Station_name,count2);
//            }
//            else if (count2<Turning_station){
//                Station_s.put(Station_name,count2);
//            }
//            else if (count2>Turning_station){
//                Station_b.put(Station_name,count2);
//            }
//        }
//        result.add(Station_s);
//        result.add(Station_b);
//        result.add(Start_position);
//        result.add(End_position);
//
//
//     //   System.out.println(result);
//
//        return result;
//
//    }
//}
