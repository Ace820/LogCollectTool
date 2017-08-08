package com.thundersoft.logcollecttool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    public final String TAG = "LogCollectTool";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        new PropertyFunctions();
        RelativeLayout frameLayout = new RelativeLayout(this);
        LogType[] checkBox =new LogType[5];
        for (int i=0;i<5;i++) {
            checkBox[i] = new LogType("kernel log","persist.sys.cKLog",i,this);
            if (i == 0 )
                checkBox[i].parms.addRule(RelativeLayout.CENTER_HORIZONTAL,checkBox[i].id);
            else
                checkBox[i].parms.addRule(RelativeLayout.BELOW,checkBox[i-1].id);
            frameLayout.addView(checkBox[i].checkBox);
        }
        setContentView(frameLayout);
    }
}
