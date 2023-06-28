package com.lazy2b.demo.test.xcode;

import com.alibaba.fastjson.JSON;
import com.lazylibs.webviewer.model.Pz;
import com.lazylibs.webviewer.model.U;
import com.lazylibs.webviewer.utils.Xc;

public class XCode {
    /////////  生成
    /***
     * 线上URL秘钥
     */
    public String urlKey = "cwMRFfwaBnuJoAUpmoEtaN1BSRp9aZ";// online
    /***
     * 动态链接获取地址秘钥
     */
    public String dUrlsApiKey = "fVDARXlz75FZstchpkh05tqYKDQFrl";
    /***
     * 本地默认配置秘钥:动态链接配置本地默认值秘钥
     */
    public String finalKey;

    public XCode(String finalKey) {
        this.finalKey = finalKey;
    }

    public XCode(String urlKey, String dUrlsApiKey, String finalKey) {
        this.urlKey = urlKey;
        this.dUrlsApiKey = dUrlsApiKey;
        this.finalKey = finalKey;
    }

    /***
     * 生成本地默认配置内容
     * @param versionUrl 版本更新json地址
     * @param pageUrls 主页地址列表，用逗号隔开
     * @param dUrlsApiUrl 动态链接获取地址
     */
    public XResult toCode(String versionUrl, String pageUrls, String dUrlsApiUrl) {
        XResult xResult = new XResult(urlKey, dUrlsApiKey, finalKey, versionUrl, pageUrls, dUrlsApiUrl);
        xResult.onlineConfig = new U(urlKey,
                // 加密版本检测地址用urlKey
                xResult.versionUrlCode = Xc.akCode(versionUrl, urlKey),
                // 加密域名列表地址用onlineKey(https://phlwin.ph,https://phlwin.com,https://phlwin.io,https://phlwin.cc),原始字符串用逗号隔开
                xResult.pageUrlsCode = Xc.akCode(pageUrls, urlKey)
        );
        xResult.onlineConfigString = JSON.toJSONString(xResult.onlineConfig);
        xResult.localConfig = new Pz(
                dUrlsApiKey,
                // 加密动态链接地址用fVDARXlz75FZstchpkh05tqYKDQFrl
                xResult.dUrlsApiUrlCode = Xc.akCode(dUrlsApiUrl, dUrlsApiKey),
                // 加密默认URL配置用 把urlsJsonFileContext()生成的文件内容加密放在本地用
                xResult.onlineConfigCode = Xc.akCode(xResult.onlineConfigString, finalKey)
        );
        xResult.localConfigString = JSON.toJSONString(xResult.localConfig);
        xResult.localConfigCode = Xc.akCode(xResult.localConfigString, finalKey);
        IGlobal.logP("线上配置(urls.json)：" + xResult.onlineConfigString);
        IGlobal.logP("本地配置：" + xResult.localConfigString);
        IGlobal.logP("本地配置加密串：" + xResult.localConfigCode.replace("\n","\\n"));
        return xResult;
    }

}
