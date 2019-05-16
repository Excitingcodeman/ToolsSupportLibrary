package com.gs.toolssupport.edit;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * @author husky
 * create on 2019-05-16-20:50
 */
public class EditFocusChangeListener implements View.OnFocusChangeListener {

    private ImageView ivDelete;

    public EditFocusChangeListener(ImageView ivDelete) {
        this.ivDelete = ivDelete;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if (v instanceof EditText) {
                if (null != v) {
                    if (!TextUtils.isEmpty(((EditText) v).getText())) {
                        if (null != ivDelete) {
                            ivDelete.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        } else {
            if (null != ivDelete) {
                ivDelete.setVisibility(View.GONE);
            }

        }
    }
}
