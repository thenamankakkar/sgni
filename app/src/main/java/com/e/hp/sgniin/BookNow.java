package com.e.hp.sgniin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class BookNow extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {
    String getFees_id, getfees, getFinalfees, getMarkup, getDuration, getAdmin_profit_markup, getAdmin_profit;

    Button calendar_image;
    String currentDateString, spinnerText;

    String batchSpinner;
    String getinstitutecourseid, getcourseName, getinstitutename, getinstitutelocation, getcid, institute_id;

    String[] spinner = {"Select Batch", "Morning", "Afternoon", "Evening"};
    String spinnerValue;
    /*variables for json response*/
    String fees, finalfees, markup, duration, fees_id, admin_profit_markup, admin_profit;

    ListView listView;

    ProgressDialog loading;
    Button payAtCenter;

    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String Phone = "nameKey";
    public static final String Studentid = "sid";

    Dialog myDialog;
    EditText ed_name, ed_mobile;
    String get_dialog_phone, get_dialog_name;
    Button btn_submit, btn_cancel;

    String stu_id, stu_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_now);
        sharedPreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        stu_id = sharedPreferences.getString(Studentid, "");
        stu_phone = sharedPreferences.getString(Phone, "");
        //Toast.makeText(this, stu_id+stu_phone, Toast.LENGTH_SHORT).show();


        myDialog = new Dialog(this);

        payAtCenter = (Button) findViewById(R.id.pay_at_center);
        payAtCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedPreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
                String value = sharedPreferences.getString(Studentid, "");
                Toast.makeText(BookNow.this, value, Toast.LENGTH_SHORT).show();


                myDialog.setContentView(R.layout.booknow_alert);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                }
                myDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                myDialog.setCancelable(false);
                myDialog.show();


                btn_submit = (Button) myDialog.findViewById(R.id.submit);
                btn_cancel = (Button) myDialog.findViewById(R.id.cancel);

                ed_mobile = (EditText) myDialog.findViewById(R.id.ed_phone);
                ed_name = (EditText) myDialog.findViewById(R.id.ed_name);
                ed_mobile.addTextChangedListener(nameTextWatcher);
                ed_name.addTextChangedListener(nameTextWatcher);
                ed_mobile.setText(stu_phone);


                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                    }
                });
                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String name = ed_name.getText().toString();
                        String mobile = ed_mobile.getText().toString();

                        JSONArray array = new JSONArray();
                        JSONArray arraybatch = new JSONArray();
                        array.put(getFees_id);
                        array.put(getFinalfees);
                        array.put(getfees);
                        array.put(getMarkup);
                        array.put(getDuration);
                        array.put(getAdmin_profit_markup);
                        array.put(getAdmin_profit);
                        arraybatch.put(currentDateString);
                        arraybatch.put(spinnerText);

                        Toast.makeText(BookNow.this, array.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("ttttt",array.toString());
                        //networking libray intialization
                        AndroidNetworking.initialize(getApplicationContext());
                        //Toast.makeText(this, "" + __instituteId, Toast.LENGTH_SHORT).show();


                        AndroidNetworking.post("https://sgni.in/api/run_new.php")

                                .addBodyParameter("call", "order")
                                .addBodyParameter("cid", getcid)
                                .addBodyParameter("fees", array.toString())
                                .addBodyParameter("batch", arraybatch.toString())
                                .addBodyParameter("name", name)
                                .addBodyParameter("ph", mobile)
                                .addBodyParameter("sid", stu_id)
                                .addBodyParameter("instid", institute_id)
                                .setTag("test")
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        loading.dismiss();

                                        if (response != null && response.length() > 0) {

                                            Log.d("response_booked", "" + response);
                                            try {
                                                ArrayList<String> res_locid_value = new ArrayList<String>();
                                                ArrayList<itemModel> arrayList = new ArrayList<>();
                                                ArrayList<String> course_name = new ArrayList<String>();
                                                JSONArray contacts = response.getJSONArray("data");
                                                for (int i = 0; i < contacts.length(); i++) {


                                                    JSONObject c = contacts.getJSONObject(i);
                                                /*    fees = c.getString("fees");
                                                    finalfees = c.getString("final_fees");
                                                    markup = c.getString("markup");*/
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
            }
        });


        //progress dialog
        loading = new ProgressDialog(this);
        loading.setTitle("Please Wait..");
        loading.setMessage("Data is Loading .....");
        loading.setMax(5);
        loading.setCancelable(false);
        loading.show();

        listView = (ListView) findViewById(R.id.durationListView);

        Intent intent = getIntent();
        getinstitutecourseid = intent.getStringExtra("institute_course_id");
        getcourseName = intent.getStringExtra("courseName");
        getinstitutename = intent.getStringExtra("intent_inst_name");
        getinstitutelocation = intent.getStringExtra("intent_inst_location");
        getcid = intent.getStringExtra("cid");
        institute_id = intent.getStringExtra("institute_id");


        //Toast.makeText(this, getinstitutecourseid, Toast.LENGTH_SHORT).show();

        TextView inst_Name = (TextView) findViewById(R.id.instName);
        inst_Name.setText(getinstitutename);
        TextView inst_location = (TextView) findViewById(R.id.courseLocation);
        inst_location.setText(getinstitutelocation);
        TextView courseName = (TextView) findViewById(R.id.yourCourse);
        courseName.setText("Your Course " + getcourseName);


        Spinner spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);


        //Toast.makeText(this, spinnerValue, Toast.LENGTH_SHORT).show();

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spinner);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);
        spinnerValue = spin.getSelectedItem().toString();
        Toast.makeText(this, spinnerValue, Toast.LENGTH_SHORT).show();


        calendar_image = (Button) findViewById(R.id.calendar);


        calendar_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");


            }
        });


        /*api is going to hit*/
        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.post("https://sgni.in/api/run_new.php")

                .addBodyParameter("call", "Show_Courses_Fees")
                .addBodyParameter("param", getinstitutecourseid)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        loading.dismiss();


                        if (response != null && response.length() > 0) {

                            Log.d("response_courseduration", "" + response);
                            try {
                                ArrayList<itemCourseDurationModel> arrayList = new ArrayList<>();

                                JSONArray contacts = response.getJSONArray("data");
                                for (int i = 0; i < contacts.length(); i++) {

                                    JSONObject c = contacts.getJSONObject(i);
                                    fees = c.getString("fees");
                                    finalfees = c.getString("final_fees");
                                    markup = c.getString("markup");
                                    duration = c.getString("duration");
                                    fees_id = c.getString("fees_id");
                                    admin_profit_markup = c.getString("admin_profit_markup");
                                    admin_profit = c.getString("admin_profit");


                                    itemCourseDurationModel model = new itemCourseDurationModel();

                                    model.setFees("\u20B9" + fees);
                                    model.setFees2("\u20B9" + finalfees);
                                    model.setMarkup(markup + "% off");
                                    model.setDuration(duration + " Months");
                                    model.setFeesid(fees_id);
                                    model.setAdmin_profit_markup(admin_profit_markup);
                                    model.setAdmin_profit(admin_profit);
                                    arrayList.add(model);


                                }


                                ListViewCourseDuration adapter = new ListViewCourseDuration(BookNow.this, arrayList);
                                listView.setAdapter(adapter);



/*                                adapter = new ArrayAdapter<String>(KnowMore.this, R.layout.course_info, res_locid_value);
                                android.widget.ListView list = (android.widget.ListView) findViewById(R.id.courseListView);
                                list.setAdapter(adapter);*/
                                listView.setTextFilterEnabled(true);
                                listView.setDescendantFocusability(ListView.FOCUS_BLOCK_DESCENDANTS);


                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view,
                                                            int position, long id) {

                                        itemCourseDurationModel model = new itemCourseDurationModel();

                                        TextView tv = (TextView) view.findViewById(R.id.duration);
                                        TextView tv2 = (TextView) view.findViewById(R.id.fees2);
                                        TextView tv3 = (TextView) view.findViewById(R.id.fees);
                                        TextView tv4 = (TextView) view.findViewById(R.id.markup);

                                        CardView cardView = (CardView) findViewById(R.id.fd_card);
                                        cardView.setVisibility(View.VISIBLE);



                                        /*Fee Details Text View*/
                                        TextView fd_discountFees = (TextView) findViewById(R.id.fd_discountFees);
                                        fd_discountFees.setText(tv3.getText().toString() + "/-");
                                        fd_discountFees.setPaintFlags(fd_discountFees.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


                                        TextView fd_dicountoff = (TextView) findViewById(R.id.fd_dicountoff);
                                        fd_dicountoff.setText(tv4.getText().toString());

                                        TextView fd_courseName = (TextView) findViewById(R.id.fd_courseName);
                                        fd_courseName.setText(getcourseName);

                                        TextView fd_courseCost = (TextView) findViewById(R.id.fd_courseCost);
                                        fd_courseCost.setText(tv2.getText().toString() + "/-INR");


                                        //Toast.makeText(BookNow.this, tv.getText().toString() + " Selected", Toast.LENGTH_SHORT).show();
                                        getFees_id = arrayList.get(position).getFeesid();
                                        getfees = arrayList.get(position).getFees();
                                        getFinalfees = arrayList.get(position).getFees2();
                                        getMarkup = arrayList.get(position).getMarkup();
                                        getDuration = arrayList.get(position).getDuration();
                                        getAdmin_profit_markup = arrayList.get(position).getAdmin_profit_markup();
                                        getAdmin_profit = arrayList.get(position).getAdmin_profit();


                                        Toast.makeText(BookNow.this, getAdmin_profit_markup, Toast.LENGTH_SHORT).show();

                                    }
                                });


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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        calendar_image.setText(currentDateString);
        Toast.makeText(this, currentDateString, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        batchSpinner = spinner[position];
        spinnerText = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), spinnerText, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private TextWatcher nameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (ed_name.getText().length() < 3) {
                ed_name.setError("Name is Mandatory");

            }


            if (ed_mobile.getText().length() < 10) {
                ed_mobile.setError("Please Enter 10 Digits Number");

            }

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            get_dialog_name = ed_name.getText().toString();
            if (get_dialog_name.isEmpty()) {
                ed_name.setError("Name is Mandatory");
            }


            get_dialog_phone = ed_mobile.getText().toString();
            if (get_dialog_phone.isEmpty()) {
                ed_mobile.setError("Please Enter Phone");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {


            if (ed_name.getText().length() < 5) {
                ed_name.setError("Name is Mandatory");
            }

            if (ed_name.getText().length() <= 5) {
                ed_name.setError(null);
            }


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
}