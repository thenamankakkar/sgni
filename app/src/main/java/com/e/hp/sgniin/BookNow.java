package com.e.hp.sgniin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
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

    Button calendar_image;
    String currentDateString;

    String batchSpinner;
    String getinstitutecourseid, getcourseName, getinstitutename, getinstitutelocation;

    String[] spinner = {"Select Batch", "Morning", "Afternoon", "Evening"};
    /*variables for json response*/
    String fees, finalfees, markup, duration;

    ListView listView;

    ProgressDialog loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_now);

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
        Toast.makeText(this, getinstitutecourseid, Toast.LENGTH_SHORT).show();

        TextView inst_Name = (TextView) findViewById(R.id.instName);
        inst_Name.setText(getinstitutename);
        TextView inst_location = (TextView) findViewById(R.id.courseLocation);
        inst_location.setText(getinstitutelocation);
        TextView courseName = (TextView) findViewById(R.id.yourCourse);
        courseName.setText("Your Course " + getcourseName);


        Spinner spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spinner);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

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

                                    itemCourseDurationModel model = new itemCourseDurationModel();

                                    model.setFees("\u20B9" + fees);
                                    model.setFees2("\u20B9" + finalfees);
                                    model.setMarkup(markup + "% off");
                                    model.setDuration(duration + " Months");
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

                                        TextView tv = (TextView) view.findViewById(R.id.duration);
                                        TextView tv2 = (TextView) view.findViewById(R.id.fees2);
                                        TextView tv3 = (TextView) view.findViewById(R.id.fees);
                                        TextView tv4 = (TextView) view.findViewById(R.id.markup);

                                        CardView cardView = (CardView) findViewById(R.id.fd_card);
                                        cardView.setVisibility(View.VISIBLE);



                                        /*Fee Details Text View*/
                                        TextView fd_discountFees = (TextView) findViewById(R.id.fd_discountFees);
                                        fd_discountFees.setText(tv3.getText().toString()+"/-");
                                        fd_discountFees.setPaintFlags(fd_discountFees.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


                                        TextView fd_dicountoff = (TextView) findViewById(R.id.fd_dicountoff);
                                        fd_dicountoff.setText(tv4.getText().toString());

                                        TextView fd_courseName = (TextView) findViewById(R.id.fd_courseName);
                                        fd_courseName.setText(courseName.getText().toString());

                                        TextView fd_courseCost = (TextView) findViewById(R.id.fd_courseCost);
                                        fd_courseCost.setText(tv2.getText().toString() + "/-INR");


                                        Toast.makeText(BookNow.this, tv.getText().toString() + "Selected", Toast.LENGTH_SHORT).show();

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

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        batchSpinner = spinner[position];

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}