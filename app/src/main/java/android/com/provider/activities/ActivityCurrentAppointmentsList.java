package android.com.provider.activities;
import android.com.provider.adapters.CurrentAppointmentsListAdapter;
import android.com.provider.apiResponses.currentJobApiResponse.CurrentJob;
import android.com.provider.httpRetrofit.HttpModule;
import android.com.provider.models.CurrentAppointmentList;
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

import java.util.List;
import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
public class ActivityCurrentAppointmentsList extends AppCompatActivity {
    private ImageView backarr;
    private RecyclerView recyclerviewCurrentAppList;
    private CurrentAppointmentsListAdapter adapter;
    private List<CurrentAppointmentList> list;
    private NoInternetDialog noInternetDialog;
    private String services, address, date, time, job_id, customer_id;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private TextView tvNoData;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_appointments_list);
        noInternetDialog = new NoInternetDialog.Builder(this).build();
        findingIdsHere();
        callTheAdapterHere();
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
    private void callTheAdapterHere() {
        compositeDisposable.add(HttpModule.provideRepositoryService().currentJob(Hawk.get("sp").equals(false) ? "en" : "es", String.valueOf(Hawk.get("savedUserId"))).
                subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<CurrentJob>() {
                    @Override
                    public void accept(CurrentJob currentJob) throws Exception {
                        if (currentJob != null && currentJob.getIsSuccess()) {
                            if (currentJob.getPayload() != null) {
                                if (currentJob.getPayload().size() > 0) {
                                    adapter = new CurrentAppointmentsListAdapter(ActivityCurrentAppointmentsList.this, currentJob.getPayload());
                                    recyclerviewCurrentAppList.setHasFixedSize(true);
                                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                    mLayoutManager.setReverseLayout(true);
                                    mLayoutManager.setStackFromEnd(true);
                                    recyclerviewCurrentAppList.setLayoutManager(mLayoutManager);
                                    recyclerviewCurrentAppList.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                    tvNoData.setVisibility(View.GONE);
                                } else {
                                    tvNoData.setVisibility(View.VISIBLE);
                                }
                            } else {
                                tvNoData.setVisibility(View.VISIBLE);
                            }
                        } else {
                            tvNoData.setVisibility(View.VISIBLE);
                            Toast.makeText(ActivityCurrentAppointmentsList.this, Objects.requireNonNull(currentJob).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        tvNoData.setVisibility(View.VISIBLE);
//                      Toast.makeText(ActivityCurrentAppointmentsList.this, throwable.toString(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                    }
                }));
    }
    private void findingIdsHere() {
        tvNoData = findViewById(R.id.noDataAvailable);
        backarr = findViewById(R.id.backarr);
        recyclerviewCurrentAppList = findViewById(R.id.recyclerviewCurrentAppList);
        backarr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
