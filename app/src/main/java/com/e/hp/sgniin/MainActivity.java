package com.e.hp.sgniin;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.e.hp.sgniin.HelperClasses.adapterphone;
import com.e.hp.sgniin.HelperClasses.phonehelper;
import com.google.android.material.navigation.NavigationView;
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, InternetConnectivityListener, adapterphone.ListItemClickListener {






    RecyclerView phoneRecycler;
    RecyclerView.Adapter adapter;



    private InternetAvailabilityChecker mInternetAvailabilityChecker;

    ProgressDialog progressdialog;
    String msg = "NO Internet Connection";

    No_Internet customProgress;


    EditText ed_phone, ed_ref, ed_otp;
    String phone, ref, getPhone, getRef;
    Button register, register2;

    String getphone, studentid, scode, otp, vkey;

    String getOtp;

    /*intent values variable*/
    String user_phone_no, student_id, intent_scode, student_otp, user_vkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user_phone_no = getIntent().getExtras().getString("user_phone_no");
        student_id = getIntent().getExtras().getString("student_id");
        intent_scode = getIntent().getExtras().getString("scode");
        student_otp = getIntent().getExtras().getString("student_otp");
        user_vkey = getIntent().getExtras().getString("user_vkey");



        //Hooks
        phoneRecycler = findViewById(R.id.my_recycler);
        phoneRecycler();



        //custom progress instance
        customProgress = No_Internet.getInstance();

        //internet availability instance
        mInternetAvailabilityChecker = InternetAvailabilityChecker.getInstance();
        mInternetAvailabilityChecker.addInternetConnectivityListener(this);

        //networking libray intialization
        AndroidNetworking.initialize(getApplicationContext());

        /*navigation drawer*/
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

    }
    /*recycler view code*/
    private void phoneRecycler() {

        //All Gradients
              /*  GradientDrawable gradient2 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffd4cbe5, 0xffd4cbe5});
                GradientDrawable gradient1 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xff7adccf, 0xff7adccf});
                GradientDrawable gradient3 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xfff7c59f, 0xFFf7c59f});
                GradientDrawable gradient4 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffb8d7f5, 0xffb8d7f5});

*/
        phoneRecycler.setHasFixedSize(true);
        phoneRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<phonehelper> phonelocations = new ArrayList<>();
        phonelocations.add(new phonehelper(R.drawable.amritsar, "Amritsar"));
        phonelocations.add(new phonehelper(R.drawable.jalandhar, "Jalandhar"));
        phonelocations.add(new phonehelper(R.drawable.pathankot, "Pathankot"));
        phonelocations.add(new phonehelper(R.drawable.bathinda, "Bathinda"));
        phonelocations.add(new phonehelper(R.drawable.faridkot, "Faridkot"));
        phonelocations.add(new phonehelper(R.drawable.fatehgarh, "Fatehgarh"));
        phonelocations.add(new phonehelper(R.drawable.fazilka, "Fazilka"));
        phonelocations.add(new phonehelper(R.drawable.gurdaspur, "Gurdaspur"));
        phonelocations.add(new phonehelper(R.drawable.hoshiarpur, "Hoshiarpur"));
        phonelocations.add(new phonehelper(R.drawable.kapurthala, "Kapurthala"));
        phonelocations.add(new phonehelper(R.drawable.ludhiana, "Ludhiana"));
        phonelocations.add(new phonehelper(R.drawable.all, "All Cities"));

        adapter = new adapterphone(phonelocations, this);
        phoneRecycler.setAdapter(adapter);

    }

    @Override
    public void onphoneListClick(int clickedItemIndex) {

        Intent mIntent;
        switch (clickedItemIndex) {
            case 0: //first item in Recycler view
                mIntent = new Intent(MainActivity.this, Location_List.class);
                mIntent.putExtra("res_parent","6");
                startActivity(mIntent);
                break;
            case 1: //second item in Recycler view
                mIntent = new Intent(MainActivity.this, Location_List.class);
                mIntent.putExtra("res_parent","32");
                startActivity(mIntent);
                break;
            case 2: //third item in Recycler view
                mIntent = new Intent(MainActivity.this, Location_List.class);
                mIntent.putExtra("res_parent","34");
                startActivity(mIntent);
                break;
            case 3: //fourth item in Recycler view
                mIntent = new Intent(MainActivity.this, Location_List.class);
                mIntent.putExtra("res_parent","35");
                startActivity(mIntent);
                break;
            case 4: //fifth item in Recycler view
                mIntent = new Intent(MainActivity.this, Location_List.class);
                mIntent.putExtra("res_parent","37");
                startActivity(mIntent);
                break;
            case 5: //sixth item in Recycler view
                mIntent = new Intent(MainActivity.this, Location_List.class);
                mIntent.putExtra("res_parent","350");
                startActivity(mIntent);
                break;
            case 6: //seventh item in Recycler view
                mIntent = new Intent(MainActivity.this, Location_List.class);
                mIntent.putExtra("res_parent","39");
                startActivity(mIntent);
                break;
            case 7: //eighth item in Recycler view
                mIntent = new Intent(MainActivity.this, Location_List.class);
                mIntent.putExtra("res_parent","40");
                startActivity(mIntent);
                break;
            case 8: //nineth item in Recycler view
                mIntent = new Intent(MainActivity.this, Location_List.class);
                mIntent.putExtra("res_parent","41");
                startActivity(mIntent);
                break;
            case 9: //tenth item in Recycler view
                mIntent = new Intent(MainActivity.this, Location_List.class);
                mIntent.putExtra("res_parent","42");
                startActivity(mIntent);
                break;
            case 10: //eleventh item in Recycler view
                mIntent = new Intent(MainActivity.this, Location_List.class);
                mIntent.putExtra("res_parent","43");
                startActivity(mIntent);
                break;

            case 11: //All cities item in Recycler view
                mIntent = new Intent(MainActivity.this, CitiesListView.class);
                startActivity(mIntent);
                break;


        }


    }





















    @Override
    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            AlertDialog.Builder builder;

            builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Do you want to close this application ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //  Action for 'NO' Button
                            dialog.cancel();
                        }
                    });
            //Creating dialog box
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle("SGNI.in Termination");
            alert.show();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {

            Toast.makeText(this, "Action logout clicked", Toast.LENGTH_SHORT).show();
           /*
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/open?id=19bKIjmuoQEgeIw6Jx6aJWmuZZrSwVIEl"));
            startActivity(i);*/
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //privacy policy action
        if (id == R.id.nav_profile) {

            Toast.makeText(this, "nav_profile", Toast.LENGTH_SHORT).show();

         /*   Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/open?id=19bKIjmuoQEgeIw6Jx6aJWmuZZrSwVIEl"));
            startActivity(i);*/


        } else if (id == R.id.nav_wallet) {

            Toast.makeText(this, "nav_wallet", Toast.LENGTH_SHORT).show();

         /*   Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/open?id=19bKIjmuoQEgeIw6Jx6aJWmuZZrSwVIEl"));
            startActivity(i);*/
        } else if (id == R.id.nav_booking) {

            Toast.makeText(this, "nav_booking", Toast.LENGTH_SHORT).show();

         /*   Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/open?id=19bKIjmuoQEgeIw6Jx6aJWmuZZrSwVIEl"));
            startActivity(i);*/
        } else if (id == R.id.nav_terms) {

            Toast.makeText(this, "nav_terms", Toast.LENGTH_SHORT).show();

         /*   Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/open?id=19bKIjmuoQEgeIw6Jx6aJWmuZZrSwVIEl"));
            startActivity(i);*/
        } else if (id == R.id.nav_review) {

            Toast.makeText(this, "Shanav_reviewre", Toast.LENGTH_SHORT).show();
          /*  try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Sidhu Moosewala Songs (Direct Download)");
                String shareMessage = "Download Official App of\nSidhu Moosewala Songs (Direct Download)\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch (Exception e) {
                //e.toString();
            }
*/
        } else if (id == R.id.nav_share) {
            Toast.makeText(this, "nav_share", Toast.LENGTH_SHORT).show();
            /*Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getApplicationContext().getPackageName()));
            startActivity(rateIntent);
*/
        } else if (id == R.id.nav_fb) {
            Toast.makeText(this, "nav_fb", Toast.LENGTH_SHORT).show();
         /*   Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getApplicationContext().getPackageName()));
            startActivity(rateIntent);*/

        } else if (id == R.id.nav_insta) {
            Toast.makeText(this, "nav_insta", Toast.LENGTH_SHORT).show();
           /* String url = "https://www.gamezop.com/?id=JTpZLc8pR";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);*/

        } else if (id == R.id.nav_linkedin) {
            Toast.makeText(this, "nav_linkedin", Toast.LENGTH_SHORT).show();
           /* String url = "https://www.gamezop.com/?id=JTpZLc8pR";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);*/

        } else if (id == R.id.nav_twitter) {
            Toast.makeText(this, "nav_twitter", Toast.LENGTH_SHORT).show();


        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //check-internet
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mInternetAvailabilityChecker.removeInternetConnectivityChangeListener(this);
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        if (isConnected) {

            customProgress.hideProgress();
        } else {
            customProgress.showProgress(MainActivity.this, msg, false);
        }
    }
}