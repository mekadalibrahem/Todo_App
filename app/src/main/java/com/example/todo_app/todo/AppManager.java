package com.example.todo_app.todo;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;


import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.todo_app.R;
import com.example.todo_app.database.DatabaseBuilder;
import com.example.todo_app.database.QueryHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * class to application manager ( init application database , permissions ...etc)
 */
public class AppManager {


    /**
     * method to init application
     * - build database
     * @param context context application
     * @return return status for method  0  its work successfully
     */
    public  static  int  init(Context context){
       try {
           DatabaseBuilder db = new DatabaseBuilder(context) ;
           db.getWritableDatabase();

       }catch (Exception e){
           System.err.println("DATABASE CREATED FAILED : " + e.getMessage());
       }

        return  0 ;
    }



    public  static     void updateLabelDate(EditText edit_text_item , Calendar calendar){
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        edit_text_item.setText(dateFormat.format(calendar.getTime()));
    }
    /**
     * check if edit text not empty
     * if edit text empty show error in view
     * if not empty return text from view
     * @param context
     * @param editText
     * @param msg
     * @return String || null
     */
    public static String validateEmptyEditText(Context context , EditText editText ,  String msg    ){

        String error_msg = (msg == null) ?   context.getResources().getString(R.string.This_field_is_required) : msg;
        String text = editText.getText().toString();
        if(text.isEmpty()){
            editText.setError(error_msg);
            return  null ;
        }else{
            return  text ;
        }

    }

    public  static void alarmWorkerBuilder(Context context , long reminder_id){
      try{
          Calendar calendar = Calendar.getInstance();
          QueryHelper q = new QueryHelper(context);
          Remainder remainder = q.getRemainderById(reminder_id);
          long diff_time , current_time , targetTime;

          current_time = calendar.getTimeInMillis();

          int hour =  Integer.parseInt(remainder.getHour()) ;
          int minute =Integer.parseInt(remainder.getMinutes());
          int year = Integer.parseInt(remainder.getYear());
          int month =Integer.parseInt(remainder.getMonth());
          int day =Integer.parseInt(remainder.getDay());


          calendar.set(year ,month, day,hour,minute,0 );
          targetTime = calendar.getTimeInMillis();
          diff_time = targetTime -current_time ;
          Data workData = new Data.Builder()
                  .putLong("trigger_at_millis", diff_time)
                  .putLong("reminder_id"  , reminder_id)
                  .build();

//
          OneTimeWorkRequest uploadWorkRequest =
                  new OneTimeWorkRequest.Builder(AlarmWorker.class)
                          .setInputData(workData)
                          .setInitialDelay(diff_time , TimeUnit.MILLISECONDS)
                          .addTag(String.valueOf(reminder_id))
                          .build();





          WorkManager
                  .getInstance(context)
                  .enqueue(uploadWorkRequest);
      }catch (Exception e){

      }
    }

}
