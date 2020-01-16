package android.com.provider.activities;
import android.app.ProgressDialog;
import android.com.provider.apiResponses.checkInRequest.ProviderCheckInRequest;
import android.com.provider.apiResponses.checkInRequest.ProviderCheckOutRequest;
import android.com.provider.apiResponses.getSiginApi.SignIn;
import android.com.provider.httpRetrofit.HttpModule;
import android.com.provider.rxUtils.AppConstant;
import android.com.provider15_nov_2018.R;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import am.appwise.components.ni.NoInternetDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
public class ActivityCurrentAppointments extends AppCompatActivity {
    private ImageView backarr, imgCheckIn, imgCheckOut;
    private NoInternetDialog noInternetDialog;
    String title, address, profilePic, date, time, jobId;
    private TextView tvUserName, tvUserAddress, tvDate, tvTime;
    private CircleImageView profileImage;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_appointments);
        noInternetDialog = new NoInternetDialog.Builder(this).build();
        progressDialog = new ProgressDialog(ActivityCurrentAppointments.this, R.style.AppTheme_Dark_Dialog);
        if (getIntent().hasExtra("Title") && getIntent().hasExtra("Address") && getIntent().hasExtra("Profile")
                && getIntent().hasExtra("Date") && getIntent().hasExtra("Time") && getIntent().hasExtra("jobId")) {
            title = getIntent().getStringExtra("Title");
            address = getIntent().getStringExtra("Address");
            profilePic = getIntent().getStringExtra("Profile");
            date = getIntent().getStringExtra("Date");
            time = getIntent().getStringExtra("Time");
            jobId = String.valueOf(getIntent().getIntExtra("jobId", 0));
        }
        findingIdsHere();
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
    private void findingIdsHere() {
        imgCheckIn = findViewById(R.id.imageCallToCleaner);
        imgCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Wait");
                progressDialog.setCancelable(false);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String date = df.format(Calendar.getInstance().getTime());
                SimpleDateFormat df1 = new SimpleDateFormat("HH:mm:ss");
                String time = df1.format(Calendar.getInstance().getTime());
                try {
                    compositeDisposable.add(HttpModule.provideRepositoryService().checkInApi(jobId, String.valueOf(Hawk.get("savedUserId")), date, time).
                            subscribeOn(io.reactivex.schedulers.Schedulers.computation()).
                            observeOn(AndroidSchedulers.mainThread()).
                            subscribe(new Consumer<ProviderCheckInRequest>() {
                                @Override
                                public void accept(final ProviderCheckInRequest providerCheckInRequest) throws Exception {
                                    progressDialog.dismiss();
                                    if (providerCheckInRequest != null){
                                        if (providerCheckInRequest.getIsSuccess()) {
                                            Toast.makeText(ActivityCurrentAppointments.this, providerCheckInRequest.getMessage(), Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(ActivityCurrentAppointments.this, providerCheckInRequest.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    progressDialog.dismiss();
                                }
                            }));
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Exception :" + e.getMessage());
                }
            }
        });
        imgCheckOut = findViewById(R.id.imageMessageToCleaner);
        imgCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCheckOutApi();
            }
        });
        backarr = findViewById(R.id.backarr);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserAddress = findViewById(R.id.tvUserAddress);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        profileImage = findViewById(R.id.profileImage);
        tvUserName.setText(title);
        tvUserAddress.setText(address);
        tvDate.setText(date);
        tvTime.setText(time);
//        tvDate.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.date_img), null, null, null);
        Picasso.get().load(profilePic).into(profileImage);
        backarr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                finish();
            }
        });
    }
    private void callCheckOutApi(){
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String date = df.format(Calendar.getInstance().getTime());
        SimpleDateFormat df1 = new SimpleDateFormat("HH:mm:ss");
        String time = df1.format(Calendar.getInstance().getTime());
        try {
            Date previousDate = df1.parse(tvTime.getText().toString());
            Date curentDate = df1.parse(time);
            if (curentDate.after(previousDate)) {
               progressDialog.setMessage("Wait");
                   progressDialog.setCancelable(false);
                   progressDialog.setIndeterminate(true);
                   progressDialog.show();
                   callCheckout();

            } else {
                Toast.makeText(ActivityCurrentAppointments.this, "Please check the appointment time ,the complete time must be greater than appointment start time", Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    private void callCheckout() {
        try {
            compositeDisposable.add(HttpModule.provideRepositoryService().checkOutApi(jobId, String.valueOf(Hawk.get("savedUserId")), date, time).
                    subscribeOn(io.reactivex.schedulers.Schedulers.computation()).
                    observeOn(AndroidSchedulers.mainThread()).
                    subscribe(new Consumer<ProviderCheckOutRequest>() {
                        @Override
                        public void accept(final ProviderCheckOutRequest providerCheckInRequest) throws Exception {
                            progressDialog.dismiss();
                            if (providerCheckInRequest != null) {
                                if (providerCheckInRequest.getIsSuccess()) {
                                    Toast.makeText(ActivityCurrentAppointments.this, providerCheckInRequest.getMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ActivityCurrentAppointments.this, providerCheckInRequest.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            progressDialog.dismiss();
                        }
                    }));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception :" + e.getMessage());
        }
    }
}
