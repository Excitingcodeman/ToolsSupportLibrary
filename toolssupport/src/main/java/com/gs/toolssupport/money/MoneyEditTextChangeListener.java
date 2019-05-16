package com.gs.toolssupport.money;


import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.annotation.NonNull;

/**
 * @author husky
 * create on 2018/10/21-12:56
 * 保留2位小数的输入框
 */
public class MoneyEditTextChangeListener implements TextWatcher {
    private EditText editText;


    public MoneyEditTextChangeListener(@NonNull EditText editText) {
        this.editText = editText;

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (s.toString().contains(".")) {
            if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                s = s.toString().subSequence(0,
                        s.toString().indexOf(".") + 3);
                editText.setText(s);
                editText.setSelection(s.length());
            }
        }
        if (s.toString().trim().equals(".")) {
            s = "0" + s;
            editText.setText(s);
            editText.setSelection(2);
        }

        if (s.toString().startsWith("0")
                && s.toString().trim().length() > 1) {
            if (!s.toString().substring(1, 2).equals(".")) {
                editText.setText(s.subSequence(0, 1));
                editText.setSelection(1);
                return;
            }
        }


    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
