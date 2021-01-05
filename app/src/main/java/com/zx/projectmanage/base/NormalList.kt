package com.zx.projectmanage.base

data class NormalList<T>(
    var pages: Int,
    var current: Int,
    var total: Int,
    var records: List<T>
)