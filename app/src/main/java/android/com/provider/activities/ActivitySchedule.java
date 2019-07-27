package android.com.provider.activities;

import android.com.provider.adapters.DailyAdapter;
import android.com.provider.models.Time;
import android.com.provider15_nov_2018.R;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

import am.appwise.components.ni.NoInternetDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class ActivitySchedule extends AppCompatActivity implements View.OnClickListener {


    public RecyclerView recyclerViewDaily, recyclerViewWeekly, recyclerViewMonthly;
    private CheckBox checkDaily, checkWeekly, checkMonthly;
    private TextView tvContinueBtn;
    private ImageView backArrowImage;


    List<Time> timeList;
    Context mCtx;


    private NoInternetDialog noInternetDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);


        noInternetDialog = new NoInternetDialog.Builder(this).build();

        getWindow().setStatusBarColor(ContextCompat.getColor(ActivitySchedule.this, R.color.statusBarColor));

        findingIdsHere();
        eventsListenerHere();

        checkBoxColorCheckedUnchecked();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onResume() {
        super.onResume();

        performOperationsHere();

    }

    private void checkBoxColorCheckedUnchecked() {

        ColorStateList colorStateList = new ColorStateList(

                new int[][]{
                        new int[]{-android.R.attr.state_checked}, // unchecked
                        new int[]{android.R.attr.state_checked}, // checked
                },
                new int[]{
                        Color.parseColor("#FFFFFF"),
                        Color.parseColor("#FF182391"),
                }
        );


        CompoundButtonCompat.setButtonTintList(checkDaily, colorStateList);
        CompoundButtonCompat.setButtonTintList(checkWeekly, colorStateList);
        CompoundButtonCompat.setButtonTintList(checkMonthly, colorStateList);


    }

    private void eventsListenerHere() {

        tvContinueBtn.setOnClickListener(this);
        backArrowImage.setOnClickListener(this);


    }

    private void performOperationsHere() {

        checkDaily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                checkBoxColorCheckedUnchecked();

                if (checkDaily.isChecked()) {

                    checkDaily.setTextColor(Color.parseColor("#FF182391"));
                    checkMonthly.setTextColor(Color.parseColor("#FFFFFF"));
                    checkWeekly.setTextColor(Color.parseColor("#FFFFFF"));
                    recyclerViewDaily.setVisibility(View.VISIBLE);
                    checkWeekly.setChecked(false);
                    checkMonthly.setChecked(false);

                    callDailyAdapterHere();

                } else {
                    recyclerViewDaily.setVisibility(View.GONE);
                    checkDaily.setTextColor(Color.parseColor("#FFFFFF"));

                }

            }
        });

        checkWeekly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                checkBoxColorCheckedUnchecked();

                if (checkWeekly.isChecked()) {

                    checkWeekly.setTextColor(Color.parseColor("#FF182391"));
                    checkDaily.setTextColor(Color.parseColor("#FFFFFF"));
                    recyclerViewWeekly.setVisibility(View.VISIBLE);
                    recyclerViewDaily.setVisibility(View.GONE);
                    TastyToast.makeText(ActivitySchedule.this, "checkWeekly.isChecked", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

                    checkDaily.setChecked(false);
                    checkMonthly.setChecked(false);


                    callWeeklyAdapterHere();


                } else {
                    recyclerViewWeekly.setVisibility(View.GONE);
                    recyclerViewDaily.setVisibility(View.GONE);
                    checkWeekly.setTextColor(Color.parseColor("#FFFFFF"));

                }

            }
        });


        checkMonthly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                checkBoxColorCheckedUnchecked();

                if (checkMonthly.isChecked()) {

                    checkMonthly.setTextColor(Color.parseColor("#FF182391"));
                    checkWeekly.setTextColor(Color.parseColor("#FFFFFF"));
                    checkDaily.setTextColor(Color.parseColor("#FFFFFF"));

                    recyclerViewMonthly.setVisibility(View.VISIBLE);
                    recyclerViewDaily.setVisibility(View.GONE);
                    recyclerViewWeekly.setVisibility(View.GONE);
                    TastyToast.makeText(ActivitySchedule.this, "checkMonthly.isChecked", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

                    checkDaily.setChecked(false);
                    checkWeekly.setChecked(false);


                } else {
                    recyclerViewMonthly.setVisibility(View.GONE);
                    recyclerViewDaily.setVisibility(View.GONE);
                    recyclerViewWeekly.setVisibility(View.GONE);
                    checkMonthly.setTextColor(Color.parseColor("#FFFFFF"));


                }

            }
        });


    }

    private void callWeeklyAdapterHere() {


        timeList = new ArrayList<>();

        timeList.add(new Time("Monday", "10:00AM"));
        timeList.add(new Time("Tuesday", "11:00AM"));
        timeList.add(new Time("Wednesday", "12:00PM"));
        timeList.add(new Time("Thursday", "1:00PM"));
        timeList.add(new Time("Friday", "2:00PM"));
        timeList.add(new Time("Satarday", "3:00PM"));
        timeList.add(new Time("Sunday", "Enjoy..!  we don't work"));

//
//        WeeklyAdapter weeklyAdapter = new WeeklyAdapter(ActivitySchedule.this, timeList);
        recyclerViewWeekly.setHasFixedSize(true);
        recyclerViewWeekly.setLayoutManager(new LinearLayoutManager(this));
//        recyclerViewWeekly.setAdapter(weeklyAdapter);


    }

    private void callDailyAdapterHere() {

        timeList = new ArrayList<>();

        timeList.add(new Time("", "10AM"));
        timeList.add(new Time("", "10AM"));
        timeList.add(new Time("", "10AM"));
        timeList.add(new Time("", "10AM"));
        timeList.add(new Time("", "10AM"));
        timeList.add(new Time("", "10AM"));
        timeList.add(new Time("", "10AM"));
        timeList.add(new Time("", "10AM"));
        timeList.add(new Time("", "10AM"));
//
        DailyAdapter adapter = new DailyAdapter(ActivitySchedule.this, timeList);
        recyclerViewDaily.setHasFixedSize(true);
        recyclerViewDaily.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDaily.setAdapter(adapter);


    }

    private void findingIdsHere() {

        checkDaily = findViewById(R.id.checkDaily);
        checkWeekly = findViewById(R.id.checkWeekly);
        checkMonthly = findViewById(R.id.checkMonthly);


        recyclerViewDaily = findViewById(R.id.recyclerViewDaily);
        recyclerViewWeekly = findViewById(R.id.recyclerViewWeekly);
        recyclerViewMonthly = findViewById(R.id.recyclerViewMonthly);


        tvContinueBtn = findViewById(R.id.tvContinueBtn);
        backArrowImage = findViewById(R.id.backArrowImage);


    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.tvContinueBtn:

                if (checkMonthly.isChecked() && !checkDaily.isChecked() && !checkWeekly.isChecked()) {

//                    Intent intent = new Intent(ActivitySchedule.this, ActivityScheduleMonthly.class);
//                    startActivity(intent);
                }

                break;


            case R.id.backArrowImage:
                finish();
                break;


        }


    }
}
