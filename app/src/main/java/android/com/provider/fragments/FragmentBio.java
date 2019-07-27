package android.com.provider.fragments;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.com.provider.activities.ActivityEditBio;
import android.com.provider.apiResponses.GetProviderBio;
import android.com.provider.apiResponses.SendBioModel;
import android.com.provider.apiResponses.Servicetype;
import android.com.provider.apiResponses.UpdateBioServiceModel;
import android.com.provider.apiResponses.getUpdateProBio.UpdateBio;
import android.com.provider.httpRetrofit.HttpModule;
import android.com.provider.rxUtils.AppConstant;
import android.com.provider15_nov_2018.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
public class FragmentBio extends Fragment implements View.OnClickListener {
    ProgressDialog mProgressDialog;
    private View view;
    private Context mcontext;
    private EditText edBio;
    private TextView tvAddBtn, tvEditBtn, tvClickStartTime, tvSetStartTime, tvSetEndTime, tvClickEndTime, tvEdit,multiSelectSpinner;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private int mYear, mMonth, mDay, mHour, mMinute;
    JsonArray jsonArray;
    private ImageView rightArrow;
    private List<String> serviceTypeList=new ArrayList<>();
    private List<Integer> serviceTypeId=new ArrayList<>();
    private  List<UpdateBioServiceModel>updateServiceList=new ArrayList<>();
    SendBioModel sendBioModel=new SendBioModel();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bio, container, false);
        Hawk.init(Objects.requireNonNull(getActivity())).build();
        mProgressDialog = new ProgressDialog(getContext(), R.style.AppTheme_Dark_Dialog);
        findingIdsHere(view);
        eventListner();
        getbioapi();
        return view;
    }
    private void getbioapi() {
        compositeDisposable.add(HttpModule.provideRepositoryService().getProviderBio(String.valueOf(Hawk.get("savedUserId"))).
                subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<GetProviderBio>(){
                    @Override
                    public void accept(GetProviderBio providerFullDetails) throws Exception {
                        if (providerFullDetails != null && providerFullDetails.getIsSuccess()) {
                            tvSetStartTime.setText(providerFullDetails.getPayLoad().getStarttime());
                            tvSetEndTime.setText(providerFullDetails.getPayLoad().getEndtime());
                            edBio.setText(providerFullDetails.getPayLoad().getBio());
                            for (Servicetype servicetype:providerFullDetails.getPayLoad().getServicetypes())
                            {
                                updateServiceList.add(new UpdateBioServiceModel().setId(servicetype.getId().toString()));
                                serviceTypeList.add(servicetype.getName());
                            }
                            String cleanerServiceType=TextUtils.join(",",serviceTypeList);
                            multiSelectSpinner.setText(cleanerServiceType);
//                            Toast.makeText(getContext(),""+updateServiceList,Toast.LENGTH_SHORT).show();

                            Hawk.put(AppConstant.START_TIME, providerFullDetails.getPayLoad().getStarttime());
                            Hawk.put(AppConstant.END_TIME, providerFullDetails.getPayLoad().getEndtime());
                            Hawk.put(AppConstant.BIO, providerFullDetails.getPayLoad().getBio());
                        } else {
                            tvEditBtn.setVisibility(View.GONE);
                            tvAddBtn.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Consumer<Throwable>(){
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("FragmentBio.accept " + throwable.toString());
                    }
                }));
    }
    private void eventListner() {
        tvAddBtn.setOnClickListener(this);
        tvEditBtn.setOnClickListener(this);
        tvClickStartTime.setOnClickListener(this);
        tvClickEndTime.setOnClickListener(this);
        tvEdit.setOnClickListener(this);
    }
    private void findingIdsHere(View view) {
        edBio = view.findViewById(R.id.edBio);
        tvAddBtn = view.findViewById(R.id.tvAddBtn);
        tvEditBtn = view.findViewById(R.id.tvEditBtn);
        tvClickStartTime = view.findViewById(R.id.tvClickStartTime);
        tvSetStartTime = view.findViewById(R.id.tvSetStartTime);
        tvSetEndTime = view.findViewById(R.id.tvSetEndTime);
        tvClickEndTime = view.findViewById(R.id.tvClickEndTime);
        tvEdit = view.findViewById(R.id.tvEdit);
        multiSelectSpinner=view.findViewById(R.id.multiselectSpinner);
        rightArrow=view.findViewById(R.id.rightarrow);
        rightArrow.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvAddBtn:
                if (TextUtils.isEmpty(edBio.getText().toString())) {
                    edBio.setError("Please Add Bio");
                    return;
                } else if (tvSetStartTime.getText().toString().isEmpty()) {
                    TastyToast.makeText(getActivity(), "Please select the start time", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (tvSetEndTime.getText().toString().isEmpty()) {
                    TastyToast.makeText(getActivity(), "Please select the end time", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (multiSelectSpinner.getText().toString().isEmpty()) {
                     TastyToast.makeText(getActivity(), "Please select the Service Type", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                }
                else
                {
                    updateBioApi();
                }
                break;
            case R.id.tvEditBtn:
                if (TextUtils.isEmpty(edBio.getText().toString())) {
                    edBio.setError("UPDATE YOUR BIO HERE , IF YOU WANT TO ADD MORE..");
                    return;
                } else {
                }
                break;
            case R.id.tvClickStartTime:
                startTimePicker();
                break;
            case R.id.tvClickEndTime:
                endTimePicker();
                break;
            case R.id.tvEdit:
                callEditBio();
                break;
            case R.id.rightarrow:
                startActivityForResult(new Intent(getContext(),ActivityEditBio.class).putExtra("type", (Serializable) updateServiceList),10);
                break;
        }
    }
    private void updateBioApi(){
        mProgressDialog.setMessage("Updating...");
//        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
        sendBioModel.setId(String.valueOf(Hawk.get("savedUserId")));
        sendBioModel.setBio(edBio.getText().toString());
        sendBioModel.setStarttime(tvSetStartTime.getText().toString());
        sendBioModel.setEndtime(tvSetEndTime.getText().toString());
        sendBioModel.setServicetype(updateServiceList);

        try {
            compositeDisposable.add(HttpModule.provideRepositoryService().getUpdateBioAPI(sendBioModel).
                    subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).
                    subscribe(new Consumer<UpdateBio>(){
                        @Override
                        public void accept(UpdateBio serviceTypeNames) throws Exception {
                            mProgressDialog.dismiss();
                            if (serviceTypeNames != null && serviceTypeNames.getIsSuccess()) {
                                Toast.makeText(getContext(),serviceTypeNames.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getContext(),serviceTypeNames.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            mProgressDialog.dismiss();
                            System.out.println("FragmentBio.accept " + throwable.toString());
                        }
                    }));
        } catch (Exception e) {
            mProgressDialog.dismiss();
            e.printStackTrace();
        }
    }
    private void callEditBio() {
        Intent intent = new Intent(getActivity(), ActivityEditBio.class);
        startActivity(intent);
    }
    private void endTimePicker() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tvSetEndTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                        Hawk.put(AppConstant.END_TIME, String.format("%02d:%02d", hourOfDay, minute));
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }
    private void startTimePicker() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tvSetStartTime.setText(hourOfDay + ":" + minute); //+ " " + AM_PM
//                        tvSetStartTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                        Hawk.put(AppConstant.START_TIME, String.format("%02d:%02d", hourOfDay, minute));
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==10 && resultCode==Activity.RESULT_OK)
        {
            updateServiceList.clear();
            updateServiceList.addAll((Collection<? extends UpdateBioServiceModel>) data.getSerializableExtra("servicelist"));
            multiSelectSpinner.setText(data.getStringExtra("serviceTypes"));
//            Toast.makeText(getContext(),""+updateServiceList.size(),Toast.LENGTH_SHORT).show();
        }
    }
}
