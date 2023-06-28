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

public class LazyWebChromeClient extends WebChromeClient {

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
    //选择本地文件类型：图片及文档
    private final String[] mimeTypes = {"image/*", DOCX, PPT, PPTX, PDF, XLSX, XLS, DOC};

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
        //需要注意
        request.grant(request.getResources());
    }

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        openFileChooseProcess(filePathCallback);
        return true;
    }

    /**
     * 打开本地存储选择文件
     */
    public void openFileChooseProcess(ValueCallback<Uri[]> valueCallback) {
        this.uploadFiles = valueCallback;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        //设置旋转文件类型（如果是所有文件：intent.setType("*/*")）
        intent.setType("image/*" + "|" + DOCX + "|" + PPT + "|" + PPTX + "|" + PDF + "|" + XLSX + "|" + XLS + "|" + DOC);
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);//iWebHandler.getString(R.string.tips_choose_title)
        iWebHandler.requireActivity().startActivityForResult(Intent.createChooser(intent, null), CHOOSE_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CHOOSE_REQUEST_CODE) {
                if (null != uploadFiles) {
                    Uri result = data == null ? null : data.getData();
                    uploadFiles.onReceiveValue(new Uri[]{result});
                    uploadFiles = null;
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            if (null != uploadFiles) {
                uploadFiles.onReceiveValue(null);
                uploadFiles = null;
            }
        }
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
}