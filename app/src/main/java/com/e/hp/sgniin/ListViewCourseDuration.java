package com.e.hp.sgniin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewCourseDuration extends BaseAdapter {
    Context context;
    ArrayList<itemCourseDurationModel> arrayList;


    public ListViewCourseDuration(Context context, ArrayList<itemCourseDurationModel> arrayList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.course_duration_listinfo, parent, false);
        }
        TextView duration,markup,fees,fees2;


        duration = (TextView) convertView.findViewById(R.id.duration);
        duration.setText(arrayList.get(position).getDuration());

        fees = (TextView) convertView.findViewById(R.id.fees);
        fees2 = (TextView) convertView.findViewById(R.id.fees2);
        markup = (TextView) convertView.findViewById(R.id.markup);
        fees.setPaintFlags(fees.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        fees.setText(arrayList.get(position).getFees());
        fees2.setText(arrayList.get(position).getFees2());
        markup.setText(arrayList.get(position).getMarkup());


/*
        */
/*book now button code*//*

        booknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String institute_course_id,courseName;


                institute_course_id= arrayList.get(position).getinst_cid();
                courseName= arrayList.get(position).getCourseName();
              */
/*  Toast.makeText(context, __institutename, Toast.LENGTH_SHORT).show();
                Toast.makeText(context, __instituteaddress, Toast.LENGTH_SHORT).show();*//*


                Intent intent = new Intent(context,BookNow.class);
                intent.putExtra("institute_course_id",institute_course_id);
                intent.putExtra("courseName",courseName);
                intent.putExtra("intent_inst_name",__institutename);
                intent.putExtra("intent_inst_location",__instituteaddress);
                context.startActivity(intent);

            }
        });
*/


        return convertView;
    }
}