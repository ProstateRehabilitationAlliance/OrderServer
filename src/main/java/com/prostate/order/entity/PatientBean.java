package com.prostate.order.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatientBean extends Patient implements Serializable {


    public String bloodGroup;

    public String professionName;

    private String EducationName;

    private String NationName;

    public List<OrderBean> orderBeanList;

    public List<String> orderStringList;


    public void setPatient(Patient patient) {
        super.setId(patient.getId());
        super.setPatientNumber(patient.getPatientNumber());
        super.setPatientName(patient.getPatientName());
        super.setPatientCard(patient.getPatientCard());
        super.setPatientPhone(patient.getPatientPhone());
        super.setPatientBirthday(patient.getPatientBirthday());
        super.setPatientAge(patient.getPatientAge());
        super.setPatientSex(patient.getPatientSex());
        super.setPatientHeight(patient.getPatientHeight());
        super.setPatientWeight(patient.getPatientWeight());
        super.setDetailAddress(patient.getDetailAddress());
        super.setCreateTime(patient.getCreateTime());
    }


    public String getEducationName() {
        return EducationName;
    }

    public void setEducationName(String educationName) {
        EducationName = educationName;
    }

    public String getNationName() {
        return NationName;
    }

    public void setNationName(String nationName) {
        NationName = nationName;
    }

    public List<String> getOrderStringList() {
        return orderStringList;
    }

    public void setOrderStringList(List<String> orderStringList) {
        this.orderStringList = orderStringList;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public List<OrderBean> getOrderBeanList() {
        return orderBeanList;
    }

    public void setOrderBeanList(List<OrderBean> orderBeanList) {
        this.orderBeanList = orderBeanList;
    }

    public String getProfessionName() {
        return professionName;
    }

    public void setProfessionName(String professionName) {
        this.professionName = professionName;
    }
}
