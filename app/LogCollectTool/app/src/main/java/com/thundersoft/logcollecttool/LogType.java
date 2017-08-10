package com.thundersoft.logcollecttool;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import static android.R.id.content;

/**
 * Created by ace on 17-8-4.
 */

public class LogType extends AppCompatActivity {
    public CheckBox checkBox;
    public String name;
    public String property;
    public int id;
    public RelativeLayout.LayoutParams parms;
    private final int BaseID = 500;
    protected LogType(String name,String property,int id,Context content) {
        this.name = name;
        this.property = property;
        this.id = BaseID + id;
        this.parms = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        checkBox = new CheckBox(content);
        checkBox.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
        checkBox.setText(this.name);
        checkBox.setId(this.id);
        checkBox.setGravity(Gravity.CENTER);
        checkBox.setLayoutParams(parms);
        checkBox.setGravity(Gravity.CENTER);
        parms.addRule(RelativeLayout.CENTER_IN_PARENT);
        parms.addRule(RelativeLayout.CENTER_HORIZONTAL,this.id);
        checkBox.setChecked(PropertyFunctions.propertyGet(this.property));
        checkBox.setOnClickListener(new CheckBoxClick());
    }
    private class CheckBoxClick implements View.OnClickListener {
        public void onClick(View v) {
                if (checkBox.isChecked()) {
                    PropertyFunctions.propertySet(property,"1");
                } else {
                    PropertyFunctions.propertySet(property,"0");
                }
        }
    }
}
