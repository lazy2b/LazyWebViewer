package com.lazy2b.demo.test.xcode;

public class UrlConfig {
    public UrlConfig(String versionUrl, String dUrlsApiUrl, String pageUrls) {
        this.versionUrl = versionUrl;
        this.pageUrls = pageUrls;
        this.dUrlsApiUrl = dUrlsApiUrl;
    }

    public String versionUrl;
    public String pageUrls;
    public String dUrlsApiUrl;
    public XResult xResult;
}
