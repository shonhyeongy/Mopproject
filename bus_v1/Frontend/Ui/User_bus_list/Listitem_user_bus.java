package com.example.sonhyeongi.bus_v1.Frontend.Ui.User_bus_list;

import java.util.ArrayList;
import java.util.HashMap;

public class Listitem_user_bus { // 버스아이디&버스넘버 및 지역 &버스정보&전체정류장&정류장S&정류장B
    private String bus_id;
    private String bus_number;
    private String bus_info;
    private ArrayList All_station;
    private HashMap<String, Integer> Station_s;
    private HashMap<String, Integer> Station_b;
    private int Turning;

    public Listitem_user_bus(String bus_id, String bus_number, String bus_info, ArrayList all_station, HashMap<String, Integer> station_s, HashMap<String, Integer> station_b, int turning) {
        this.bus_id = bus_id;
        this.bus_number = bus_number;
        this.bus_info = bus_info;
        All_station = all_station;
        Station_s = station_s;
        Station_b = station_b;
        Turning = turning;
    }

    public ArrayList getAll_station() {
        return All_station;
    }

    public HashMap<String, Integer> getStation_s() {
        return Station_s;
    }

    public HashMap<String, Integer> getStation_b() {
        return Station_b;
    }

    public int getTurning() {
        return Turning;
    }

    public String getBus_id() {
        return bus_id;
    }

    public String getBus_number() {
        return bus_number;
    }

    public String getBus_info() {
        return bus_info;
    }
}
