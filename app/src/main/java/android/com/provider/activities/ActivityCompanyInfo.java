package android.com.provider.activities;

import android.com.provider.adapters.CompanyInfoAdapter;
import android.com.provider.models.CompanyInfo;
import android.com.provider15_nov_2018.R;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import am.appwise.components.ni.NoInternetDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityCompanyInfo extends AppCompatActivity {


    private RecyclerView recyclerViewCompanyInfo;
    private ImageView backarr;

    private List<CompanyInfo> infoList;


    private NoInternetDialog noInternetDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_info);


        noInternetDialog = new NoInternetDialog.Builder(this).build();

        changingStatusBarColorHere();
        findingIdsHere();

        callingCompanyInfoAdapterHere();


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


    private void changingStatusBarColorHere() {
        getWindow().setStatusBarColor(ContextCompat.getColor(ActivityCompanyInfo.this, R.color.statusBarColor));
    }

    private void callingCompanyInfoAdapterHere() {


        infoList = new ArrayList<>();

        infoList.add(new CompanyInfo("About Company", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."));
        infoList.add(new CompanyInfo("Address & Timing", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."));
        infoList.add(new CompanyInfo("Phone", "+91-8860984985 , +91-9760113981"));
        infoList.add(new CompanyInfo("Certificate", "AMBS , BMBS"));


        CompanyInfoAdapter companyInfoAdapter = new CompanyInfoAdapter(ActivityCompanyInfo.this, infoList);
        recyclerViewCompanyInfo.setHasFixedSize(true);
        recyclerViewCompanyInfo.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCompanyInfo.setAdapter(companyInfoAdapter);


    }


    private void findingIdsHere() {

        recyclerViewCompanyInfo = findViewById(R.id.recyclerViewCompanyInfo);
        backarr = findViewById(R.id.backarr);


        backarr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


}
