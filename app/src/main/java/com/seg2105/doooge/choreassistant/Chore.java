package com.seg2105.doooge.choreassistant;


import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

/**
 * Created by dustin on 11/25/17.
 */

public class Chore implements Serializable {

    private String description;
    private String choreName;
    private Long timeInMillis;
    private Calendar cal;
    private String choreIdentification;
    private int choreID;
    private Boolean complete;

    public Chore(String choreName, String description, Calendar cal) throws NoSuchAlgorithmException {
        this.choreName = choreName;
        this.description = description;
        //this.cal = cal;
        this.timeInMillis = timeInMillis;
        complete = false;

        this.cal = cal;
        this.choreID = choreID;
        generateChoreCharacIdentification();

        /*
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
        */
    }

    @Exclude
    public Calendar getCal() {
        return cal;
    }

    /*
    private int day;
    private int month;
    private int year;
    private int hour;
    private int minute;
    */

    //public Chore(String choreName, String description, int day, int month, int year, int hour, int minute ){

    public int getChoreID() {
        return choreID;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getChoreName(){
        return choreName;
    }

    public void setChoreName(String choreName){
        this.choreName = choreName;
    }

    public Calendar getCalendar(){
        return cal;
    }

    public void setCalendar(Calendar cal){
        this.cal = cal;
    }

    public void setComplete(boolean value){
        this.complete = value;
    }

    public Boolean getComplete(){
        return complete;
    }


    private void generateChoreCharacIdentification() throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] choreNameInByte = choreName.getBytes();
        byte[] descriptionInByte = description.getBytes();
        byte[] calInByte = cal.getTime().toString().getBytes();

        md.update(concatenation(concatenation(choreNameInByte, descriptionInByte), calInByte));
        byte[] choreIdentification = md.digest();

        choreIdentification.toString();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < choreIdentification.length; i++) {
            sb.append(Integer.toString((choreIdentification[i] & 0xff) + 0x100, 16).toUpperCase().substring(1));
        }
        this.choreIdentification = sb.toString();
    }


    private byte[] concatenation(byte[] a, byte[] b) {
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    public String getChoreIdentification() {
        return choreIdentification;
    }
    /*

    public int getDay(){
        return day;
    }

    public void setDay(int day){
        this.day = day;
    }

    public int getMonth(){
        return month;
    }

    public void setMonth(int month){
        this.month = month;
    }

    public int getYear(){
        return year;
    }

    public void setYear(int year){
        this.year = year;
    }

    public int getHour(){
        return hour;
    }

    public void setHour(int hour){
        this.hour= hour;
    }

    public int getMinute(){
        return minute;
    }

    public void setMinute(int minute){
        this.minute = minute;
    }
    */

}
