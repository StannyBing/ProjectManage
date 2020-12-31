package com.zx.projectmanage.module.projectapplication.construction.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gt.giscollect.base.NormalList
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import com.zx.projectmanage.R
import com.zx.projectmanage.app.toJson
import com.zx.projectmanage.base.BaseActivity
import com.zx.projectmanage.module.projectapplication.construction.bean.ReportSubListBean
import com.zx.projectmanage.module.projectapplication.construction.func.adapter.ReportChildListAdapter
import com.zx.projectmanage.module.projectapplication.construction.func.tool.setHintKtx
import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ConstructionReportChildContract
import com.zx.projectmanage.module.projectapplication.construction.mvp.model.ConstructionReportChildModel
import com.zx.projectmanage.module.projectapplication.construction.mvp.presenter.ConstructionReportChildPresenter
import com.zx.zxutils.util.ZXToastUtil
import kotlinx.android.synthetic.main.activity_construction_report.*
import kotlinx.android.synthetic.main.activity_construction_report_child.head
import kotlinx.android.synthetic.main.activity_construction_report_child.searchText
import kotlinx.android.synthetic.main.activity_construction_report_child.swipeRecyler
import kotlinx.android.synthetic.main.dialog_filter_project.view.*


/**
 * Create By admin On 2017/7/11
 * 功能： 项目列表
 */
class ConstructionReportChildActivity : BaseActivity<ConstructionReportChildPresenter, ConstructionReportChildModel>(), ConstructionReportChildContract.View {
    private var list: MutableList<ReportSubListBean> = arrayListOf<ReportSubListBean>()
    private val reportListAdapter = ReportChildListAdapter(list)
    private var mVals = listOf<String>("1", "2", "3")
    private var mVals1 = listOf<String>("已完成", "未通过", "进行中")
    private var pageNo = 1
    private var pageSize = 10
    var isRefresh = true
    lateinit var projectId: String

    companion object {
        /**
         * 启动器
         */
        fun startAction(activity: Activity, isFinish: Boolean) {
            val intent = Intent(activity, ConstructionReportChildActivity::class.java)
            activity.startActivity(intent)
            if (isFinish) activity.finish()
        }

        /**
         * 启动器
         */
        fun startAction(activity: Activity, isFinish: Boolean, projectId: String, projectName: String) {
            val intent = Intent(activity, ConstructionReportChildActivity::class.java)
            intent.putExtra("projectName", projectName)
            intent.putExtra("projectId", projectId)
            activity.startActivity(intent)
            if (isFinish) activity.finish()
        }
    }

    /**
     * layout配置
     */
    override fun getLayoutId(): Int {
        return R.layout.activity_construction_report_child
    }

    /**
     * 初始化
     */
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        projectId = intent.getStringExtra("projectId").toString()
        head.setCenterString(intent.getStringExtra("projectName").toString())
        searchText.setHintKtx(13, "请输入项目名称")
        swipeRecyler.apply {
            layoutManager = LinearLayoutManager(mContext)
            adapter = reportListAdapter
//            addItemDecoration(SimpleDecoration(mContext))
        }
        refresh()
    }

    /**
     * 刷新
     */
    fun refresh(searchText: String? = null) {
        pageNo = 1
        mPresenter.getPageSubProject(
            hashMapOf(
                "projectId" to projectId
            )
        )
    }


    private fun loadMore() {
        pageNo++
        mPresenter.getPageSubProject(
            hashMapOf(
                "projectId" to projectId,
                "pageNo" to pageNo.toString()
            )
        )
    }


    //设置数据到适配器
    private fun setData(data: List<ReportSubListBean>) {
        pageNo++
        val size = data?.size ?: 0
        if (isRefresh) {
            reportListAdapter.setNewData(data as List<ReportSubListBean?>?)
        } else {
            if (size > 0) {
                reportListAdapter.addData(data!!)
            }
        }
        if (size < pageSize) {
            reportListAdapter.loadMoreEnd(isRefresh)
        } else {
            reportListAdapter.loadMoreComplete()
        }
    }

    /**
     * View事件设置
     */
    override fun onViewListener() {
        //头部点击事件
        head.setRightImageViewClickListener {
            val inflate = View.inflate(mContext, R.layout.dialog_filter_project, null)
            setPeriodFlow(inflate, mVals, 1)
            setPeriodFlow(inflate, mVals1, 2)

            val bottomSheetDialog = BottomSheetDialog(this, R.style.flow_dialog)
            bottomSheetDialog.setCanceledOnTouchOutside(false)
            inflate.bottomSheetCancel.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            inflate.bottomSheetOK.setOnClickListener {
                //发起筛选请求
//                refresh(searchText.toString())
                bottomSheetDialog.dismiss()
            }
            inflate?.let { it1 -> bottomSheetDialog.setContentView(it1) }
            //设置bottomsheet behavior
            val mDialogBehavior = BottomSheetBehavior.from(inflate.parent as View)
            mDialogBehavior.peekHeight = getPeekHeight()
            bottomSheetDialog.show()
        }.setLeftImageViewClickListener {
            finish()
        }

        //刷新列表
        refresh.setOnRefreshListener {
            refresh()
            refresh.isRefreshing = false
        }
        reportListAdapter.setOnLoadMoreListener({ loadMore() }, swipeRecyler)

    }

    /**
     * 设置Flow选项卡列表
     */
    private fun setPeriodFlow(inflate: View, mVals: List<String>, flag: Int) {

        val tagAdapter = object : TagAdapter<Any>(mVals) {
            override fun getView(parent: FlowLayout?, position: Int, t: Any?): View {

                val view = View.inflate(mContext, R.layout.flowlayout_textview_selected, null) as TextView
                //设置展示的值
                view.text = mVals[position]
                //动态设置标签宽度
                view.width = 170
                return view
            }
        }
        //预先设置选中
        tagAdapter.setSelectedList(0, 1)
        if (flag == 1) {
            inflate.periodFlow.adapter = tagAdapter
            inflate.periodFlow.setMaxSelectCount(9)
            //为FlowLayout的标签设置选中监听事件
            inflate.periodFlow.setOnSelectListener(object : TagFlowLayout.OnSelectListener {
                override fun onSelected(selectPosSet: Set<Int>) {
                    //选中的index
                    ZXToastUtil.showToast(selectPosSet.toString())
                }
            })
        } else {
            inflate.statusFlow.adapter = tagAdapter
            inflate.statusFlow.setMaxSelectCount(9)
            //为FlowLayout的标签设置选中监听事件
            inflate.statusFlow.setOnSelectListener(object : TagFlowLayout.OnSelectListener {
                override fun onSelected(selectPosSet: Set<Int>) {
                    //选中的index
                    ZXToastUtil.showToast(selectPosSet.toString())
                }
            })
        }

    }

    /**
     * 弹窗高度，默认为屏幕高度的四分之三
     * 子类可重写该方法返回peekHeight
     *
     * @return height
     */
    protected fun getPeekHeight(): Int {
        val peekHeight = resources.displayMetrics.heightPixels
        //设置弹窗高度为屏幕高度的3/4
        return peekHeight - peekHeight / 3
    }

    override fun getDataSubResult(baseRespose: NormalList<ReportSubListBean>?) {

        if (baseRespose?.records != null) {
            setData(baseRespose.records)
        }
    }


}
