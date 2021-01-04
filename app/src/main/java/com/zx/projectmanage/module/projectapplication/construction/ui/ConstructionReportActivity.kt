package com.zx.projectmanage.module.projectapplication.construction.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import com.zx.projectmanage.R
import com.zx.projectmanage.base.BaseActivity

import com.zx.projectmanage.module.projectapplication.approve.bean.ProjectPeriodBean
import com.zx.projectmanage.module.projectapplication.approve.bean.ProjectStatusBean
import com.zx.projectmanage.module.projectapplication.approve.bean.ReportListBean
import com.zx.projectmanage.module.projectapplication.construction.func.adapter.ReportListAdapter
import com.zx.projectmanage.module.projectapplication.construction.func.tool.hitSoft
import com.zx.projectmanage.module.projectapplication.construction.func.tool.setHintKtx
import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ConstructionReportContract
import com.zx.projectmanage.module.projectapplication.construction.mvp.model.ConstructionReportModel
import com.zx.projectmanage.module.projectapplication.construction.mvp.presenter.ConstructionReportPresenter
import kotlinx.android.synthetic.main.activity_construction_report.*
import kotlinx.android.synthetic.main.dialog_filter_project.view.*


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class ConstructionReportActivity : BaseActivity<ConstructionReportPresenter, ConstructionReportModel>(), ConstructionReportContract.View {
    private var list: MutableList<ReportListBean.RecordsBean> = arrayListOf<ReportListBean.RecordsBean>()
    private val reportListAdapter = ReportListAdapter(list)

    //期次list
    private var mVals: MutableList<ProjectPeriodBean> = ArrayList()

    //状态list
    private var mVals1: MutableList<ProjectStatusBean> = ArrayList()
    var arrayPeriod = StringBuffer()
    var arraystatus = StringBuffer()

    private var pageNo = 1
    private var pageSize = 10
    var isRefresh = true

    companion object {
        /**
         * 启动器
         */
        fun startAction(activity: Activity, isFinish: Boolean) {
            val intent = Intent(activity, ConstructionReportActivity::class.java)
            activity.startActivity(intent)
            if (isFinish) activity.finish()
        }
    }

    /**
     * layout配置
     */
    override fun getLayoutId(): Int {
        return R.layout.activity_construction_report
    }

    /**
     * 初始化
     */
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        searchText.setHintKtx(13, "请输入项目名称")
        //设置adapter

        swipeRecyler.apply {
            layoutManager = LinearLayoutManager(mContext)
            adapter = reportListAdapter
//            addItemDecoration(SimpleDecoration(mContext))
            reportListAdapter.setEmptyView(R.layout.item_empty, swipeRecyler)
        }
        mPresenter.getPageProject()
        mPresenter.getProjectStatus()
        mPresenter.getProjectPeriod()

    }

    /**
     * 刷新
     */
    fun refresh() {
        isRefresh = true
        pageNo = 1
        mPresenter.getPageProject()
    }

    //设置数据到适配器
    private fun setData(data: MutableList<ReportListBean.RecordsBean>?) {
        pageNo++
        val size = data?.size ?: 0
        if (isRefresh) {
            reportListAdapter.setNewData(data)
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
            setPeriodFlow(inflate, 1)
            setPeriodFlow(inflate, 2)

            val bottomSheetDialog = BottomSheetDialog(this, R.style.flow_dialog)
            bottomSheetDialog.setCanceledOnTouchOutside(false)
            inflate.bottomSheetCancel.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            inflate.bottomSheetOK.setOnClickListener {
                //发起筛选请求
                if (arraystatus.isNotEmpty() && arrayPeriod.isNotEmpty()) {
                    mPresenter.getPageProject(pageNo = 1, projectStatus = arraystatus.toString(), buildPeriod = arrayPeriod.toString())
                } else if (arraystatus.isNotEmpty() && arrayPeriod.isEmpty()) {
                    mPresenter.getPageProject(pageNo = 1, projectStatus = arraystatus.toString())
                } else if (arraystatus.isEmpty() && arrayPeriod.isNotEmpty()) {
                    mPresenter.getPageProject(pageNo = 1, buildPeriod = arrayPeriod.toString())
                } else {
                    mPresenter.getPageProject(pageNo = 1)
                }
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

        //软键盘搜索按钮监听
        searchText.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                //点击搜索的时候隐藏软键盘
                searchText.hitSoft()
                // 网络请求数据
                mPresenter.getPageProject(pageSize = 50, keyword = v.text.toString().trim())
                return@OnEditorActionListener true
            }
            false
        })
        //搜索图标点击监听
        searchImg.setOnClickListener {
            // 网络请求数据
            mPresenter.getPageProject(pageSize = 50, keyword = searchText.text.toString().trim())
        }
        //刷新列表
        refresh.setOnRefreshListener {
            refresh()
            refresh.isRefreshing = false
        }
        reportListAdapter.setOnLoadMoreListener({
            isRefresh = false
            mPresenter.getPageProject(pageNo = pageNo)
        }, swipeRecyler)
    }

    /**
     * 设置Flow选项卡列表
     */
    private fun setPeriodFlow(inflate: View, flag: Int) = if (flag == 1) {
        val tagAdapter = object : TagAdapter<Any>(mVals as List<Any>?) {
            override fun getView(parent: com.zhy.view.flowlayout.FlowLayout?, position: Int, t: Any?): View {

                val view = View.inflate(mContext, R.layout.flowlayout_textview_selected, null) as TextView
                //设置展示的值
                view.text = mVals[position].label
                //动态设置标签宽度
                view.width = 170
                return view
            }
        }
        if (arrayPeriod.toString().isNotEmpty()) {
            arrayPeriod.delete(0, arrayPeriod.toString().length)
        }
        //预先设置选中
        inflate.periodFlow.adapter = tagAdapter
        inflate.periodFlow.setMaxSelectCount(9)
        //为FlowLayout的标签设置选中监听事件
        inflate.periodFlow.setOnSelectListener(object : TagFlowLayout.OnSelectListener {
            override fun onSelected(selectPosSet: Set<Int>) {

                for (i in selectPosSet) {
                    if (i == selectPosSet.size - 1) {
                        arrayPeriod.append(mVals[i].dictId)
                    } else {
                        arrayPeriod.append(mVals[i].dictId)
                        arrayPeriod.append(",")
                    }
                    arrayPeriod.toString()
                }
            }
        })
    } else {
        val tagAdapter = object : TagAdapter<Any>(mVals1 as List<Any>?) {
            override fun getView(parent: com.zhy.view.flowlayout.FlowLayout?, position: Int, t: Any?): View {

                val view = View.inflate(mContext, R.layout.flowlayout_textview_selected, null) as TextView
                //设置展示的值
                view.text = mVals1[position].statusName
                //动态设置标签宽度
                view.width = 170
                return view
            }
        }
        inflate.statusFlow.adapter = tagAdapter
        inflate.statusFlow.setMaxSelectCount(9)
        if (arraystatus.toString().isNotEmpty()) {
            arraystatus.delete(0, arraystatus.toString().length )
        }

        //为FlowLayout的标签设置选中监听事件
        inflate.statusFlow.setOnSelectListener(object : TagFlowLayout.OnSelectListener {
            override fun onSelected(selectPosSet: Set<Int>) {

                for (i in selectPosSet) {
                    if (i == selectPosSet.size - 1) {
                        arraystatus.append(mVals1[i].statusId)
                    } else {
                        arraystatus.append(mVals1[i].statusId)
                        arraystatus.append(",")
                    }

                }
                arraystatus.toString()

            }
        })
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

    override fun getDataResult(baseRespose: ReportListBean?) {
        setData(baseRespose?.getRecords() as MutableList<ReportListBean.RecordsBean>?)

    }

    override fun getProjectStatusResult(baseRespose: Any?) {
        val map: MutableMap<String, String> = Gson().fromJson(baseRespose.toString(), MutableMap::class.java) as MutableMap<String, String>
        for (mutableEntry in map) {
            mVals1.add(ProjectStatusBean(mutableEntry.key, mutableEntry.value))
        }
    }

    override fun getProjectPeriodResult(data: MutableList<ProjectPeriodBean>?) {
        if (data != null) {
            mVals = data
        }
    }


}
