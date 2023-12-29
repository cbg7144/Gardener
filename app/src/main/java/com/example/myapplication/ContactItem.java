package com.example.myapplication;

public class ContactItem {
    String name;
    String number;
    int resourceId;

    public ContactItem(String name, String number, int resourceId){
        this.name = name;
        this.number = number;
        this.resourceId = resourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }
}
