package com.app.demo.fragments;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.demo.MainActivity;
import com.app.demo.R;
import com.app.demo.activitys.LoginActivity;
import com.app.demo.adapters.HomeListAdapter;
import com.app.demo.beans.HomeBean;
import com.app.shop.mylibrary.MyWebActivity;
import com.app.shop.mylibrary.base.BaseFragment;
import com.app.shop.mylibrary.beans.EventMessage;
import com.app.shop.mylibrary.utils.DialogUtil;
import com.app.shop.mylibrary.utils.SharedPreferencesUtil;
import com.app.shop.mylibrary.utils.UserManager;
import com.app.shop.mylibrary.widgts.CustomDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.iflytek.cloud.RecognizerResult;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class  HomeFragment extends BaseFragment  {

    public static HomeFragment homefragment = new HomeFragment();
    @BindView(R.id.recy)
    RecyclerView recy;
    @BindView(R.id.iv_1)
    ImageView iv_1;
    @BindView(R.id.iv_2)
    ImageView iv_2;
    @BindView(R.id.iv_3)
    ImageView iv_3;
    @BindView(R.id.dong_tai)
    TextView dong_tai;

    private List<HomeBean> arrayList = new ArrayList<>();
    private List<HomeBean> list = new ArrayList<>();
    private HomeListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        int a = arrayList.size();
        registerEventBus();

        initList();

        return view;
    }

    //??????
    private void initList() {
        //?????????????????????
        if (!UserManager.isLogin(getActivity())) {
            skipActivity(getActivity(), LoginActivity.class);
            getActivity().finish();
        }

        recy.setFocusable(false);
        recy.setFocusableInTouchMode(false);
        list.clear();
        arrayList.clear();
        //arrayList = DataSupport.findAll(HomeBean.class);
        if (arrayList == null || arrayList.size() == 0) {
            initData();
        }
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).stype == 0) {
                list.add(arrayList.get(i));
            }
        }

        adapter = new HomeListAdapter(R.layout.item_list, list);
        recy.setLayoutManager(new LinearLayoutManager(getActivity()));
        recy.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MyWebActivity.start(getActivity(), list.get(position).getUrl(), "????????????");
            }
        });

        setview(iv_1, issel1);
        setview(iv_2, issel2);
        setview(iv_3, issel3);
    }


    public void initData() {
        String[] pics = new String[]{
                "https://p26.toutiaoimg.com/origin/pgc-image/386ffca79eac43738680dcbec14a6b2d?from=pc",
                "https://p3-tt.byteimg.com/img/tos-cn-i-qvj2lq49k0/a7587b02fb304687905a8109b3368557~tplv-lab-cspd:276:184.jpeg",
                "https://p9.toutiaoimg.com/origin/tos-cn-i-qvj2lq49k0/e5003b4808644690a17cb989a4561f70.png?from=pc",
                "https://p9.toutiaoimg.com/origin/tos-cn-i-tjoges91tu/Suwmzm3DtePELw?from=pc",

                "https://p3-tt.byteimg.com/img/tos-cn-i-qvj2lq49k0/ed5a9d59eeee4e7e8a5ca0fe60248bb7~tplv-lab-cspd:276:184.jpeg",
                "https://p9-tt.byteimg.com/img/tos-cn-i-qvj2lq49k0/ebd090f8e1f74c958ef275b04eeab90b~tplv-lab-cspd:276:184.jpeg",
                "https://p3-tt.byteimg.com/img/tos-cn-i-tjoges91tu/SoXeSrfAu2dB1D~tplv-lab-cspd:276:184.jpeg",
                "https://p3-search.byteimg.com/img/labis/8a3a4f3034c58a9e3871f4ba42977073~tplv-26tn0yjwph-cspd-v1:276:184.jpeg",

                "https://img.qiluyidian.net/2021113f0a620007a4050d0f6aed4a7d311f02e.jpg",
                "https://pic3.zhimg.com/v2-3c66feedf886838ca71f12ac4636a155_r.jpg",
                "https://p1.ssl.qhmsg.com/t0131456e05a868927e.jpg",
                "https://tse1-mm.cn.bing.net/th/id/R-C.60f935b0d2ad5f851812ec12b8d9c231?rik=zLCzBGRxvV3Kxw&riu=http%3a%2f%2foss.cloud.jstv.com%2fcdv-yuntonglian_QMTNRK_YUNSHI_P00080033_3C352AB88BA14FF1B30FE627FB197327.png%3fx-oss-process%3dstyle%2fpaipai&ehk=prFiBvw4xdNQBguJ1LW2LLa5H9q9rH7WjHtUP7Mw4dg%3d&risl=&pid=ImgRaw&r=0&sres=1&sresct=1",

                "https://tse1-mm.cn.bing.net/th/id/R-C.3d20f0525b422bc4b5f2a0b8c7aebd3d?rik=AZkPxo4nShf7jw&riu=http%3a%2f%2fwww.tongzhuo100.com%2fuploads%2f2021%2f02%2f20210226115035958.png&ehk=YjHziH13VRs4lLGYWrf1NO0i3G%2bytrObWy%2b9cNXCGx4%3d&risl=&pid=ImgRaw&r=0&sres=1&sresct=1",
                "https://tse1-mm.cn.bing.net/th/id/R-C.30d36a21ccf3aab9a12ff0f15b22e399?rik=2cWFpCeTBs%2f1Iw&riu=http%3a%2f%2finews.gtimg.com%2fnewsapp_bt%2f0%2f2305940778%2f641&ehk=hnXd6zS%2bmuohfpSSJU5pra8QF2G8vtNs2hbl7nhO4kI%3d&risl=&pid=ImgRaw&r=0",
                "https://tse1-mm.cn.bing.net/th/id/R-C.4810a4471ed861ae90c5367ddc484968?rik=CMWr0qi8uHUPMg&riu=http%3a%2f%2fclkx.jlju.edu.cn%2f__local%2f4%2f81%2f0A%2f4471ED861AE90C5367DDC484968_848EF2B6_89B2.jpg&ehk=TFZJwyZkg4%2bo4GKuew72aVdYCzX%2fJKrbF0GHnXH96D0%3d&risl=&pid=ImgRaw&r=0&sres=1&sresct=1",
                "https://tse1-mm.cn.bing.net/th/id/R-C.cb7bbad23ca73d01ba79692d02a56457?rik=qVkDxtb0HprOiQ&riu=http%3a%2f%2fpic.ntimg.cn%2ffile%2f20190119%2f20646450_085343468000_2.jpg&ehk=CJtb2AmqHhlilPnkTInO7wfiPLnaVoTF%2bCiDELcn6Is%3d&risl=&pid=ImgRaw&r=0",

                "https://cz.nankai.edu.cn/_upload/article/images/41/96/8a0c81404d8baec825e1b00699c9/1f6a4574-8fd8-4fd1-9dd8-30a4b2a45ea7.jpg",
                "https://tse1-mm.cn.bing.net/th/id/R-C.5fa5cbed29edf910c52479bb9adaf529?rik=LrQ5VrzaNBHYqg&riu=http%3a%2f%2fbwc.xauat.edu.cn%2f__local%2f5%2fFA%2f5C%2fBED29EDF910C52479BB9ADAF529_390485E7_734C.jpg&ehk=PHIVA9G9XULYvocdVm79vyur58j1AmKnUwP9vvbWeRQ%3d&risl=&pid=ImgRaw&r=0&sres=1&sresct=1",
                "https://tse4-mm.cn.bing.net/th/id/OIP-C.PeiM1q0ZzRLm7t6H0Qf3_QHaFG?pid=ImgDet&rs=1",
                "https://gwh.nit.edu.cn/__local/B/2F/23/AF9450F2730C83DBFB4A1F79233_06BA8D09_39828.png",
        };

        String[] titles = new String[]{
                "??????????????????????????????????????????",
                "???????????? ?????????????????????",
                "????????????????????????????????? ???????????????????????????",
                "??????????????????|??????????????????????????????",

                "?????????????????????????????????????????????????????????????????????????????????????????????????????????",
                "??????????????????????????????",
                "????????????????????????????????????????????????????????????????????????????????????????????????????????????",
                "?????????????????????????????????????????????????????????",

                "????????????????????????????????????????????????????????????????????????",
                "????????????????????????????????????",
                "????????????????????????????????????????????????????????????",
                "??????????????????????????????",

                "??????????????????????????????",
                "?????????????????????????????????????????????????????????",
                "??????????????????",
                "??????????????????????????????????????????",

                "?????????????????????????????????????????????????????????????????????",
                "??????????????????????????????",
                "??????????????????????????????????????????",
                "?????????????????????????????????",

        };
        String[] urls = new String[]{
                "https://www.toutiao.com/article/7027997081252053540/?channel=&source=search_tab",
                "https://www.toutiao.com/article/7084794049319780905/?channel=&source=search_tab",
                "https://www.toutiao.com/article/7062627988075971102/?channel=&source=search_tab",
                "https://www.toutiao.com/article/7083521313704247816/?channel=&source=search_tab",

                "https://www.toutiao.com/article/7034683156817035808/?channel=&source=search_tab",
                "https://www.toutiao.com/article/7065882904924144165/?channel=&source=search_tab",
                "https://www.toutiao.com/article/7029618098206409223/?channel=&source=search_tab",
                "https://www.thepaper.cn/newsDetail_forward_13684069",

                "https://www.toutiao.com/article/7034683156817035808/?channel=&source=search_tab",
                "https://www.toutiao.com/article/7065882904924144165/?channel=&source=search_tab",
                "https://www.toutiao.com/article/7029618098206409223/?channel=&source=search_tab",
                "https://www.thepaper.cn/newsDetail_forward_13684069",

                "https://www.toutiao.com/article/7034683156817035808/?channel=&source=search_tab",
                "https://www.toutiao.com/article/7065882904924144165/?channel=&source=search_tab",
                "https://www.toutiao.com/article/7029618098206409223/?channel=&source=search_tab",
                "https://www.thepaper.cn/newsDetail_forward_13684069",

                "https://www.toutiao.com/article/7034683156817035808/?channel=&source=search_tab",
                "https://www.toutiao.com/article/7065882904924144165/?channel=&source=search_tab",
                "https://www.toutiao.com/article/7029618098206409223/?channel=&source=search_tab",
                "https://www.thepaper.cn/newsDetail_forward_13684069",

        };
        int[] stype = new int[]{
                0,
                0,
                1,
                1,

                1,
                1,
                2,
                2,

                0,
                0,
                0,
                0,

                1,
                1,
                1,
                1,

                2,
                2,
                2,
                2,

        };

        for (int i = 0; i < titles.length; i++) {
            HomeBean bean = new HomeBean();
            bean.setId(i);
            bean.setPic(pics[i]);
            bean.stype = stype[i];
            bean.setName(titles[i]);
            bean.setUrl(urls[i]);
            bean.save();
            arrayList.add(bean);
        }
        int a = arrayList.size();
    }



    int duan_xin = 0,dian_hua = 0,app = 0;
    @OnClick({R.id.iv_1, R.id.iv_2, R.id.iv_3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_1:
                issel1 = (issel1 + 1) % 2;
                setview(iv_1, issel1);
                dian_hua++;

                break;
            case R.id.iv_2:
                issel2 = (issel2 + 1) % 2;
                setview(iv_2, issel2);
                duan_xin++;

                System.out.println("?????????");


                break;
            case R.id.iv_3:
                issel3 = (issel3 + 1) % 2;
                setview(iv_3, issel3);
                app++;


                break;
        }
    }


    //????????????????????????????????????????????????
    public static PhoneStateListener phoneStateListener = MainActivity.phoneStateListener;
    public static TelephonyManager telephonyManager = MainActivity.telephonyManager;
    @Override
    public void onResume() {
        super.onResume();
        //??????????????????????????????
        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String phoneNumber) {
                super.onCallStateChanged(state, phoneNumber);
                switch (state) {
                    //????????????????????????????????????????????????
                    case TelephonyManager.CALL_STATE_IDLE:
                        Log.i("tag","????????????");
                        break;
                    //???????????????????????????
                    case TelephonyManager.CALL_STATE_RINGING:
                        Log.i("tag","????????????");
                        break;
                    //???????????????????????????????????????
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        Log.i("tag","???????????????");
                        break;
                }
            }
        };
        //?????????????????????????????????
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
    }


    int issel1, issel2, issel3;
    private void setview(ImageView imageView, int issel) {
        imageView.setImageResource(issel == 0 ? R.mipmap.s0 : R.mipmap.s1);
    }


    @Override
    public void onEvent(EventMessage msg) {
        super.onEvent(msg);

        if (msg.getMessageType() == 111) {
            list.clear();
            List<HomeBean> temp = DataSupport.findAll(HomeBean.class);
            list.addAll(temp);
            adapter.notifyDataSetChanged();
        }
    }
}
