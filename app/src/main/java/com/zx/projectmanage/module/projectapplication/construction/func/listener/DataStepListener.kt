package com.zx.projectmanage.module.projectapplication.construction.func.listener

interface DataStepListener {

    fun onFileDelete(stepPos : Int, filePos : Int)

    fun onEditChange(pos : Int, info : String)
}