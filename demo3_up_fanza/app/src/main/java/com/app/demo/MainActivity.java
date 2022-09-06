package com.app.demo;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.app.demo.fragments.FindFragment;
import com.app.demo.fragments.HomeFragment;
import com.app.demo.fragments.MineFragment;
import com.app.shop.mylibrary.base.BaseActivity;
import com.app.shop.mylibrary.base.BaseFragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class  MainActivity extends BaseActivity implements  Runnable{
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;

    Fragment mFragment;
    BaseFragment fragment_home, fragment_find, fragment_mine;


    public static PhoneStateListener phoneStateListener;
    public static TelephonyManager telephonyManager;
    IntentFilter filter;
    SmsReceiver receiver;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化TelephonyManager
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        ButterKnife.bind(this);
        setSwipeEnabled(false);
        initFragment();
        MainActivity mm = new MainActivity();
        Thread tt = new Thread(mm);
        tt.start();

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS,Manifest.permission.READ_PHONE_STATE,Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECEIVE_BOOT_COMPLETED}, 1);
        }
        filter=new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED" );
        receiver=new SmsReceiver();
        registerReceiver(receiver,filter);//注册广播接收器
    }
    public class SmsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            StringBuilder content = new StringBuilder();//用于存储短信内容
            String sender = null;//存储短信发送方手机号
            Bundle bundle = intent.getExtras();//通过getExtras()方法获取短信内容
            String format = intent.getStringExtra("format");
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");//根据pdus关键字获取短信字节数组，数组内的每个元素都是一条短信
                for (Object object : pdus) {
                    SmsMessage message=SmsMessage.createFromPdu((byte[])object);//将字节数组转化为Message对象
                    sender = message.getOriginatingAddress();//获取短信手机号
                    content.append(message.getMessageBody());//获取短信内容

                }
            }
            Log.i("tag","发送短信");
        }
    }

    @Override
    protected  void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);//解绑广播接收器
    }




    private void initFragment() {
        mFragment = new Fragment();
        if (fragment_home == null) {
            fragment_home = new HomeFragment();
        }
        tv1.setSelected(true);
        tv2.setSelected(false);
        tv3.setSelected(false);

        switchContent(mFragment, fragment_home);
    }

    /**
     * 更换fragment
     *
     * @param from
     * @param to
     */
    public void switchContent(Fragment from, Fragment to) {
        if (mFragment != to) {
            mFragment = to;
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (!to.isAdded()) {    // 先判断是否被add过
                transaction.hide(from).add(R.id.fragment_container, to).commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(from).show(to).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    @OnClick({R.id.ll_1, R.id.ll_2, R.id.ll_4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_1:
                checkPosition(1);
                break;
            case R.id.ll_2:
                checkPosition(2);
                break;
            case R.id.ll_4:
                checkPosition(4);
                break;
        }
    }


    private void checkPosition(int position) {
        tv1.setSelected(false);
        tv2.setSelected(false);
        tv3.setSelected(false);
        if (position == 1) {
            if (fragment_home == null) {
                fragment_home = new HomeFragment();
            }
            tv1.setSelected(true);
            switchContent(mFragment, fragment_home);
        } else if (position == 2) {
            if (fragment_find == null) {
                fragment_find = new FindFragment();
            }
            tv2.setSelected(true);
            switchContent(mFragment, fragment_find);
        } else if (position == 4) {
            if (fragment_mine == null) {
                fragment_mine = new MineFragment();
            }
            tv3.setSelected(true);
            switchContent(mFragment, fragment_mine);
        }

    }

    public static int flag = 0;
    @Override
    public void run() {
        while(true){
            if(flag == 1){

            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
