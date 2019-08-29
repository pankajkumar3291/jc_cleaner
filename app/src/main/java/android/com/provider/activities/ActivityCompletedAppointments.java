package android.com.provider.activities;
import android.com.provider.adapters.CompletedAppointmentsAdapter;
import android.com.provider.apiResponses.cancelledJobApi.CancelledJob;
import android.com.provider.apiResponses.completedJobApiResposne.CompletedJobs;
import android.com.provider.httpRetrofit.HttpModule;
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
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
public class ActivityCompletedAppointments extends AppCompatActivity {
    private ImageView backarr;
    private RecyclerView recycler;
    private CompletedAppointmentsAdapter adapter;
    private TextView tvNoData;
    private Context context;
    private NoInternetDialog noInternetDialog;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_completed_appointments);
        noInternetDialog = new NoInternetDialog.Builder(this).build();
        findingIdsHere();
        callTheAdapterHere();
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
    private void callTheAdapterHere() {
        compositeDisposable.add(HttpModule.provideRepositoryService().completedJob(Hawk.get("sp").equals(true) ? "es" : "es", String.valueOf(Hawk.get("savedUserId"))).
                subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<CompletedJobs>() {
                    @Override
                    public void accept(CompletedJobs completedJobs) throws Exception {
                        if (completedJobs != null && completedJobs.getIsSuccess()) {
                            if (completedJobs.getPayload() != null && completedJobs.getPayload().size() > 0) {
                                adapter = new CompletedAppointmentsAdapter(ActivityCompletedAppointments.this, completedJobs.getPayload());
                                recycler.setHasFixedSize(true);
                                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                mLayoutManager.setReverseLayout(true);
                                mLayoutManager.setStackFromEnd(true);
                                recycler.setLayoutManager(mLayoutManager);
                                recycler.setAdapter(adapter);
                                tvNoData.setVisibility(View.GONE);
                            } else {
                                tvNoData.setVisibility(View.VISIBLE);
                            }
                        } else {
                            tvNoData.setVisibility(View.VISIBLE);
                            Toast.makeText(ActivityCompletedAppointments.this, Objects.requireNonNull(completedJobs).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        tvNoData.setVisibility(View.VISIBLE);
//                        TastyToast.makeText(ActivityCompletedAppointments.this, throwable.toString(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                    }
                }));
    }
    private void findingIdsHere() {
        tvNoData = findViewById(R.id.noDataAvailable);
        backarr = findViewById(R.id.backarr);
        recycler = findViewById(R.id.recycler);
        backarr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
