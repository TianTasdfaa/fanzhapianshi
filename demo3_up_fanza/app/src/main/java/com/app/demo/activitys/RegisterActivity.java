package com.app.demo.activitys;

import android.content.ContentValues;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.app.demo.R;
import com.app.shop.mylibrary.base.BaseActivity;
import com.app.shop.mylibrary.beans.EventMessage;
import com.app.shop.mylibrary.beans.UserBean;
import com.app.shop.mylibrary.utils.SharedPreferencesUtil;
import com.app.shop.mylibrary.utils.StringUtil;
import com.app.shop.mylibrary.utils.ToastUtil;
import com.app.shop.mylibrary.utils.UserManager;

import org.litepal.crud.DataSupport;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class  RegisterActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.edtMobile)
    EditText edtMobile;
    @BindView(R.id.edtpwd)
    EditText edtpwd;
    @BindView(R.id.edtpwdrepeate)
    EditText edtpwdrepeate;

    @BindView(R.id.et_nickname)
    EditText et_nickname;
    @BindView(R.id.et_age)
    EditText et_age;

    @BindView(R.id.layout_regi)
    LinearLayout layout_regi;

    String userid;
    UserBean userBean;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initData();
    }

    int type;//0:从登录来，1：从个人中心来

    private void initData() {
        type = getIntent().getIntExtra("type", 0);
        if (type == 0) {
            tvTitle.setText("注册");
            layout_regi.setVisibility(View.VISIBLE);
        } else {
            tvTitle.setText("个人信息");
            layout_regi.setVisibility(View.GONE);

            userid = UserManager.getUserId(this);
            userBean = UserManager.getUser(userid);

            et_nickname.setText(userBean.name);
            et_age.setText(userBean.age);
            edtMobile.setText(userBean.getUser_id());
            edtpwd.setText(userBean.password);
            edtpwdrepeate.setText(userBean.password);
        }
    }

    @OnClick({R.id.imgv_return, R.id.tv_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgv_return:
                onBackPressed();
                break;

            case R.id.tv_register:
                String name = et_nickname.getText().toString();
                String age = et_age.getText().toString();
                if (type == 0) {
                    String num = edtMobile.getText().toString();
                    String ps = edtpwd.getText().toString();
                    String ps2 = edtpwdrepeate.getText().toString();

                    if (StringUtil.isEmpty(name) || StringUtil.isEmpty(ps) || StringUtil.isEmpty(ps2)
                            || StringUtil.isEmpty(num) || StringUtil.isEmpty(age)  ) {
                        ToastUtil.showToast(this, "请输入完整信息");
                        return;
                    }

                    if (!ps.equals(ps2)) {
                        ToastUtil.showToast(this, "两次密码不一致");
                        return;
                    }

                    UserBean userBean = new UserBean();
                    userBean.setUser_id(num);
                    userBean.setName(name);
                    userBean.setPassword(ps);
                    userBean.setPic(R.mipmap.logo);
                    userBean.age = age;
                    userBean.save();
                } else {
                    if (StringUtil.isEmpty(name)
                            || StringUtil.isEmpty(age)  ) {
                        ToastUtil.showToast(this, "请输入完整信息");
                        return;
                    }

                    ContentValues values = new ContentValues();

                    values.put("age", age);
                    values.put("name", name);
                    DataSupport.updateAll(UserBean.class, values, "user_id=?", userid);

                    SharedPreferencesUtil.saveData(this, "user", "age", age);
                    SharedPreferencesUtil.saveData(this, "user", "name", name);
                    EventBus.getDefault().post(new EventMessage(EventMessage.Refresh));
                }
                ToastUtil.showToast(this, "操作成功");
                onBackPressed();
                break;
        }
    }
}
