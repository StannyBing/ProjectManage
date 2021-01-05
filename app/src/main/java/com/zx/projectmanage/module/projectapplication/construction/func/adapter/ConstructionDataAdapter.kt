package com.zx.projectmanage.module.projectapplication.construction.func.adapter

import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zx.projectmanage.R
import com.zx.projectmanage.module.projectapplication.construction.bean.ConstructionDataBean
import com.zx.projectmanage.module.projectapplication.construction.func.listener.DataStepListener
import com.zx.projectmanage.module.projectapplication.construction.func.tool.InScrollGridLayoutManager
import com.zx.zxutils.other.QuickAdapter.ZXBaseHolder
import com.zx.zxutils.other.QuickAdapter.ZXMultiItemQuickAdapter

class ConstructionDataAdapter(dataList: List<ConstructionDataBean>) : ZXMultiItemQuickAdapter<ConstructionDataBean, ZXBaseHolder>(dataList) {

    private var listener: DataStepListener? = null

    init {
        addItemType(ConstructionDataBean.Title_Type, R.layout.item_construction_data_title)
        addItemType(ConstructionDataBean.Edit_Type, R.layout.item_construction_data_edit)
        addItemType(ConstructionDataBean.Text_Type, R.layout.item_construction_data_text)
        addItemType(ConstructionDataBean.Select_Type, R.layout.item_construction_data_select)
        addItemType(ConstructionDataBean.Location_Type, R.layout.item_construction_data_location)
        addItemType(ConstructionDataBean.Step_Type, R.layout.item_construction_data_step)
    }

    override fun convert(helper: ZXBaseHolder, item: ConstructionDataBean) {
        when (item.type) {
            ConstructionDataBean.Title_Type -> {
                helper.setText(R.id.tv_data_title_name, item.name)
            }
            ConstructionDataBean.Edit_Type -> {
                helper.setText(R.id.tv_data_edit_name, item.name)
                helper.setText(R.id.et_data_edit_value, item.stringValue)
                helper.getView<EditText>(R.id.et_data_edit_value).hint = "请输入${item.name}"
            }
            ConstructionDataBean.Text_Type -> {
                helper.setText(R.id.tv_data_text_name, item.name)
                helper.setText(R.id.tv_data_text_value, item.stringValue)
            }
            ConstructionDataBean.Select_Type -> {
                helper.setText(R.id.tv_data_select_name, item.name)
                helper.setText(R.id.tv_data_select_value, if (item.stringValue.isEmpty()) "请选择${item.name}" else item.stringValue)
                helper.addOnClickListener(R.id.tv_data_select_value)
            }
            ConstructionDataBean.Location_Type -> {
                helper.setText(R.id.tv_data_location_name, item.name)
                helper.setText(R.id.tv_data_location_value, if (item.stringValue.isEmpty()) "未获取到位置" else item.stringValue)
                helper.addOnClickListener(R.id.tv_data_location_get)
                if (item.stringValue.isEmpty()) helper.getView<TextView>(R.id.tv_data_location_get).performClick()
            }
            ConstructionDataBean.Step_Type -> {
                helper.setText(R.id.tv_data_step_name, item.name)
                helper.setText(R.id.tv_data_step_info, item.stringValue)
                helper.addOnClickListener(R.id.iv_data_step_camera)
                val rvStep = helper.getView<RecyclerView>(R.id.rv_data_step_file)
                rvStep.apply {
                    layoutManager = InScrollGridLayoutManager(mContext, 2)
                    adapter = DataStepFileAdapter(item.stepInfos).apply {
                        setOnItemChildClickListener { adapter, view, position ->
                            when (view.id) {
                                R.id.iv_data_step_delete -> {
                                    listener?.onFileDelete(helper.adapterPosition, position)
                                    notifyDataSetChanged()
                                }
                            }
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