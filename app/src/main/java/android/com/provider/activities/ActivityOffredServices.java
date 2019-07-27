package android.com.provider.activities;

import android.app.ProgressDialog;
import android.com.provider.adapters.OffredServicesAdapter;
import android.com.provider.apiResponses.getOffredServices.OffrredServices;
import android.com.provider.httpRetrofit.HttpModule;
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

import com.sdsmdg.tastytoast.TastyToast;


import am.appwise.components.ni.NoInternetDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityOffredServices extends AppCompatActivity {


    private ImageView backarr;
    private RecyclerView recyclerViewOffredServices;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private NoInternetDialog noInternetDialog;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offred_services);


        noInternetDialog = new NoInternetDialog.Builder(this).build();
        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        mProgressDialog.setMessage("Waiting..");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();

        getWindow().setStatusBarColor(ContextCompat.getColor(ActivityOffredServices.this, R.color.statusBarColor));


        findingIdsHere();

        callingAdapterHere();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
        compositeDisposable.dispose();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void callingAdapterHere() {


        compositeDisposable.add(HttpModule.provideRepositoryService().
                getOffrredServicesAPI().
                subscribeOn(io.reactivex.schedulers.Schedulers.computation()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<OffrredServices>() {


                    @Override
                    public void accept(OffrredServices offrredServices) throws Exception {

                        mProgressDialog.dismiss();

                        if (offrredServices != null && offrredServices.getIsSuccess()) {

                            OffredServicesAdapter offredServicesAdapter = new OffredServicesAdapter(ActivityOffredServices.this, offrredServices.getPayload());
                            recyclerViewOffredServices.setHasFixedSize(true);
                            recyclerViewOffredServices.setLayoutManager(new LinearLayoutManager(ActivityOffredServices.this));
                            recyclerViewOffredServices.setAdapter(offredServicesAdapter);


                        } else {

                            TastyToast.makeText(ActivityOffredServices.this, offrredServices.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        }

                    }

                }, new Consumer<Throwable>() {

                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        mProgressDialog.dismiss();
                        TastyToast.makeText(ActivityOffredServices.this, throwable.toString(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();

                    }

                }));


    }


    private void findingIdsHere() {

        backarr = findViewById(R.id.backarr);
        recyclerViewOffredServices = findViewById(R.id.recyclerViewOffredServices);

        backarr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

    }


}
