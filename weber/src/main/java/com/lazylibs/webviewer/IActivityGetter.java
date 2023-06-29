package com.lazylibs.webviewer;

import android.app.Activity;
import android.content.res.Resources;


public interface IActivityGetter {
    Activity requireActivity();

    default String getString(int resId) {
        try {
            return requireActivity() != null ? requireActivity().getResources().getString(resId) : "";
        } catch (Resources.NotFoundException e) {
            return "";
        }
    }
}
