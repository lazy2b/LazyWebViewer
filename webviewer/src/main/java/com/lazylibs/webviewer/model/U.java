package com.lazylibs.webviewer.model;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;

import com.alibaba.fastjson.annotation.JSONField;
import com.lazylibs.webviewer.IGlobal;
import com.lazylibs.webviewer.utils.Xc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/***
 * 动态URL配置
 */
@Keep
public class U {
    @Keep
    public U(String k, String v, String w) {
        this.k = k;
        this.v = v;
        this.w = w;
    }

    @Keep
    public U() {
    }

    /***
     * 迷惑key, 没什么卵用
     */
    @JSONField(name = "k")
    public String k;
    /***
     * 版本更新接口
     */
    @JSONField(name = "v")
    public String v;
    /***
     * 主页地址
     */
    @JSONField(name = "w")
    public String w;

    @JSONField(serialize = false, deserialize = false)
    public String zv() {
        return Xc.akRaw(v, k);
    }

    @JSONField(serialize = false, deserialize = false)
    public String zw() {
        return Xc.akRaw(w, k);
    }

    @JSONField(serialize = false, deserialize = false)
    public Rur rzv(@Nullable String bad) {
        return rUrl(bad, zv());
    }

    @JSONField(serialize = false, deserialize = false)
    public Rur rzw(@Nullable String bad) {
        return rUrl(bad, zw());
    }

    @JSONField(serialize = false, deserialize = false)
    public Rur rzv() {
        return rUrl(null, zv());
    }

    @JSONField(serialize = false, deserialize = false)
    public Rur rzw() {
        return rUrl(null, zw());
    }

    /***
     * 随机获取地址
     * @param bad 代表当前失败的地址，如果不为空，则移除本地，并随机获取下一个
     * @param urls
     * @return
     */
    @JSONField(serialize = false, deserialize = false)
    private Rur rUrl(@Nullable String bad, String urls) {
        Rur result = new Rur();
        result.bad = bad;
        if (!IGlobal.isEmptyOrNull(urls)) {
            if (urls.contains(",")) {
                List<String> uList = new ArrayList<>(Arrays.asList(urls.split(",")));
                if (!IGlobal.isEmptyOrNull(bad)) {
                    uList.remove(bad);
                    result.replace = Arrays.toString(uList.toArray()).replace("[","").replace("]","").replace(" ","").trim();//.replace("");
                }
                result.url = ((uList.size() > 0) ? uList.get(new Random().nextInt(uList.size())) : bad);
            } else {
                result.url = urls;
            }
        }
        result.url = IGlobal.isEmptyOrNull(result.url)?"":result.url.trim();
        return result;
    }
}

