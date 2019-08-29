package android.com.provider.activities;
import android.app.NotificationManager;
import android.com.provider.adapters.OpneApportunityAdapter;
import android.com.provider.applicationUtils.App;
import android.com.provider.models.OpenApportunity;
import android.com.provider15_nov_2018.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.com.provider.applicationUtils.App.listOfValues;
public class ActivityOpenApportunity extends AppCompatActivity {
    private RecyclerView recyclerviewOpenApportunity;
    private ImageView backarr;
    private OpneApportunityAdapter opneApportunityAdapter;
    private TextView tvNoData;
    private List<OpenApportunity> apportunityList;
    private NoInternetDialog noInternetDialog;
    private String title, services, address, date, time, jobId, customer_id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_apportunity);
        noInternetDialog = new NoInternetDialog.Builder(this).build();
        findingIdsHere();
        ((App) getApplicationContext()).getRxBus().toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<OpenApportunity>() {
            @Override
            public void onSubscribe(Disposable d) {

            }
            @Override
            public void onNext(OpenApportunity openApportunity) {
                Log.d("THREADNAME", Thread.currentThread().getName());
//                opneApportunityAdapter.notifyDataSetChanged();
                if (listOfValues != null && listOfValues.size() > 0){
                    System.out.println("ActivityOpenApportunity.onCreate " + listOfValues.size());
                    callTheOpenApportunityAdapter(listOfValues);
                    recyclerviewOpenApportunity.setVisibility(View.VISIBLE);
                    tvNoData.setVisibility(View.GONE);
                } else {
//            callTheOpenApportunityAdapter(listOfValues);
                    tvNoData.setVisibility(View.VISIBLE);
                    Toast.makeText(ActivityOpenApportunity.this, "No data available", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
            @Override
            public void onComplete() {
            }
        });
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        if (listOfValues != null && listOfValues.size() > 0) {
            System.out.println("ActivityOpenApportunity.onCreate "+listOfValues.size());
            callTheOpenApportunityAdapter(listOfValues);
            recyclerviewOpenApportunity.setVisibility(View.VISIBLE);
            tvNoData.setVisibility(View.GONE);
        } else {
//            callTheOpenApportunityAdapter(listOfValues);
            tvNoData.setVisibility(View.VISIBLE);
            Toast.makeText(ActivityOpenApportunity.this, "No data available", Toast.LENGTH_SHORT).show();
        }
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
    private void callTheOpenApportunityAdapter(ArrayList<OpenApportunity> test) {
//        apportunityList = new ArrayList<>();
//        apportunityList.add((new OpenApportunity(title, services, address, date, time, jobId, customer_id)));
        opneApportunityAdapter = new OpneApportunityAdapter(this, test);
        recyclerviewOpenApportunity.setHasFixedSize(true);
        recyclerviewOpenApportunity.setLayoutManager(new LinearLayoutManager(this));
        recyclerviewOpenApportunity.setAdapter(opneApportunityAdapter);
    }
    private void findingIdsHere() {
        tvNoData = findViewById(R.id.noDataAvailable);
        recyclerviewOpenApportunity = findViewById(R.id.recyclerviewOpenApportunity);
        backarr = findViewById(R.id.backarr);
        backarr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
//                Intent intent = new Intent(ActivityOpenApportunity.this, DashboardActivity.class);
//                startActivity(intent);
//                finish();
            }
        });
    }
}
