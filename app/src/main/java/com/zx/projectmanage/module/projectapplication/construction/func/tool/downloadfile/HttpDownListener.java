package com.zx.projectmanage.module.projectapplication.construction.func.tool.downloadfile;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Date:2021/1/11
 * Time:10:55 AM
 * author:qingsong
 */
public interface HttpDownListener{
    void onFailure(Call call, IOException e);
    void onResponse(Call call, Response response, long mTotalLength, long mAlreadyDownLength);
}
