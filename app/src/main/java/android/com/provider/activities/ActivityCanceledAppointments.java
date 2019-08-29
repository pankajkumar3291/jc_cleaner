package android.com.provider.activities;
import android.com.provider.adapters.CancelAppointmentsAdapter;
import android.com.provider.apiResponses.cancelledJobApi.CancelledJob;
import android.com.provider.httpRetrofit.HttpModule;
import android.com.provider.locale_helper.LocaleHelper;
import android.com.provider15_nov_2018.R;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
public class ActivityCanceledAppointments extends AppCompatActivity {
    private ImageView backarr;
    private RecyclerView recyclerviewCancelAppointments;
    private CancelAppointmentsAdapter cancelAppointmentsAdapter;
    private NoInternetDialog noInternetDialog;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private TextView tvNoData;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canceled_appointments);
        noInternetDialog = new NoInternetDialog.Builder(this).build();
        findingIdsHere();
        setupList();
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }
    private void setupList() {
        compositeDisposable.add(HttpModule.provideRepositoryService().
                cancelledJob(Hawk.get("sp").equals(false) ? "en" : "es", String.valueOf(Hawk.get("savedUserId"))).
                subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<CancelledJob>() {
                    @Override
                    public void accept(CancelledJob cancelledJob) throws Exception {
                        if (cancelledJob != null && cancelledJob.getIsSuccess()) {
                            if (cancelledJob.getPayload() != null) {
                                if (cancelledJob.getPayload().size() > 0) {
                                    tvNoData.setVisibility(View.GONE);
                                    cancelAppointmentsAdapter = new CancelAppointmentsAdapter(ActivityCanceledAppointments.this, cancelledJob.getPayload());
                                    recyclerviewCancelAppointments.setHasFixedSize(true);
                                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                    mLayoutManager.setReverseLayout(true);
                                    mLayoutManager.setStackFromEnd(true);
                                    recyclerviewCancelAppointments.setLayoutManager(new LinearLayoutManager(ActivityCanceledAppointments.this));
                                    recyclerviewCancelAppointments.setAdapter(cancelAppointmentsAdapter);
                                } else {
                                    tvNoData.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            tvNoData.setVisibility(View.VISIBLE);
                            TastyToast.makeText(ActivityCanceledAppointments.this, Objects.requireNonNull(cancelledJob).getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        tvNoData.setVisibility(View.VISIBLE);
                        TastyToast.makeText(ActivityCanceledAppointments.this, throwable.toString(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                    }
                }));
    }
    private void findingIdsHere() {
        tvNoData = findViewById(R.id.noDataAvailable);
        backarr = findViewById(R.id.backarr);
        recyclerviewCancelAppointments = findViewById(R.id.recyclerviewCancelAppointments);
        backarr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
