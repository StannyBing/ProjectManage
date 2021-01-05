package com.zx.projectmanage.module.projectapplication.construction.bean;

/**
 * Date:2021/1/4
 * Time:5:00 PM
 * author:qingsong
 */
public class FileInfoBean {

    /**
     * bucketName : string
     * createTime : 2021-01-04T08:59:01.083Z
     * createUser : string
     * delFlag : string
     * fileName : string
     * fileSize : 0
     * id : string
     * original : string
     * type : string
     * updateTime : 2021-01-04T08:59:01.083Z
     * updateUser : string
     */

    private String bucketName;
    private String createTime;
    private String createUser;
    private String delFlag;
    private String fileName;
    private int fileSize;
    private String id;
    private String original;
    private String type;
    private String updateTime;
    private String updateUser;

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
}
