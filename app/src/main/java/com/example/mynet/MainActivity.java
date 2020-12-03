package com.example.mynet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.app.UiModeManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.SnackbarUtils;
import com.example.mynet.callback.LoginCallBackListener;
import com.example.mynet.callback.WIFICallBackListener;
import com.example.mynet.javabean.DevicesInfoBean;

import com.githang.statusbar.StatusBarCompat;
import com.github.chengang.library.TickView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.material.snackbar.Snackbar;


import java.util.ArrayList;
import java.util.List;

import static com.example.mynet.DeleteDevicesClass.DeleteDevices;
import static com.example.mynet.LoginClass.getPostBean;
import static com.example.mynet.LoginClass.login;
import static com.example.mynet.LoginClass.postBean;
import static com.example.mynet.RequestDevicesClass.devicesInfoBeanArrayList;
import static com.example.mynet.WIFIValidate.checkWIFIValidate;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "testhttp";
    private EditText et_name;
    private EditText et_password;
    static Button btn_login;
    static TickView btn_success;
    static Button btn_fail;
    static Button btn_wifi;
    public static CoordinatorLayout coordinator;
    private CheckBox cb_rm_password;
    private CheckBox cb_au_login;
    private ImageView mushroom;
    private ImageView mushroomsad;
    static ProgressBar progressBar;
    static LoginCallBackListener loginCallBackListener;
    static WIFICallBackListener wifiCallBackListener;
    private AlertDialog.Builder builder;


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: 我回来了，再次检测网络");
        View view = getButtonVisiable();
        view2view(view, progressBar);
        NewThreadCheckWIFI();
    }


    static Boolean saveifau;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();




        setSateBarColor();

        SharedPreferences sp = getSharedPreferences("mypassword", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        getSpSetSitting(sp);

        WIFICallBack();

        LoginCallBack(editor);

        //检测wifi状况，顺便检测是否自动登陆
        checkWIFIValidate();


        cb_rm_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) cb_au_login.setChecked(false);
            }
        });

        btn_wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });


        cb_au_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cb_au_login.isChecked()) {
                    cb_rm_password.setChecked(true);
                    Log.d(TAG, "我要自动登录");
                } else {
                    Log.d(TAG, "我不要自动登录");
                }
                editor.putBoolean("IFAU", cb_au_login.isChecked());
                editor.apply();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LegalToLogin();
            }
        });


        mushroom.setOnClickListener(new ClickUtils.OnMultiClickListener(10) {
            @Override
            public void onTriggerClick(View v) {
                Snackbar.make(coordinator, "恭喜你发现彩蛋啦！    🚗 ❤ ❤ 🍄", Snackbar.LENGTH_SHORT).show();

            }

            @Override
            public void onBeforeTriggerClick(View v, int count) {
            }
        });

