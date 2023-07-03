package com.lazylibs.webviewer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

public class LazyWebChromeClient extends WebChromeClient implements ActivityResultCallback<Uri> {
    private static final int CHOOSE_REQUEST_CODE = 0x601;
    //    private ValueCallback<Uri> uploadFile;
    private ValueCallback<Uri[]> uploadFiles;
    public static final String DOC = "application/msword";
    public static final String DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    public static final String XLS = "application/vnd.ms-excel application/x-excel";
    public static final String XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final String PPT = "application/vnd.ms-powerpoint";
    public static final String PPTX = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
    public static final String PDF = "application/pdf";
    public static final String[] mimeTypes = {"image/*", DOCX, PPT, PPTX, PDF, XLSX, XLS, DOC};

    private final IWebHandler iWebHandler;
    private final LazyFinishedHelper finishedHelper;

    public LazyWebChromeClient(IWebHandler handler, LazyFinishedHelper finishedHelper) {
        this.iWebHandler = handler;
        this.finishedHelper = finishedHelper;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
//        WebViewHelper.logE("onProgressChanged", newProgress +"->"+view.getUrl());
        this.iWebHandler.onProgressChanged(view, newProgress);
        if (newProgress == 100) {
//        WebViewHelper.logE("onProgressChanged", newProgress +"->"+view.getUrl());
            this.iWebHandler.doProgressed(view.getUrl());
//                WebViewHelper.logE("doProgressed", "11111");
            if (finishedHelper.isRealFinished(view.getUrl())) {
//                    WebViewHelper.logE("doProgressed", "22222");
                return;
            }
//                WebViewHelper.logE("doProgressed", "33333");
            this.iWebHandler.onRealPageFinished(view.getUrl(), finishedHelper.isReceivedError());
        }
    }


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        view = new WebView(iWebHandler.requireActivity());
        view.getSettings().setJavaScriptEnabled(true);
        ((WebView.WebViewTransport) resultMsg.obj).setWebView(view);
        resultMsg.sendToTarget();
        return true;
    }

    @Override
    public void onPermissionRequest(PermissionRequest request) {
        //handle permission... （TODO）
        request.grant(request.getResources());
    }

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        openFileChooseProcess(filePathCallback);
        return true;
    }

    public void openFileChooseProcess(ValueCallback<Uri[]> valueCallback) {
        this.uploadFiles = valueCallback;
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        //intent.setType("*/*") => select all...
//        intent.setType(String.join("|", mimeTypes));
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);//iWebHandler.getString(R.string.tips_choose_title)
//        iWebHandler.requireActivity().startActivityForResult(Intent.createChooser(intent, null), CHOOSE_REQUEST_CODE);
        ActivityResultLauncher<String> launcher = this.iWebHandler.startActivityForResult(String.join("|", mimeTypes), new ActivityResultContracts.GetContent(), this);
    }


    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        AlertDialog.Builder b2 = new AlertDialog.Builder(iWebHandler.requireActivity()).setTitle(android.R.string.dialog_alert_title).setMessage(message).setPositiveButton(android.R.string.ok, (AlertDialog.OnClickListener) (dialog, which) -> result.confirm());
        b2.setCancelable(false);
        b2.create();
        b2.show();
        return true;
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        return super.onJsConfirm(view, url, message, result);
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }

    @Override
    public void onActivityResult(Uri result) {
        if (null != uploadFiles) {
            uploadFiles.onReceiveValue(new Uri[]{result});
            uploadFiles = null;
        }
    }
}