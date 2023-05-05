package com.lazylibs.webviewer;

import android.app.Activity;
import android.content.res.Resources;

import androidx.annotation.StringRes;

public interface IActivityGetter {
    Activity requireActivity();

    default String getString(@StringRes int resId) {
        try {
            return requireActivity() != null ? requireActivity().getResources().getString(resId) : "";
        } catch (Resources.NotFoundException e) {
            return "";
        }
    }
}
