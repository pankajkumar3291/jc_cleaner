package android.com.provider.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.com.provider.apiResponses.getForgetPassword.ForgetPassword;
import android.com.provider.httpRetrofit.HttpModule;
import android.com.provider15_nov_2018.R;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.List;
import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityForgetPassword extends AppCompatActivity implements View.OnClickListener, Validator.ValidationListener {


    @NotEmpty
    @Email(message = "Please enter the valid email")
    private EditText edForgetPassword;

    private TextView btnSend;
    private ImageView backArrowImage;
    Validator validator;
    ProgressDialog mProgressDialog;

    private NoInternetDialog noInternetDialog;


    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;


    private TextView tvToastError, tvClose, tvToast;
    private TextView tvYes;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);


        noInternetDialog = new NoInternetDialog.Builder(this).build();


        findingIdsHere();
        eventsListener();
        changingStatusBarColorHere();

        validator = new Validator(this);
        validator.setValidationListener(this);

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


    private void eventsListener() {
        btnSend.setOnClickListener(this);
        backArrowImage.setOnClickListener(this);

    }


    @Override
    protected void onResume() {
        super.onResume();

        performOperationsHere();
    }

    private void performOperationsHere() {


    }

    private void findingIdsHere() {

        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);


        edForgetPassword = findViewById(R.id.edForgetPassword);
        btnSend = findViewById(R.id.btnSend);
        backArrowImage = findViewById(R.id.backArrowImage);


    }

    private void changingStatusBarColorHere() {
        getWindow().setStatusBarColor(ContextCompat.getColor(ActivityForgetPassword.this, R.color.statusBarColor));

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.btnSend:
                loginProgressing();
                validator.validate();

                break;

            case R.id.backArrowImage:
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

    @Override
    public void onValidationSucceeded() {

        clearingTheEdittextHere();
        savingValuesInHawk();
        gettingValuesFromHawk();


        callTheForgetPasswordApiFromHere();


    }

    private void callTheForgetPasswordApiFromHere() {


        compositeDisposable.add(HttpModule.provideRepositoryService().
                getForgetPasswordAPI(edForgetPassword.getText().toString()).
                subscribeOn(io.reactivex.schedulers.Schedulers.computation()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<ForgetPassword>() {


                    @Override
                    public void accept(ForgetPassword forgetPassword) throws Exception {

                        if (forgetPassword != null && forgetPassword.getIsSuccess()) {

                            mProgressDialog.dismiss();

                            Hawk.put("ED_FORGET_PASSWORD", edForgetPassword.getText().toString());
                            showTheDialogMessageForOk(forgetPassword.getMessage());


                        } else {

                            mProgressDialog.dismiss();
                            showTheDialogMessageForError(forgetPassword.getMessage());
                        }

                    }

                }, new Consumer<Throwable>() {

                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        System.out.println("ActivityForgetPassword.accept " + throwable.toString());
                        mProgressDialog.dismiss();
                        TastyToast.makeText(ActivityForgetPassword.this, throwable.toString(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();

                    }

                }));


    }

    private void showTheDialogMessageForError(String message) {

        LayoutInflater li = LayoutInflater.from(this);
        View dialogView = li.inflate(R.layout.dialog_show_for_message_error, null);


        findingIdsForError(dialogView, message);


        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder
                .setCancelable(false);
        alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();


    }

    private void findingIdsForError(View dialogView, String message) {

        tvToastError = dialogView.findViewById(R.id.tvToastError);
        tvClose = dialogView.findViewById(R.id.tvClose);

        tvToastError.setText(message);
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
            }
        });

    }

    private void showTheDialogMessageForOk(String message) {

        LayoutInflater li = LayoutInflater.from(this);
        View dialogView = li.inflate(R.layout.dialog_show_for_message_ok, null);


        findingDialogOkIds(dialogView, message);


        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder
                .setCancelable(false);
        alertDialog = alertDialogBuilder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();


    }

    private void findingDialogOkIds(View dialogView, String message) {

        tvToast = dialogView.findViewById(R.id.tvToast);
        tvYes = dialogView.findViewById(R.id.tvYes);
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                callOtpActivityFromHere();
                alertDialog.dismiss();
            }
        });
        tvToast.setText(message);

    }


    private void callOtpActivityFromHere() {

        Intent intent = new Intent(this, ActivityOTP.class);
        startActivity(intent);


    }

    private void clearingTheEdittextHere() {

        edForgetPassword.setError(null);

    }

    private void gettingValuesFromHawk() {
        String edPassword = Hawk.get("ED_FORGET_PASSWORD");
    }

    private void savingValuesInHawk() {
        Hawk.put("ED_FORGET_PASSWORD", edForgetPassword.getText().toString());
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        mProgressDialog.dismiss();

//        Toast.makeText(this, errors + "", Toast.LENGTH_LONG).show();

        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);


            if (view instanceof EditText) {  // Display error messages

                ((EditText) view).setError(message);

            } else {
//                Toast.makeText(ActivityForgetPassword.this, message, Toast.LENGTH_LONG).show();
            }
        }

    }
}
