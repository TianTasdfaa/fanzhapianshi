package com.app.demo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.demo.R;
import com.app.demo.activitys.LoginActivity;
import com.app.demo.adapters.HomeListAdapter;
import com.app.demo.beans.HomeBean;
import com.app.shop.mylibrary.MyWebActivity;
import com.app.shop.mylibrary.base.BaseFragment;
import com.app.shop.mylibrary.beans.EventMessage;
import com.app.shop.mylibrary.utils.UserManager;
import com.chad.library.adapter.base.BaseQuickAdapter;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;


/**
 * A simple {@link Fragment} subclass.
 */
public class  FindFragment extends BaseFragment {

    @BindView(R.id.recy)
    RecyclerView recy;
    @BindView(R.id.tv_1)
    TextView tv_1;
    @BindView(R.id.tv_2)
    TextView tv_2;

    int selStype = 1;

    private List<HomeBean> list = new ArrayList<>();
    private HomeListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find, container, false);
        ButterKnife.bind(this, view);
        registerEventBus();

        selStype = 1;
        setSel();
        initList();
        return view;
    }

    @OnClick({R.id.tv_1, R.id.tv_2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_1:
                selStype = 1;
                setSel();
                EventBus.getDefault().post(new EventMessage(EventMessage.Refresh));
                break;
            case R.id.tv_2:
                selStype = 2;
                setSel();
                EventBus.getDefault().post(new EventMessage(EventMessage.Refresh));
                break;
        }
    }


    private void setSel() {
        if (selStype == 1) {
            tv_1.setTextColor(getResources().getColor(R.color.whilt));
            tv_2.setTextColor(getResources().getColor(R.color.color_999999));
        } else {
            tv_1.setTextColor(getResources().getColor(R.color.color_999999));
            tv_2.setTextColor(getResources().getColor(R.color.whilt));
        }
    }

    //列表
    private void initList() {
        //判断是否已登录
        if (!UserManager.isLogin(getActivity())) {
            skipActivity(getActivity(), LoginActivity.class);
            getActivity().finish();
        }

        recy.setFocusable(false);
        recy.setFocusableInTouchMode(false);
//        list.clear();
//        list = DataSupport.findAll(HomeBean.class);

        adapter = new HomeListAdapter(R.layout.item_list, list);
        recy.setLayoutManager(new LinearLayoutManager(getActivity()));
        recy.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MyWebActivity.start(getActivity(), list.get(position).getUrl(), "详情信息");
            }
        });

        EventBus.getDefault().post(new EventMessage(EventMessage.Refresh));
    }


    @Override
    public void onEvent(EventMessage msg) {
        super.onEvent(msg);

        if (msg.getMessageType() == EventMessage.Refresh) {
            list.clear();
            List<HomeBean> temp = DataSupport.findAll(HomeBean.class);

            for (int i = 0; i < temp.size(); i++) {
                if (temp.get(i).stype == selStype) {
                    list.add(temp.get(i));
                }
            }

//            list.addAll(temp);
            adapter.notifyDataSetChanged();
        }
    }
}