package com.lt.zxmap.view;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lt.zxmap.R;
import com.zx.zxutils.other.QuickAdapter.ZXBaseHolder;
import com.zx.zxutils.other.QuickAdapter.ZXQuickAdapter;

import java.util.List;

public class MeaToolAdapter extends ZXQuickAdapter<MeaToolBean, ZXBaseHolder> {
    public MeaToolAdapter(@Nullable List<MeaToolBean> data) {
        super(R.layout.item_mea_tool, data);
    }

    @Override
    protected void convert(@NonNull ZXBaseHolder helper, MeaToolBean item) {
        helper.setText(R.id.mea_func_name, item.getName());
        helper.setImageResource(R.id.mea_func_icon, item.getIcon());
        ((TextView) helper.getView(R.id.mea_func_name)).setTextSize(item.getName().length() > 3 ? 6f : 8f);
    }
}
