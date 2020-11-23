package com.example.mynet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mynet.utils.GetAddress;
import com.example.mynet.utils.MyThread;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

import static com.blankj.utilcode.util.NetworkUtils.isAvailableByPing;
import static com.example.mynet.SendPost.passwordPost;
import static com.example.mynet.utils.GetAddress.getIpAddress;
import static com.example.mynet.utils.GetAddress.getMacAddressFromIp;
import static com.example.mynet.utils.GetAddress.getWifiName;
import static com.example.mynet.utils.GetAddress.isWifiEnabled;
import static com.example.mynet.utils.MyThread.WebValidate;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "testhttp";
    private EditText et_password;
    private EditText et_name;
    Button btn_login;
    static CoordinatorLayout coordinator;
    private CheckBox cb_rm_password;
    private CheckBox cb_au_login;
    private ImageView mushroom;

    PostBean postBean = new PostBean();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyThread myThread = new MyThread ();
        new MyThread().start();



        SharedPreferences sp = getSharedPreferences("mypassword", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        initView();
        wifiValidate();
        getInfo();

        Boolean saveifrm = sp.getBoolean("IFRM", false);
        Boolean saveifau = sp.getBoolean("IFAU", false);
        String savename = sp.getString("NAME", null);
        String savepassword = sp.getString("PASSWORD", null);
        cb_rm_password.setChecked(saveifrm);
        cb_au_login.setChecked(saveifau);
        et_name.setText(savename);
        et_password.setText(savepassword);

        autoLogin(savename, savepassword);


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
                login(editor);
                setProgressBar();

                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            }
        });

        mushroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(coordinator, "好痛啊，别点我啦！", Snackbar.LENGTH_SHORT).show();

            }
        });

        mushroom.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Snackbar.make(coordinator, "🚗 ❤ 🍄", Snackbar.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private void setProgressBar() {
        //进度条
        btn_login.setVisibility(View.GONE);
        ProgressBar progressBar ;
        Sprite doubleBounce = new DoubleBounce();
        progressBar = findViewById(R.id.progress);
        progressBar.setIndeterminateDrawable(doubleBounce);
        progressBar.setVisibility(View.VISIBLE);
    }




    private void initView() {
        et_name = findViewById(R.id.et_name);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        coordinator = findViewById(R.id.coordinator);
        cb_rm_password = findViewById(R.id.rm_password);
        cb_au_login = findViewById(R.id.au_login);
        mushroom = findViewById(R.id.mushroom);



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

    private boolean nameValidate() {
        boolean valid = true;
        String loginname = et_name.getText().toString();
        String loginpassword = et_password.getText().toString();

        if (loginname.isEmpty() ) {
            et_name.setError("用户名不能为空");
            valid = false;
        } else {
            et_name.setError(null);
        }

        return valid;
    }

    private boolean wifiValidate() {
        boolean valid = false;
        if (isWifiEnabled(this)) {
            if (!webValidate()) {
                valid = true;
            }
        } else {
            Snackbar.make(coordinator, "少来消遣我 \n你咋不瞅瞅你连WIFI了没👀", Snackbar.LENGTH_LONG).show();
        }
        return valid;
    }

    private boolean webValidate() {
        if (WebValidate) {
            Snackbar.make(coordinator, "你已经连网啦，还来看人家，爱你哟", Snackbar.LENGTH_LONG).show();
        }
        return WebValidate;
    }


    private void login(SharedPreferences.Editor editor) {
        if (!wifiValidate()) {
            return;
        }
        if (!nameValidate()) {
            return;
        }

//        setProgressBar();
        SendPost.LoginPost(postBean);
        spSave(postBean, editor);
    }
    private void autoLogin(String savename, String savepassword) {
        if (!wifiValidate()) {
            return;
        }
        if (cb_au_login.isChecked()) {
            postBean.setName(savename);
            postBean.setPassword(savepassword);
            setProgressBar();
            SendPost.LoginPost(postBean);
        }
    }

}