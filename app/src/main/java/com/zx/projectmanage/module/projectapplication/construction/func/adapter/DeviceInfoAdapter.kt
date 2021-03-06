package com.zx.projectmanage.module.projectapplication.construction.func.adapter

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zx.projectmanage.R
import com.zx.projectmanage.module.other.ui.CameraPreviewActivity
import com.zx.projectmanage.module.projectapplication.construction.bean.DeviceInfoBean
import com.zx.projectmanage.module.projectapplication.construction.bean.DataStepInfoBean
import com.zx.projectmanage.module.projectapplication.construction.func.listener.DataStepListener
import com.zx.projectmanage.module.projectapplication.construction.func.tool.InScrollGridLayoutManager
import com.zx.zxutils.other.QuickAdapter.ZXBaseHolder
import com.zx.zxutils.other.QuickAdapter.ZXMultiItemQuickAdapter

class DeviceInfoAdapter(dataList: List<DeviceInfoBean>) : ZXMultiItemQuickAdapter<DeviceInfoBean, ZXBaseHolder>(dataList) {

    var isEditable: Boolean = true
    private var listener: DataStepListener? = null

    init {
        addItemType(DeviceInfoBean.Title_Type, R.layout.item_device_info_title)
        addItemType(DeviceInfoBean.Edit_Type, R.layout.item_device_info_edit)
        addItemType(DeviceInfoBean.Text_Type, R.layout.item_device_info_text)
        addItemType(DeviceInfoBean.Select_Type, R.layout.item_device_info_select)
        addItemType(DeviceInfoBean.Location_Type, R.layout.item_device_info_location)
        addItemType(DeviceInfoBean.Step_Type, R.layout.item_device_info_step)
    }

    override fun convert(helper: ZXBaseHolder, item: DeviceInfoBean) {
        when (item.type) {
            DeviceInfoBean.Title_Type -> {
                helper.setText(R.id.tv_data_title_name, item.name)
            }
            DeviceInfoBean.Edit_Type -> {
                helper.setText(R.id.tv_data_edit_name, item.name)
                helper.setText(R.id.et_data_edit_value, item.stringValue)
                helper.getView<EditText>(R.id.et_data_edit_value).hint = "请输入${item.name}"
                helper.getView<EditText>(R.id.et_data_edit_value).addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        listener?.onEditChange(helper.adapterPosition, helper.getView<EditText>(R.id.et_data_edit_value).text.toString())
                    }
                })
                helper.setEnabled(R.id.et_data_edit_value, isEditable)
            }
            DeviceInfoBean.Text_Type -> {
                helper.setText(R.id.tv_data_text_name, item.name)
                helper.setText(R.id.tv_data_text_value, item.stringValue)
            }
            DeviceInfoBean.Select_Type -> {
                helper.setText(R.id.tv_data_select_name, item.name)
                helper.getView<TextView>(R.id.tv_data_select_value).hint = "请输入${item.name}"
                helper.setText(R.id.tv_data_select_value, item.stringValue)
                if (isEditable) helper.addOnClickListener(R.id.tv_data_select_value)
            }
            DeviceInfoBean.Location_Type -> {
                helper.setText(R.id.tv_data_location_name, item.name)
                helper.setText(R.id.tv_data_location_value, if (item.stringValue.isEmpty()) "未获取到位置" else item.stringValue)
                helper.addOnClickListener(R.id.tv_data_location_get)
                helper.setGone(R.id.tv_data_location_get, isEditable)
                if (item.stringValue.isEmpty() && isEditable) helper.getView<TextView>(R.id.tv_data_location_get).performClick()
            }
            DeviceInfoBean.Step_Type -> {
                helper.setText(R.id.tv_data_step_name, item.name)
                helper.setText(R.id.tv_data_step_info, item.stringValue)
                helper.addOnClickListener(R.id.iv_data_step_camera)
                helper.setGone(R.id.iv_data_step_camera, isEditable)
                val rvStep = helper.getView<RecyclerView>(R.id.rv_data_step_file)
                rvStep.apply {
                    layoutManager = InScrollGridLayoutManager(mContext, 2)
                    adapter = DataStepFileAdapter(item.stepInfos).apply {
                        this.editable = isEditable
                        setOnItemChildClickListener { adapter, view, position ->
                            when (view.id) {
                                R.id.iv_data_step_delete -> {
                                    listener?.onFileDelete(helper.adapterPosition, position)
                                    notifyDataSetChanged()
                                }
                            }
                        }
                        setOnItemClickListener { adapter, view, position ->
                            CameraPreviewActivity.startAction(
                                mContext as Activity, false, "", item.stepInfos[position].path, when (item.stepInfos[position].type) {
                                    DataStepInfoBean.Type.PICTURE -> 0
                                    DataStepInfoBean.Type.VIDEO -> 1
                                }
                            )
                        }
                    }
                }
            }
        }
    }


    fun setDataStepListener(listener: DataStepListener) {
        this.listener = listener
    }
}