package com.example.todo_app.todo;

import android.content.Context;

import androidx.work.WorkManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Remainder {
    private long todo_id;
    private String year;
    private String month;
    private String day;
    private String hour;
    private String minutes;
    private  long id;


    public Remainder() {}



    public Remainder(long id , long todo_id, String year, String month, String day, String hour, String minutes) {
        this.id = id ;
        this.todo_id = todo_id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minutes = minutes;
    }

    /**
     *
     * @param todo_id
     * @param date_string
     * @param time_string
     */
    public  Remainder( long todo_id , String date_string , String time_string ){
        this.todo_id = todo_id ;
        String myFormat="MM/dd/yy-HH:mm";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        try {
            // try and catch for parse string date
            Date date =  dateFormat.parse(date_string+"-"+time_string);
            Calendar calendar  = Calendar.getInstance();
            calendar.setTime(date);
            this.year = String.valueOf(calendar.get(Calendar.YEAR));
            this.month = String.valueOf(calendar.get(Calendar.MONTH));
            this.day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            this.hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
            this.minutes = String.valueOf(calendar.get(Calendar.MINUTE));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    // Getter and Setter methods

    public long getId() {return id;}

    public void setId(long id) {this.id = id;}

    public long getTodo_id() {
        return todo_id;
    }

    public void setTodo_id(long todo_id) {
        this.todo_id = todo_id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public  String  getDateTime(){
        return ( getYear() + "/" + getMonth() + "/" + getDay() +"-" +
                getHour() + ":" + getMinutes()
                );
    }

    public  void  canselAlarmWorker(Context context){
        WorkManager.getInstance(context).cancelAllWorkByTag(String.valueOf(this.getId()));
    }
}
