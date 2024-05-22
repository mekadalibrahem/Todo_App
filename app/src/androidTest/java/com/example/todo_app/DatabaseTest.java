package com.example.todo_app;

import static junit.framework.TestCase.assertNotNull;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.todo_app.database.DatabaseBuilder;

import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    public  void testDatabaseConnection(){
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        DatabaseBuilder databaseBuilder = new DatabaseBuilder(context);

        assertNotNull(databaseBuilder.getWritableDatabase());
    }

}
