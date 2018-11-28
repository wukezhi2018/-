package top.wukezhi.personalmanagement.util;

import cn.bmob.v3.BmobObject;

public class Person extends BmobObject{
    private String name;
    private String address;

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {

        return name;
    }

    public String getAddress() {
        return address;
    }
}
