package com.example.todo_app.todo.Dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;

import com.example.todo_app.R;
import com.example.todo_app.database.QueryHelper;
import com.example.todo_app.todo.AppManager;
import com.example.todo_app.todo.Remainder;

import java.util.Calendar;

public class AddReminderDialog  extends Dialog {

    private Calendar calendar = Calendar.getInstance();

    private  long todo_id ;
    private EditText et_date ;
    private  EditText et_time ;
    private Button btn_save ;
    private ImageButton btn_close ;

    private QueryHelper queryHelper ;



    public  AddReminderDialog(@NonNull Context context  , long todo_id){
        super(context);
        this.todo_id = todo_id ;
        this.queryHelper = new QueryHelper(context);
        this.setTitle(R.string.add_reminder);
        // get layout xml file
        this.setContentView(R.layout.add_reminder_dialog);
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // not close dialog when user click in anywhere on view
        this.setCancelable(false);
        // set animation style
        this.getWindow().getAttributes().windowAnimations = R.style.animation ;
        // get  elements form view dialog
        this.et_date = (EditText) findViewById(R.id.addReminderDilaog_et_remainder_date);
        this.et_time = (EditText) findViewById(R.id.addReminderDilaog_et_remainder_time);
        this.btn_save = (Button) findViewById(R.id.addReminderDilaog_btn_save_reminder);
        this.btn_close =  findViewById(R.id.addReminderDialog_btn_close);
                // set Date dialog for reminder date and set update label when user select date
        DatePickerDialog.OnDateSetListener date_reminder = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR ,year);
                calendar.set(Calendar.MONTH ,month);
                calendar.set(Calendar.DAY_OF_MONTH , dayOfMonth);
                AppManager.updateLabelDate( et_date ,calendar);
            }


        };
        // show date dialog when user click in date reminder edit text item
        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new  DatePickerDialog(context , date_reminder ,
                        calendar.get(Calendar.YEAR) , calendar.get(Calendar.MONTH) ,
                        calendar.get(Calendar.DAY_OF_MONTH)
                        ).show();
            }
        });

        et_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
//                                       /* update text when user select time and click done  */
                        et_time.setText(hourOfDay + ":" + minutes);
                    }
                },  /* show dialog with current time  */ calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);

                timePickerDialog.show();

            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get data from edit text items
                String reminder_date  = et_date.getText().toString();
                String reminder_time = et_time.getText().toString();
                // check null values
                if(AppManager.validateEmptyEditText(context, et_date , null)  == null ||
                        AppManager.validateEmptyEditText(context ,et_time , null)  == null){
                    Toast.makeText( context ,
                            context.getResources().getString(R.string.msg_empty_data) ,
                            Toast.LENGTH_LONG).show();

                }else{

                    // if not empty values  insert data to database
                    Remainder remainder = new Remainder(todo_id ,reminder_date , reminder_time);
                    long remainder_id = queryHelper.insert(remainder);
                    if(remainder_id != 0 ){
                        // save alarm worker for notification
                        AppManager.alarmWorkerBuilder(context , remainder_id);
                        Toast.makeText(
                                context ,
                                context.getResources().getString(R.string.msg_task_insert_done),
                                Toast.LENGTH_LONG
                        ).show();
                    }else {
                        // for test
                        Log.d("MyError" , "Error Insert Reminder ");
                    }

                }

            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddReminderDialog.this.dismiss();
            }
        });


    }

}
