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
import com.zx.projectmanage.base.NormalList
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import com.zx.projectmanage.R
import com.zx.projectmanage.base.BaseActivity
import com.zx.projectmanage.base.BottomSheetTool
import com.zx.projectmanage.module.projectapplication.construction.bean.ProjectPeriodBean
import com.zx.projectmanage.module.projectapplication.construction.bean.ProjectStatusBean
import com.zx.projectmanage.module.projectapplication.construction.bean.ReportSubListBean
import com.zx.projectmanage.module.projectapplication.construction.func.adapter.ReportChildListAdapter
import com.zx.projectmanage.module.projectapplication.construction.func.tool.hitSoft
import com.zx.projectmanage.module.projectapplication.construction.func.tool.setHintKtx
import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ConstructionReportChildContract
import com.zx.projectmanage.module.projectapplication.construction.mvp.model.ConstructionReportChildModel
import com.zx.projectmanage.module.projectapplication.construction.mvp.presenter.ConstructionReportChildPresenter
import com.zx.zxutils.util.ZXToastUtil

import kotlinx.android.synthetic.main.activity_construction_report.refresh
import kotlinx.android.synthetic.main.activity_construction_report_child.*
import kotlinx.android.synthetic.main.activity_construction_report_child.head
import kotlinx.android.synthetic.main.activity_construction_report_child.searchText
import kotlinx.android.synthetic.main.activity_construction_report_child.swipeRecyler
import kotlinx.android.synthetic.main.dialog_filter_project.view.*
import java.io.Serializable


/**
 * Create By admin On 2017/7/11
 * 功能： 项目列表
 */
class ConstructionReportChildActivity : BaseActivity<ConstructionReportChildPresenter, ConstructionReportChildModel>(), ConstructionReportChildContract.View {
    private var list: MutableList<ReportSubListBean> = arrayListOf<ReportSubListBean>()
    private lateinit var reportListAdapter: ReportChildListAdapter

    //期次list
    private var mVals: MutableList<ProjectPeriodBean> = ArrayList()

    //状态list
    private var mVals1: MutableList<ProjectStatusBean> = ArrayList()
    private var pageNo = 1
    private var pageSize = 10
    var isRefresh = true
    lateinit var projectId: String
    var arrayPeriod = StringBuffer()
    var arraystatus = StringBuffer()
    var type: Int = 0

