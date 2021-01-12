package com.zx.projectmanage.module.other.func.tool.image;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.zx.projectmanage.R;
import com.zx.projectmanage.module.other.func.tool.image.core.IMGText;
import com.zx.projectmanage.module.other.func.tool.image.view.IMGColorGroup;

public class IMGTextEditDialogTool {

    public static Dialog showDialog(Context context, IMGText mDefaultText, Callback mCallback) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_text_dialog, null, false);
        IMGColorGroup mColorGroup = view.findViewById(R.id.cg_colors);
        EditText mEditText = view.findViewById(R.id.et_text);
        mColorGroup.setOnCheckedChangeListener((group, checkedId) -> mEditText.setTextColor(mColorGroup.getCheckColor()));

        if (mDefaultText != null) {
            mEditText.setText(mDefaultText.getText());
            mEditText.setTextColor(mDefaultText.getColor());
            if (!mDefaultText.isEmpty()) {
                mEditText.setSelection(mEditText.length());
            }
            mDefaultText = null;
        } else mEditText.setText("");
        mColorGroup.setCheckColor(mEditText.getCurrentTextColor());

        Dialog mDialog = new AlertDialog.Builder(context)
                .setView(view)
//                .setTitle("文字")
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("确定", (dialog, which) -> {
                    String text = mEditText.getText().toString();
                    if (!TextUtils.isEmpty(text) && mCallback != null) {
                        mCallback.onText(new IMGText(text, mEditText.getCurrentTextColor()));
                    }
                    dialog.dismiss();
                })
                .create();
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.show();
        return mDialog;
    }

    public interface Callback {

        void onText(IMGText text);
    }

}
