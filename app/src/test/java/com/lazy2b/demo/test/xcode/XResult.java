package com.lazy2b.demo.test.xcode;

import com.alibaba.fastjson.JSON;
import com.lazylibs.webviewer.model.Pz;
import com.lazylibs.webviewer.model.U;
import com.lazylibs.webviewer.utils.Xc;

public class XResult extends XCode{

    public String versionUrl;
    public String pageUrls;
    public String dUrlsApiUrl;

    public String versionUrlCode;
    public String pageUrlsCode;
    public String dUrlsApiUrlCode;

    public U onlineConfig;
    public String onlineConfigString;
    public String onlineConfigCode;
    public Pz localConfig;
    public String localConfigString;
    public String localConfigCode;

    public XResult(String urlKey, String dUrlsApiKey, String finalKey, String versionUrl, String pageUrls, String dUrlsApiUrl) {
        super(urlKey,dUrlsApiKey, finalKey);
        this.versionUrl = versionUrl;
        this.pageUrls = pageUrls;
        this.dUrlsApiUrl = dUrlsApiUrl;
    }

    public XResult(String finalKey){
        super("","",finalKey);
    }

    public String rePrintLocalCode(String _localConfigCode){
        this.localConfigCode = _localConfigCode;
        return verifyLocalConfig();
    }

    public String verifyResult() {
        IGlobal.logP("-------------------校验-----------------------");
        IGlobal.logP("------------线上urls.json配置还原---------------");
        vDu(onlineConfigString);
        return verifyLocalConfig();
    }

    private String verifyLocalConfig() {
        IGlobal.logP("---------------本地默认配置还原------------------");
        String localConfigRawString = Xc.akRaw(localConfigCode, finalKey);
        IGlobal.logP("本地配置【还原】：" + localConfigRawString);
        Pz dPz = JSON.parseObject(localConfigRawString, Pz.class);
        IGlobal.logP("dp：" + dPz.zdp());
        IGlobal.logP("du：" + dPz.zdu());
        IGlobal.logP("-------------本地urls.json配置还原---------------");
        vDu(dPz.zdp());
        return localConfigRawString;
    }

    public void vDu(String json) {
        U du = JSON.parseObject(json, U.class);
        IGlobal.logP("v：" + du.zv());
        IGlobal.logP("w：" + du.zw());
    }
}
