package com.example.leeli.windroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.os.Build;

import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Base64;


import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    private WebView LoginBackground;
    private Button LoginButton;
    private Button RegisterButton;
    private TextView Username;
    private TextView Password;
    private CheckBox isRemember;
    private CheckBox isAuto;
    private LocalBroadcastManager LoginBroad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitImmersionModel();
        InitLoginBackground();
        InitAlpha();
        Init();
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });
        IntentFilter filter = new IntentFilter("com.example.leeli.windroid");
        LoginBroad = LocalBroadcastManager.getInstance(this);
        LoginBroad.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String Result = intent.getStringExtra(Intent.EXTRA_TEXT);
                String ResultTemp[] = Result.split(":");
                if(ResultTemp[0].equals("Login"))
                {
                    if(ResultTemp[1].equals("LoginError"))
                    {
                        Toast.makeText(MainActivity.this,"登录失败,请检查账号或用户名", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        String ResultArray[] = ResultTemp[1].split("@");
                        if(ResultArray[0].equals("LoginSuccess"))
                        {
                            Toast.makeText(MainActivity.this,"登录成功,正在转跳...", Toast.LENGTH_SHORT).show();
                            Intent bodyActivity = new Intent(MainActivity.this,body.class);
                            startActivity(bodyActivity);
                        }
                    }
                }
                else if(ResultTemp[0].equals("Register"))
                {
                    if(ResultTemp[1].equals("RegisterSuccess"))
                    {
                        Toast.makeText(MainActivity.this,"注册成功!", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Log.i("play","123");
                        Toast.makeText(MainActivity.this,"注册失败!请更换用户名", Toast.LENGTH_LONG).show();
                    }
                }
            }
        },filter);
    }
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
        Toast.makeText(MainActivity.this,"Windroid进入后台运行",Toast.LENGTH_SHORT).show();
    }
    public void Init()
    {
        LoginButton = (Button)findViewById(R.id.Login);
        RegisterButton = (Button)findViewById(R.id.Register);
        Username = (TextView)findViewById(R.id.Username);
        Password = (TextView)findViewById(R.id.Password);
        isRemember = (CheckBox)findViewById(R.id.checkBoxRemember);
        isAuto = (CheckBox)findViewById(R.id.checkBoxAutoLogin);
        isAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAuto.isChecked())
                {
                    isRemember.setChecked(true);
                }
                else
                {
                    isRemember.setChecked(false);
                }
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
    public void InitLoginBackground() //加载背景动画
    {
        LoginBackground = (WebView)findViewById(R.id.LoginBackground);
        LoginBackground.loadUrl("file:///android_asset/LoginBackground.html");
    }
    public void InitAlpha()
    {
        View LoginCard = findViewById(R.id.LoginCard);
        LoginCard.setAlpha(0.9f);
    }
    private void Login()
    {
        String UsernameStr = Username.getText().toString();
        String PasswordStr = Password.getText().toString();
        String url = "http://192.168.0.102:6888/Login";
        final OkHttpClient okHttpClient=new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Username", Base64.encodeToString(UsernameStr.getBytes(), Base64.DEFAULT))
                .add("Password", Base64.encodeToString(PasswordStr.getBytes(), Base64.DEFAULT))
                .build();
        final Request request=new Request.Builder().url(url).post(body).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response=okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()){
                        String body="Login:"+response.body().string();
                        Intent intent = new Intent("com.example.leeli.windroid");
                        intent.putExtra(Intent.EXTRA_TEXT,body);
                        LoginBroad.sendBroadcast(intent);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void Register()
    {
        String UsernameStr = Username.getText().toString();
        String PasswordStr = Password.getText().toString();
        String url = "http://192.168.0.102:6888/Register";
        final OkHttpClient okHttpClient=new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Username", Base64.encodeToString(UsernameStr.getBytes(), Base64.DEFAULT))
                .add("Password", Base64.encodeToString(PasswordStr.getBytes(), Base64.DEFAULT))
                .build();
        final Request request=new Request.Builder().url(url).post(body).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response=okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()){
                        String body="Register:"+response.body().string();
                        Intent intent = new Intent("com.example.leeli.windroid");
                        intent.putExtra(Intent.EXTRA_TEXT,body);
                        LoginBroad.sendBroadcast(intent);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

