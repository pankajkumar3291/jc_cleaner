package android.com.provider.activities;

import android.app.ProgressDialog;
import android.com.provider.apiResponses.getForgetPassword.ForgetPassword;
import android.com.provider.apiResponses.getMatchOTPApi.MatchOtp;
import android.com.provider.httpRetrofit.HttpModule;
import android.com.provider15_nov_2018.R;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
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

public class ActivityOTP extends AppCompatActivity implements View.OnClickListener {


    private EditText ed1, ed2, ed3, ed4;
    private TextView tvSend, tvResendOtp;
    private ImageView backarr;
    ProgressDialog mProgressDialog;
    private NoInternetDialog noInternetDialog;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        noInternetDialog = new NoInternetDialog.Builder(this).build();

        Hawk.init(this).build();
        getWindow().setStatusBarColor(ContextCompat.getColor(ActivityOTP.this, R.color.statusBarColor));

        findingIdsHere();
        eventListenerHere();
        changingFocusOfEdittextFromHere();

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


    private void changingFocusOfEdittextFromHere() {


        ed1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void afterTextChanged(Editable s) {


                if (s.length() == 1) {
                    ed2.requestFocus();

                } else {
                    ed1.requestFocus();
                }
            }

        });


        ed2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                if (s.length() == 1) {
                    ed3.requestFocus();

                } else {
                    ed1.requestFocus();
                }

            }
        });


        ed3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 1) {
                    ed4.requestFocus();

                } else {
                    ed2.requestFocus();
                }


            }
        });


        ed4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 1) {
                    ed4.requestFocus();

                } else {
                    ed3.requestFocus();
                }
            }
        });
    }

    private void eventListenerHere() {

        tvSend.setOnClickListener(this);
        tvResendOtp.setOnClickListener(this);

    }

    private void findingIdsHere() {

        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);

        ed1 = findViewById(R.id.ed1);
        ed2 = findViewById(R.id.ed2);
        ed3 = findViewById(R.id.ed3);
        ed4 = findViewById(R.id.ed4);


        tvSend = findViewById(R.id.tvSend);
        tvResendOtp = findViewById(R.id.tvResendOtp);

        backarr = findViewById(R.id.backarr);

        tvResendOtp.setPaintFlags(tvResendOtp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {


            case R.id.tvSend:

                callingMatchOtpApiHere();


                break;


            case R.id.tvResendOtp:

                loginProgressing();
                resendOtpCode();
                ed1.getText().clear();
                ed2.getText().clear();
                ed3.getText().clear();
                ed4.getText().clear();


                break;


            case R.id.backarr:
                finish();
                break;
        }
    }
    private void loginProgressing() {

        mProgressDialog.setMessage("Waiting..");
//        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();

    }
    private void resendOtpCode() {

        compositeDisposable.add(HttpModule.provideRepositoryService().
                getForgetPasswordAPI(String.valueOf(Hawk.get("ED_FORGET_PASSWORD"))).
                subscribeOn(io.reactivex.schedulers.Schedulers.computation()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<ForgetPassword>() {
                    @Override
                    public void accept(ForgetPassword forgetPassword) throws Exception {

                        if (forgetPassword != null && forgetPassword.getIsSuccess()) {
                            mProgressDialog.dismiss();
                            TastyToast.makeText(ActivityOTP.this, "Sending you the code again", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                        } else {
                            TastyToast.makeText(ActivityOTP.this, "Not able to send the code again", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mProgressDialog.dismiss();
                        TastyToast.makeText(ActivityOTP.this, throwable.toString(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                    }
                }));
    }

    private void callingMatchOtpApiHere() {
        mProgressDialog.setMessage("Waiting..");
//        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
        compositeDisposable.add(HttpModule.provideRepositoryService().
                getMatchOtpAPI(String.valueOf(Hawk.get("savedUserId")), ed1.getText().toString() + ed2.getText().toString() + ed3.getText().toString() + ed4.getText().toString()).
                subscribeOn(io.reactivex.schedulers.Schedulers.computation()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<MatchOtp>() {
                    @Override
                    public void accept(MatchOtp matchOtp) throws Exception {
                        mProgressDialog.dismiss();
                        if (matchOtp != null && matchOtp.getIsSuccess()) {
                            Hawk.delete("savedUserId");
                            Toast.makeText(ActivityOTP.this, matchOtp.getMessage(),Toast.LENGTH_SHORT).show();
                            callResetPasswordActivityFromHere();
                        } else {
                            Toast.makeText(ActivityOTP.this, Objects.requireNonNull(matchOtp).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mProgressDialog.dismiss();
                        Toast.makeText(ActivityOTP.this, throwable.toString(),Toast.LENGTH_SHORT).show();
                    }
                }));
    }
    private void callResetPasswordActivityFromHere() {
        Intent intent = new Intent(ActivityOTP.this, ActivityResetPassword.class);
        startActivity(intent);
        finish();
    }
}
