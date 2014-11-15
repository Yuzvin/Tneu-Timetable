package com.example.Sesiya_FCIT;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MyActivity extends Activity
{
    /**
     * Called when the activity is first created.
     */

    DatabaseHelper myDbHelper;
    SQLiteDatabase myDB;

    List<String> teachers;
    List<String> groups;

    List<Object[]> lessons = new ArrayList<Object[]>();

    ListView lessonsLv = null;

    Spinner spnrTeachers;
    Spinner spnrGroups;
    TextView tvTeacherOrGroup;

    TextView tvShowOnlyName;

    boolean isFirstListEnabled = true;
    boolean isSecondListEnabled = true;
    boolean isThirdListEnabled = true;
    boolean isFourthListEnabled = true;
    boolean isFifthListEnabled = true;
    boolean isSixthListEnabled = true;
    private String groupFromSpinner;
    private int listViewGroupWidth;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);

        //tvShowOnlyName = (TextView)findViewById(R.id.tvShowOnlyName);

        lessonsLv = (ListView) findViewById(R.id.listLessons);

        lessonsLv.setCacheColorHint(Color.TRANSPARENT);

        lessonsLv.setCacheColorHint(Color.TRANSPARENT);

        spnrTeachers = (Spinner)findViewById(R.id.spnrTeachers);
        spnrGroups = (Spinner) findViewById(R.id.spnrGroups);

        tvTeacherOrGroup = (TextView)findViewById(R.id.teacherOrStudent);

        myDbHelper = new DatabaseHelper(this);

        try {

            myDbHelper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }


        try
        {
            myDbHelper.openDataBase();
        } catch (SQLException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        myDB = this.openOrCreateDatabase("tte.db", MODE_PRIVATE, null);


        Cursor rdrTeachers = myDB.query(true, "timetable", new String[] {"teacher"}, null, null, null, null, "teacher", null);
        Cursor rdrGroups = myDB.query(true, "timetable", new String[]{"st_group"}, null, null, null, null, "st_group", null);

        teachers = new ArrayList<String>();
        groups = new ArrayList<String>();

        while (rdrTeachers.moveToNext())
        {
                teachers.add(rdrTeachers.getString(0));
        }
        teachers.remove(0);
        teachers.add(0, "Оберіть викладача: ");
        teachers.subList(1,21).clear();



        while (rdrGroups.moveToNext())
            groups.add((rdrGroups.getString(0)));
        groups.add(0, "Оберіть групу: ");

        ArrayAdapter<String> spnrAdapterTeacher = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, teachers);
        spnrTeachers.setAdapter(spnrAdapterTeacher);

        ArrayAdapter<String> spnrAdapterGroup = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, groups);
        spnrGroups.setAdapter(spnrAdapterGroup);

        //rdrTeachers = myDB.query("timetable",null,null,null,null,null,"lesson_time");

        spnrTeachers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                lessons.clear();

                lessonsLv.setAdapter(new LessonAdapter(getApplicationContext(), lessons));

                String teacherFromSpinner = spnrTeachers.getSelectedItem().toString();

                Cursor rdrGlobal = myDB.rawQuery("select * from timetable where teacher = ?", new String[]{teacherFromSpinner});

                populateLessonList(rdrGlobal, true);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

        spnrGroups.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                lessons.clear();

                lessonsLv.setAdapter(new LessonAdapter(getApplicationContext(), lessons));

                groupFromSpinner = spnrGroups.getSelectedItem().toString();

                Cursor rdrGlobal = myDB.rawQuery("select * from timetable where st_group = ?", new String[]{groupFromSpinner});

                populateLessonList(rdrGlobal, false);
//
//                if(!(groupFromSpinner.equals("Оберіть групу: ")))
//                {
//
//                listViewGroupWidth = lvGroup.getWidth();
//
//                tvShowOnlyName.setWidth(listViewGroupWidth);
//                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

    }

    SimpleDateFormat myDateFormat = new SimpleDateFormat("h:m");
    SimpleDateFormat stdDateFormat = new SimpleDateFormat("h:m:s");

    private void populateLessonList(Cursor rdrGlobal, Boolean isTeacher) {
        while (rdrGlobal.moveToNext())
        {
            Object[] lesson = new Object[6];
            for (int k = 0; k < lesson.length; k++)

            if(isTeacher == true)
            {
                tvTeacherOrGroup.setText("Група");
                lesson[k] = rdrGlobal.getString(1 + k);
                if(k == 3)  lesson[k] = rdrGlobal.getString(1 + 6);
                spnrGroups.setSelection(0);
            }
            else
            {
                tvTeacherOrGroup.setText("Викл.");
                lesson[k] = rdrGlobal.getString(1 + k);
                spnrTeachers.setSelection(0);
            }

            try
            {
                //lesson[1] = myDateFormat.format(stdDateFormat.parse(lesson[1].toString()));
                lesson[2] = lesson[2].toString().substring(0, lesson[2].toString().length() - 3);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            lessons.add(lesson);
        }
    }

}
