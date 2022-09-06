package com.app.demo.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.app.demo.R;
import com.app.demo.beans.HomeBean;
import com.app.shop.mylibrary.utils.GlideUtils;
import com.app.shop.mylibrary.utils.StringUtil;
import com.app.shop.mylibrary.utils.ToastUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class  HomeListAdapter extends BaseQuickAdapter<HomeBean, HomeListAdapter.ViewHolder> {

    public HomeListAdapter(int layoutResId, @Nullable List<HomeBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(ViewHolder helper, HomeBean item) {
        GlideUtils.getInstance().loadImage(mContext, item.getPic(), helper.imgv);
        helper.title.setText(StringUtil.getContent(item.getName()));
    }

    public class ViewHolder extends BaseViewHolder {

        ImageView imgv ;
        TextView title;

        public ViewHolder(View view) {
            super(view);
            imgv = view.findViewById(R.id.imgv_list);
            title = view.findViewById(R.id.tv_title_item);

        }
    }
}