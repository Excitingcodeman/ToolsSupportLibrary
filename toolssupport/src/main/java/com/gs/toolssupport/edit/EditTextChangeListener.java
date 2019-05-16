package com.gs.toolssupport.edit;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * @author husky
 * create on 2019-05-16-20:52
 */
public class EditTextChangeListener implements TextWatcher {

    private ImageView ivDelete;
    private EditText editText;

    public EditTextChangeListener(ImageView ivDelete) {
        this.ivDelete = ivDelete;
    }

    public EditTextChangeListener(ImageView ivDelete, EditText editText) {
        this.ivDelete = ivDelete;
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence str, int start, int before, int count) {
        String contents = str.toString();
        int length = contents.length();
        if (null != editText && editText.isEnabled()) {
            if (length > 0) {
                ivDelete.setVisibility(View.VISIBLE);
            } else {
                ivDelete.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
