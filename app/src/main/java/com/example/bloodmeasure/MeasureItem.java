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

    public int getPatienteImageResource() {
        return patienteImageResource;
    }

    public String getMeasured() {
        return measured;
    }

    public String getDevice() {
        return device;
    }

    public String getDoctorsName() {
        return doctorsName;
    }

    public String getSincLastMeasure() {
        return sincLastMeasure;
    }

    public String getCondition() {
        return condition;
    }

    public String getDate() {
        return date;
    }

    public String getComment() {
        return comment;
    }

    public String getPatienteName() {
        return patienteName;
    }
}
