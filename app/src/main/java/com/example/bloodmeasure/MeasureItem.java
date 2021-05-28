package com.example.bloodmeasure;

public class MeasureItem {
    private  int patienteImageResource;
    private String measured;
    private String device;
    private String doctorsName;
    private String sincLastMeasure;
    private String condition;
    private String date;
    private String comment;
    private String patienteName;


    public MeasureItem() {
    }

    public MeasureItem(int patienteImage, String measured, String device, String doctorsName, String sincLastMeasure, String condition, String date, String comment,String patienteName) {
        this.patienteImageResource = patienteImage;
        this.measured = measured;
        this.device = device;
        this.doctorsName = doctorsName;
        this.sincLastMeasure = sincLastMeasure;
        this.condition = condition;
        this.date = date;
        this.comment = comment;
        this.patienteName = patienteName;
    }

    public String getPatienteName() {
        return patienteName;
    }

    public void setPatienteName(String patienteName) {
        this.patienteName = patienteName;
    }

    public int getPatienteImageResource() {
        return patienteImageResource;
    }

    public String getMeasured() {
        return measured;
    }

    public void setMeasured(String measured) {
        this.measured = measured;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getDoctorsName() {
        return doctorsName;
    }

    public void setDoctorsName(String doctorsName) {
        this.doctorsName = doctorsName;
    }

    public String getSincLastMeasure() {
        return sincLastMeasure;
    }

    public void setSincLastMeasure(String sincLastMeasure) {
        this.sincLastMeasure = sincLastMeasure;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
