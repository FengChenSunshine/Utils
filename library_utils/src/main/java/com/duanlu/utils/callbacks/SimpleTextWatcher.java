package com.duanlu.utils.callbacks;

import android.text.Editable;
import android.text.TextWatcher;

/********************************
 * @name SimpleTextWatcher
 * @author 段露
 * @createDate 2019/3/14  15:40.
 * @updateDate 2019/3/14  15:40.
 * @version V1.0.0
 * @describe 一个简单的TextWatcher.
 ********************************/
public abstract class SimpleTextWatcher implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}