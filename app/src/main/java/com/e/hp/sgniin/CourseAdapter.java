package com.e.hp.sgniin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<CourseData> data = Collections.emptyList();
    CourseData current;
    int currentPos = 0;

    // create constructor to innitilize context and data sent from MainActivity
    public CourseAdapter(Context context, List<CourseData> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.course_item_list2, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder = (MyHolder) holder;
        CourseData current = data.get(position);
        myHolder.institutename.setText(current.fishName);
        myHolder.location.setText(current.fishaddress);


    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {

/*        ImageView movieImage;
        TextView textViewName;
        TextView textViewDate;*/


        ImageView movieImage;
        TextView location, institutename;
        TextView textViewDate;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
         /*   movieImage = itemView.findViewById(R.id.imageview);
            textViewName = itemView.findViewById(R.id.textName);
            textViewDate = itemView.findViewById(R.id.textdate);*/



            movieImage = itemView.findViewById(R.id.imageView4);
            location = itemView.findViewById(R.id.location);
            institutename = itemView.findViewById(R.id.instituteName);


        }

    }

}