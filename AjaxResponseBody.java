package com.hiveag.geepy.validation3;

import java.util.List;

import com.hiveag.geepy.pojo.Bike;

public class AjaxResponseBody {

    String msg;
    List<Bike> result;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Bike> getResult() {
        return result;
    }

    public void setResult(List<Bike> result) {
        this.result = result;
    }

}
