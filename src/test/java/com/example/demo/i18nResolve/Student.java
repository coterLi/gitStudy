package com.example.demo.i18nResolve;

/**
 * @author nelson.li
 * @date 2022/12/7
 **/
public class Student {
    /**
     * 部门ID
     */
    private Long departmentId;
    /**
     * 部门名称
     */
    private String departmentName;
    /**
     * 职位
     */
    private String positionName;

    /**
     * 试用期到期日
     */
    private String probationEndDate;

    private Boolean flag;

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public Student() {
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getProbationEndDate() {
        return probationEndDate;
    }

    public void setProbationEndDate(String probationEndDate) {
        this.probationEndDate = probationEndDate;
    }
}
