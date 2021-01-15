package com.e.hp.sgniin;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Bookings extends AppCompatActivity {
    RecyclerView recyclerView;


    SharedPreferences sharedPreferences;
    public static final String Studentid = "sid";
    public static final String mypreference = "mypref";

    ProgressDialog yourcourse_Loading;

    private BookingAdapter mAdapter;

    String orderid, orderquantity, ordertotal, orderdate, orderstatus, feestatus, cid, course_name, inst_name,order_detail_id,roll_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);
        //networking libray intialization
        AndroidNetworking.initialize(getApplicationContext());


        //progress dialog
        yourcourse_Loading = new ProgressDialog(this);
        yourcourse_Loading.setTitle("Please Wait..");
        yourcourse_Loading.setMessage("Booked Courses Loading .....");
        yourcourse_Loading.setMax(5);
        yourcourse_Loading.setCancelable(false);
        yourcourse_Loading.show();

        sharedPreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        String value = sharedPreferences.getString(Studentid, "");


        //networking libray intialization
        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.post("https://sgni.in/api/run_new.php")

                .addBodyParameter("call", "myorder")
                .addBodyParameter("s", "211")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(JSONObject response) {
                        yourcourse_Loading.dismiss();
                        List<BookingsData> data = new ArrayList<>();
                        Log.d("response_orderBookings", "" + response);
                        try {

                            JSONArray contacts = response.getJSONArray("data");
                            for (int i = 0; i < contacts.length(); i++) {

                                BookingsData fishData = new BookingsData();
                                JSONObject c = contacts.getJSONObject(i);


                                if (c.has("order_detail_id")) {
                                    order_detail_id = c.getString("order_detail_id");

                                //    Toast.makeText(Bookings.this, order_detail_id, Toast.LENGTH_SHORT).show();

                                }

                                if (c.has("roll_id")) {
                                    roll_id = c.getString("roll_id");

                                }



/*                                if (c.has("inst_name")) {
                                    inst_name = c.getString("inst_name");
                                    Toast.makeText(Bookings.this, inst_name, Toast.LENGTH_SHORT).show();

                                }*/


                                if (c.has("orderid")) {
                                    orderid = c.optString("orderid");

                                }

                                if (c.has("order_total")) {
                                    ordertotal = c.getString("order_total");

                                }

                                if (c.has("order_qty")) {
                                    orderquantity = c.getString("order_qty");
                                }
                                if (c.has("order_date")) {
                                    orderdate = c.getString("order_date");
                                }

                          /*      orderid = c.getString("orderid");
                                orderquantity = c.getString("order_qty");
                                ordertotal = c.getString("order_total");
                                orderdate = c.getString("order_date");
                                orderstatus = c.getString("order_status");
                                feestatus = c.getString("fee_status");*/

                                if (Objects.equals(order_detail_id, roll_id)&& (Objects.equals(orderid, roll_id))) {

                                    //Toast.makeText(Bookings.this, "Matched", Toast.LENGTH_SHORT).show();

                                    fishData.orderid=" "+orderid;
                                    fishData.order_qty=" "+orderquantity;
                                    fishData.order_total=" "+ordertotal+" \u20B9";
                                    fishData.order_date=" "+orderdate;
                                    data.add(fishData);
                                }


/*                                if (order_detail_id.equals(roll_id))
                                {
                                    Toast.makeText(Bookings.this, "Matched", Toast.LENGTH_SHORT).show();
                                    fishData.orderid=c.getString("orderid");
                                    fishData.order_qty=orderquantity;
                                    fishData.order_total=ordertotal;
                                    fishData.order_date=orderdate;
                                    data.add(fishData);
                                }*/



/*
                                          fishData.order_qty=orderquantity;
                                           fishData.order_total=ordertotal;
                                           fishData.order_date=orderdate;*/



/*                                            orderquantity = c.getString("order_qty");
                                            ordertotal = c.getString("order_total");
                                            orderdate = c.getString("order_date");
                                            orderstatus = c.getString("order_status");
                                            feestatus = c.getString("fee_status");
                                            cid = c.getString("cid");
                                            course_name = c.getString("course_name");*/


                            }


                            /*recycler view code*/
                            recyclerView = findViewById(R.id.bookingrecyclerView);
                            mAdapter = new BookingAdapter(Bookings.this, data);

                            //CourseViewAdapter courseViewAdapter = new CourseViewAdapter(data);
                            recyclerView.setAdapter(mAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(Bookings.this));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("Exceptionaagai", "" + e);
                            Toast.makeText(Bookings.this, "" + e, Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.d("erroraaaaa", "ha ha" + error);
                    }
                });


    }
}