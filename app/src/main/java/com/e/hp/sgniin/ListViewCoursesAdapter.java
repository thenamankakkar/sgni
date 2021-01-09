package com.e.hp.sgniin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;

public class ListViewCoursesAdapter extends BaseAdapter {

    Context context;
    ArrayList<itemModel> arrayList;
    String __institutename,__instituteaddress,cid,__instituteid,__institute_slug;

    public ListViewCoursesAdapter(Context context, ArrayList<itemModel> arrayList, String __instituteaddress, String __institutename,String cid,String __instituteid,String __institute_slug) {
        this.context = context;
        this.arrayList = arrayList;
        this.__instituteaddress=__instituteaddress;
        this.__institutename=__institutename;
        this.cid=cid;
        this.__instituteid=__instituteid;
        this.__institute_slug=__institute_slug;
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
        Button booknow;

        booknow =(Button) convertView.findViewById(R.id.book_now);

        courseName = (TextView) convertView.findViewById(R.id.courseName);
        fees = (TextView) convertView.findViewById(R.id.fees);
        fees2 = (TextView) convertView.findViewById(R.id.fees2);
        fees.setPaintFlags(fees.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        courseName.setText(arrayList.get(position).getCourseName());
        fees.setText(arrayList.get(position).getFees());
        fees2.setText(arrayList.get(position).getFees2());


        /*book now button code*/
        booknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String institute_course_id,courseName;


                institute_course_id= arrayList.get(position).getinst_cid();
                courseName= arrayList.get(position).getCourseName();
              /*  Toast.makeText(context, __institutename, Toast.LENGTH_SHORT).show();
                Toast.makeText(context, __instituteaddress, Toast.LENGTH_SHORT).show();*/
               // Toast.makeText(context, __instituteid, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context,BookNow.class);
                intent.putExtra("institute_course_id",institute_course_id);
                intent.putExtra("courseName",courseName);
                intent.putExtra("intent_inst_name",__institutename);
                intent.putExtra("intent_inst_location",__instituteaddress);
                intent.putExtra("cid",cid);
                intent.putExtra("institute_id",__instituteid);
                intent.putExtra("__institute_slug",__institute_slug);
                //Toast.makeText(context, __institute_slug, Toast.LENGTH_SHORT).show();
                context.startActivity(intent);

            }
        });


        return convertView;
    }
}