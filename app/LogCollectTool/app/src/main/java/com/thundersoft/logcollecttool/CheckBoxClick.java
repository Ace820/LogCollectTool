package com.thundersoft.logcollecttool;

/**
 * Created by ace on 17-8-3.
 */

import android.view.View;
import android.widget.CheckBox;
import java.lang.reflect.Method;

class CheckBoxClick implements View.OnClickListener {
    private CheckBox CB;
    private String prop;

    public CheckBoxClick(CheckBox CB,String prop) {
        this.CB = CB;
        this.prop = prop;

    }

    public void onClick(View v) {
        CheckBox cBox = this.CB;
        try {
            if (cBox.isChecked()) {
                PropertyFunctions.propertySet(this.prop,"1");
            } else {
                PropertyFunctions.propertySet(this.prop,"0");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
