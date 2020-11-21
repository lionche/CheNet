package com.example.mynet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.mynet.SendPost.getInfo;
import static com.example.mynet.SendPost.passwordPost;
import static com.example.mynet.utils.GetAddress.getIpAddress;
import static com.example.mynet.utils.GetAddress.getMacAddressFromIp;

public class MainActivity extends AppCompatActivity {

    private EditText et_password;
    private EditText et_name;
    private Button bt_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_name = findViewById(R.id.et_name);
        et_password = findViewById(R.id.et_password);
        bt_login = findViewById(R.id.bt_login);

        String ipadr = getIpAddress(this);
        String macadr = getMacAddressFromIp(this);




        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = et_name.getText().toString();
                String password = et_password.getText().toString();

                Log.d("testhttp",name+" "+password);
                getInfo(name,password,ipadr,macadr);
                SendPost.LoginPost();
            }
        });


    }

}