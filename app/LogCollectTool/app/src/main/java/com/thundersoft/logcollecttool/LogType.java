package com.thundersoft.logcollecttool;

import android.util.Log;
import android.widget.CheckBox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
/**
 * Created by ace on 17-8-4.
 */

public class LogType extends AppCompatActivity {
    public CheckBox checkBox;
    public String name;
    public String property;
    public boolean value;

    public LogType(String name,CheckBox CB,String property) {
        this.checkBox = CB;
        this.name = name;
        this.property = property;
        this.value = PropertyFunctions.propertyGet(this.property);
        this.checkBox.setChecked(value);
        this.checkBox.setOnClickListener(new CheckBoxClick(checkBox,this.property));
        Log.d("LogCollectTool","new LogType props"+this.name+","+this.value);
    }
}
