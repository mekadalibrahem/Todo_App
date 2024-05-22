package com.example.todo_app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.todo_app.database.QueryHelper;
import com.example.todo_app.todo.AppManager;
import com.example.todo_app.todo.Category;
import com.example.todo_app.todo.Dialogs.AddReminderDialog;

import com.example.todo_app.todo.Todo;
import com.example.todo_app.todo.adapter.CategoriesSpinnerAdapter;


import java.util.ArrayList;
import java.util.Calendar;



public class NewTaskActivity extends AppCompatActivity {
    final Calendar myCalendar= Calendar.getInstance();

    public long todo_id = 0 ;


    public EditText et_date ;
    public EditText et_time ;
    public EditText et_title ;
    public Spinner sp_category ;
    public Button btn_save_new_task ;




    // variables

    // query helper  object to call database queries
    public  QueryHelper queryHelper ;
    // selected category id  when selected by spinner category
    public  long selected_category = 0 ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        //get activity items
        et_date = findViewById(R.id.new_task_et_date);
        et_time = findViewById(R.id.new_task_et_time);
        et_title = findViewById(R.id.new_task_et_title);
        sp_category =  findViewById(R.id.new_task_sp_category);
        btn_save_new_task =  findViewById(R.id.new_task_btn_save) ;


        // define query helper   for query to database ;
         queryHelper = new QueryHelper(this);

        // init spinner category  data
            // get all categories from database
            ArrayList<Category> categories = new ArrayList<>();
            categories.add(new Category( "None"  , 0 ));
            categories.addAll(queryHelper.getAllCategories());
            // define categories spinner adapter
        CategoriesSpinnerAdapter categoriesSpinnerAdapter = new CategoriesSpinnerAdapter(this, R.layout.spinner_category_item, categories);
            // set adapter to spinner
            sp_category.setAdapter(categoriesSpinnerAdapter);

            // handler when selected item in spinner categories
            sp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Category category_selected = (Category) sp_category.getSelectedItem();
                    selected_category  =category_selected.getId();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });



        // when user select date and click on done label (done label in date picker dialog ) update date value
        DatePickerDialog.OnDateSetListener date_calender = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                AppManager.updateLabelDate(et_date , myCalendar);
            }
        };
        // when click edit text item for date show date Picker dialog
        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(NewTaskActivity.this , date_calender ,
                        /* show dialog with current date  */
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH) ).show();
            }
        });


        et_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 TimePickerDialog timePickerDialog = new TimePickerDialog(NewTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
//                                       /* update text when user select time and click done  */
                                        String time = hourOfDay + ":" + minutes ;
                                        et_time.setText(time);
                        }
                },  /* show dialog with current time  */ myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), false);

                timePickerDialog.show();

            }
        });

        btn_save_new_task.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onClick(View v) {
                // get data from view
                String title = et_title.getText().toString();
                String date = et_date.getText().toString();
                String time = et_time.getText().toString();
                long category_id = selected_category ;
                Todo todo = new Todo(title ,date , time ,category_id , false);
                // check if value empty
                if (AppManager.validateEmptyEditText( NewTaskActivity.this , et_time ,null)  == null
                        || AppManager.validateEmptyEditText(NewTaskActivity.this ,et_date ,null)   == null
                        || AppManager.validateEmptyEditText(NewTaskActivity.this ,et_title ,null ) == null
                ){
                    Toast.makeText( NewTaskActivity.this ,
                            getResources().getString(R.string.msg_empty_data) ,
                            Toast.LENGTH_LONG).show();

                }else{

                    todo_id = queryHelper.insert(todo)  ;
                    if(todo_id > 0) {
                        // show message for insert item done
                        AddReminderDialog add_reminder_dialog = new AddReminderDialog(NewTaskActivity.this , todo_id);
                        add_reminder_dialog.show();
                        Toast.makeText(NewTaskActivity.this,
                                getResources().getString(R.string.msg_task_insert_done) ,
                                Toast.LENGTH_LONG
                        ).show();
                    }else{
                        Toast.makeText(NewTaskActivity.this ,
                                getResources().getString(R.string.error_insert),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }


                }
        });









    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        // return MainActivity
        startActivity( new Intent(getApplicationContext() , MainActivity.class));
        finish();
    }







}