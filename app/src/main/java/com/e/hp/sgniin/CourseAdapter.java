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
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements AdapterView.OnItemSelectedListener {

    String __instituteId, __courseId, __institutename, __instituteaddress,__institute_slug;


    Dialog myDialog;

    private Context context;
    private LayoutInflater inflater;
    List<CourseData> data = Collections.emptyList();
    CourseData current;
    int currentPos = 0;
    Button btn_cancel, btn_submit;
    EditText ed_mobile, ed_submit;

    String spinnertext;
    String get_dialog_phone, get_submittext;
    ProgressDialog progressDialog;

    String[] country = {"Anytime", "9-12", "12-3", "3-6"};

    CourseData fishdata;


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
        myDialog = new Dialog(context);
        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder = (MyHolder) holder;
        CourseData current = data.get(position);
        myHolder.institutename.setText(current.fishName);
        myHolder.location.setText(current.fishaddress);
        myHolder.views.setText(current.fishviews);
        myHolder.inst_name.setText(current.inst_name);


    /*    myHolder.btn_know_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,BookNow.class);
                context.startActivity(intent);
            }
        });*/


        myHolder.btn_enquire_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.setContentView(R.layout.custom_alert);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                }
                myDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                myDialog.setCancelable(false);

                //myDialog.getWindow().getAttributes().windowAnimations = R.style.animation;

                btn_submit = (Button) myDialog.findViewById(R.id.submit);
                btn_cancel = (Button) myDialog.findViewById(R.id.cancel);

                ed_mobile = (EditText) myDialog.findViewById(R.id.ed_mobile_no);
                ed_submit = (EditText) myDialog.findViewById(R.id.ed_submit_text);
                ed_mobile.addTextChangedListener(nameTextWatcher);


                //Getting the instance of Spinner and applying OnItemSelectedListener on it
                Spinner spin = (Spinner) myDialog.findViewById(R.id.spinner);
                spin.setOnItemSelectedListener(CourseAdapter.this);

                //Creating the ArrayAdapter instance having the country list
                ArrayAdapter aa = new ArrayAdapter(context, android.R.layout.simple_spinner_item, country);
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                spin.setAdapter(aa);


                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Toast.makeText(context, "cancel button clicked", Toast.LENGTH_SHORT).show();
                        myDialog.dismiss();
                    }
                });


                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressDialog = new ProgressDialog(context);
                        progressDialog.setTitle("Please Wait..");
                        progressDialog.setMessage("Sending Your Request .....");
                        progressDialog.setMax(5);
                        progressDialog.setCancelable(false);


                        get_submittext = ed_submit.getText().toString();
                        //Toast.makeText(context,"Spinner values is : "+spinnertext + "Phone no is this : "+ get_dialog_phone, Toast.LENGTH_SHORT).show();
                        AndroidNetworking.post("https://sgni.in/api/run_new.php")

                                .addBodyParameter("call", "enquire")
                                .addBodyParameter("phone", get_dialog_phone)
                                .addBodyParameter("time", spinnertext)
                                .addBodyParameter("msg", get_submittext)
                                .setTag("test")
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {


                                        if (response != null && response.length() > 0) {

                                            List<CourseData> data = new ArrayList<>();
                                            progressDialog.dismiss();
                                            Toast.makeText(context, "Your Request Has Been Sent", Toast.LENGTH_SHORT).show();

                                            myDialog.dismiss();


                                            Log.d("response_alert", "" + response);
                                            try {

                                                JSONArray contacts = response.getJSONArray("data");
                                                for (int i = 0; i < contacts.length(); i++) {

                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onError(ANError error) {
                                        // handle error
                                        Log.d("error", "ha ha" + error);
                                    }
                                });
                    }
                });


                myDialog.show();


            }
        });


        /*know more button code*/
        myHolder.btn_know_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   Intent intent = new Intent(context,KnowMore.class);
                context.startActivity(intent);*/

                CourseData current = data.get(position);
                __instituteId = current.institute_id;
                __courseId = current.course_id;
                __institutename = current.fishName;
                __instituteaddress = current.fishaddress;
                __institute_slug = current.__institute_slug;
                Intent intent = new Intent(context, KnowMore.class);
                intent.putExtra("__instituteId", __instituteId);
                intent.putExtra("__courseId", __courseId);
                intent.putExtra("__institutename", __institutename);
                intent.putExtra("__instituteaddress", __instituteaddress);
                intent.putExtra("__institute_slug", __institute_slug);
                context.startActivity(intent);


                Log.d("testdata", __instituteId + " testdata2 " + __courseId);
            }
        });


    }


    private TextWatcher nameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (ed_mobile.getText().length() < 10) {
                ed_mobile.setError("Please Enter 10 Digits Number");

            }

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            get_dialog_phone = ed_mobile.getText().toString();
            if (get_dialog_phone.isEmpty()) {
                ed_mobile.setError("Please Enter Phone");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

            if (ed_mobile.getText().length() > 0) {
                ed_mobile.setError(null);
            }


            if (ed_mobile.getText().length() < 10) {
                ed_mobile.setError("Please Enter 10 Digits Number");

            }
            if (ed_mobile.getText().length() == 10) {
                ed_mobile.setError(null);
                btn_submit.setEnabled(true);
            }
        }
    };


    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        spinnertext = country[position];


        //Toast.makeText(context, "item clicked" + country[position], Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    class MyHolder extends RecyclerView.ViewHolder {
        ImageView movieImage;
        TextView location, institutename;
        TextView views, inst_name;
        Button btn_enquire_now, btn_know_more;


        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);

            movieImage = itemView.findViewById(R.id.imageView4);
            location = itemView.findViewById(R.id.location);
            institutename = itemView.findViewById(R.id.instituteName);
            views = itemView.findViewById(R.id.views);
            inst_name = itemView.findViewById(R.id.course);
            btn_enquire_now = itemView.findViewById(R.id.enquirenow);
            btn_know_more = itemView.findViewById(R.id.know_more);

            //networking libray intialization
            AndroidNetworking.initialize(context);

        }

    }
}