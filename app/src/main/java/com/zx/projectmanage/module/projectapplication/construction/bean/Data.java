package com.zx.projectmanage.module.projectapplication.construction.bean;

import java.util.List;

/**
 * Date:2020/12/30
 * Time:1:52 PM
 * author:qingsong
 */
class Data {


    /**
     * processId : 1341276250770563074
     * processName : 测试修复
     * remarks : fsdafs
     * createTime : 2020-12-30 11:32:38
     * createUser : null
     * updateTime : 2020-12-30 11:32:38
     * updateUser : null
     * delFlag : null
     * detailedList : [{"id":"1344208677254467585","processId":"1341276250770563074","approvalMode":"354724257168625664","assessment":"354136375345287168,354134317070290944,356239445776666624,1","operationGuide":"356633892335128576","safetyRegulations":"356633901403213824","materials":"356633910802649088","subProcessName":"工序1","scoreRights":"354136375345287168,354134317070290944,356239445776666624","resetPermissions":"","enableAssessment":0,"enableScoreRights":1,"enableResetPermissions":0,"sort":0,"showOperationGuide":1,"showSafetyRegulations":1,"showMaterials":1,"createTime":"2020-12-31 12:40:19","createUser":null,"updateTime":"2020-12-31 04:40:23","updateUser":null,"participants":"354136375345287168,354134317070290944,356239445776666624,1,354344298709258240","delFlag":"0"},{"id":"1344209269234339841","processId":"1341276250770563074","approvalMode":"354724257168625664","assessment":"354136375345287168,354134317070290944,356239445776666624","operationGuide":"356634417109667840","safetyRegulations":"356634425183703040","materials":"356634434788659200","subProcessName":"工序1","scoreRights":"354136375345287168,354134317070290944","resetPermissions":"","enableAssessment":0,"enableScoreRights":1,"enableResetPermissions":0,"sort":0,"showOperationGuide":1,"showSafetyRegulations":1,"showMaterials":1,"createTime":"2020-12-31 12:40:23","createUser":null,"updateTime":"2020-12-31 04:40:26","updateUser":null,"participants":"354136375345287168,354134317070290944,356239445776666624","delFlag":"0"}]
     * processCount : null
     */

    private String processId;
    private String processName;
    private String remarks;
    private String createTime;
    private Object createUser;
    private String updateTime;
    private Object updateUser;
    private Object delFlag;
    private Object processCount;
    private List<DetailedListBean> detailedList;

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Object getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Object createUser) {
        this.createUser = createUser;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Object getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Object updateUser) {
        this.updateUser = updateUser;
    }

    public Object getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Object delFlag) {
        this.delFlag = delFlag;
    }

    public Object getProcessCount() {
        return processCount;
    }

    public void setProcessCount(Object processCount) {
        this.processCount = processCount;
    }

    public List<DetailedListBean> getDetailedList() {
        return detailedList;
    }

    public void setDetailedList(List<DetailedListBean> detailedList) {
        this.detailedList = detailedList;
    }

    public static class DetailedListBean {
        /**
         * id : 1344208677254467585
         * processId : 1341276250770563074
         * approvalMode : 354724257168625664
         * assessment : 354136375345287168,354134317070290944,356239445776666624,1
         * operationGuide : 356633892335128576
         * safetyRegulations : 356633901403213824
         * materials : 356633910802649088
         * subProcessName : 工序1
         * scoreRights : 354136375345287168,354134317070290944,356239445776666624
         * resetPermissions :
         * enableAssessment : 0
         * enableScoreRights : 1
         * enableResetPermissions : 0
         * sort : 0
         * showOperationGuide : 1
         * showSafetyRegulations : 1
         * showMaterials : 1
         * createTime : 2020-12-31 12:40:19
         * createUser : null
         * updateTime : 2020-12-31 04:40:23
         * updateUser : null
         * participants : 354136375345287168,354134317070290944,356239445776666624,1,354344298709258240
         * delFlag : 0
         */

        private String id;
        private String processId;
        private String approvalMode;
        private String assessment;
        private String operationGuide;
        private String safetyRegulations;
        private String materials;
        private String subProcessName;
        private String scoreRights;
        private String resetPermissions;
        private int enableAssessment;
        private int enableScoreRights;
        private int enableResetPermissions;
        private int sort;
        private int showOperationGuide;
        private int showSafetyRegulations;
        private int showMaterials;
        private String createTime;
        private Object createUser;
        private String updateTime;
        private Object updateUser;
        private String participants;
        private String delFlag;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProcessId() {
            return processId;
        }

        public void setProcessId(String processId) {
            this.processId = processId;
        }

        public String getApprovalMode() {
            return approvalMode;
        }

        public void setApprovalMode(String approvalMode) {
            this.approvalMode = approvalMode;
        }

        public String getAssessment() {
            return assessment;
        }

        public void setAssessment(String assessment) {
            this.assessment = assessment;
        }

        public String getOperationGuide() {
            return operationGuide;
        }

        public void setOperationGuide(String operationGuide) {
            this.operationGuide = operationGuide;
        }

        public String getSafetyRegulations() {
            return safetyRegulations;
        }

        public void setSafetyRegulations(String safetyRegulations) {
            this.safetyRegulations = safetyRegulations;
        }

        public String getMaterials() {
            return materials;
        }

        public void setMaterials(String materials) {
            this.materials = materials;
        }

        public String getSubProcessName() {
            return subProcessName;
        }

        public void setSubProcessName(String subProcessName) {
            this.subProcessName = subProcessName;
        }

        public String getScoreRights() {
            return scoreRights;
        }

        public void setScoreRights(String scoreRights) {
            this.scoreRights = scoreRights;
        }

        public String getResetPermissions() {
            return resetPermissions;
        }

        public void setResetPermissions(String resetPermissions) {
            this.resetPermissions = resetPermissions;
        }

        public int getEnableAssessment() {
            return enableAssessment;
        }

        public void setEnableAssessment(int enableAssessment) {
            this.enableAssessment = enableAssessment;
        }

        public int getEnableScoreRights() {
            return enableScoreRights;
        }

        public void setEnableScoreRights(int enableScoreRights) {
            this.enableScoreRights = enableScoreRights;
        }

        public int getEnableResetPermissions() {
            return enableResetPermissions;
        }

        public void setEnableResetPermissions(int enableResetPermissions) {
            this.enableResetPermissions = enableResetPermissions;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getShowOperationGuide() {
            return showOperationGuide;
        }

        public void setShowOperationGuide(int showOperationGuide) {
            this.showOperationGuide = showOperationGuide;
        }

        public int getShowSafetyRegulations() {
            return showSafetyRegulations;
        }

        public void setShowSafetyRegulations(int showSafetyRegulations) {
            this.showSafetyRegulations = showSafetyRegulations;
        }

        public int getShowMaterials() {
            return showMaterials;
        }

        public void setShowMaterials(int showMaterials) {
            this.showMaterials = showMaterials;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public Object getCreateUser() {
            return createUser;
        }

        public void setCreateUser(Object createUser) {
            this.createUser = createUser;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public Object getUpdateUser() {
            return updateUser;
        }

        public void setUpdateUser(Object updateUser) {
            this.updateUser = updateUser;
        }

        public String getParticipants() {
            return participants;
        }

        public void setParticipants(String participants) {
            this.participants = participants;
        }

        public String getDelFlag() {
            return delFlag;
        }

        public void setDelFlag(String delFlag) {
            this.delFlag = delFlag;
        }
    }
}