//        mushroom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(coordinator, "没事点我干嘛 😝", Snackbar.LENGTH_SHORT).show();
//                Log.d(TAG, "onLongClick: 点击蘑菇");
//
//            }
//        });

        mushroomsad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onLongClick: 点击哭蘑菇");

            }
        });


        btn_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(coordinator, "人家都帮你登录好啦，别点啦😏", Snackbar.LENGTH_LONG).show();
                btn_success.setChecked(true);
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

    private void setSateBarColor() {
        UiModeManager uiModeManager = (UiModeManager) this.getSystemService(Context.UI_MODE_SERVICE);
        int modeType = uiModeManager.getNightMode();
        switch (modeType){
            case 1:
                Log.d(TAG, "onCreate: 白天模式");
                StatusBarCompat.setStatusBarColor(this, Color.parseColor("#FFFFFF"));
                break;
            case 2:
                Log.d(TAG, "onCreate: 夜间模式");
                StatusBarCompat.setStatusBarColor(this, Color.parseColor("#FF000000"));
                break;
        }
    }


    private View getButtonVisiable() {
        View view = null;
        if (btn_success.getVisibility() == View.VISIBLE)
            view = btn_success;
        if (btn_login.getVisibility() == View.VISIBLE)
            view = btn_login;
        if (btn_fail.getVisibility() == View.VISIBLE)
            view = btn_fail;
        if (progressBar.getVisibility() == View.VISIBLE)
            view = progressBar;
        if (btn_wifi.getVisibility() == View.VISIBLE)
            view = btn_wifi;
        return view;
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

    private static void NewThreadCheckWIFI() {
        new Thread() {
            @Override
            public void run() {
                checkWIFIValidate();
            }
        }.start();
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
                message.setData(bundle);
                handler.sendMessage(message);
            }

            @Override
            public void toLogin() {
                //获取WiFi和MAC
                getPostBean();

                if (cb_au_login.isChecked()) {
                    Log.d(TAG, "toLogin: 发送消息给HANDLER自动登陆");
                    Message message = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putString("ButtonCallBack", "AutoLogin");
                    bundle.putString("TYPE", "ButtonCallBack");
                    message.setData(bundle);
                    handler.sendMessage(message);
                }

            }
        });
    }

    private void LoginCallBack(SharedPreferences.Editor editor) {
        Log.d(TAG, "loginCallBack: 我在等登录消息的回调");

        loginCallBackListener = new LoginCallBackListener();
        loginCallBackListener.setmListener(new LoginCallBackListener.Listener() {
            @Override
            public void SendLoginMessage(Boolean b, char c) {
                Message message = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putChar("WrongeMessage", c);
                bundle.putBoolean("LoginCallBack", b);
                bundle.putString("TYPE", "LoginCallBack");
                message.setData(bundle);
                handler.sendMessage(message);

                if (b) {
                    if (cb_rm_password.isChecked()) {
                        RemPassword(editor);
                    }
                    if (!cb_rm_password.isChecked()) {
                        ForgetPassword(editor);
                    } else if (cb_au_login.isChecked()) {
                    }
                } else {
                    editor.putBoolean("IFAU", false);
                    editor.putString("NAME", postBean.getName());
                }
                editor.apply();

            }
        });
    }




    private void RemPassword(SharedPreferences.Editor editor) {
        String name = postBean.getName();
        String password = postBean.getPassword();
        editor.putString("NAME", name);
        editor.putString("PASSWORD", password);
        editor.putBoolean("IFRM", true);
        Log.d(TAG, "我要记住密码 " + name + " " + password);
        editor.apply();
    }

    private void ForgetPassword(SharedPreferences.Editor editor) {
        editor.putString("PASSWORD", null);
        editor.putBoolean("IFRM", false);
        Log.d(TAG, "我要不记住密码 ");
        editor.apply();
    }


    static int shortAnimationDuration = 300;
    static int longAnimationDuration = 300;


    public void view2view(View view1, View view2) {
        //进度条

        view2.setAlpha(0f);
        view2.setVisibility(View.VISIBLE);

        view2.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(null);

        view1.animate()
                .alpha(0f)
                .setDuration(longAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view1.setVisibility(View.GONE);
                    }
                });
    }

    public void load2succ() {
        Log.d(TAG, "load2succ: 按键加载到成功");
        //进度条
        btn_success.setAlpha(0f);
        btn_success.setVisibility(View.VISIBLE);

        btn_success.animate()
                .alpha(1f)
                .setDuration(400)
                .setListener(null);

        progressBar.animate()
                .alpha(0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        progressBar.setVisibility(View.GONE);
                    }
                });
        btn_success.setChecked(true);
    }




    public void setMushroomFace(View view1, View view2) {

        //进度条

        view2.setAlpha(0f);
        view2.setVisibility(View.VISIBLE);
        view2.animate()
                .alpha(1f)
                .setDuration(1000)
                .setListener(null);

        view1.animate()
                .alpha(0f)
                .setDuration(1000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view1.setVisibility(View.GONE);
                    }
                });


    }


    private void load2fail() {
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

    private void load2login() {
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

    private void succ2load() {
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

    private void fail2load() {
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


    private void login2load() {
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


    private void showDeleteDevicesDialog(List<DevicesInfoBean> devicesInfoBeanArrayList) {
        final List<Integer> choice = new ArrayList<>();
        Log.d(TAG, "showDeleteDevicesDialog大小: " + devicesInfoBeanArrayList.size());
        String[] deviceslistDeviceType = new String[devicesInfoBeanArrayList.size()];

        for (int i = 0; i < devicesInfoBeanArrayList.size(); i++) {
            deviceslistDeviceType[i] = devicesInfoBeanArrayList.get(i).getDeviceType();
        }


        //默认都未选中
        boolean[] isSelect = {false, false};

        builder = new AlertDialog.Builder(this).setIcon(R.drawable.mushroomlogosad)
                .setTitle("放弃掉一个设备 ")
                .setMultiChoiceItems(deviceslistDeviceType, isSelect, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {

                        if (b) {
                            choice.add(i);
                        } else {
                            choice.remove(choice.indexOf(i));
                        }


                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int whitch) {
                        StringBuilder str = new StringBuilder();

                        for (int i = 0; i < choice.size(); i++) {
                            Log.d(TAG, "onClick抛弃名字: "+devicesInfoBeanArrayList.get(choice.get(i)).getDeviceType()+"    ");
                            str.append(devicesInfoBeanArrayList.get(choice.get(i)).getDeviceType()+"    ");
                            DeleteDevices(devicesInfoBeanArrayList.get(choice.get(i)).getAcct_unique_id());
                        }
                        Snackbar.make(coordinator, "你抛弃了" + str, Snackbar.LENGTH_LONG).show();
                        Log.d(TAG, "onClick: "+"你抛弃了"+str);

                        if(choice.size() != 0){
                            LegalToLogin();
                            Log.d(TAG, "onClick: 抛弃完，我再登录");
                        }else {
                            Snackbar.make(coordinator, "舍不得孩子连不了网啊" + str, Snackbar.LENGTH_LONG)
                                    .setAction("再试一次", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            showDeleteDevicesDialog(devicesInfoBeanArrayList);
                                        }
                                    })
                                    .show();

                        }

                        

                    }
                });
        builder.create().show();
    }


    private void initView() {
        et_name = findViewById(R.id.et_name);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        btn_success = findViewById(R.id.btn_success);
        btn_fail = findViewById(R.id.btn_fail);
        btn_wifi = findViewById(R.id.btn_wifi);
        coordinator = findViewById(R.id.coordinator);
        cb_rm_password = findViewById(R.id.rm_password);
        cb_au_login = findViewById(R.id.au_login);
        mushroom = findViewById(R.id.mushroom);
        mushroomsad = findViewById(R.id.mushroomsad);
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


    private void LegalToLogin() {
        if (!nameValidate()) {
            return;
        }
        postBean.setName(et_name.getText().toString());
        postBean.setPassword(et_password.getText().toString());
        login();
        view2view(btn_login, progressBar);
        view2view(btn_fail,progressBar);
    }

    private void fail2login(SharedPreferences.Editor editor) {
        if (!nameValidate()) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                LoginPostClass.LoginPost(postBean);
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
                    char wrongmessage = bundle.getChar("WrongeMessage");
                    Boolean ifLoginSucc = bundle.getBoolean("LoginCallBack");
                    loginMessageHandler(ifLoginSucc, wrongmessage);
                    break;
                case "WIFICallBack":
                    int ifWIFIValidate = bundle.getInt("WIFICallBack");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            WIFIMessageHandler(ifWIFIValidate);
                        }
                    }, 800); // 延时1.5秒
                    break;
                case "ButtonCallBack":
                    String ButtonChange = bundle.getString("ButtonCallBack");
                    switch (ButtonChange) {
                        case "AutoLogin":
                            //自动登陆
                            Log.d(TAG, "handleMessage: 我收到消息要自动登陆");
                            Snackbar.make(coordinator, "偷偷帮你自动登陆啦！ 🤫 ", Snackbar.LENGTH_LONG).show();
                            postBean.setName(et_name.getText().toString());
                            postBean.setPassword(et_password.getText().toString());
                            login();
                            break;
                    }
            }
            return false;
        }

        private void WIFIMessageHandler(int ifWIFIValidate) {
            switch (ifWIFIValidate) {
                case 1:
                    Log.d(TAG, "checkWIFIValidate: WIFI都没打开哥");
/*                    SnackbarUtils.with(coordinator)
                            .setMessage("测试")
                            .setMessageColor(Color.BLACK)
                            .setBgResource(R.color.mushroom)
                            .show();*/

                    Snackbar.make(coordinator, "WIFI都没打开哥 😨", Snackbar.LENGTH_LONG)
                            .setAction("开启WIFI", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                                }
                            })
                            .show();
                    view2view(progressBar, btn_wifi);
                    setMushroomFace(mushroom, mushroomsad);


                    break;
                case 2:
                    Log.d(TAG, "checkWIFIValidate: 这就来找我了 你咋不瞅瞅你连WIFI了没");
                    Snackbar.make(coordinator, "这就来找我了 \n你咋不瞅瞅你连WIFI了没👀", Snackbar.LENGTH_LONG)
                            .setAction("选择网络", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                                }
                            })
                            .show();
                    setMushroomFace(mushroom, mushroomsad);
                    view2view(progressBar, btn_wifi);
                    break;
                case 3:
                    Log.d(TAG, "checkWIFIValidate: 哈哈哈哈哈哈哈哈你其实已经登陆咯");
                    Snackbar.make(coordinator, "哈哈哈哈哈哈哈哈\n你其实已经连网咯😙", Snackbar.LENGTH_LONG)
                            .setAction("再见", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    finish();
                                }
                            })
                            .show();
                    setMushroomFace(mushroomsad, mushroom);
                    load2succ();

                    break;
                case 4:
                    if (!saveifau) {
                        load2login();
                        setMushroomFace(mushroom, mushroomsad);
                        view2view(mushroom,mushroomsad);
                        Log.d(TAG, "checkWIFIValidate: 让我帮你登录叭");
                        Snackbar.make(coordinator, "让我帮你登录叭😃", Snackbar.LENGTH_LONG).show();
                    }
                    break;
            }
        }

        private void loginMessageHandler(Boolean ifLoginSucc, char wrongmessage) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (ifLoginSucc) {
                        // 按键转成功
                        load2succ();
                        setMushroomFace(mushroomsad, mushroom);

                        Snackbar.make(coordinator, "登录成功啦 😚", Snackbar.LENGTH_LONG)
                                .setAction("爱我一下", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Snackbar.make(coordinator, "我也爱你! 😜", Snackbar.LENGTH_LONG)
                                                .setAction("拜拜 😘", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        finish();
                                                    }
                                                })
                                                .show();
                                    }
                                })
                                .show();
                        Log.d(TAG, "登录成功啦");

                    } else {
                        view2view(progressBar, btn_fail);
                        setMushroomFace(mushroom, mushroomsad);
                        if (wrongmessage == 'i') {
                            //按键转失败

                            Snackbar.make(coordinator, "你搞错用户名或者密码啦 😭", Snackbar.LENGTH_LONG)
                                    .setAction("忘记密码", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Snackbar.make(coordinator, "忘记了你还连个P🤭", Snackbar.LENGTH_LONG).show();
                                        }
                                    })
                                    .show();
                            Log.d(TAG, "登录密码错误");
                        }
                        if (wrongmessage == 'm') {
                            Snackbar.make(coordinator, "唉，只能登录2个设备\n没法帮你啦 😭", Snackbar.LENGTH_LONG)
                                    .show();
                            //弹出删除设备的对话框
                            showDeleteDevicesDialog(devicesInfoBeanArrayList);
                            Log.d(TAG, "登录2个设备");
                        }
                        if (wrongmessage == 't') {
                            Snackbar.make(coordinator, "连接超时？ 你是不是连错网啦", Snackbar.LENGTH_LONG)
                                    .setAction("换个网络", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                                        }
                                    })
                                    .show();
                            Log.d(TAG, "连接超时");
                        }
                    }
                }
            }, 400); // 延时1.5秒
        }
    });


}