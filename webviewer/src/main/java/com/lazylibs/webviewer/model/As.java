package com.lazylibs.webviewer.model;


import androidx.annotation.Keep;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.HashMap;

@Keep
public class As implements Serializable {
    /***
     * 首页地址
     */
    @JSONField(name = "link")
    public String link = "";
    /***
     * 币种代码
     */
    @JSONField(name = "currency")
    public String currency = "";
    /***
     * Adjust事件列表
     */
    @JSONField(name = "eventTknMap")
    public HashMap<String, String> eventTknMap = new HashMap<>();

    @Keep
    public As() {
    }
    @Keep
    public As(String link, String currency, HashMap<String, String> eventTknMap) {
        this.link = link;
        this.currency = currency;
        this.eventTknMap = eventTknMap;
    }
}
