package com.thundersoft.logcollecttool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView resultView;
    LogType kernelLog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new PropertyFunctions();
        kernelLog = new LogType("kernel log",(CheckBox)findViewById(R.id.kernelCB),"persist.sys.cKLog");
        resultView = (TextView)findViewById(R.id.showResult);
    }
}
