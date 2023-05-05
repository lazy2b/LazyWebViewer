package com.lazylibs.webviewer.update;

import com.alibaba.fastjson.annotation.JSONField;

//{"code":0,"msg":"获
// 取成功！","data":{"packageName":"com.xiaobugo.eems","version":"0.0.10","upgradeContent":"1 代理商无法获取客户监测点问题修复","downloadPath":"https://dl.xiaobugo.com/apk/fat.debug.apk","forceUpgrade":0,"forceUpgradeVersionList":[]},"error":null}
public class HttpResult<T>{
    @JSONField(name = "error")
    public String error = "";
    @JSONField(name = "msg")
    public String msg = "";
    @JSONField(name = "data")
    public T data = null;
    @JSONField(name = "code")
    public int code = 0;
}