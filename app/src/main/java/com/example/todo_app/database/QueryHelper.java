package com.example.todo_app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.todo_app.todo.Category;
import com.example.todo_app.todo.Remainder;
import com.example.todo_app.todo.Todo;

import java.util.ArrayList;

public class QueryHelper  extends DatabaseBuilder {
    public Context context ;
    public QueryHelper(@Nullable Context context) {
        super(context);
        this.context = context ;
    }


    /**
     * insert new Category in database
     * @param category
     * @return id number for row insert else 0
     */
    public   long insert(Category category){
       try{
          
           SQLiteDatabase db = this.getWritableDatabase();
           ContentValues values = new ContentValues();
           values.put(CATEGORY_COLUMN_NAME , category.getName());
           return db.insert(TABLE_CATEGORY ,null ,values);

       }catch (Exception e){
           return 0 ;
       }
    }

    /**
     * insert new todo_row  in  database
     *
     * @param todo
     * @return id number for row insert else 0
     */

    public long insert(Todo todo ){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(TODO_COLUMN_TITLE, todo.getTitle());
            values.put(TODO_COLUMN_CATEGORY_ID, todo.getCategory_id());
            values.put(TODO_COLUMN_IS_DONE, todo.isDone());
            values.put(TODO_COLUMN_DATE, todo.getDate());
            values.put(TODO_COLUMN_TIME, todo.getTime());
            return db.insert(TABLE_TODO, null, values);
        }catch (Exception e) {
            return 0;
        }
    }


    /**
     * insert new remainder row in database
     *
     * @param remainder
     * @return id number for row insert else 0
     */
    public long insert(Remainder remainder) {
       try{
           SQLiteDatabase db = this.getWritableDatabase();
           ContentValues values = new ContentValues();
           values.put(REMAINDER_COLUMN_YEAR, remainder.getYear());
           values.put(REMAINDER_COLUMN_MONTH, remainder.getMonth());
           values.put(REMAINDER_COLUMN_DAY, remainder.getDay());
           values.put(REMAINDER_COLUMN_HOUR, remainder.getHour());
           values.put(REMAINDER_COLUMN_MINUTES, remainder.getMinutes());
           values.put(REMAINDER_COLUMN_TODO_ID, remainder.getTodo_id());
           return db.insert(TABLE_REMAINDER, null, values);

       }catch (Exception e){
           return  0 ;
       }
    }



    /**
     * sql query to return all categories stored from database
     * @return ArrayList<Category>
     */
    public ArrayList<Category> getAllCategories(){
        ArrayList<Category> categories = new ArrayList<>();
        try {


            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor =  db.rawQuery("SELECT * FROM " + TABLE_CATEGORY + " ;" , null);

            if(cursor.moveToFirst()){
                do {
                    Category category =  new Category();
                    category.setId(cursor.getInt((int) cursor.getColumnIndex(CATEGORY_COLUMN_ID)));
                    category.setName(cursor.getString((int)cursor.getColumnIndex(CATEGORY_COLUMN_NAME)));
                    categories.add(category);
                }while (cursor.moveToNext());

            }




        }catch (Exception e){
            Log.d("MyInfo" , e.getMessage());
        }

        return  categories;
    }


    /**
     * sql query return category id  by category name
     * @param name
     * @return
     */
    public int getCategoryIdByName(String name) {
        int id = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        // استعلام لإسترجاع عنصر من قاعدة البيانات 
        //  وسيط اول اسم الجدول 
        //  وسيط تاني مصفوفة اسماء الحقول يلي بدي يرجعها الاستعلام 
        //  وسيط تالت شرط الاستعلام 
        //  وسيط رابع القيم يلي رح يمررها للشرط بدال إشارة الاستفهام 
        //  وسطاء الفاضية هي متغيرات  orderby , group by ......
        Cursor cursor = db.query(TABLE_CATEGORY, new String[]{CATEGORY_COLUMN_ID}, CATEGORY_COLUMN_NAME + "=?", 
        new String[]{name}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getInt((int) cursor.getColumnIndex(CATEGORY_COLUMN_ID));
            cursor.close();
        }
        return id;
    }

    public ArrayList<Remainder> getRemaindersByTodoId(long todo_id) {
        ArrayList<Remainder> remainders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] whereArgs = new String[] {String.valueOf(todo_id)};
        Cursor cursor = db.query(TABLE_REMAINDER, null, REMAINDER_COLUMN_TODO_ID + "=?",
                whereArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
               int id = cursor.getInt((int) cursor.getColumnIndex(REMAINDER_COLUMN_ID));
               String year = cursor.getString((int) cursor.getColumnIndex(REMAINDER_COLUMN_YEAR) );
                String month = cursor.getString((int) cursor.getColumnIndex(REMAINDER_COLUMN_MONTH) );
                String day = cursor.getString((int) cursor.getColumnIndex(REMAINDER_COLUMN_DAY) );
                String hour = cursor.getString((int) cursor.getColumnIndex(REMAINDER_COLUMN_HOUR) );
                String minutes = cursor.getString((int) cursor.getColumnIndex(REMAINDER_COLUMN_MINUTES) );
                Remainder remainder = new Remainder(id ,todo_id ,year,month,day,hour,minutes);
                 remainders.add(remainder);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return remainders;
    }

    /**
     * delete category  form database
     * and change all todoItem have this category to 0
     * @param category
     * @return
     */
    public boolean delete(Category category ) {
        try{
            SQLiteDatabase db = this.getWritableDatabase();
               int row_deleted =  db.delete(TABLE_CATEGORY , "id =?" , new String[]{String.valueOf(category.getId())});
                if(row_deleted > 0 ){
                    ContentValues values = new ContentValues();
                    values.put( TODO_COLUMN_CATEGORY_ID , 0);
                    int row_update = db.update(TABLE_TODO ,
                            values ,
                            TODO_COLUMN_CATEGORY_ID + "=?" ,
                            new String[]{String.valueOf(category.getId())}
                    );
                    return  true ;
                } else  return false ;
        }catch (Exception e){
            return  false ;
        }
    }

    /**
     * delete remainder  form database
     * @param remainder
     * @return
     */
    public boolean delete(Remainder remainder ) {
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            // من اجل كل تذكير في أشعار خاص فيه فوقت احذف تنبيه لازم احذف الاشعار الخاص فيه 
            remainder.canselAlarmWorker(this.context);
            //  تابع الحذف 
            // وسيط الاول اسم الجدول 
            // وسيط تاني شرط الحذف 
            //  وسيط تالت هو القيمة يلي رح يقارن فيها ضمن الشرط بدال إشارة الاستفهام 

            int row_deleted =  db.delete(TABLE_REMAINDER , "id =?" , new String[]{String.valueOf(remainder.getId())});
            if(row_deleted > 0 ){
                return  true ;
            } else  return false ;
        }catch (Exception e){
            Log.e("MYERROR" , "error "+ e.getMessage());
            return  false ;
        }
    }


    /**
     * delete todo_item  form database and all it's reminders
     * @param todo
     * @return
     */
    public boolean delete(Todo todo ) {
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            int row_deleted =  db.delete(TABLE_TODO , "id =?" , new String[]{String.valueOf(todo.getId())});
            if(row_deleted > 0 ){
                // delete all reminder for this todo_item
                ArrayList<Remainder> remainders = this.getRemaindersByTodoId(todo.getId());
                for(Remainder remainder : remainders) {
                    this.delete(remainder);
                }
                return  true ;
            } else  return false ;
        }catch (Exception e){
            return  false ;
        }
    }

    /**
     *  query for update category name
     * @param category
     * @param new_name
     * @return true if updated else return false
     */
    public  boolean updateCategoryName(Category category , String new_name){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(CATEGORY_COLUMN_NAME , new_name);
            // update method  تعديل قيمة ضمن جدول 
            //  وسيط اول اسم الجدول 
            //  وسيط تاني القيم الجديدة 
            //  وسيط تالت هو شرط التعديل 
            //  وسيط رابع القيمة يلي رح تنحط ضمن شرط بالاستعلام  ببدلها بإشارة الاستفهام 
            int row_updated = db.update(
                    TABLE_CATEGORY,
                    values,
                    CATEGORY_COLUMN_NAME + "=?" ,
                    new String[]{category.getName()}
            );
            db.close();
           if(row_updated >0){
               return  true;
           }else return false;
        }catch (Exception e){return false;}
    }


    public ArrayList<Todo> getAllTodos(){
        ArrayList<Todo> todos = new ArrayList<>();
        try {


            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor =  db.rawQuery("SELECT * FROM " + TABLE_TODO + " ;" , null);

            if(cursor.moveToFirst()){
                do {
                    Todo todo =  new Todo();
                    todo.setId(cursor.getInt((int) cursor.getColumnIndex(TODO_COLUMN_ID)));
                    todo.setTitle(cursor.getString((int)cursor.getColumnIndex(TODO_COLUMN_TITLE)));
                    todo.setCategory_id(cursor.getLong((int)cursor.getColumnIndex(TODO_COLUMN_CATEGORY_ID)));
                    todo.setDate(cursor.getString((int)cursor.getColumnIndex(TODO_COLUMN_DATE)));
                    todo.setTime(cursor.getString((int)cursor.getColumnIndex(TODO_COLUMN_TIME)));
                    if( cursor.getInt((int)cursor.getColumnIndex(TODO_COLUMN_IS_DONE)) > 0){
                        todo.setDone(true);
                    }else todo.setDone(false);



                    todos.add(todo);
                }while (cursor.moveToNext());

            }
        }catch (Exception e){
            Log.d("MyInfo" , e.getMessage());
        }

        return todos;
    }


    public  Category getCategoryById(long id){
        Category category = new Category();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORY, null , CATEGORY_COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString((int) cursor.getColumnIndex(CATEGORY_COLUMN_NAME));
            category.setName(name);
            category.setId(id);
            cursor.close();
        }
       return category ;
    }


    public  boolean setDone(Todo todo ,  boolean is_done){
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            int done  = (is_done) ? 1 : 0 ;
            values.put(TODO_COLUMN_IS_DONE ,done);

            int updated = db.update(TABLE_TODO ,
                    values,
                    TODO_COLUMN_ID + "=?" ,
                    new String[]{String.valueOf(todo.getId())}
                    );
            db.close();
            if(updated > 0 ){
                return  true;
            }else return false;

        }catch (Exception e){return  false;}


    }

    public  boolean update(Todo todo , Todo newTodo ){

        try {

            SQLiteDatabase db  = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(TODO_COLUMN_IS_DONE , newTodo.isDone() ? 1 : 0);
            values.put(TODO_COLUMN_TIME , newTodo.getTime());
            values.put(TODO_COLUMN_DATE , newTodo.getDate());
            values.put(TODO_COLUMN_TITLE , newTodo.getTitle());
            values.put(TODO_COLUMN_CATEGORY_ID , newTodo.getCategory_id());

            int updated =  db.update(
                    TABLE_TODO ,
                    values ,
                    TODO_COLUMN_ID + "=?",
                    new String[]{String.valueOf(todo.getId())}
            );


            db.close();

            if (updated > 0 ) return  true ;
            else return  false ;


        }catch (Exception e){return false ;}
    }




    public Remainder getRemainderById(long id){

        try {
            if(id <=0 ){
                return  new Remainder() ;
            }

            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor =  db.rawQuery("SELECT * FROM " + TABLE_REMAINDER + " WHERE "+ REMAINDER_COLUMN_ID  +" =? ;" ,
                    new String[]{String.valueOf(id)});

            if(cursor.moveToFirst()){
                do {
                    Remainder remainder =  new Remainder();
                    remainder.setId(cursor.getInt((int) cursor.getColumnIndex(TODO_COLUMN_ID)));
                    remainder.setYear(cursor.getString((int)cursor.getColumnIndex(REMAINDER_COLUMN_YEAR)));
                    remainder.setDay(cursor.getString((int)cursor.getColumnIndex(REMAINDER_COLUMN_DAY)));
                    remainder.setMonth(cursor.getString((int)cursor.getColumnIndex(REMAINDER_COLUMN_MONTH)));
                    remainder.setHour(cursor.getString((int)cursor.getColumnIndex(REMAINDER_COLUMN_HOUR)));
                    remainder.setMinutes(cursor.getString((int) cursor.getColumnIndex(REMAINDER_COLUMN_MINUTES)));
                    remainder.setTodo_id(cursor.getLong((int) cursor.getColumnIndex(REMAINDER_COLUMN_TODO_ID)));

                    return  remainder;
                }while (cursor.moveToNext());

            }
        }catch (Exception e){
            Log.d("MyInfo" , e.getMessage());
        }
        return  new Remainder() ;
    }
    public Todo getTodoById(long id){

        try {
            if(id <=0 ){
                return  new Todo() ;
            }

            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor =  db.rawQuery("SELECT * FROM " + TABLE_TODO + " WHERE "+ TODO_COLUMN_ID  +" =? ;" ,
                    new String[]{String.valueOf(id)});

            if(cursor.moveToFirst()){
                do {
                    Todo todo =  new Todo();
                    todo.setId(cursor.getInt((int) cursor.getColumnIndex(TODO_COLUMN_ID)));
                    todo.setTitle(cursor.getString((int)cursor.getColumnIndex(TODO_COLUMN_TITLE)));
                    todo.setCategory_id(cursor.getLong((int)cursor.getColumnIndex(TODO_COLUMN_CATEGORY_ID)));
                    todo.setDate(cursor.getString((int)cursor.getColumnIndex(TODO_COLUMN_DATE)));
                    todo.setTime(cursor.getString((int)cursor.getColumnIndex(TODO_COLUMN_TIME)));
                    if( cursor.getInt((int)cursor.getColumnIndex(TODO_COLUMN_IS_DONE)) > 0){
                        todo.setDone(true);
                    }else todo.setDone(false);



                    return  todo;
                }while (cursor.moveToNext());

            }
        }catch (Exception e){
            Log.d("MyInfo" , e.getMessage());
        }
        return  new Todo() ;
    }

}
