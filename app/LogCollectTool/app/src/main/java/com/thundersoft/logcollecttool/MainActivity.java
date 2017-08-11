package com.thundersoft.logcollecttool;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
        Button getProfile = new Button(this);
        Button uploadNow = new Button(this);
        SharedPreferences sP = getSharedPreferences("logType",Activity.MODE_PRIVATE);
        String names = sP.getString("name","");
        String properties = sP.getString("property","");
        String[] name,property;
        if (names.equals("")){
            //TODO: may need a dialog to ask if need to import a profile
            name = new String[]{"kernel log","logcat"};
            property = new String[]{"persist.sys.cKLog","persist.sys.cLogcat"};
        } else {
            name = names.split(":");
            property = properties.split(":");
        }
        LogType[] checkBox =new LogType[name.length];
        Log.d(TAG,"name length="+name.length);
        if (name.length >1) {
            for (int i = 0; i < name.length; i++) {
                checkBox[i] = new LogType(name[i], property[i], i, this);
            }
            if (checkBox.length != 1) {
                for (int i = (checkBox.length / 2 - 1); i >= 0; i--) {
                    checkBox[i].parms.addRule(RelativeLayout.ABOVE, checkBox[i + 1].id);
                    checkBox[i].parms.addRule(RelativeLayout.ALIGN_LEFT, checkBox[i + 1].id);
                }
                for (int i = (checkBox.length / 2 + 1); i < checkBox.length; i++) {
                    checkBox[i].parms.addRule(RelativeLayout.BELOW, checkBox[i - 1].id);
                    checkBox[i].parms.addRule(RelativeLayout.ALIGN_LEFT, checkBox[i - 1].id);
                }
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
                intent.setType("text/*.ini");
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
        String tempStr;
        String name = "";
        String property = "";
        SharedPreferences preferences = getSharedPreferences("logType", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode != Activity.RESULT_OK) {
            Log.e(TAG,"Choose file error,error code is "+resultCode);
            return;
        }
        uri = data.getData();
        Log.d(TAG,"get file path:"+uri.getPath());
        // check file read permission
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
            //check if permission allowed
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }
        //analyse file
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(uri.getPath())));
            if (!reader.readLine().equals("LogCollectTool")){
                //TODO start a Dialog to warn not correct file
                Toast.makeText(MainActivity.this,"Profile not right!",Toast.LENGTH_SHORT).show();
                return;
            }
            while ((tempStr = reader.readLine()) != null ) {
                String[] value = tempStr.split(":");
                if (value.length !=2 ){
                    //TODO start a Dialog to warn line error
                    Toast.makeText(MainActivity.this,"line format error:"+tempStr,Toast.LENGTH_SHORT).show();
                } else {
                    if(name.equals("")) {
                        name = name +  value[0];
                        property = property +  value[1];

                    } else {
                        name = name + ":" + value[0];
                        property = property + ":" + value[1];
                    }
                }
            }
            if (!name.equals("")) {
                editor.putString("name", name);
                editor.putString("property", property);
            }
            editor.commit();
            //restart main activity
            Intent intent = getIntent();
            overridePendingTransition(0,0);
            intent.addFlags(intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            startActivity(intent);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
