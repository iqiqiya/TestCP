package com.iakie.smscat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Telephony;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String defaultSmsApp;
    private EditText sms_num;
    private EditText sms_con;
    private Button insert_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        //简单的获取读取短信权限
        ActivityCompat.requestPermissions(this,new String[]
                {Manifest.permission.READ_SMS},1);
        setDefaultSms();
    }

    private void initView() {

        sms_num = findViewById(R.id.sms_number);
        sms_con = findViewById(R.id.sms_content);
        insert_btn = findViewById(R.id.btn_insert_sms);
        insert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertSms();
            }
        });
    }

    public void setDefaultSms() {

        String currentPn = getPackageName();//获取当前程序包名
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            defaultSmsApp = Telephony.Sms.getDefaultSmsPackage(this);//获取手机当前设置的默认短信应用的包名
        }
        assert defaultSmsApp != null;
        if (!defaultSmsApp.equals(currentPn)) {
            Intent intent = null;
            intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, currentPn);
            startActivity(intent);
        }
    }

    public void insertSms() {

        //开启线程，延迟5秒
        new Thread(){
            public void run(){
                try {
                    Thread.sleep(3000);

                    ContentResolver resolver=getContentResolver();
                    Uri url=Uri.parse("content://sms/");
                    ContentValues values=new ContentValues();
                    values.put("address", sms_num.getText().toString());
                    values.put("type", 1);

                    values.put("date", System.currentTimeMillis());
                    //values.put("date", date.getTime());

                    values.put("body", sms_con.getText().toString());
                    resolver.insert(url, values);
                    Looper.prepare();
                    Toast.makeText(MainActivity.this,"成功插入一条短信",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}