    companion object {

        /**
         * 启动器
         */
        fun startAction(
            activity: Activity, isFinish: Boolean, projectId: String, projectName: String,
            assessmentId: String, type: Int,
            statusBean: MutableList<ProjectStatusBean>,
            periodBean: MutableList<ProjectPeriodBean>
        ) {
            val intent = Intent(activity, ConstructionReportChildActivity::class.java)
            intent.putExtra("projectName", projectName)
            intent.putExtra("projectId", projectId)
            intent.putExtra("assessmentId", assessmentId)
            intent.putExtra("type", type)
            intent.putExtra("statusBean", statusBean as Serializable)
            intent.putExtra("periodBean", periodBean as Serializable)
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
        type = intent.getIntExtra("type", 0)
        mVals = intent.getSerializableExtra("periodBean") as MutableList<ProjectPeriodBean>
        mVals1 = intent.getSerializableExtra("statusBean") as MutableList<ProjectStatusBean>
        reportListAdapter = ReportChildListAdapter(list, type)
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
    private fun refresh(searchText: String? = null) {
        pageNo = 1
        isRefresh = true
        mPresenter.getPageSubProject(
            hashMapOf(
                "projectId" to projectId
            ),
            type
        )
    }


    private fun loadMore() {
        isRefresh = false
        pageNo++
        mPresenter.getPageSubProject(
            hashMapOf(
                "projectId" to projectId,
                "pageNo" to pageNo.toString()
            ),
            type
        )
    }


    //设置数据到适配器
    private fun setData(data: List<ReportSubListBean>) {
        pageNo++
        val size = data.size ?: 0
        if (isRefresh) {
            reportListAdapter.setNewData(data as List<ReportSubListBean?>?)
        } else {
            if (size > 0) {
                reportListAdapter.addData(data)
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

            val bottomSheet = BottomSheetTool.showBottomSheet(mContext, "筛选", inflate, {
                var takeStatus = ""
                var takePeriod = ""
                if (arraystatus.toString().length > 2) {
                    takeStatus = arraystatus.toString().take(arraystatus.toString().length - 1)
                }
                if (arrayPeriod.toString().length > 2) {
                    takePeriod = arrayPeriod.toString().take(arrayPeriod.toString().length - 1)
                }

                //发起筛选请求
                if (takeStatus.isNotEmpty() && takePeriod.isNotEmpty()) {

                    mPresenter.getPageSubProject(
                        hashMapOf(
                            "projectId" to projectId,
                            "pageNo" to "1",
                            "pageSize" to "999",
                            "status" to takeStatus,
                            "period" to takePeriod
                        ),
                        type
                    )
                } else if (takeStatus.isNotEmpty() && takePeriod.isEmpty()) {

                    mPresenter.getPageSubProject(
                        hashMapOf(
                            "projectId" to projectId,
                            "pageNo" to "1",
                            "pageSize" to "999",
                            "status" to takeStatus

                        ),
                        type
                    )
                } else if (takeStatus.isEmpty() && takePeriod.isNotEmpty()) {

                    mPresenter.getPageSubProject(
                        hashMapOf(
                            "projectId" to projectId,
                            "pageNo" to "1",
                            "pageSize" to "999",
                            "period" to takePeriod
                        ),
                        type
                    )
                } else {

                    mPresenter.getPageSubProject(
                        hashMapOf(
                            "projectId" to projectId,
                            "pageNo" to "1",
                            "pageSize" to "999"
                        ),
                        type
                    )
                }
                isRefresh = true
                it.dismiss()
            })

        }.setLeftImageViewClickListener {
            finish()
        }

        //刷新列表
        refresh.setOnRefreshListener {
            refresh()
            refresh.isRefreshing = false
        }


        //软键盘搜索按钮监听
        searchText.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                //点击搜索的时候隐藏软键盘
                searchText.hitSoft()
                isRefresh = true
                // 网络请求数据
                mPresenter.getPageSubProject(
                    hashMapOf(
                        "projectId" to projectId,
                        "pageNo" to "1",
                        "pageSize" to "999",
                        "subProjectName" to v.text.toString()
                    ),
                    type
                )
                return@OnEditorActionListener true
            }
            false
        })
        //搜索图标点击监听
        searchImg.setOnClickListener {
            // 网络请求数据
            isRefresh = true
            mPresenter.getPageSubProject(
                hashMapOf(
                    "projectId" to projectId,
                    "pageNo" to "1",
                    "pageSize" to "999",
                    "subProjectName" to searchText.text.toString()
                ),
                type
            )
        }
        reportListAdapter.setOnLoadMoreListener({ loadMore() }, swipeRecyler)

        reportListAdapter.setOnItemClickListener { adapter, view, position ->
            val item = adapter.data[position] as ReportSubListBean
            if (type == 1) {
                item.let {
                    val assessmentId = item.assessmentId.toString()
                    ApproveProcessActivity.startAction(
                        mContext as Activity,
                        false,
                        item.processId.toString(),
                        item.projectId.toString(),
                        item.subProjectId.toString(),
                        type,
                        assessmentId
                    )
                }
            } else {
                item.let {
                    MacroReportInfoActivity.startAction(
                        mContext as Activity,
                        false,
                        item.processId.toString(),
                        item.projectId.toString(),
                        item.subProjectId.toString(),
                        type
                    )
                }
            }
        }
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
                if (arrayPeriod.toString().isNotEmpty()) {
                    arrayPeriod.delete(0, arrayPeriod.toString().length)
                }
                for (i in selectPosSet) {
                    if (i == selectPosSet.size - 1) {
                        arrayPeriod.append(mVals[i].dictId)
                    } else {
                        arrayPeriod.append(mVals[i].dictId)
                        arrayPeriod.append(",")
                    }
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
            arraystatus.delete(0, arraystatus.toString().length)
        }

        //为FlowLayout的标签设置选中监听事件
        inflate.statusFlow.setOnSelectListener(object : TagFlowLayout.OnSelectListener {
            override fun onSelected(selectPosSet: Set<Int>) {
                if (arraystatus.toString().isNotEmpty()) {
                    arraystatus.delete(0, arraystatus.toString().length)
                }
                for (i in selectPosSet) {
                    if (i == selectPosSet.size - 1) {
                        arraystatus.append(mVals1[i].statusId)
                    } else {
                        arraystatus.append(mVals1[i].statusId)
                        arraystatus.append(",")
                    }

                }


            }
        })
    }


    override fun getDataSubResult(baseRespose: NormalList<ReportSubListBean>?) {

        if (baseRespose?.records != null) {
            setData(baseRespose.records)
        }
    }


}
