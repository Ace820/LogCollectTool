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
    public boolean value;
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
        this.checkBox = new CheckBox(content);
        this.checkBox.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
        this.checkBox.setText(this.name);
        this.checkBox.setId(this.id);
        this.checkBox.setGravity(Gravity.CENTER);
        this.checkBox.setLayoutParams(parms);
        this.checkBox.setGravity(Gravity.CENTER);
        this.parms.addRule(RelativeLayout.CENTER_IN_PARENT);
        this.checkBox.setChecked(PropertyFunctions.propertyGet(this.property));
        this.checkBox.setOnClickListener(new CheckBoxClick(checkBox,this.property));
    }
    private class CheckBoxClick implements View.OnClickListener {
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
}
