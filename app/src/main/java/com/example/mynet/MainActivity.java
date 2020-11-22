package com.example.mynet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import static com.example.mynet.SendPost.passwordPost;
import static com.example.mynet.utils.GetAddress.getIpAddress;
import static com.example.mynet.utils.GetAddress.getMacAddressFromIp;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "testhttp";
    private EditText et_password;
    private EditText et_name;
    private Button btn_login;
    static CoordinatorLayout coordinator;
    private CheckBox cb_rm_password;
    private CheckBox cb_au_login;
    private ImageView mushroom;

    PostBean postBean = new PostBean();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_name = findViewById(R.id.et_name);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        coordinator = findViewById(R.id.coordinator);
        cb_rm_password = findViewById(R.id.rm_password);
        cb_au_login = findViewById(R.id.au_login);
        mushroom = findViewById(R.id.mushroom);


        SharedPreferences sp = getSharedPreferences("mypassword", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        getInfo();

        Boolean saveifrm = sp.getBoolean("IFRM", false);
        Boolean saveifau = sp.getBoolean("IFAU", false);
        String savename = sp.getString("NAME", null);
        String savepassword = sp.getString("PASSWORD", null);


        cb_rm_password.setChecked(saveifrm);
        cb_au_login.setChecked(saveifau);
        et_name.setText(savename);
        et_password.setText(savepassword);

        if (cb_au_login.isChecked()) {
            postBean.setName(savename);
            postBean.setPassword(savepassword);
            SendPost.LoginPost(postBean);
        }


        cb_au_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) cb_rm_password.setChecked(true);
            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postBean.setName(et_name.getText().toString());
                postBean.setPassword(et_password.getText().toString());

                SendPost.LoginPost(postBean);
                spSave(postBean, editor);
            }
        });

        mushroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onCreate: 我记住的密码为" + savename + " " + savepassword);
            }
        });
    }


    private void getInfo() {
        postBean.setIpadr(getIpAddress(this));
        postBean.setMacadr(getMacAddressFromIp(this));


    }

    private void spSave(PostBean postBean, SharedPreferences.Editor editor) {
        String name = postBean.getName();
        String password = postBean.getPassword();
        if (cb_rm_password.isChecked()) {
            editor.putString("NAME", name);
            editor.putString("PASSWORD", password);
            editor.putBoolean("IFRM", true);
            Log.d(TAG, "我要记住密码 " + name + " " + password);
        } else {
            Log.d(TAG, "我不记住密码");
            editor.putBoolean("IFRM", false);
            editor.putString("PASSWORD", null);

        }
        if (cb_au_login.isChecked()) {
            editor.putBoolean("IFAU", true);
            Log.d(TAG, "我要自动登录");
        } else {
            editor.putBoolean("IFAU", false);
            Log.d(TAG, "我不要自动登录");
        }
        editor.apply();
    }

}