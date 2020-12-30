package com.zx.projectmanage.module.projectapplication.construction.bean;

import java.util.List;

/**
 * Date:2020/12/30
 * Time:1:52 PM
 * author:qingsong
 */
class Data {

    /**
     * current : 0
     * hitCount : true
     * optimizeCountSql : true
     * orders : [{"asc":true,"column":"string"}]
     * pages : 0
     * records : [{"assessmentId":"string","buildPeriod":"string","completedTime":"2020-12-30T05:50:26.687Z","createTime":"2020-12-30T05:50:26.687Z","createUser":"string","delFlag":"string","districtCode":"string","finalJudgmen":"string","latitude":"string","longitude":"string","mainProjectName":"string","participates":[{"approvalOrder":0,"chargeUser":"string","contactWay":"string","createTime":"2020-12-30T05:50:26.687Z","createUser":"string","delFlag":"string","id":"string","orgId":"string","orgName":"string","projectId":"string","superviseUser":"string","type":"string","updateTime":"2020-12-30T05:50:26.687Z","updateUser":"string"}],"projectAddress":"string","projectDesc":"string","projectId":"string","projectMeasures":"string","projectName":"string","projectNumber":"string","projectStatus":0,"projectSurvey":"string","recordNo":"string","remarks":"string","score":0,"subProjectCount":0,"tenders":"string","updateTime":"2020-12-30T05:50:26.687Z","updateUser":"string"}]
     * searchCount : true
     * size : 0
     * total : 0
     */

    private int current;
    private boolean hitCount;
    private boolean optimizeCountSql;
    private int pages;
    private boolean searchCount;
    private int size;
    private int total;
    private List<OrdersBean> orders;
    private List<RecordsBean> records;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public boolean isHitCount() {
        return hitCount;
    }

    public void setHitCount(boolean hitCount) {
        this.hitCount = hitCount;
    }

    public boolean isOptimizeCountSql() {
        return optimizeCountSql;
    }

