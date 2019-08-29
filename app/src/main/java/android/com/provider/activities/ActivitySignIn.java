package android.com.provider.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.com.provider.apiResponses.getSiginApi.SignIn;
import android.com.provider.httpRetrofit.HttpModule;
import android.com.provider.rxUtils.AppConstant;
import android.com.provider.sharedPreferenceHelper.SharedPrefsHelper;
import android.com.provider15_nov_2018.R;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;


import java.util.List;
import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class ActivitySignIn extends AppCompatActivity implements View.OnClickListener, Validator.ValidationListener {


    private ImageView backArrowImage;

    @NotEmpty(message = "Please enter the Phone/Email")
    private EditText edEmailPhone;

//    @NotEmpty
//    @Password(message = "Please enter the valid password", min = 6)
//    //,scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS
    private EditText edPassword;

    private CheckBox chKeemMeLoggedIn;
    private TextView loginBtn, btnDontHave;

    private RelativeLayout relativeDontHaveAnAccount;
    private TextView tvForgetPass;

    private Context context;
    String emailValue, passwordValue, keep_me_logged_in;

    Validator validator;
    ProgressDialog mProgressDialog;


    private NoInternetDialog noInternetDialog;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    SharedPrefsHelper preferences;

    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private TextView tvToastError, tvClose, tvToast;
    private TextView tvYes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_new);


        noInternetDialog = new NoInternetDialog.Builder(this).build();

        context = this;
        Hawk.init(this).build();


        findingIdsHere();

        initializingValidationHere();

        eventListener();

        changingStatusBarColorHere();


        // MARK : REMEMBER ME OR KEEP ME LOGGED IN

        if (Hawk.get("afterLogout", false)) {

            chKeemMeLoggedIn.setChecked(true);
            edEmailPhone.setText((CharSequence) Hawk.get("email_phone_saved"));
            edPassword.setText((CharSequence) Hawk.get("password_saved"));


        } else {


            chKeemMeLoggedIn.setChecked(false);
            edEmailPhone.setText(null);
            edPassword.setText(null);

        }


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

    private void initializingValidationHere() {

        validator = new Validator(this);
        validator.setValidationListener(this);
    }


    private void setupWindowAnimations() {

//        Transition fade = TransitionInflater.from(this).inflateTransition(R.transition.slide_and_changebounds_sequential);
//        getWindow().setEnterTransition(fade);

    }

    private void callingActivity() {

        mProgressDialog.dismiss();
        Intent intent1 = new Intent(ActivitySignIn.this, DashboardActivity.class);
        intent1.putExtra("Uniqid", "ActivitySignIn");
        startActivity(intent1);
        finish();

    }

    private void changingStatusBarColorHere() {

        getWindow().setStatusBarColor(ContextCompat.getColor(ActivitySignIn.this, R.color.statusBarColor));
    }


    private void eventListener() {

        backArrowImage.setOnClickListener(this);

        tvForgetPass.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        relativeDontHaveAnAccount.setOnClickListener(this);
        btnDontHave.setOnClickListener(this);
    }

    private void findingIdsHere() {


        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        tvForgetPass = findViewById(R.id.tvForgetPass);
        backArrowImage = findViewById(R.id.backArrowImage);
        loginBtn = findViewById(R.id.loginBtn);

        edEmailPhone = findViewById(R.id.edEmailPhone);
        edPassword = findViewById(R.id.edPassword);
        chKeemMeLoggedIn = findViewById(R.id.chKeemMeLoggedIn);
        relativeDontHaveAnAccount = findViewById(R.id.relativeDontHaveAnAccount);
        btnDontHave = findViewById(R.id.btnDontHave);


        preferences = new SharedPrefsHelper(ActivitySignIn.this);

        TextInputLayout usernameTextObj = (TextInputLayout) findViewById(R.id.tilTwo);
        usernameTextObj.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/headingbrew.otf"));


    }


    @Override
    public void onClick(View view) {


        switch (view.getId()) {

            case R.id.backArrowImage:

                finish();
                break;


            case R.id.tvForgetPass:
                callingForgetPasswordActivityHere();
                break;


            case R.id.loginBtn:

                loginProgressing();
                validator.validate();


                break;

            case R.id.btnDontHave:

                Intent intent1 = new Intent(ActivitySignIn.this, ActivitySignUp.class);
                startActivity(intent1);

                break;

        }


    }

    private void loginProgressing() {

        mProgressDialog.setMessage("Login..");
//        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();

    }

    private void callingForgetPasswordActivityHere() {

        Intent intent = new Intent(ActivitySignIn.this, ActivityForgetPassword.class);
        startActivity(intent);

    }


    @Override
    public void onValidationSucceeded() {


        preferences.put(AppConstant.USER_NAME, edEmailPhone.getText().toString());
        preferences.put(AppConstant.USER_PASSWORD, edPassword.getText().toString());


        setupWindowAnimations();

        clearingTheEditTextEnteredValues();

        callingSignInApiHere();


    }

    private void callingSignInApiHere() {
        String tokenFromFirebase = FirebaseInstanceId.getInstance().getToken();
        compositeDisposable.add(HttpModule.provideRepositoryService().signInAPI(preferences.get(AppConstant.USER_NAME, null), preferences.get(AppConstant.USER_PASSWORD, null), tokenFromFirebase).
                subscribeOn(io.reactivex.schedulers.Schedulers.computation()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<SignIn>(){

                    @Override
                    public void accept(final SignIn signIn) throws Exception {

                        mProgressDialog.dismiss();

                        if (signIn != null && signIn.getIsSuccess()) {

                            if (chKeemMeLoggedIn.isChecked()) {
                                Hawk.put("keepMeLoggedInCheck", true);

                                Hawk.put("afterLogout", true);
                            }

                            Hawk.put("email_phone_saved", edEmailPhone.getText().toString());
                            Hawk.put("password_saved", edPassword.getText().toString());
                            Hawk.put("USER_ID", signIn.getPayload().getUserId());
                            showTheDialogMessageForOk(signIn.getMessage());
                            Hawk.put("savedUserId", signIn.getPayload().getUserId());

                        } else {
                            mProgressDialog.dismiss();
                            showTheDialogMessageForError(signIn.getMessage());

                        }


                    }

                }, new Consumer<Throwable>() {

                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        mProgressDialog.dismiss();
                        TastyToast.makeText(ActivitySignIn.this, throwable.toString(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                        System.out.println("ActivitySignIn.accept " + throwable.toString());


                    }
                }));


    }


    // MARK: DIALOG MESSAGE FOR ERROR
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


    // MARK: DIALOG MESSAGE FOR OK

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


                callTheDashboardActivity();
                alertDialog.dismiss();
            }
        });
        tvToast.setText(message);

    }

    private void callTheDashboardActivity() {
        Intent intent1 = new Intent(ActivitySignIn.this, DashboardActivity.class);
        startActivity(intent1);
        finish();
    }


    private void clearingTheEditTextEnteredValues() {

        edEmailPhone.setError(null);
        edPassword.setError(null);

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


            }
        }


    }

}
