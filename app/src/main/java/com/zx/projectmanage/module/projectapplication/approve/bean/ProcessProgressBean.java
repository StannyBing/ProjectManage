package com.zx.projectmanage.module.projectapplication.approve.bean;

/**
 * Date:2021/1/4
 * Time:3:06 PM
 * author:qingsong
 */
public class ProcessProgressBean {

    /**
     * auditReason : string
     * auditStatus : string
     * createTime : 2021-01-04T07:05:35.266Z
     * equipmentName : string
     * postName : string
     * realName : string
     */

    private String auditReason;
    private String auditStatus;
    private String createTime;
    private String equipmentName;
    private String postName;
    private String realName;

    public String getAuditReason() {
        return auditReason;
    }

    public void setAuditReason(String auditReason) {
        this.auditReason = auditReason;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
