package com.iakie.testcp;

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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
        //getMsgs();
        setDefaultSms();
        //insertSms();
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
        //rec_btn = findViewById(R.id.btn_rec_sms);
        //set_btn = findViewById(R.id.btn_set_sms);
    }

    /**
    private void getMsgs() {
        Uri uri = Uri.parse("content://sms/");
        ContentResolver resolver = getContentResolver();
        // 获取的是哪些列的信息
        Cursor cursor = resolver.query(uri, new String[]{"address","date","type","body"},
                null,null, null);
        if (cursor != null) {
            while (cursor.moveToNext()){
                String address = cursor.getString(0);
                String date = cursor.getString(1);
                String type = cursor.getString(2);
                String body = cursor.getString(3);
                System.out.println("地址："+ address);
                System.out.println("时间: "+ date);
                System.out.println("类型: "+ type);
                System.out.println("内容: "+ body);
                System.out.println("=====================");
            }
        }
        if (cursor != null) {
            cursor.close();
        }
    }**/

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
                    /**
                    GregorianCalendar gc = new GregorianCalendar();
                    gc.set(Calendar.YEAR,2018);//年
                    gc.set(Calendar.MONTH, 3);//月，0为1月
                    gc.set(Calendar.DAY_OF_MONTH, 27);//天
                    gc.set(Calendar.HOUR_OF_DAY,17);//时
                    gc.set(Calendar.MINUTE, 2);//分
                    gc.set(Calendar.SECOND, 6);//秒
                    gc.set(Calendar.MILLISECOND,200);//毫秒
                    Date date = gc.getTime();**/

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
    /**

    private void setRecoverySms() {
        //恢复结束，将默认短信还原
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            String myPackageName = getPackageName();
            if (Telephony.Sms.getDefaultSmsPackage(this)
                    .equals(myPackageName)) {
                Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                //这里的defaultSmsApp是前面保存的
                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, defaultSmsApp);
                startActivity(intent);
            }
        }
    }
    **/
}