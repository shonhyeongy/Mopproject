package com.example.sonhyeongi.bus_v1.Frontend.Ui.None_bus_list;
public class Listitem_none_bus {

    private String head;
    private String desc;
    private String id;

    public Listitem_none_bus(String head, String desc, String id) {
        this.head = head;
        this.desc = desc;
        this.id = id;
    }

    public String getHead() {
        return head;
    }

    public String getDesc() {
        return desc;
    }

    public String getId(){return id;}
}
