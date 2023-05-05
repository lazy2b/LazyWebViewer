package com.lazylibs.webviewer.model;


import androidx.annotation.Keep;

import com.alibaba.fastjson.annotation.JSONField;
import com.lazylibs.webviewer.IGlobal;
import com.lazylibs.webviewer.utils.Xc;

//    {"k":"",
//    "dz":"",
//    "du":""}
@Keep
public class  Pz {
    @Keep
    public Pz() {
    }
    @Keep
    public Pz(String k, String du, String dp) {
        this.k = k;
        this.du = du;
        this.dp = dp;
    }

    /***
     * key
     */
    @JSONField(name = "k")
    public String k = "";
    /***
     * 請求地址
     */
    @JSONField(name = "du")
    public String du = "";
    /***
     * 默認配置
     */
    @JSONField(name = "dp")
    public String dp = "";

    @JSONField(serialize = false,deserialize = false)
    public String zdu() {
        return Xc.akRaw(du, k);
    }

    @JSONField(serialize = false,deserialize = false)
    public String zdp() {
        return Xc.akRaw(dp, IGlobal.pk);
    }
}