package com.prostate.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Patient extends BaseEntity implements Serializable {

    @Null
    private String id;

    @Null
    private String patientNumber;

    @Length(min = 2,max = 5)
    private String patientName;

    @Length(min = 16,max = 18)
    private String patientCard;


    private String patientPhone;

    private String patientBirthday;

    @DecimalMin(value = "1" ,message = "年龄必须大于1")
    private String patientAge;

    private String patientHeight;

    private String patientWeight;

    private String patientSex;

    private String cityId;

    private String detailAddress;

    private String educationId;

    private String professionId;

    private String nationId;

    private String bloodGroupId;

    @Null
    private String createDoctor;

    //    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+08")
    @JsonFormat(pattern = "yyyyMMdd", timezone = "GMT+08")
    @Null
    private Date createTime;

    @Null
    private String updateDoctor;

    @Null
    private Date updateTime;

    @Null
    private String deleteDoctor;

    @Null
    private Date deleteTime;

    @Null
    private String delFlag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getPatientNumber() {
        return patientNumber;
    }

    public void setPatientNumber(String patientNumber) {
        this.patientNumber = patientNumber == null ? null : patientNumber.trim();
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName == null ? null : patientName.trim();
    }

    public String getPatientCard() {
        return patientCard;
    }

    public void setPatientCard(String patientCard) {
        this.patientCard = patientCard == null ? null : patientCard.trim();
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone == null ? null : patientPhone.trim();
    }

    public String getPatientBirthday() {
        return patientBirthday;
    }

    public void setPatientBirthday(String patientBirthday) {
        this.patientBirthday = patientBirthday == null ? null : patientBirthday.trim();
    }

    public String getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    public String getPatientSex() {
        return patientSex;
    }

    public void setPatientSex(String patientSex) {
        this.patientSex = patientSex == null ? null : patientSex.trim();
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId == null ? null : cityId.trim();
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress == null ? null : detailAddress.trim();
    }

    public String getEducationId() {
        return educationId;
    }

    public void setEducationId(String educationId) {
        this.educationId = educationId == null ? null : educationId.trim();
    }

    public String getProfessionId() {
        return professionId;
    }

    public void setProfessionId(String professionId) {
        this.professionId = professionId == null ? null : professionId.trim();
    }

    public String getNationId() {
        return nationId;
    }

    public void setNationId(String nationId) {
        this.nationId = nationId == null ? null : nationId.trim();
    }

    public String getBloodGroupId() {
        return bloodGroupId;
    }

    public void setBloodGroupId(String bloodGroupId) {
        this.bloodGroupId = bloodGroupId == null ? null : bloodGroupId.trim();
    }

    public String getCreateDoctor() {
        return createDoctor;
    }

    public void setCreateDoctor(String createDoctor) {
        this.createDoctor = createDoctor == null ? null : createDoctor.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateDoctor() {
        return updateDoctor;
    }

    public void setUpdateDoctor(String updateDoctor) {
        this.updateDoctor = updateDoctor == null ? null : updateDoctor.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getDeleteDoctor() {
        return deleteDoctor;
    }

    public void setDeleteDoctor(String deleteDoctor) {
        this.deleteDoctor = deleteDoctor == null ? null : deleteDoctor.trim();
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag == null ? null : delFlag.trim();
    }

    @Override
    public String toString() {
        return "id:" + id
                + ",patientNumber:" + patientNumber
                + ",patientName:" + patientName
                + ",patientCard:" + patientCard
                + ",patientPhone:" + patientPhone
                + ",patientBirthday:" + patientBirthday
                + ",patientAge:" + patientAge
                + ",patientSex:" + patientSex
                + ",cityId:" + cityId
                + ",detailAddress:" + detailAddress
                + ",educationId:" + educationId
                + ",professionId:" + professionId
                + ",nationId:" + nationId
                + ",bloodGroupId:" + bloodGroupId
                + ",createDoctor:" + createDoctor
                + ",createTime:" + createTime
                + ",updateDoctor:" + updateDoctor
                + ",updateTime:" + updateTime
                + ",deleteDoctor:" + deleteDoctor
                + ",deleteTime:" + deleteTime
                + ",delFlag:" + delFlag;
    }

    public String getPatientHeight() {
        return patientHeight;
    }

    public void setPatientHeight(String patientHeight) {
        this.patientHeight = patientHeight;
    }

    public String getPatientWeight() {
        return patientWeight;
    }

    public void setPatientWeight(String patientWeight) {
        this.patientWeight = patientWeight;
    }
}