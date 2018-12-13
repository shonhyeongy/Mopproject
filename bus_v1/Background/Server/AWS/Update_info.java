package com.example.sonhyeongi.bus_v1.Background.Server.AWS;

import org.jsoup.Jsoup;

public class Update_info {


    public static boolean conn(String busnumber, String location, String info) {
        String json = null;
        String URL = "http://13.209.133.231:8080/demo_war/update?Busnumber=";

        //http://13.209.133.231:8080/demo_war/update?Busnumber=110A&Location=%EC%84%9C%EC%9A%B8&Businfo=%EB%8F%99%ED%95%B4%EB%AC%BC%EA%B3%BC%EB%B0%B1%EB%91%90%EC%82%B0%EC%9D%B4%EB%A7%88%EB%A5%B4%EA%B3%A0%EB%8B%B3%EB%8F%84%EB%A1%9D

        boolean flag = false;
        final String url = URL.concat(busnumber).concat("&")
                .concat("Location=").concat(location).concat("&")
                .concat("Businfo=").concat(info);
        try {
            json = Jsoup.connect(url).ignoreContentType(true).execute().body();
            //Log.d("JSON", json);

            if (json.trim().equals("true")){
                flag = true;
            }
            else{
                flag = false;
            }


        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }


}
