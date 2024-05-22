package com.example.todo_app.todo;



public class Todo   {
    private  long id ;
    private  String title ;
    private  String date;
    private String time;
    private long category_id ;
    private  boolean done ;


    public  Todo(){}
    public  Todo(String title , String date , String time ,  long category_id , boolean is_done){
        this.title = title ;
        this.date =date ;
        this.time = time ;
        this.category_id = category_id ;
        this.done = is_done ;
    }

    public  Todo(long id , String title , String date , String time ,  long category_id , boolean is_done){
        this.title = title ;
        this.date =date ;
        this.time = time ;
        this.category_id = category_id ;
        this.done = is_done ;
        this.id = id ;
    }



    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public long getCategory_id() {
        return category_id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public boolean isDone() {
        return done;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCategory_id(long category_id) {
        this.category_id = category_id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTitle(String title) {
        this.title = title;
    }



}
