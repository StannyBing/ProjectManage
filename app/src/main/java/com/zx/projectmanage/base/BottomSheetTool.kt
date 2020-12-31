package com.zx.projectmanage.base

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.zx.projectmanage.R
import com.zx.zxutils.views.BottomSheet.ZXBottomSheet

object BottomSheetTool {

    private var bottomSheet: ZXBottomSheet? = null

    fun showBottomSheet(context: Context, title: String, customView: View, submitCall: (ZXBottomSheet) -> Unit, cancelCall: (ZXBottomSheet) -> Unit = {}): ZXBottomSheet {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_normal_bottomsheet, null)
        view.findViewById<TextView>(R.id.tv_sheet_name).text = title
        view.findViewById<TextView>(R.id.tv_sheet_cancel).setOnClickListener {
            bottomSheet?.dismiss()
            cancelCall(bottomSheet!!)
        }
        view.findViewById<TextView>(R.id.tv_sheet_submit).setOnClickListener { submitCall(bottomSheet!!) }
        view.findViewById<FrameLayout>(R.id.fm_sheet_custom).addView(customView)
        bottomSheet = ZXBottomSheet.initCustom(context, view).build().show()
        return bottomSheet!!
    }

}