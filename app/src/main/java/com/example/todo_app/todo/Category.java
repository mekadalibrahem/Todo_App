package com.example.todo_app.todo;

import android.util.Log;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Category {
    private   String name ;
    private  long id ;


    public Category(){}


    public Category(String name , long id){
        this.id = id ;
        this.name = name;
    }

    // set and get method for object properties

    public  void  setName(String name){
        this.name = name ;
    }

    public  void setId(long id){
        this.id = id ;
    }

    public  long getId(){return this.id;}
    public  String getName(){return  this.name;}
    // end set and get




//
//    // for test only
//    public static void printArrayList(ArrayList<Category> categories){
//        try {
//            for (Category category: categories) {
//
//                Log.d("MyInfo" , "id" + category.getId()  + "  -  name :" + category.getName());
//            }
//        }catch (Exception e){
//            Log.d("MyInfo" , e.getMessage());
//        }
//
//    }
//
//    public  static String[] toArray(ArrayList<Category> categories){
//        return  categories.stream().map(Category::getName).collect(Collectors.toList()).toArray(new String[0]);
//    }





}
