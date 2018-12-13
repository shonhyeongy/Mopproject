package com.example.sonhyeongi.bus_v1.Function.Data;

import android.app.Activity;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class Data_ {
    static private HashMap<String, Integer> Station_s = null;
    static private HashMap<String, Integer> Station_b = null;
    static private ArrayList All_station = null;
    static private ArrayList Data = null;
    static private String Bus_id = null;
    static private String Bus_number = null;
    static private String Location = null;  // 지역
    static private String User_location = null;  // 유저 정류장 스트링형식
    static private int User_location_s = 0; // 유저 정류장 나가는거 정수형식
    static private int User_location_b = 0; // 유저 정류장 돌아오는거 정수형식
    static private int Turnning_station = 0;
    static private String Device_value = null;
    static private String Ahead = null;
    static private Activity Loading = null;

    public static Activity getLoading() {
        return Loading;
    }

    public static void setLoading(Activity loading) {
        Loading = loading;
    }

    public static String getAhead() {
        return Ahead;
    }

    public static void setAhead(String ahead) {
        Ahead = ahead;
    }

    public static String getDevice_value() {
        return Device_value;
    }

    public static void setDevice_value(String device_value) {
        Device_value = device_value;
    }

    public static String getUser_location() {
        return User_location;
    }

    public static void setUser_location(String user_location) {
        User_location = user_location;
    }

    public static ArrayList getAll_station() {
        return All_station;
    }

    public static void setAll_station(ArrayList all_station) {
        All_station = all_station;
    }

    public static HashMap<String, Integer> getStation_s() {
        return Station_s;
    }

    public static String getLocation() {
        return Location;
    }

    public static int getTurnning_station() {
        return Turnning_station;
    }

    public static void setTurnning_station(int turnning_station) {
        Turnning_station = turnning_station;
    }

    public static void setLocation(String location) {
        Location = location;
    }


    public static int getUser_location_s() {
        return User_location_s;
    }

    public static void setUser_location_s(int user_location_s) {
        User_location_s = user_location_s;
    }

    public static int getUser_location_b() {
        return User_location_b;
    }

    public static void setUser_location_b(int user_location_b) {
        User_location_b = user_location_b;
    }

    public static HashMap<String, Integer> getStation_b() {
        return Station_b;
    }

    public static ArrayList getData() {
        return Data;
    }

    public static String getBus_id() {
        return Bus_id;
    }

    public static String getBus_number() {
        return Bus_number;
    }

    public static void setStation_s(HashMap<String, Integer> station_s) {
        Station_s = station_s;
    }

    public static void setStation_b(HashMap<String, Integer> station_b) {
        Station_b = station_b;
    }

    public static void setData(ArrayList data) {
        Data = data;
    }

    public static void setBus_id(String bus_id) {
        Bus_id = bus_id;
    }

    public static void setBus_number(String bus_number) {
        Bus_number = bus_number;
    }
}
