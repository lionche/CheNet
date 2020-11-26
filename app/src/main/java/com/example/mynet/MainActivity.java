package com.example.mynet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapRegionDecoder;
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

import com.example.mynet.callback.LoginCallBackListener;
import com.example.mynet.callback.WIFICallBackListener;
import com.githang.statusbar.StatusBarCompat;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.material.snackbar.Snackbar;
import static com.blankj.utilcode.util.NetworkUtils.isAvailableByPing;
import static com.example.mynet.LoginClass.getPostBean;
import static com.example.mynet.LoginClass.login;
import static com.example.mynet.LoginClass.postBean;
import static com.example.mynet.WIFIValidate.checkWIFIValidate;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "testhttp";
    private EditText et_name;
    private EditText et_password;
    static Button btn_login;
    static Button btn_success;
    static Button btn_fail;
    public static CoordinatorLayout coordinator;
    private CheckBox cb_rm_password;
    private CheckBox cb_au_login;
    private ImageView mushroom;
    static ProgressBar progressBar;
    boolean WebValidate;
    boolean WIFIEnable;
    static LoginCallBackListener loginCallBackListener;
    static WIFICallBackListener wifiCallBackListener;



    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: 我回来了，再次检测网络");

        WebValidate = false;

        button2load();

        NewThreadCheckWIFI();

//        aulogin(cb_au_login.isChecked(), et_name.getText().toString(), et_password.getText().toString());
    }


    private void button2load() {
        if (btn_fail.getVisibility() == View.VISIBLE) {
            fail2load();
        }
        if (btn_success.getVisibility() == View.VISIBLE) {
            succ2load();
        }
        if (btn_login.getVisibility() == View.VISIBLE) {
            login2load();

        }
    }

    static Boolean saveifau;
    static Boolean IfRemPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#32F2E1D6"));
        setContentView(R.layout.activity_main);
        initView();

        SharedPreferences sp = getSharedPreferences("mypassword", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        getSpSetSitting(sp);

        WIFICallBack();

        LoginCallBack(editor);

        NewThreadCheckWIFI();


        LogicCheckBox();


        cb_rm_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cb_rm_password.isChecked()) {
                    editor.putBoolean("IFRM", true);
                    Log.d(TAG, "我要记住密码");
                } else {
                    editor.putBoolean("IFRM", false);
                    Log.d(TAG, "我不要记住密码");
                }
                editor.apply();
            }
        });


        cb_au_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cb_au_login.isChecked()) {
                    editor.putBoolean("IFAU", true);
                    Log.d(TAG, "我要自动登录");
                } else {
                    editor.putBoolean("IFAU", false);
                    Log.d(TAG, "我不要自动登录");
                }
                editor.apply();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LegalToLogin(editor);
                login2load();

            }
        });

