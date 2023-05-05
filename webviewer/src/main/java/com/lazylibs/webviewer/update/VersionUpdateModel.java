package com.lazylibs.webviewer.update;

import android.text.TextUtils;

import androidx.annotation.Keep;

import com.alibaba.fastjson.annotation.JSONField;
import com.lazylibs.updater.interfaces.IUpgradeModel;
import com.lazylibs.webviewer.IGlobal;

import java.util.List;

//{"packageName":"com.xiaobugo.eems",
// "version":"0.0.10",
// "upgradeContent":"1 代理商无法获取客户监测点问题修复",
// "downloadPath":"https://dl.xiaobugo.com/apk/fat.debug.apk",
// "forceUpgrade":0,"forceUpgradeVersionList":[]
// }


public class VersionUpdateModel implements IUpgradeModel {
    @Keep
    public VersionUpdateModel() {
    }

    @JSONField(name = "packageName")
    public String packageName = "";
    @JSONField(name = "version")
    public String version = "";
    @JSONField(name = "upgradeContent")
    public String upgradeContent = "";
    @JSONField(name = "downloadPath")
    public String downloadPath = "";
    @JSONField(name = "forceUpgrade")
    public int forceUpgrade = 0;
    @JSONField(name = "forceUpgradeVersionList")
    public List<String> forceUpgradeVersionList;

    @JSONField(name = "isNeedUpgrade")
    private boolean isNeedUpgrade = false;

    @Override
    public void setNeedUpgrade(Object versionName) {
        isNeedUpgrade = hasNewVersion(this.version, (String) versionName);
    }

    public boolean isNeedUpgrade() {
        return isNeedUpgrade;
    }

    @Override
    public boolean isForceUpdate() {
        return forceUpgrade == 1;
    }

    @Override
    public String getUpdateInfo() {
        return upgradeContent;
    }

    @Override
    public String getNewVersionName() {
        return version;
    }

    @Override
    public String getDownloadUrl() {
        return IGlobal.removeZWSP(downloadPath);
    }

    public static boolean hasNewVersion(String server, String client) {
        /* 2 */
        if (TextUtils.isEmpty(server) || TextUtils.isEmpty(client) || server.equals(client)) {
            return false;
        }
        String[] serverArr = server.split("\\.");
        String[] clientArr = client.split("\\.");
        for (int i = 0; i < serverArr.length; i++) {
            int iServer = Integer.parseInt(serverArr[i]), iClient = Integer.parseInt(clientArr[i]);
            if (iClient > iServer) return false;
            if (iServer > iClient) {
                return true;
            }
        }
        return false;
        /* 2 */
    }

}
