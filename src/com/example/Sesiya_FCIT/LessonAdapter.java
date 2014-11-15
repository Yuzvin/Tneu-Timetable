package com.example.Sesiya_FCIT;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: WriterMix
 * Date: 11.12.13
 * Time: 10:08
 * To change this template use File | Settings | File Templates.
 */
public class LessonAdapter extends BaseAdapter
{
    public LessonAdapter(Context c, List<Object[]> lessons)
    {
        this.lessons = lessons;
        inflater = LayoutInflater.from(c);
    }

    List<Object[]> lessons;

    LayoutInflater inflater;




    @Override
    public int getCount()
    {
        return lessons.size();
    }

    @Override
    public Object getItem(int position) {
        return lessons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static int[] rowIds = new int[]{
            R.id.week,
            R.id.day,
            R.id.time,
            R.id.teacher,
            R.id.subject,
            R.id.room
    };

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Object[] lesson =(Object[]) getItem(position) ;
        View v = convertView;
        TextView[] row;
        if (convertView == null || convertView.getTag()==null){
            v = inflater.inflate(R.layout.row, null);

            row = new TextView[rowIds.length];
            for(int i=0; i<row.length; i++)
                row[i]=(TextView) v.findViewById(rowIds[i]);

            v.setTag(row);
        }
        row = (TextView[])v.getTag();

        for(int i=0; i<row.length; i++)
            row[i].setText(lesson[i].toString());

        return v;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
