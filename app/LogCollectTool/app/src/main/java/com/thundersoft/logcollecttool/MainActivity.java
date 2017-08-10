package com.thundersoft.logcollecttool;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public final String TAG = "LogCollectTool";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        new PropertyFunctions();
        createView();
    }

    protected void createView() {
        RelativeLayout frameLayout = new RelativeLayout(this);
        LogType[] checkBox =new LogType[5];
        Button getProfile = new Button(this);
        Button uploadNow = new Button(this);
        SharedPreferences sP = getSharedPreferences("logType",Activity.MODE_PRIVATE);
        String names = sP.getString("name","");
        String properties = sP.getString("property","");

        String[] name = names.split(" ");
        String[] property = properties.split(" ");
        if (name.length >0) {
            for (int i = 0; i < name.length; i++) {
                checkBox[i] = new LogType(name[i], property[i], i, this);
            }
            //TODO adjust if the input object number is 0 or 1
            if (checkBox.length != 1) {
                for (int i = (checkBox.length / 2 - 1); i >= 0; i--)
                    checkBox[i].parms.addRule(RelativeLayout.ABOVE, checkBox[i + 1].id);
                for (int i = (checkBox.length / 2 + 1); i < checkBox.length; i++)
                    checkBox[i].parms.addRule(RelativeLayout.BELOW, checkBox[i - 1].id);
                for (int i = 0; i < name.length; i++) {
                    frameLayout.addView(checkBox[i].checkBox);
                }
            }
        } else {
            checkBox[0] = new LogType("kernel log","persist.sys.cKLog", 0, this);
        }
        //Button get profile
        RelativeLayout.LayoutParams profileParms = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        getProfile.setLayoutParams(profileParms);
        getProfile.setId(50);
        getProfile.setText("get profile");
        getProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(Intent.createChooser(intent,"Choose a profile"),0);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(MainActivity.this,"Can not find File explorer",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        profileParms.addRule(RelativeLayout.BELOW,checkBox[checkBox.length-1].id);
        getProfile.setWidth(getWindowManager().getDefaultDisplay().getWidth()/2);
        frameLayout.addView(getProfile);
        //button upload immediately
        RelativeLayout.LayoutParams uploadParms = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        uploadNow.setLayoutParams(uploadParms);
        uploadNow.setWidth(getWindowManager().getDefaultDisplay().getWidth()/2);
        uploadNow.setId(51);
        uploadNow.setText("upload now");
        uploadNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PropertyFunctions.propertySet("sys.logCollectTool.UploadNow","1");
                Toast.makeText(MainActivity.this,"正在上传中...",Toast.LENGTH_SHORT).show();
                Log.d(TAG,"set upload now");
            }
        });
        uploadParms.addRule(RelativeLayout.BELOW,checkBox[checkBox.length-1].id);
        uploadParms.addRule(RelativeLayout.RIGHT_OF,50);
        frameLayout.addView(uploadNow);
        setContentView(frameLayout);
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        Uri uri;

        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode != Activity.RESULT_OK) {
            Log.e(TAG,"Choose file error,error code is "+resultCode);
            return;
        }
        if (requestCode == 0) {
            uri = data.getData();
            Log.d(TAG,"get file path:"+uri.getPath());
        }
    }
}
