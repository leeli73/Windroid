package com.example.leeli.windroid;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.os.Build;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class body extends AppCompatActivity {
    ListView AllInfo;
    BaseAdapter adapter;
    TextView UserName;
    String StrUserName;
    TextView PassWord;
    String StrPassWord;
    TextView MaxDataLength;
    String StrMaxDataLength;
    TextView LocalDataSaveTime;
    String StrLocalDataSaveTime;
    TextView RomoteDataSaveDate;
    String StrRomoteDataSaveDate;
    TextView UserID;
    String StrUserID;
    TextView Email;
    String StrEmail;
    TextView LANIP;
    String StrLANIP;
    TextView LANPort;
    String StrLANPort;
    TextView LANAutoScan;
    String StrLANAutoScan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.body);
        //InitImmersionModel();
        //ReadData();
        InitData();
        InitList();
        StartWorkThread();
    }
    public void StartWorkThread()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    while (true)
                    {
                        String url = "http://192.168.0.102:6888/GetData";
                        final OkHttpClient okHttpClient=new OkHttpClient();
                        RequestBody body = new FormBody.Builder()
                                .add("UserID", Base64.encodeToString(StrUserID.getBytes(), Base64.DEFAULT))
                                .build();
                        final Request request=new Request.Builder().url(url).post(body).build();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Response response=okHttpClient.newCall(request).execute();
                                    if (response.isSuccessful()){
                                        String body=response.body().string();
                                        String Temp[] = body.split("@");
                                        if(Temp[0].equals("New"))
                                        {
                                            /*//获取剪贴板管理器：
                                            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                            // 创建普通字符型ClipData
                                            ClipData mClipData = ClipData.newPlainText("Label", Temp[1]);
                                            // 将ClipData内容放到系统剪贴板里。
                                            cm.setPrimaryClip(mClipData);*/
                                            Log.i("test","Get New Data "+ Temp[1]);
                                        }
                                        else
                                        {
                                            Log.i("test","No New Data");
                                        }
                                    }
                                    else
                                    {
                                        Log.i("test","No New Data");
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        Thread.sleep(1000);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String OldData = "";
                    while (true)
                    {
                        Log.i("test","Set");
                        //ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        //String Data = cm.getText().toString().trim();
                        //Log.i("test",Data);
                        String Data = "123";
                        if(!Data.equals(OldData))
                        {
                            String url = "http://192.168.0.102:6888/SetData";
                            final OkHttpClient okHttpClient=new OkHttpClient();
                            RequestBody body = new FormBody.Builder()
                                    .add("UserID", Base64.encodeToString(StrUserID.getBytes(), Base64.DEFAULT))
                                    .add("Data",Base64.encodeToString(Data.getBytes(),Base64.DEFAULT))
                                    .build();
                            final Request request=new Request.Builder().url(url).post(body).build();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Response response=okHttpClient.newCall(request).execute();
                                        if (response.isSuccessful()){
                                            String body=response.body().string();
                                            Log.i("test",body);
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                        Thread.sleep(1000);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void InitData()
    {
        try
        {
            ReadData();
            UserName = new TextView(body.this);
            UserName.setText(StrUserName);
            UserName.setTextSize(20);
            PassWord = new TextView(body.this);
            PassWord.setText(StrPassWord);
            PassWord.setTextSize(20);
            MaxDataLength = new TextView(body.this);
            MaxDataLength.setText(StrMaxDataLength);
            MaxDataLength.setTextSize(20);
            LocalDataSaveTime = new TextView(body.this);
            LocalDataSaveTime.setText(StrLocalDataSaveTime);
            LocalDataSaveTime.setTextSize(20);
            RomoteDataSaveDate = new TextView(body.this);
            RomoteDataSaveDate.setText(StrRomoteDataSaveDate);
            RomoteDataSaveDate.setTextSize(20);
            UserID = new TextView(body.this);
            UserID.setText(StrUserID);
            UserID.setTextSize(20);
            Email = new TextView(body.this);
            Email.setText(StrEmail);
            Email.setTextSize(20);
            LANIP = new TextView(body.this);
            LANIP.setText(StrLANIP);
            LANIP.setTextSize(20);
            LANPort = new TextView(body.this);
            LANPort.setText(StrLANPort);
            LANPort.setTextSize(20);
            LANAutoScan = new TextView(body.this);
            if(StrLANAutoScan.equals("true"))
            {
                LANAutoScan.setText("自动局域网搜索开启");
            }
            else
            {
                LANAutoScan.setText("自动局域网搜索关闭");
            }
            LANAutoScan.setTextSize(20);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void ReadData()
    {
        try
        {
            InputStreamReader inputStreamReader = new InputStreamReader(getResources().getAssets().open("UserInfo.data"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line="";
            while((line=bufferedReader.readLine())!=null)
            {
                String Temp[] = line.split(":");
                if(Temp[0].equals("Username"))
                {
                    StrUserName = new String(Temp[1]);
                }
                else if(Temp[0].equals("Password"))
                {
                    StrPassWord = new String(Temp[1]);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            InputStreamReader inputStreamReader = new InputStreamReader(getResources().getAssets().open("Setting.data"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line="";
            while((line=bufferedReader.readLine())!=null)
            {
                String Temp[] = line.split(":");
                if(Temp[0].equals("MaxDataLength"))
                {
                    StrMaxDataLength = new String(Temp[1]);
                }
                else if(Temp[0].equals("LocalDataSaveTime"))
                {
                    StrLocalDataSaveTime = new String(Temp[1]);
                }
                else if(Temp[0].equals("RemoteDataSaveTime"))
                {
                    StrRomoteDataSaveDate = new String(Temp[1]);
                }
                else if(Temp[0].equals("UserID"))
                {
                    StrUserID = new String(Temp[1]);
                }
                else if(Temp[0].equals("Email"))
                {
                    StrEmail = new String(Temp[1]);
                }
                else if(Temp[0].equals("LANIP"))
                {
                    StrLANIP = new String(Temp[1]);
                }
                else if(Temp[0].equals("LANPort"))
                {
                    StrLANPort = new String(Temp[1]);
                }
                else if(Temp[0].equals("LANAutoScan"))
                {
                    StrLANAutoScan = new String(Temp[1]);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void InitList()
    {
        AllInfo = findViewById(R.id.AllInfo);
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return 13;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LinearLayout linearLayout = new LinearLayout(body.this);
                TextView tital = new TextView(body.this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                tital.setTextSize(25);
                switch (position)
                {
                    case 0:
                        tital.setText("用户信息");
                        tital.setGravity(LinearLayout.TEXT_ALIGNMENT_CENTER);
                        tital.setTextSize(30);
                        linearLayout.addView(tital);
                        break;
                    case 1:
                        tital.setText("用户名ID");
                        linearLayout.addView(tital);
                        linearLayout.addView(UserID);
                        break;
                    case 2:
                        tital.setText("用户名");
                        linearLayout.addView(tital);
                        linearLayout.addView(UserName);
                        break;
                    case 3:
                        tital.setText("电子邮箱");
                        linearLayout.addView(tital);
                        linearLayout.addView(Email);
                        break;
                    case 4:
                        tital.setText("密码");
                        linearLayout.addView(tital);
                        linearLayout.addView(PassWord);
                        break;
                    case 5:
                        tital.setText("设置");
                        tital.setGravity(LinearLayout.TEXT_ALIGNMENT_CENTER);
                        tital.setTextSize(30);
                        linearLayout.addView(tital);
                        break;
                    case 6:
                        tital.setText("最大数据长度/K");
                        linearLayout.addView(tital);
                        linearLayout.addView(MaxDataLength);
                        break;
                    case 7:
                        tital.setText("远程存储时间/s(<3600s)");
                        linearLayout.addView(tital);
                        linearLayout.addView(RomoteDataSaveDate);
                        break;
                    case 8:
                        tital.setText("本地存储时间/s(<3600s)");
                        linearLayout.addView(tital);
                        linearLayout.addView(LocalDataSaveTime);
                        break;
                    case 9:
                        tital.setText("局域网连接");
                        tital.setGravity(LinearLayout.TEXT_ALIGNMENT_CENTER);
                        tital.setTextSize(30);
                        linearLayout.addView(tital);
                        break;
                    case 10:
                        tital.setText("自动扫描");
                        linearLayout.addView(tital);
                        linearLayout.addView(LANAutoScan);
                        break;
                    case 11:
                        tital.setText("局域网IP");
                        linearLayout.addView(tital);
                        linearLayout.addView(LANIP);
                        break;
                    case 12:
                        tital.setText("端口");
                        linearLayout.addView(tital);
                        linearLayout.addView(LANPort);
                        break;
                }
                return linearLayout;
            }
        };
        AllInfo.setAdapter(adapter);
        AllInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final EditText MyInput = new EditText(body.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(body.this);
                builder.setTitle("请输入信息").setIcon(android.R.drawable.ic_dialog_alert).setView(MyInput).setNegativeButton("取消",null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (position)
                        {
                            case 0:
                                //用户信息
                                break;
                            case 1:
                                //用户名ID
                                Toast.makeText(body.this,"用户ID不允许修改",Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                //用户名
                                Toast.makeText(body.this,"用户名不允许修改",Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                //电子邮箱
                                StrEmail = MyInput.getText().toString();
                                Email.setText(StrEmail);
                                Toast.makeText(body.this,"修改成功",Toast.LENGTH_SHORT).show();
                                break;
                            case 4:
                                //密码
                                StrPassWord = MyInput.getText().toString();
                                PassWord.setText(StrPassWord);
                                Toast.makeText(body.this,"修改成功",Toast.LENGTH_SHORT).show();
                                break;
                            case 5:
                                //设置
                                break;
                            case 6:
                                //最大数据长度
                                StrMaxDataLength = MyInput.getText().toString();
                                MaxDataLength.setText(StrMaxDataLength);
                                Toast.makeText(body.this,"修改成功",Toast.LENGTH_SHORT).show();
                                break;
                            case 7:
                                //远程存储时间/s(<3600s)
                                StrRomoteDataSaveDate = MyInput.getText().toString();
                                RomoteDataSaveDate.setText(StrRomoteDataSaveDate);
                                Toast.makeText(body.this,"修改成功",Toast.LENGTH_SHORT).show();
                                break;
                            case 8:
                                //本地存储时间/s(<3600s)
                                StrLocalDataSaveTime = MyInput.getText().toString();
                                LocalDataSaveTime.setText(StrLocalDataSaveTime);
                                Toast.makeText(body.this,"修改成功",Toast.LENGTH_SHORT).show();
                                break;
                            case 9:
                                //局域网连接
                                break;
                            case 10:
                                //自动扫描
                                StrLANAutoScan = MyInput.getText().toString();
                                LANAutoScan.setText(StrLANAutoScan);
                                Toast.makeText(body.this,"修改成功",Toast.LENGTH_SHORT).show();
                                break;
                            case 11:
                                //局域网IP
                                StrLANIP = MyInput.getText().toString();
                                LANIP.setText(StrLANIP);
                                Toast.makeText(body.this,"修改成功",Toast.LENGTH_SHORT).show();
                                break;
                            case 12:
                                //端口
                                StrLANPort = MyInput.getText().toString();
                                LANPort.setText(StrLANPort);
                                Toast.makeText(body.this,"修改成功",Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
                builder.show();
            }
        });
    }
    public void InitImmersionModel() //沉浸模式
    {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
        Toast.makeText(body.this,"Windroid进入后台运行",Toast.LENGTH_SHORT).show();
    }
}
