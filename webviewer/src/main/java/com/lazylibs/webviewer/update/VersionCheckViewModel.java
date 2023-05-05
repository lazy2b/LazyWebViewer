package com.lazylibs.webviewer.update;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.lazylibs.updater.VersionUpdateHelper;
import com.lazylibs.updater.interfaces.IUpgradeModel;
import com.lazylibs.updater.model.UpdateResult;
import com.lazylibs.webviewer.IGlobal;
import com.lazylibs.webviewer.model.U;
import com.lazylibs.webviewer.model.Us;
import com.lazylibs.webviewer.utils.SimpleOkHttp;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

public class VersionCheckViewModel extends ViewModel {

    IUpgradeModel vModel;
    VersionUpdateHelper vHelper;

    public void startCheckVersion(IVersionChecker checker, String url) {
        IGlobal.logE("startCheckVersion:"+url);
        if (TextUtils.isEmpty(url)) {
            checker.requireActivity().runOnUiThread(() -> checker.vHelperCallBack(UpdateResult.Error));
        } else {
            SimpleOkHttp simpleOkHttp = new SimpleOkHttp() {
                @Override
                public void onSuccess(String data) {
                    try {
                        if ((vModel = parseBody(data)) == null) {
                            checker.requireActivity().runOnUiThread(() -> checker.vHelperCallBack(UpdateResult.NoNews));
                        } else {
                            vHelper = VersionUpdateHelper.create(checker.requireActivity(), checker, vModel);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                IUpgradeModel parseBody(String body) {
                    try {
                        return JSON.parseObject(body, VersionUpdateModel.class);
                    } catch (JSONException exception) {
                        exception.printStackTrace();
                    }
                    return null;
                }

                @Override
                public void onFailure(@NonNull Exception exception) {
                    super.onFailure(exception);
                    checker.requireActivity().runOnUiThread(() -> checker.vHelperCallBack(UpdateResult.Error));
                }
            };
            simpleOkHttp.get(url);
        }

    }

    void doCheckVersion(Callback callback, String url) {
        new OkHttpClient.Builder()
                .build()
                .newCall(
                        new Request.Builder()
                                .url(url)
                                .get()
                                .build()
                )
                .enqueue(callback);
    }

    IUpgradeModel parseBody(ResponseBody body) throws IOException {
        if (body != null && body.contentLength() != 0) {
            try {
                String bodyString = body.string();
                VersionUpdateModel vum = JSON.parseObject(bodyString, VersionUpdateModel.class);
                IGlobal.logD(bodyString);
                return vum;
            } catch (JSONException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onCleared() {
        VersionUpdateHelper.destroy(vHelper);
        super.onCleared();
    }

}
