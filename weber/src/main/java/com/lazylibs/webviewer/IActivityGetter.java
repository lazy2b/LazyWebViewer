package com.lazylibs.webviewer;

import android.app.Activity;
import android.content.res.Resources;
import android.net.Uri;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;


public interface IActivityGetter {
    default Activity requireActivity() {
        return null;
    }

    default String getString(int resId) {
        try {
            return requireActivity() != null ? requireActivity().getResources().getString(resId) : "";
        } catch (Resources.NotFoundException e) {
            return "";
        }
    }

    default ActivityResultCaller requireActivityResultCaller() {
        return null;
    }

    default <I, O> ActivityResultLauncher<I> startActivityForResult(@NonNull I input, @NonNull ActivityResultContract<I, O> contract, @NonNull ActivityResultCallback<O> callback) {
        Object caller = requireActivityResultCaller();
        caller = caller != null ? caller : requireActivity();
        if (caller instanceof ActivityResultCaller) {
            ActivityResultLauncher<I> launcher = ((ActivityResultCaller) caller).registerForActivityResult(contract, callback);
            launcher.launch(input);
            return launcher;
        }
        return null;
    }

}
