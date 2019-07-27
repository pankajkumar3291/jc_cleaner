package android.com.provider.activities;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.com.provider.apiResponses.getCityListApi.CityList;
import android.com.provider.apiResponses.getServiceList.GetServiceList;
import android.com.provider.apiResponses.getStateListApi.GetStateList;
import android.com.provider.apiResponses.getStateListApi.Payload;
import android.com.provider.apiResponses.getZipCodeListApi.ZipCodeList;
import android.com.provider.apiResponses.signupApi.SignUp;
import android.com.provider.httpRetrofit.HttpModule;
import android.com.provider.rxUtils.AppConstant;
import android.com.provider15_nov_2018.R;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
public class ActivitySignUp extends AppCompatActivity implements View.OnClickListener, Validator.ValidationListener {
    private ImageView backArrowImage;
    @NotEmpty(message = "Please enter the name")
    public EditText edName;
    @NotEmpty(message = "Please enter the address")
    public EditText edAddress;
    @NotEmpty
    public EditText edPhoneNumber;
    @NotEmpty
    @Email(message = "Please enter the valid email")
    public EditText edEmail;
    @NotEmpty
    @Password(message = "Please enter the valid password")
    // , min = 6, scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS
    public EditText edPassword;
    @NotEmpty(message = "Please enter the city")
    public EditText edCity;
    @NotEmpty(message = "Please enter the zip code")
    public EditText edZipCode;
    private Spinner spinnerState, spinnerCity, spinnerZipCode, spinnerServices;
    private TextView tvAlreadyMember;
    private TextView SignUpBtn;
    private Context context;
    private Validator validator;
    private ProgressDialog mProgressDialog;
    private String android_DeviceID;
    private String selectedValueFromStateSpinnerIds, selectedValuesFromCitySpinner, getSelectedValuesFromZipCodeSpinner;
    private String selectedServicesId;
    private ArrayList<String> stateIdListPos = new ArrayList<>();
    private ArrayList<String> cityIdListPos = new ArrayList<>();
    private ArrayList<String> zipcodeIdListPos = new ArrayList<>();
    private ArrayList<String> serviceIdList = new ArrayList<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private NoInternetDialog noInternetDialog;
    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private TextView tvToastError, tvClose, tvToast;
    private TextView tvYes;
    private String selectedStateId = "";
    private String selectedCityId = "";
    private String mCountryHolder, mStateHolder, mCityHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_new);
        context = this;
        noInternetDialog = new NoInternetDialog.Builder(this).build();
        Hawk.init(this).build();
        findingIdsHere();
        initializingValidationHere();
        eventListener();
        changingStatusBarColorHere();
        spinnerOperationForState();
        spinnerOperationForService();
    }
    private void spinnerOperationForService(){
        HttpModule.provideRepositoryService().getServiceListAPI().enqueue(new Callback<GetServiceList>() {
            @Override
            public void onResponse(Call<GetServiceList> call, Response<GetServiceList> response) {
                if (response.body() != null && response.body().getIsSuccess()) {
                    spinnerSetupForServicesGoesHere(response.body().getPayload());
                } else {
//                    TastyToast.makeText(ActivitySignUp.this, response.body().getMessage(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                }
            }
            @Override
            public void onFailure(Call<GetServiceList> call, Throwable t) {
                TastyToast.makeText(ActivitySignUp.this, t.toString(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                System.out.println("ActivitySignUp.onFailure" + t);
            }
        });
    }
    private void spinnerSetupForServicesGoesHere(List<android.com.provider.apiResponses.getServiceList.Payload> payload) {
        ArrayList<String> servicesMainList = new ArrayList<>();
        servicesMainList.add("Select Services");
        serviceIdList.add("0");
        for (int x = 0; x < payload.size(); x++) {
            servicesMainList.add(payload.get(x).getName());
            serviceIdList.add(String.valueOf(payload.get(x).getId()));
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, servicesMainList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerServices.setDropDownWidth(1000);
        spinnerServices.setAdapter(dataAdapter);
        spinnerItemSelectionForServices();
    }
    private void spinnerItemSelectionForServices() {
        spinnerServices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedServicesId = serviceIdList.get(position);
//                TastyToast.makeText(getApplicationContext(), "You selected " + selectedServicesId, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                Hawk.put(AppConstant.SAVED_SERVICE_ID, selectedServicesId);
                Hawk.put("SELECTED_SERVICE", selectedServicesId);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                TastyToast.makeText(ActivitySignUp.this, parent + "", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
            }
        });
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    private void initializingValidationHere() {
        validator = new Validator(this);
        validator.setValidationListener(this);
    }
    private void spinnerOperationForState() {
        compositeDisposable.add(HttpModule.provideRepositoryService().getStateListAPI()
                .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetStateList>() {
                               @Override
                               public void accept(GetStateList getStateList) throws Exception {
                                   if (getStateList.getIsSuccess()) {
                                       spinnerSetupGoesHere(getStateList.getPayload());
                                   } else {
                                       TastyToast.makeText(ActivitySignUp.this, getStateList.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                                   }
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   System.out.println("ActivitySignUp.accept " + throwable);
                                   TastyToast.makeText(ActivitySignUp.this, String.valueOf(throwable), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                                   compositeDisposable.dispose();
                               }
                           }
                ));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
        noInternetDialog.onDestroy();
        compositeDisposable.dispose();
    }
    private void spinnerSetupGoesHere(List<Payload> payload) {
//        stateArraylist.add("Select State");
////        stateIdListPos.add("0");
        Payload[] array = new Payload[payload.size()];
        payload.toArray(array);
//        String compareValue = spinnerState.getSelectedItem().toString();
        SpinAdapter spinAdapter = new SpinAdapter(ActivitySignUp.this, android.R.layout.simple_spinner_item, array);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerState.setDropDownWidth(1000);
        spinnerState.setAdapter(spinAdapter);
        spinnerItemSelectionForState(spinAdapter);
    }
    private void spinnerItemSelectionForState(final SpinAdapter spinAdapter) {
        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Payload payload = spinAdapter.getItem(position);
                selectedValueFromStateSpinnerIds = String.valueOf(payload.getId());
                Hawk.put("SELECTED_SATATE", payload.getId());
                spinnerOperationForCities(payload.getId());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                TastyToast.makeText(ActivitySignUp.this, parent + "", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
            }
        });
    }
    private void spinnerOperationForCities(int selectedValueFromSpinnerIds) {
        compositeDisposable.add(HttpModule.provideRepositoryService().getCitiesAPI(String.valueOf(selectedValueFromSpinnerIds)).
                subscribeOn(io.reactivex.schedulers.Schedulers.computation()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<CityList>() {
                    @Override
                    public void accept(CityList cityList) throws Exception {
                        if (cityList != null && cityList.getIsSuccess()) {
                            spinnerSetupForCities(cityList.getPayload());
                        } else {
//                            TastyToast.makeText(ActivitySignUp.this, cityList.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("ActivitySignUp.accept " + throwable.toString());
                    }
                }));
    }
    // todo               for spinner setup of cities
    private void spinnerSetupForCities(List<Payload> payload) {
        Payload[] array = new Payload[payload.size()];
        payload.toArray(array);
//        String compareValue = spinnerState.getSelectedItem().toString();
        SpinAdapter spinAdapter = new SpinAdapter(ActivitySignUp.this, android.R.layout.simple_spinner_item, array);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setDropDownWidth(1000);
        spinnerCity.setAdapter(spinAdapter);
        spinnerItemSelectionForState(spinAdapter);
        spinnerItemSelectionForCity(spinAdapter);
    }
    private void spinnerItemSelectionForCity(final SpinAdapter spinAdapter) {
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Payload payload = spinAdapter.getItem(i);
                selectedValuesFromCitySpinner = String.valueOf(payload.getId());
                Hawk.put("SELECTED_CITY", payload.getId());
                spinnerOperationForZipcode(payload.getId());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    private void spinnerOperationForZipcode(int selectedValuesFromCitySpinner) {
        compositeDisposable.add(HttpModule.provideRepositoryService().getZipCodeListAPI(String.valueOf(selectedValuesFromCitySpinner)).
                subscribeOn(io.reactivex.schedulers.Schedulers.computation()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<ZipCodeList>() {
                    @Override
                    public void accept(ZipCodeList zipCodeList) throws Exception {
                        if (zipCodeList != null && zipCodeList.getIsSuccess()) {
                            spinnerSetupForZipcode(zipCodeList);
                        } else {
//                            TastyToast.makeText(ActivitySignUp.this, zipCodeList.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception{
                        System.out.println("ActivitySignUp.accept" + throwable.toString());
                        TastyToast.makeText(ActivitySignUp.this, throwable.toString(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                    }
                }));
    }
    //todo                      for spinner set up of zip code

    private void spinnerSetupForZipcode(ZipCodeList zipCodeList) {
        ArrayList<String> zipArrList = new ArrayList<>();
//        zipArrList.add("Select zipcode");
//        zipcodeIdListPos.add("0");
        for (int y = 0; y < zipCodeList.getPayload().size(); y++) {
            zipArrList.add(zipCodeList.getPayload().get(y).getZipcode());
            zipcodeIdListPos.add(String.valueOf(zipCodeList.getPayload().get(y).getCityId()));
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, zipArrList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerZipCode.setDropDownWidth(1000);
        spinnerZipCode.setAdapter(dataAdapter);
        spinnerItemSelectionForZipCode();
    }
    //todo                         for zip code
    private void spinnerItemSelectionForZipCode() {
        spinnerZipCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getSelectedValuesFromZipCodeSpinner = zipcodeIdListPos.get(i);
                Hawk.put("SELECTED_ZIPCODE", getSelectedValuesFromZipCodeSpinner);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    private void changingStatusBarColorHere() {
        getWindow().setStatusBarColor(ContextCompat.getColor(ActivitySignUp.this, R.color.statusBarColor));
    }
    private void eventListener() {
        backArrowImage.setOnClickListener(this);
        SignUpBtn.setOnClickListener(this);
        tvAlreadyMember.setOnClickListener(this);
    }
    private void findingIdsHere() {
        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        backArrowImage = findViewById(R.id.backArrowImage);
        edName = findViewById(R.id.edName);
        edAddress = findViewById(R.id.edAddress);
        edPhoneNumber = findViewById(R.id.edPhoneNumber);
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        edCity = findViewById(R.id.edCity);
        edZipCode = findViewById(R.id.edZipCode);
        SignUpBtn = findViewById(R.id.SignUpBtn);
        tvAlreadyMember = findViewById(R.id.tvAlreadyMember);
        spinnerState = findViewById(R.id.spinnerState);
        spinnerCity = findViewById(R.id.spinnerCity);
        spinnerZipCode = findViewById(R.id.spinnerZipCode);
        spinnerServices = findViewById(R.id.spinnerServices);
        TextInputLayout usernameTextObj = (TextInputLayout) findViewById(R.id.tilPass);
        usernameTextObj.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/headingbrew.otf"));
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backArrowImage:
                finish();
                break;
            case R.id.tvAlreadyMember:
                Intent intent = new Intent(ActivitySignUp.this, ActivitySignIn.class);
                startActivity(intent);
                finish();
                break;
            case R.id.SignUpBtn:
                validator.validate();
                break;
        }
    }
    private void loginProgressing() {
        mProgressDialog.setMessage("Signing up..");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
    }
    private void callingActivity() {
        Intent intent1 = new Intent(ActivitySignUp.this, ActivitySignIn.class);
        intent1.putExtra("Uniqid", "ActivitySignUp");
        startActivity(intent1);
        finish();
    }
    @Override
    public void onValidationSucceeded() {
        if (TextUtils.isEmpty(selectedValueFromStateSpinnerIds)) {
            Toast.makeText(ActivitySignUp.this, "State field is required", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(selectedValuesFromCitySpinner)) {
            Toast.makeText(ActivitySignUp.this, "City field is required", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(getSelectedValuesFromZipCodeSpinner)) {
            Toast.makeText(ActivitySignUp.this, "Zip code field is required", Toast.LENGTH_SHORT).show();
        } else if (spinnerServices.getSelectedItem() == null) {
            Toast.makeText(ActivitySignUp.this, " Service field is required", Toast.LENGTH_SHORT).show();
        } else if (spinnerServices.getSelectedItemPosition() == 0 || spinnerServices.getSelectedItem().toString().isEmpty()) {
            Toast.makeText(ActivitySignUp.this, " Service field is required", Toast.LENGTH_SHORT).show();
        } else {
            loginProgressing();
            callingSignUpApiHere();
        }
    }
    private void callingSignUpApiHere() {
        try {
            android_DeviceID = Settings.Secure.getString(ActivitySignUp.this.getContentResolver(), Settings.Secure.ANDROID_ID);
            String tokenFromFirebase = FirebaseInstanceId.getInstance().getToken();
            HttpModule.provideRepositoryService().signUpAPI(edName.getText().toString(), "Provider", edEmail.getText().toString(),
                    edPassword.getText().toString(), edPhoneNumber.getText().toString(), "United State",
                    selectedValueFromStateSpinnerIds, selectedValuesFromCitySpinner, getSelectedValuesFromZipCodeSpinner,
                    edAddress.getText().toString(), tokenFromFirebase, "A", "pr", selectedServicesId.equalsIgnoreCase("0") ? "" : selectedServicesId).enqueue(new Callback<SignUp>() {
                @Override
                public void onResponse(Call<SignUp> call, Response<SignUp> response) {
                    mProgressDialog.dismiss();
                    if (response.body() != null) {
                        if (response.body().getIsSuccess()) {
                            clearingTheEdittextHere();
                            String savedUserId = String.valueOf(response.body().getPayload().getUserId());
                            Hawk.put("savedUserId", savedUserId);
                            Hawk.put("emailId", edEmail.getText().toString());
                            savingEnteredValues();
                            showTheDialogMessageForOk(response.body().getMessage());
                        } else {
                            showTheDialogMessageForError(response.body().getMessage());
                        }
                    } else {
                        TastyToast.makeText(ActivitySignUp.this, "Something went wrong", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                    }
                }
                @Override
                public void onFailure(Call<SignUp> call, Throwable t) {
                    System.out.println("ActivitySignUp.onFailure " + t.getMessage());
                    Toast.makeText(ActivitySignUp.this, t.toString(), Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            mProgressDialog.dismiss();
        }
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
    // MARK: OK MESSAGE DIALOG
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
        Intent intent1 = new Intent(ActivitySignUp.this, ActivitySignIn.class);
//        intent1.putExtra("Uniqid", "ActivitySignUp");
        startActivity(intent1);
        finish();
    }
    private void clearingTheEdittextHere() {
        edName.setError(null);
        edAddress.setError(null);
        edPhoneNumber.setError(null);
        edEmail.setError(null);
        edPassword.setError(null);
        edCity.setError(null);
        edZipCode.setError(null);
    }
    private void savingEnteredValues() {
        Hawk.put("NAME", edName.getText().toString());
        Hawk.put("EMAIL", edEmail.getText().toString());
        Hawk.put("PASSWORD", edPassword.getText().toString());
        Hawk.put("PHONE_NUMBER", edPhoneNumber.getText().toString());
        Hawk.put("ADDRESS", edAddress.getText().toString());
        Hawk.put("STATE", spinnerState.getSelectedItem().toString());
        Hawk.put("CITY", spinnerCity.getSelectedItem().toString());
        Hawk.put("ZIPCODE", spinnerZipCode.getSelectedItem().toString());
        Hawk.put("SERVICES", spinnerServices.getSelectedItem().toString());
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
//                Toast.makeText(ActivitySignUp.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
    // todo                              spinner adapter
    public class SpinAdapter extends ArrayAdapter<Payload> {
        // Your sent context
        private Context context;
        // Your custom values for the spinner (User)
        private Payload[] values;
        public SpinAdapter(Context context, int textViewResourceId,
                           Payload[] values) {
            super(context, textViewResourceId, values);
            this.context = context;
            this.values = values;
        }
        @Override
        public int getCount() {
            return values.length;
        }
        @Override
        public Payload getItem(int position) {
            return values[position];
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        // And the "magic" goes here
        // This is for the "passive" state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
            TextView label = (TextView) super.getView(position, convertView, parent);
            label.setTextColor(Color.BLACK);
            // Then you can get the current item using the values array (Users array) and the current position
            // You can NOW reference each method you has created in your bean object (User class)
            label.setText(values[position].getName());
            // And finally return your dynamic (or custom) view for each spinner item
            return label;
        }
        // And here is when the "chooser" is popped up
        // Normally is the same view, but you can customize it if you want
        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            TextView label = (TextView) super.getDropDownView(position, convertView, parent);
            label.setTextColor(Color.BLACK);
            label.setText(values[position].getName());
            return label;
        }
    }
}
