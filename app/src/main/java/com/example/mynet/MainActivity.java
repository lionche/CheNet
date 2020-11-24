package com.example.mynet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.githang.statusbar.StatusBarCompat;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.material.snackbar.Snackbar;

import static com.blankj.utilcode.util.NetworkUtils.isAvailableByPing;
import static com.example.mynet.utils.GetAddress.getIpAddress;
import static com.example.mynet.utils.GetAddress.getMacAddressFromIp;
import static com.example.mynet.utils.GetAddress.isWifiEnabled;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "testhttp";
    private EditText et_name;
    private EditText et_password;
    static Button btn_login;
    static CoordinatorLayout coordinator;
    private CheckBox cb_rm_password;
    private CheckBox cb_au_login;
    private ImageView mushroom;
    static ProgressBar progressBar;
    boolean WebValidate;
    boolean WIFIEnable;
    boolean WIFIValidate;
    static boolean ifSucc;
    static boolean LoginSuccesss;

    PostBean postBean = new PostBean();

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: 我回来了，再次检测网络");
        WebValidate = false;

        IfWIFIValidate();
        IfLogin();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#32F2E1D6"));


        setContentView(R.layout.activity_main);
        initView();


        SharedPreferences sp = getSharedPreferences("mypassword", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();


        Boolean saveifrm = sp.getBoolean("IFRM", false);
        Boolean saveifau = sp.getBoolean("IFAU", false);
        String savename = sp.getString("NAME", null);
        String savepassword = sp.getString("PASSWORD", null);
        cb_rm_password.setChecked(saveifrm);
        cb_au_login.setChecked(saveifau);
        et_name.setText(savename);
        et_password.setText(savepassword);

        IfWIFIValidate();

        IfLogin();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (saveifau && WIFIValidate) {
                    autoLogin(savename, savepassword);
                }
            }
        }, 100); // 延时1.5秒


        cb_au_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) cb_rm_password.setChecked(true);
            }
        });

        cb_rm_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) cb_au_login.setChecked(false);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postBean.setName(et_name.getText().toString());
                postBean.setPassword(et_password.getText().toString());
                login(editor);

                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            }
        });

        mushroom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                int action = motionEvent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    Snackbar.make(coordinator, "恭喜你发现彩蛋啦！ 🚗 ❤ 🍄", Snackbar.LENGTH_SHORT).show();
                    setProgressBar(0,0);
                } else if (action == MotionEvent.ACTION_UP) {
                    setLoginBtn();
                }
                return true;
            }
        });






        mushroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(coordinator, "点人家干嘛，烦不烦呀", Snackbar.LENGTH_LONG).show();
                Log.d(TAG, "wifi是否链接: " + WIFIEnable + " 是否有网" + WebValidate);

            }
        });


    }


    private void IfLogin() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                WIFIValidate = WIFIEnable && !WebValidate;
                LoginSuccesss = !WIFIValidate;
                Log.d(TAG, "wifi是否链接: " + WIFIEnable + " 是否有网" + WebValidate + "我可以登陆" + WIFIValidate);
                wifiValidate();
            }
        }, 100); // 延时1.5秒
    }

    private void IfWIFIValidate() {
        iswebValidate();
        WIFIEnable = isWifiEnabled(getApplicationContext());
        getInfo();
    }

    private void iswebValidate() {
        new Thread() {
            @Override
            public void run() {
                WebValidate = isAvailableByPing("www.baidu.com");
            }
        }.start();
    }

    private void setProgressBar(int in, int out) {
        //进度条


        int shortAnimationDuration = in;
        int longAnimationDuration = out;


        progressBar.setAlpha(0f);
        progressBar.setVisibility(View.VISIBLE);

        progressBar.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(null);

        btn_login.animate()
                .alpha(0f)
                .setDuration(longAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        btn_login.setVisibility(View.GONE);
                    }
                });
    }

    private static void setLoginBtn() {
        //进度条


        int shortAnimationDuration = 1000;
        int longAnimationDuration = 700;


        btn_login.setAlpha(0f);
        btn_login.setVisibility(View.VISIBLE);

        btn_login.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(null);

        progressBar.animate()
                .alpha(0f)
                .setDuration(longAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }


    private void initView() {
        et_name = findViewById(R.id.et_name);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        coordinator = findViewById(R.id.coordinator);
        cb_rm_password = findViewById(R.id.rm_password);
        cb_au_login = findViewById(R.id.au_login);
        mushroom = findViewById(R.id.mushroom);
        progressBar = findViewById(R.id.progress);
        Sprite doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);

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

        if (loginname.isEmpty()) {
            et_name.setError("用户名不能为空🤭");
            valid = false;
        } else {
            et_name.setError(null);
        }

        return valid;
    }

    private boolean wifiValidate() {
        Boolean Validate = false;
        if (WIFIEnable) {
            if (!WebValidate) {
                Validate = true;
            } else {
                Snackbar.make(coordinator, "哈哈哈哈哈哈哈哈\n你其实已经登陆咯😙", Snackbar.LENGTH_LONG).show();
            }

        } else {
            Snackbar.make(coordinator, "这就来找我了 \n你咋不瞅瞅你连WIFI了没👀", Snackbar.LENGTH_LONG).show();
        }
        return Validate;
    }


    private void login(SharedPreferences.Editor editor) {
        Log.d(TAG, "wifiValidate: " + LoginSuccesss + "别点了，已经登录了");


        if (!wifiValidate())
            return;
        if (!nameValidate()) {
            return;
        }
        if (LoginSuccesss) {
            Snackbar.make(coordinator, "人家都帮你登录好啦，别点啦😏", Snackbar.LENGTH_LONG).show();
            return;
        }

        new Thread() {
            @Override
            public void run() {
                SendPost.LoginPost(postBean);
            }
        }.start();
        setProgressBar(700, 1000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ifSucc) {
                    spSave(postBean, editor);
                }
            }
        }, 1000); // 延时1.5秒
        iswebValidate();
    }

    static public Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            ifSucc = (boolean) msg.obj;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setLoginBtn();
                    if (ifSucc) {
                        Snackbar.make(coordinator, "登录成功啦 😚", Snackbar.LENGTH_LONG)
                                .show();
                        Log.d(TAG, "登录成功啦");
                        LoginSuccesss = true;

                    } else {
                        Snackbar.make(coordinator, "登录失败惹 😭", Snackbar.LENGTH_LONG)
                                .show();
                        Log.d(TAG, "登录失败惹");
                    }
                }
            }, 1500); // 延时1.5秒

        }
    };


    private void autoLogin(String savename, String savepassword) {
        postBean.setName(savename);
        postBean.setPassword(savepassword);
        new Thread() {
            @Override
            public void run() {
                SendPost.LoginPost(postBean);
            }
        }.start();
        setProgressBar(700, 1000);
    }


}