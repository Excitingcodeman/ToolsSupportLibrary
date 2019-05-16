package com.gs.toolssupport.inputmethodmanager;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import androidx.annotation.NonNull;

/**
 * @author husky
 * create on 2019-05-16-17:24
 */
public class InputMethodManagerHelper {

    /**
     * 隐藏键盘
     *
     * @param view     View
     * @param activity Activity
     */
    public static void hideInputMethod(@NonNull View view, @NonNull Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != inputMethodManager) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 弹出键盘
     *
     * @param view     View
     * @param activity Activity
     */
    public static void showKeyBoard(@NonNull View view, @NonNull Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != inputMethodManager) {
            view.requestFocus();
            inputMethodManager.showSoftInput(view, 0);
        }
    }
}
