package android.com.provider.fragments;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.com.provider.activities.ActivitySignIn;
import android.com.provider.apiResponses.displayProviderProfile.DisplayProfile;
import android.com.provider.apiResponses.getAddressProfileUpdated.AddressProfileUpdated;
import android.com.provider.apiResponses.getChangePassword.ChangePassword;
import android.com.provider.apiResponses.getCityListApi.CityList;
import android.com.provider.apiResponses.getProfileApi.Profile;
import android.com.provider.apiResponses.getServiceList.GetServiceList;
import android.com.provider.apiResponses.getServiceList.Payload;
import android.com.provider.apiResponses.getStateListApi.GetStateList;
import android.com.provider.apiResponses.getZipCodeListApi.ZipCodeList;
import android.com.provider.apiResponses.providerFullDetails.ProviderFullDetails;
import android.com.provider.httpRetrofit.HttpModule;
import android.com.provider.rxUtils.AppConstant;
import android.com.provider.sharedPreferenceHelper.SharedPrefsHelper;
import android.com.provider15_nov_2018.R;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.enums.EPickType;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class FragmentAddress extends Fragment implements View.OnClickListener {
    private View view;
    private ImageView chooserImage;
    private CircleImageView providerProfile;
    private EditText edName, edEmail, edPassword, edAddress, edPhoneNumber, edCity, edZipCode;
    private Spinner stateSpinner, spinnerCity, spinnerZipcode, spinnerServices;
    private TextView tvChangePass;
    private Button SignUpBtn;
    private SharedPrefsHelper sharedPrefsHelper;
    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private EditText edOldPass, edNewPassword, edPasswordConfirm;
    private TextView tvSave;
    private TextView tvToastError,tvClose,tvToast,tvstates,tvcity,tvzipcode;
    private TextView tvYes;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ProgressDialog mProgressDialog;
    private ArrayList<String> listSpinnerState = new ArrayList<>();
    private ArrayList<String> listSpinnerCity = new ArrayList<>();
    private ArrayList<String> listSpinnerZipcode = new ArrayList<>();
    private ArrayList<String> listSpinnerServices = new ArrayList<>();
    ArrayList<String> listForState;
    ArrayList<String> listForServices;
    ArrayList<String> listForCities;
    ArrayList<String> listForZipcode;
    private ArrayAdapter<String> dataAdapterForzipcodes;
    private ArrayAdapter<String> dataAdapterForCities;
    private ArrayAdapter<String> dataAdapterForStates;
    private TextView tvStayHere, tvLoginAgain, tvMessage;
    private String selectedValuesFromServices;
    private String name, phone_number, address, state, city, zipcode, service;
    private String currentSelectedState, currentCitySelectedItem, currentSelectedZipcode, currentSelectedService;
    private String stateID;
    private String cityID;
    private String zipcodeID;
    private String serviceID;
    private boolean isStateSelected,isCitySelected,isZipCodeSelected;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_address, container, false);
        Hawk.init(Objects.requireNonNull(getActivity())).build();
        sharedPrefsHelper = new SharedPrefsHelper(getActivity());
        findingIdsHere(view);
        providerFullDetailsApiGoesHere();
        eventsListenerHere();
        mProgressDialog = new ProgressDialog(getActivity(),R.style.AppTheme_Dark_Dialog);
        return view;
    }
    private void providerFullDetailsApiGoesHere() {
        compositeDisposable.add(HttpModule.provideRepositoryService().providerFullDetails(String.valueOf(Hawk.get("savedUserId"))).
                subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<ProviderFullDetails>(){
                    @Override
                    public void accept(ProviderFullDetails providerFullDetails) throws Exception {
                        if (providerFullDetails != null && providerFullDetails.getIsSuccess()) {
                            edName.setText(providerFullDetails.getPayLoad().getUserDetails().getFirstName());
                            edEmail.setText(providerFullDetails.getPayLoad().getUserDetails().getEmail());
                            edPhoneNumber.setText(providerFullDetails.getPayLoad().getUserDetails().getMobile());
                            edAddress.setText(providerFullDetails.getPayLoad().getAddress());
                            name = providerFullDetails.getPayLoad().getUserDetails().getFirstName();
                            phone_number = providerFullDetails.getPayLoad().getUserDetails().getMobile();
                            address = providerFullDetails.getPayLoad().getAddress();
                            tvstates.setText(providerFullDetails.getPayLoad().getState());
                            tvcity.setText(providerFullDetails.getPayLoad().getCityname());
                            tvzipcode.setText(providerFullDetails.getPayLoad().getZipcode());
                            state = providerFullDetails.getPayLoad().getState();
                            city = providerFullDetails.getPayLoad().getCityname();
                            zipcode = providerFullDetails.getPayLoad().getZipcode();
                            service = providerFullDetails.getPayLoad().getService_name();
                            serviceID = String.valueOf(providerFullDetails.getPayLoad().getUserDetails().getServices());
                            stateID = providerFullDetails.getPayLoad().getState_id();
                            cityID = providerFullDetails.getPayLoad().getCity_id();
                            zipcodeID = providerFullDetails.getPayLoad().getZipcode_id();
                            spinnerStateOPGoesHere();
                            spinnerServicesOPGoesHere();
                        } else {
                            System.out.println("FragmentAddress.accept " + Objects.requireNonNull(providerFullDetails).getMessage());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("FragmentAddress.accept " + throwable.toString());
                    }
                }));
    }
    @Override
    public void onResume() {
        super.onResume();
        HttpModule.provideRepositoryService().displayProviderImage(String.valueOf(Hawk.get("savedUserId"))).enqueue(new Callback<DisplayProfile>() {
            @Override
            public void onResponse(Call<DisplayProfile> call, Response<DisplayProfile> response) {
                if (response.body() != null && Objects.requireNonNull(response.body()).getIsSuccess()) {
                    Picasso.get()
                            .load(Objects.requireNonNull(response.body()).getPayLoad())
                            .placeholder(R.drawable.no_image_available)
                            .error(R.drawable.no_image_available)
                            .into(providerProfile);
                    Hawk.put(AppConstant.SAVE_IMAGE_DASHBOARD, response.body().getPayLoad());
                } else {
                    Picasso.get()
                            .load(R.drawable.no_image_available)
                            .placeholder(R.drawable.no_image_available)
                            .error(R.drawable.no_image_available)
                            .into(providerProfile);
                }
            }
            @Override
            public void onFailure(Call<DisplayProfile> call, Throwable t) {
                TastyToast.makeText(getActivity(), t.toString(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
            }
        });
    }
    // MARK: SPINNER-SERVICE
    private void spinnerServicesOPGoesHere() {
        HttpModule.provideRepositoryService().getServiceListAPI().enqueue(new Callback<GetServiceList>() {
            @Override
            public void onResponse(Call<GetServiceList> call, Response<GetServiceList> response) {
                if (response.body() != null && response.body().getIsSuccess()) {
                    setSpinnerForService(Objects.requireNonNull(response.body()).getPayload());
                } else {
                }
            }
            @Override
            public void onFailure(Call<GetServiceList> call, Throwable t) {
                System.out.println("FragmentAddress.onFailure " + t.toString());
                TastyToast.makeText(getActivity(), t.toString(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
            }
        });
    }
    private void setSpinnerForService(List<Payload> payload) {
        listForServices = new ArrayList<>();
        for (int k = 0; k < payload.size(); k++) {
            listForServices.add(payload.get(k).getName());
            listSpinnerServices.add(String.valueOf(payload.get(k).getId()));
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, listForServices);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerServices.setDropDownWidth(1000);
        spinnerServices.setAdapter(dataAdapter);
        int posService = dataAdapter.getPosition(service);
        spinnerServices.setSelection(posService);
        itemSelectionForServices();
    }
    private void itemSelectionForServices() {
        spinnerServices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentSelectedService = spinnerServices.getSelectedItem().toString();
                Hawk.put("SERVICES", currentSelectedService);
                selectedValuesFromServices = listSpinnerServices.get(i);
                Hawk.put("SELECTED_SERVICE", selectedValuesFromServices);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    // SPINNER-SERVICE END
    // MARK: SPINNER-ZIPCODE
    private void spinnerZipCodeOPGoeshere(String selectedValuesFromCitySpinner) {
        compositeDisposable.add(HttpModule.provideRepositoryService().
                getZipCodeListAPI(selectedValuesFromCitySpinner).
                subscribeOn(io.reactivex.schedulers.Schedulers.computation()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<ZipCodeList>() {
                    @Override
                    public void accept(ZipCodeList zipCodeList) throws Exception {
                        if (zipCodeList != null && zipCodeList.getIsSuccess()) {
                            setSpinnerForZipcode(zipCodeList);
                        } else {
                            listForZipcode.clear();
                            listSpinnerZipcode.clear();
                            dataAdapterForzipcodes.notifyDataSetChanged();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }));
    }
    private void setSpinnerForZipcode(ZipCodeList zipCodeList) {
        listForZipcode = new ArrayList<>();
        listForZipcode.clear();
        listSpinnerZipcode.clear();
        for (int j = 0; j < zipCodeList.getPayload().size(); j++) {
            listForZipcode.add(zipCodeList.getPayload().get(j).getZipcode());
            listSpinnerZipcode.add(String.valueOf(zipCodeList.getPayload().get(j).getCityId()));
        }
        dataAdapterForzipcodes = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, listForZipcode);
        dataAdapterForzipcodes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerZipcode.setDropDownWidth(1000);
        spinnerZipcode.setAdapter(dataAdapterForzipcodes);
        int posZipcode = dataAdapterForzipcodes.getPosition(zipcode);
        spinnerZipcode.setSelection(posZipcode);
        isZipCodeSelected = true;
        itemSelectionForZipcodes();
    }
    private void itemSelectionForZipcodes() {
        spinnerZipcode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!zipcodeID.equals(listSpinnerZipcode.get(i))) {
                    isZipCodeSelected = false;
                }
                zipcodeID = listSpinnerZipcode.get(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    // SPINNER-ZIPCODE END
    // MARK: SPINNER-CITY
    private void spinnerCitiesOPGoesHere(String selectedValuesFromStateSpinner) {
        compositeDisposable.add(HttpModule.provideRepositoryService().
                getCitiesAPI(selectedValuesFromStateSpinner).
                subscribeOn(io.reactivex.schedulers.Schedulers.computation()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<CityList>() {
                    @Override
                    public void accept(CityList cityList) throws Exception {
                        if (cityList != null && cityList.getIsSuccess()) {
                            setCitiesValues(cityList);
                        } else {
                            listForCities.clear();
                            listSpinnerCity.clear();
                            dataAdapterForCities.notifyDataSetChanged();
                            // MARK: CLEARING AND REFRESHING THE ADAPTER FOR ZIPCODES AS WELL
                            listForZipcode.clear();
                            listSpinnerZipcode.clear();
                            dataAdapterForzipcodes.notifyDataSetChanged();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("FragmentAddress.accept " + throwable.toString());
                        TastyToast.makeText(getActivity(), throwable.toString(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).
                                show();
                    }
                }));
    }
    private void setCitiesValues(CityList cityList) {
        listForCities = new ArrayList<>();
        listForCities.clear();
        listSpinnerCity.clear();
        for (int i = 0; i < cityList.getPayload().size(); i++) {
            listForCities.add(cityList.getPayload().get(i).getName());
            listSpinnerCity.add(String.valueOf(cityList.getPayload().get(i).getId()));
        }
        dataAdapterForCities = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, listForCities);
        dataAdapterForCities.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setDropDownWidth(1000);
        spinnerCity.setAdapter(dataAdapterForCities);
        int poscity = dataAdapterForCities.getPosition(city);
        spinnerCity.setSelection(poscity);
        isCitySelected = true;
        itemSelectionForCities();
    }
    private void itemSelectionForCities() {
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!cityID.equals(listSpinnerCity.get(i))) {
                    isCitySelected = false;
                }
                cityID = listSpinnerCity.get(i);
                spinnerZipCodeOPGoeshere(cityID);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    // SPINNER-CITY END
    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
    // MARK: SPINNER-STATE
    private void spinnerStateOPGoesHere() {
        compositeDisposable.add(HttpModule.provideRepositoryService().
                getStateListAPI().subscribeOn(io.reactivex.schedulers.Schedulers.computation()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<GetStateList>() {
                    @Override
                    public void accept(GetStateList getStateList) throws Exception {
                        if (getStateList != null && getStateList.getIsSuccess()) {
                            setStateValues(getStateList);
                        } else {
                            TastyToast.makeText(getActivity(), Objects.requireNonNull(getStateList).getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        TastyToast.makeText(getActivity(), "Soemthing went wrong", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                    }
                }));
    }
    private void setStateValues(GetStateList getStateList) {
        listForState = new ArrayList<>();
        listForState.clear();
        listSpinnerState.clear();
        for (int x = 0; x < getStateList.getPayload().size(); x++) {
            listForState.add(getStateList.getPayload().get(x).getName());
            listSpinnerState.add(String.valueOf(getStateList.getPayload().get(x).getId()));
        }
        dataAdapterForStates = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, listForState);
        dataAdapterForStates.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setDropDownWidth(1000);
        stateSpinner.setAdapter(dataAdapterForStates);
        int pos = dataAdapterForStates.getPosition(state);
        stateSpinner.setSelection(pos);
        isStateSelected = true;
        itemSelection();
    }
    private void itemSelection() {
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!stateID.equals(listSpinnerState.get(i))) {
                    isCitySelected = false;
                }
                // MARK: STATEID WILL REPLCAE FOR SECOND TIME
                stateID = listSpinnerState.get(i);
                spinnerCitiesOPGoesHere(stateID);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    // SPINNER-STATE END
    // MARK: EVENT-LISTENER
    private void eventsListenerHere() {
        chooserImage.setOnClickListener(this);
        SignUpBtn.setOnClickListener(this);
    }
    private void findingIdsHere(View view) {
        tvstates = view.findViewById(R.id.tvstatec);
        tvcity = view.findViewById(R.id.tvcityc);
        tvzipcode = view.findViewById(R.id.tvzipcodec);
        chooserImage = view.findViewById(R.id.chooserImage);
        providerProfile = view.findViewById(R.id.providerProfile);
        tvChangePass = view.findViewById(R.id.tvChangePass);
        tvStayHere = view.findViewById(R.id.tvStayHere);
        tvLoginAgain = view.findViewById(R.id.tvLoginAgain);
        SignUpBtn = view.findViewById(R.id.SignUpBtn);
        edName = view.findViewById(R.id.edName);
        edEmail = view.findViewById(R.id.edEmail);
        edPassword = view.findViewById(R.id.edPassword);
        edAddress = view.findViewById(R.id.edAddress);
        edPhoneNumber = view.findViewById(R.id.edPhoneNumber);
        edCity = view.findViewById(R.id.edCity);
        edZipCode = view.findViewById(R.id.edZipCode);
        stateSpinner = view.findViewById(R.id.stateSpinner);
        spinnerCity = view.findViewById(R.id.spinnerCity);
        spinnerZipcode = view.findViewById(R.id.spinnerZipcode);
        spinnerServices = view.findViewById(R.id.spinnerServices);
        settingTheValuesHere();
        tvChangePass.setPaintFlags(tvChangePass.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePasswordDialog();
            }
        });
    }
    // MARK: DIALOG FOR UPDATE PROFILE
    private void showTheDialogMessageForUpdateProfile(String message) {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View dialogView = li.inflate(R.layout.dialog_show_for_update_profile, null);
        findingDialogIdsForUpdateProfile(dialogView, message);
        alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder
                .setCancelable(false);
        alertDialog = alertDialogBuilder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }
    private void findingDialogIdsForUpdateProfile(View dialogView, String message) {
        tvMessage = dialogView.findViewById(R.id.tvMessage);
        tvMessage.setText(message);
        tvStayHere = dialogView.findViewById(R.id.tvStayHere);
        tvLoginAgain = dialogView.findViewById(R.id.tvLoginAgain);
        tvStayHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        tvLoginAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivitySignIn.class);
                Objects.requireNonNull(getActivity()).startActivity(intent);
            }
        });
    }
    private void changePasswordDialog() {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View dialogView = li.inflate(R.layout.dialog_change_password, null);
        findingDialodIdsHere(dialogView);
        alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(true);
        alertDialog = alertDialogBuilder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }
    private void findingDialodIdsHere(View dialogView) {
        edOldPass = dialogView.findViewById(R.id.edOldPass);
        edNewPassword = dialogView.findViewById(R.id.edNewPassword);
        edPasswordConfirm = dialogView.findViewById(R.id.edPasswordConfirm);
        tvSave = dialogView.findViewById(R.id.tvSave);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePasswordApiGoesHere();
            }
        });
    }
    private void changePasswordApiGoesHere() {
        compositeDisposable.add(HttpModule.provideRepositoryService().
                getChangePaaswordAPI(String.valueOf(Hawk.get("savedUserId")), edOldPass.getText().toString(), edNewPassword.getText().toString()).
                subscribeOn(io.reactivex.schedulers.Schedulers.computation()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<ChangePassword>() {
                    @Override
                    public void accept(ChangePassword changePassword) throws Exception {
                        if (changePassword != null && changePassword.getIsSuccess()) {
                            TastyToast.makeText(getActivity(), changePassword.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                            alertDialog.dismiss();
                        } else {
                            TastyToast.makeText(getActivity(), Objects.requireNonNull(changePassword).getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("FragmentAddress.accept " + throwable.toString());
                        TastyToast.makeText(getActivity(), throwable.toString(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                    }
                }));
    }
    private void settingTheValuesHere() {
        edPassword.setText((CharSequence) Hawk.get("PASSWORD"));
        edEmail.setEnabled(false);
        edPassword.setEnabled(false);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chooserImage:
                onImageViewClick();
                break;
            case R.id.SignUpBtn:
                updateWholeProfile();
                break;
        }
    }
    private void updateWholeProfile() {
        if (edName.getText().toString().equalsIgnoreCase(name) && edPhoneNumber.getText().toString().
                equalsIgnoreCase(phone_number) && edAddress.getText().toString().equalsIgnoreCase(address) &&
                stateSpinner.getSelectedItem().toString().equalsIgnoreCase(state)
                && spinnerCity.getSelectedItem().toString().equalsIgnoreCase(city) &&
                spinnerZipcode.getSelectedItem().toString().equalsIgnoreCase(zipcode) &&
                spinnerServices.getSelectedItem().toString().equalsIgnoreCase(service)) {
            TastyToast.makeText(getActivity(), "You have not updated anything new", TastyToast.LENGTH_SHORT, TastyToast.WARNING).show();
        } else {
            HttpModule.provideRepositoryService().addressProfileUpdated((String.valueOf(Hawk.get("savedUserId"))),
                    edName.getText().toString(), "Provider",
                    edPhoneNumber.getText().toString(),
                    edAddress.getText().toString(), stateID,
                    cityID,
                    zipcodeID,
                    serviceID).enqueue(new Callback<AddressProfileUpdated>() {
                @Override
                public void onResponse(Call<AddressProfileUpdated> call, Response<AddressProfileUpdated> response) {
                    if (response.body() != null && response.body().getIsSuccess()) {
                        Hawk.put("PROVIDER_NAME_DASHBOARD", edName.getText().toString());
                        showTheDialogMessageForUpdateProfile(response.body().getMessage());
                    } else {
                        TastyToast.makeText(getActivity(), Objects.requireNonNull(response.body()).getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                    }
                }
                @Override
                public void onFailure(Call<AddressProfileUpdated> call, Throwable t) {
                    TastyToast.makeText(getActivity(), t.toString(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                }
            });
        }
    }
    private void onImageViewClick() {
        PickSetup setup = new PickSetup()
                .setTitle("Choose")
                .setTitleColor(Color.WHITE)
                .setBackgroundColor(Color.WHITE)
                .setCancelText("Close")
                .setCancelTextColor(Color.WHITE)
                .setButtonTextColor(Color.WHITE)
                .setMaxSize(500)
                .setPickTypes(EPickType.GALLERY, EPickType.CAMERA)
                .setCameraButtonText("Camera")
                .setGalleryButtonText("Gallery")
                .setIconGravity(Gravity.LEFT)
                .setButtonOrientation(LinearLayout.VERTICAL) // LinearLayoutCompat.VERTICAL
                .setSystemDialog(false)
                .setGalleryIcon(R.drawable.gallery_picker)
                .setCameraIcon(R.drawable.camera_picker)
                .setSystemDialog(false).setBackgroundColor(Color.parseColor("#FF146BAC"));
        PickImageDialog.build(setup).setOnPickResult(new IPickResult() {
            @Override
            public void onPickResult(PickResult pickResult) {
                mProgressDialog.setMessage("Loding..");
                mProgressDialog.setCancelable(false);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.show();
                userProfilePicApi(pickResult);
            }
        }).setOnPickCancel(new IPickCancel() {
            @Override
            public void onCancelClick() {
            }
        }).show(Objects.requireNonNull(getActivity()));
    }
    private void userProfilePicApi(final PickResult pickResult) {
        File file = new File(pickResult.getPath());
        final RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(Hawk.get("savedUserId")));
        HttpModule.provideRepositoryService().postImage(id, body).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                mProgressDialog.dismiss();
                if (response.body() != null && response.body().getIsSuccess()) {
                    showTheDialogMessageForOk(response.body().getMessage());
                } else {
                    mProgressDialog.dismiss();
                    showTheDialogMessageForError(Objects.requireNonNull(response.body()).getMessage());
                }
            }
            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                mProgressDialog.dismiss();
                System.out.println("FragmentAddress.onFailure " + t);
            }
        });
    }
    private void showTheDialogMessageForError(String message) {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View dialogView = li.inflate(R.layout.dialog_show_for_message_error, null);
        findingIdsForError(dialogView, message);
        alertDialogBuilder = new AlertDialog.Builder(getActivity());
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
        LayoutInflater li = LayoutInflater.from(getActivity());
        View dialogView = li.inflate(R.layout.dialog_show_for_message_ok, null);
        findingDialogOkIds(dialogView, message);
        alertDialogBuilder = new AlertDialog.Builder(getActivity());
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
                alertDialog.dismiss();
            }
        });
        tvToast.setText(message);
    }
}