    public void setOptimizeCountSql(boolean optimizeCountSql) {
        this.optimizeCountSql = optimizeCountSql;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public boolean isSearchCount() {
        return searchCount;
    }

    public void setSearchCount(boolean searchCount) {
        this.searchCount = searchCount;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<OrdersBean> getOrders() {
        return orders;
    }

    public void setOrders(List<OrdersBean> orders) {
        this.orders = orders;
    }

    public List<RecordsBean> getRecords() {
        return records;
    }

    public void setRecords(List<RecordsBean> records) {
        this.records = records;
    }

    public static class OrdersBean {
        /**
         * asc : true
         * column : string
         */

        private boolean asc;
        private String column;

        public boolean isAsc() {
            return asc;
        }

        public void setAsc(boolean asc) {
            this.asc = asc;
        }

        public String getColumn() {
            return column;
        }

        public void setColumn(String column) {
            this.column = column;
        }
    }

    public static class RecordsBean {
        /**
         * assessmentId : string
         * buildPeriod : string
         * completedTime : 2020-12-30T05:50:26.687Z
         * createTime : 2020-12-30T05:50:26.687Z
         * createUser : string
         * delFlag : string
         * districtCode : string
         * finalJudgmen : string
         * latitude : string
         * longitude : string
         * mainProjectName : string
         * participates : [{"approvalOrder":0,"chargeUser":"string","contactWay":"string","createTime":"2020-12-30T05:50:26.687Z","createUser":"string","delFlag":"string","id":"string","orgId":"string","orgName":"string","projectId":"string","superviseUser":"string","type":"string","updateTime":"2020-12-30T05:50:26.687Z","updateUser":"string"}]
         * projectAddress : string
         * projectDesc : string
         * projectId : string
         * projectMeasures : string
         * projectName : string
         * projectNumber : string
         * projectStatus : 0
         * projectSurvey : string
         * recordNo : string
         * remarks : string
         * score : 0
         * subProjectCount : 0
         * tenders : string
         * updateTime : 2020-12-30T05:50:26.687Z
         * updateUser : string
         */

        private String assessmentId;
        private String buildPeriod;
        private String completedTime;
        private String createTime;
        private String createUser;
        private String delFlag;
        private String districtCode;
        private String finalJudgmen;
        private String latitude;
        private String longitude;
        private String mainProjectName;
        private String projectAddress;
        private String projectDesc;
        private String projectId;
        private String projectMeasures;
        private String projectName;
        private String projectNumber;
        private int projectStatus;
        private String projectSurvey;
        private String recordNo;
        private String remarks;
        private int score;
        private int subProjectCount;
        private String tenders;
        private String updateTime;
        private String updateUser;
        private List<ParticipatesBean> participates;

        public String getAssessmentId() {
            return assessmentId;
        }

        public void setAssessmentId(String assessmentId) {
            this.assessmentId = assessmentId;
        }

        public String getBuildPeriod() {
            return buildPeriod;
        }

        public void setBuildPeriod(String buildPeriod) {
            this.buildPeriod = buildPeriod;
        }

        public String getCompletedTime() {
            return completedTime;
        }

        public void setCompletedTime(String completedTime) {
            this.completedTime = completedTime;
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

        public String getDistrictCode() {
            return districtCode;
        }

        public void setDistrictCode(String districtCode) {
            this.districtCode = districtCode;
        }

        public String getFinalJudgmen() {
            return finalJudgmen;
        }

        public void setFinalJudgmen(String finalJudgmen) {
            this.finalJudgmen = finalJudgmen;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getMainProjectName() {
            return mainProjectName;
        }

        public void setMainProjectName(String mainProjectName) {
            this.mainProjectName = mainProjectName;
        }

        public String getProjectAddress() {
            return projectAddress;
        }

        public void setProjectAddress(String projectAddress) {
            this.projectAddress = projectAddress;
        }

        public String getProjectDesc() {
            return projectDesc;
        }

        public void setProjectDesc(String projectDesc) {
            this.projectDesc = projectDesc;
        }

        public String getProjectId() {
            return projectId;
        }

        public void setProjectId(String projectId) {
            this.projectId = projectId;
        }

        public String getProjectMeasures() {
            return projectMeasures;
        }

        public void setProjectMeasures(String projectMeasures) {
            this.projectMeasures = projectMeasures;
        }

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        public String getProjectNumber() {
            return projectNumber;
        }

        public void setProjectNumber(String projectNumber) {
            this.projectNumber = projectNumber;
        }

        public int getProjectStatus() {
            return projectStatus;
        }

        public void setProjectStatus(int projectStatus) {
            this.projectStatus = projectStatus;
        }

        public String getProjectSurvey() {
            return projectSurvey;
        }

        public void setProjectSurvey(String projectSurvey) {
            this.projectSurvey = projectSurvey;
        }

        public String getRecordNo() {
            return recordNo;
        }

        public void setRecordNo(String recordNo) {
            this.recordNo = recordNo;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getSubProjectCount() {
            return subProjectCount;
        }

        public void setSubProjectCount(int subProjectCount) {
            this.subProjectCount = subProjectCount;
        }

        public String getTenders() {
            return tenders;
        }

        public void setTenders(String tenders) {
            this.tenders = tenders;
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

        public List<ParticipatesBean> getParticipates() {
            return participates;
        }

        public void setParticipates(List<ParticipatesBean> participates) {
            this.participates = participates;
        }

        public static class ParticipatesBean {
            /**
             * approvalOrder : 0
             * chargeUser : string
             * contactWay : string
             * createTime : 2020-12-30T05:50:26.687Z
             * createUser : string
             * delFlag : string
             * id : string
             * orgId : string
             * orgName : string
             * projectId : string
             * superviseUser : string
             * type : string
             * updateTime : 2020-12-30T05:50:26.687Z
             * updateUser : string
             */

            private int approvalOrder;
            private String chargeUser;
            private String contactWay;
            private String createTime;
            private String createUser;
            private String delFlag;
            private String id;
            private String orgId;
            private String orgName;
            private String projectId;
            private String superviseUser;
            private String type;
            private String updateTime;
            private String updateUser;

            public int getApprovalOrder() {
                return approvalOrder;
            }

            public void setApprovalOrder(int approvalOrder) {
                this.approvalOrder = approvalOrder;
            }

            public String getChargeUser() {
                return chargeUser;
            }

            public void setChargeUser(String chargeUser) {
                this.chargeUser = chargeUser;
            }

            public String getContactWay() {
                return contactWay;
            }

            public void setContactWay(String contactWay) {
                this.contactWay = contactWay;
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

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getOrgId() {
                return orgId;
            }

            public void setOrgId(String orgId) {
                this.orgId = orgId;
            }

            public String getOrgName() {
                return orgName;
            }

            public void setOrgName(String orgName) {
                this.orgName = orgName;
            }

            public String getProjectId() {
                return projectId;
            }

            public void setProjectId(String projectId) {
                this.projectId = projectId;
            }

            public String getSuperviseUser() {
                return superviseUser;
            }

            public void setSuperviseUser(String superviseUser) {
                this.superviseUser = superviseUser;
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
    }
}