//        mushroom.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//
//                int action = motionEvent.getAction();
//                if (action == MotionEvent.ACTION_DOWN) {
//                    Snackbar.make(coordinator, "恭喜你发现彩蛋啦！ 🚗 ❤ 🍄", Snackbar.LENGTH_SHORT).show();
//                    login2load();
//                } else if (action == MotionEvent.ACTION_UP) {
//                    load2succ();
//                    ;
//                }
//                return true;
//            }
//        });



        btn_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(coordinator, "人家都帮你登录好啦，别点啦😏", Snackbar.LENGTH_LONG).show();
            }
        });

        btn_fail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fail2load();
                postBean.setName(et_name.getText().toString());
                postBean.setPassword(et_password.getText().toString());
                fail2login(editor);
            }
        });

    }

    private void getSpSetSitting(SharedPreferences sp) {
        Boolean saveifrm = sp.getBoolean("IFRM", false);
        saveifau = sp.getBoolean("IFAU", false);
        String savename = sp.getString("NAME", null);
        String savepassword = sp.getString("PASSWORD", null);
        cb_rm_password.setChecked(saveifrm);
        cb_au_login.setChecked(saveifau);
        et_name.setText(savename);
        et_password.setText(savepassword);
    }

    private void NewThreadCheckWIFI() {
        new Thread() {
            @Override
            public void run() {
                checkWIFIValidate();
            }
        }.start();
    }

    private void LogicCheckBox() {
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


    }

    private void WIFICallBack() {
        Log.d(TAG, "WIFICallBack: 我在等网络状况的消息的回调");

        wifiCallBackListener = new WIFICallBackListener();

        wifiCallBackListener.setmListener(new WIFICallBackListener.Listener() {

            @Override
            public void SendWIFIMessage(int caseid) {
                Message message = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putInt("WIFICallBack", caseid);
                bundle.putString("TYPE", "WIFICallBack");

                bundle.putBoolean("AutoLogin", MainActivity.saveifau);
                message.setData(bundle);
                handler.sendMessage(message);
            }
            @Override
            public void toLogin() {
                //获取WiFi和MAC
                getPostBean();

            }
        });
    }

    private void LoginCallBack(SharedPreferences.Editor editor) {
        Log.d(TAG, "loginCallBack: 我在等登录消息的回调");

        loginCallBackListener = new LoginCallBackListener();
        loginCallBackListener.setmListener(new LoginCallBackListener.Listener() {
            @Override
            public void SendLoginMessage(Boolean b) {
                Message message = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putBoolean("LoginCallBack", b);
                bundle.putString("TYPE", "LoginCallBack");
                message.setData(bundle);
                handler.sendMessage(message);

                if(cb_rm_password.isChecked() && b)
                    RemPassword(editor);
            }
        });
    }


    private void RemPassword(SharedPreferences.Editor editor) {
        String name = postBean.getName();
        String password = postBean.getPassword();
        editor.putString("NAME", name);
        editor.putString("PASSWORD", password);
        Log.d(TAG, "我要记住密码 " + name + " " + password);
        editor.apply();
    }


    private void iswebValidate() {
        new Thread() {
            @Override
            public void run() {
                WebValidate = isAvailableByPing("www.baidu.com");
            }
        }.start();
    }


    static int shortAnimationDuration = 200;
    static int longAnimationDuration = 200;


    static public void load2succ() {
        //进度条

        btn_success.setAlpha(0f);
        btn_success.setVisibility(View.VISIBLE);

        btn_success.animate()
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

    static private void load2fail() {
        //进度条


        btn_fail.setAlpha(0f);
        btn_fail.setVisibility(View.VISIBLE);

        btn_fail.animate()
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

    static private void load2login() {
        //进度条


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

    static private void succ2load() {
        //进度条


        progressBar.setAlpha(0f);
        progressBar.setVisibility(View.VISIBLE);

        progressBar.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(null);

        btn_success.animate()
                .alpha(0f)
                .setDuration(longAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        btn_success.setVisibility(View.GONE);
                    }
                });
    }

    static private void fail2load() {
        //进度条


        progressBar.setAlpha(0f);
        progressBar.setVisibility(View.VISIBLE);

        progressBar.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(null);

        btn_fail.animate()
                .alpha(0f)
                .setDuration(longAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        btn_fail.setVisibility(View.GONE);
                    }
                });
    }


    static private void login2load() {
        Log.d(TAG, "login2load: 登录到加载");
        //登录到加载
        //进度条


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


    private void initView() {
        et_name = findViewById(R.id.et_name);
        et_password = findViewById(R.id.et_password);

        btn_login = findViewById(R.id.btn_login);
        btn_success = findViewById(R.id.btn_success);
        btn_fail = findViewById(R.id.btn_fail);
        coordinator = findViewById(R.id.coordinator);
        cb_rm_password = findViewById(R.id.rm_password);
        cb_au_login = findViewById(R.id.au_login);
        mushroom = findViewById(R.id.mushroom);
        progressBar = findViewById(R.id.progress);
        Sprite doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);

    }



    public boolean nameValidate() {
        boolean valid = true;
        String loginname = et_name.getText().toString();

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
                load2login();
                Snackbar.make(coordinator, "让我帮你登录叭😃", Snackbar.LENGTH_LONG).show();
                Log.d(TAG, "wifiValidate: 连wifi但没有网");
            } else {
                Snackbar.make(coordinator, "哈哈哈哈哈哈哈哈\n你其实已经登陆咯😙", Snackbar.LENGTH_LONG).show();
                Log.d(TAG, "wifiValidate: 连wifi有网");
                load2succ();
            }

        } else {
            Snackbar.make(coordinator, "这就来找我了 \n你咋不瞅瞅你连WIFI了没👀", Snackbar.LENGTH_LONG).show();
            Log.d(TAG, "wifiValidate: 没连wifi");
        }
        return Validate;
    }


    private void LegalToLogin(SharedPreferences.Editor editor) {
        if (!nameValidate()) {
            return;
        }
        postBean.setName(et_name.getText().toString());
        postBean.setPassword(et_password.getText().toString());
        login();
            }

    private void fail2login(SharedPreferences.Editor editor) {
        if (!nameValidate()) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                SendPost.LoginPost(postBean);
            }
        }.start();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (ifLoginSucc) {
//                    spSave(postBean, editor);
//                }
//            }
//        }, 1000); // 延时1.5秒

    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            Bundle bundle = message.getData();
            String type = bundle.getString("TYPE");


            switch (type) {
                case "LoginCallBack":
                    Boolean ifLoginSucc = bundle.getBoolean("LoginCallBack");
                    Log.d(TAG, "handleMessage: ifLoginSucc" + ifLoginSucc);
                    loginMessageHandler(ifLoginSucc);
                    break;
                case "WIFICallBack":
                    int ifWIFIValidate = bundle.getInt("WIFICallBack");

                    WIFIMessageHandler(ifWIFIValidate);
                    Log.d(TAG, "handleMessage: 我收到消息，在自动登录嘛？ "+bundle.getBoolean("AutoLogin"));
                    if(bundle.getBoolean("AutoLogin"))
                        btn_login.performClick();
                    break;
            }
            return false;
        }

        private void WIFIMessageHandler(int ifWIFIValidate) {
            switch (ifWIFIValidate) {
                case 1:
                    Log.d(TAG, "checkWIFIValidate: WIFI都没打开哥");
                    Snackbar.make(coordinator, "WIFI都没打开哥😓 ", Snackbar.LENGTH_LONG).show();
                    load2fail();
                    break;
                case 2:
                    Log.d(TAG, "checkWIFIValidate: 这就来找我了 你咋不瞅瞅你连WIFI了没");
                    Snackbar.make(coordinator, "这就来找我了 \n你咋不瞅瞅你连WIFI了没👀", Snackbar.LENGTH_LONG).show();
                    load2fail();
                    break;
                case 3:
                    Log.d(TAG, "checkWIFIValidate: 哈哈哈哈哈哈哈哈你其实已经登陆咯");
                    Snackbar.make(coordinator, "哈哈哈哈哈哈哈哈,\n你其实已经登陆咯😙", Snackbar.LENGTH_LONG).show();
                    load2succ();
                    break;
                case 4:
                    Log.d(TAG, "checkWIFIValidate: 让我帮你登录叭");
                    Snackbar.make(coordinator, "让我帮你登录叭😃", Snackbar.LENGTH_LONG).show();
                    load2login();
                    break;
            }
        }

        private void loginMessageHandler(Boolean ifLoginSucc) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (ifLoginSucc) {
                        // 按键转成功
                        load2succ();
                        Snackbar.make(coordinator, "登录成功啦 😚", Snackbar.LENGTH_LONG).show();
                        Log.d(TAG, "登录成功啦");

                    } else {
                        //按键转失败
                        load2fail();
                        Snackbar.make(coordinator, "登录失败惹 😭", Snackbar.LENGTH_LONG)
                                .show();
                        Log.d(TAG, "登录失败惹");
                    }
                }
            }, 300); // 延时1.5秒
        }
    });




}