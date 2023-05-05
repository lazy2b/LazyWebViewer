package com.lazy2b.demo.test.xcode;

import com.lazylibs.webviewer.IGlobal;

public class Project {
    public String code;
    public String name;

    public Project(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public UrlConfig develop;
    public UrlConfig preview;
    public UrlConfig online;

    private void xCode(XCode xCode, UrlConfig config) {
        if (config != null){
            config.xResult = xCode.toCode(config.versionUrl, config.pageUrls, config.dUrlsApiUrl);}else {
            IGlobal.logP("------------------>null<-------------------");
        }
    }

    public void xCode(XCode xCode) {
        IGlobal.logP(String.format("++++++++++++++++%s-%s-开始！++++++++++++++++++", code, name));
        IGlobal.logP(String.format("++++++++++++++++++++++++++++++++++++++++++++++++++", code, name));
        IGlobal.logP("-------------------develop-------------------");
        xCode(xCode, develop);
        IGlobal.logP("-------------------preview-------------------");
        xCode(xCode, preview);
        IGlobal.logP("-------------------online--------------------");
        xCode(xCode, online);
        IGlobal.logP(String.format("++++++++++++++++++++++++++++++++++++++++++++++++++", code, name));
        IGlobal.logP(String.format("++++++++++++++++%s-%s-结束！++++++++++++++++++", code, name));
        IGlobal.logP(String.format("———————————————————————————————————————————————————"));
    }
}
