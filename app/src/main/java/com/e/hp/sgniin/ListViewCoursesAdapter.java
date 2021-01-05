package com.e.hp.sgniin;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;

public class ListViewCoursesAdapter extends BaseAdapter {

    Context context;
    ArrayList<itemModel> arrayList;

    public ListViewCoursesAdapter(Context context, ArrayList<itemModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.course_info, parent, false);
        }
        TextView courseName,fees,fees2;

        courseName = (TextView) convertView.findViewById(R.id.courseName);
        fees = (TextView) convertView.findViewById(R.id.fees);
        fees2 = (TextView) convertView.findViewById(R.id.fees2);
        fees.setPaintFlags(fees.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        courseName.setText(arrayList.get(position).getCourseName());
        fees.setText(arrayList.get(position).getFees());
        fees2.setText(arrayList.get(position).getFees2());


        return convertView;
    }
}