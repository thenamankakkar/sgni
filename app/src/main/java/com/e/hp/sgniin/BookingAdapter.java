package com.e.hp.sgniin;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements AdapterView.OnItemSelectedListener {


    private Context context;
    private LayoutInflater inflater;
    List<BookingsData> data = Collections.emptyList();




    // create constructor to innitilize context and data sent from MainActivity
    public BookingAdapter(Context context, List<BookingsData> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;

    }

    // Inflate the layout when viewholder created
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.booking_course_adapter, parent, false);
        return new MyHolder(view);
    }

    // Bind data
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder = (MyHolder) holder;
        BookingsData current = data.get(position);
        myHolder.value_orderid.setText(current.orderid);
        myHolder.value_quantity.setText(current.order_qty);
        myHolder.value_fees.setText(current.order_total);
        myHolder.value_orderdate.setText(current.order_date);
    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    class MyHolder extends RecyclerView.ViewHolder {
        TextView value_orderid, value_quantity, value_fees, value_orderdate;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);

            value_orderid = itemView.findViewById(R.id.value_orderid);
            value_quantity = itemView.findViewById(R.id.value_quantity);
            value_fees = itemView.findViewById(R.id.value_fees);
            value_orderdate = itemView.findViewById(R.id.value_orderdate);

            //networking libray intialization
            AndroidNetworking.initialize(context);

        }

    }
}