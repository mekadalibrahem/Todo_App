package com.example.todo_app.database;


/**
 * define database information ( name  , tables  , columns  )
 */
public interface DatabaseInfo {
    final  static  String DATABASE_NAME = "todo_db";
    final  static  int DATABASE_VERSION = 1 ;
    // table category
    final  static  String TABLE_CATEGORY  = "categories";

    final static  String CATEGORY_COLUMN_ID = "id" ;
    final static  String CATEGORY_COLUMN_NAME = "name" ;





    final  static  String TABLE_TODO  = "todos";
    final static  String TODO_COLUMN_ID = "id" ;

    final  static  String TODO_COLUMN_TITLE = "title" ;

    final  static  String  TODO_COLUMN_CATEGORY_ID = "category_id" ;
    final  static  String  TODO_COLUMN_IS_DONE = "is_done" ;
    final  static  String TODO_COLUMN_DATE = "date" ;
    final  static  String TODO_COLUMN_TIME = "time" ;



    final  static  String TABLE_REMAINDER = "reminders";

    final  static  String  REMAINDER_COLUMN_ID = "id" ;
    final static  String REMAINDER_COLUMN_YEAR = "year";
    final  static  String REMAINDER_COLUMN_MONTH = "month";
    final  static  String REMAINDER_COLUMN_DAY = "day" ;

    final  static  String REMAINDER_COLUMN_HOUR = "hour";
    final  static  String REMAINDER_COLUMN_MINUTES = "minutes";

    final  static  String REMAINDER_COLUMN_TODO_ID = "todo_id"  ;



    // sql query to create tables


    final  static  String CREATE_TABLE_CATEGORY = "  " +
            "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORY + " ( " +
            CATEGORY_COLUMN_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
            CATEGORY_COLUMN_NAME +  " TEXT " +
            " ) ;";
    final  static  String CREATE_TABLE_TODO = "  " +
            "CREATE TABLE IF NOT EXISTS " + TABLE_TODO + " ( " +
            TODO_COLUMN_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
            TODO_COLUMN_TITLE +  " TEXT, " +
            TODO_COLUMN_CATEGORY_ID + " INTEGER , " +
            TODO_COLUMN_DATE + " TEXT ," +
            TODO_COLUMN_TIME  + " TEXT ," +
            TODO_COLUMN_IS_DONE + " INTEGER " +
            " ) ; ";

    final  static  String CREATE_TABLE_REMAINDER = "  " +
            "CREATE TABLE IF NOT EXISTS " + TABLE_REMAINDER + " ( " +
            REMAINDER_COLUMN_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
            REMAINDER_COLUMN_TODO_ID + " INTEGER," +
            REMAINDER_COLUMN_YEAR  + " INTEGER ," +
            REMAINDER_COLUMN_MONTH + " INTEGER," +
            REMAINDER_COLUMN_DAY + " INTEGER," +
            REMAINDER_COLUMN_HOUR + " INTEGER," +
            REMAINDER_COLUMN_MINUTES + " INTEGER" +

            " ) ; ";






}
