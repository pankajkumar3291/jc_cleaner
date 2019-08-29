package android.com.provider.activities;
import android.com.provider.apiResponses.UpdateBioServiceModel;
import android.com.provider.apiResponses.getServiceTypeNames.Payload;
import android.com.provider.apiResponses.getServiceTypeNames.ServiceTypeNames;
import android.com.provider.httpRetrofit.HttpModule;
import android.com.provider15_nov_2018.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
public class ActivityEditBio extends AppCompatActivity {
    private RecyclerView recjobtype;
    private List<Payload> serviceList = new ArrayList<>();
    private List<UpdateBioServiceModel> userService=new ArrayList<>();
    private List<String> sendSelectedlist=new ArrayList<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Button btnokk;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bio);
        recjobtype = findViewById(R.id.recjobtypes);
        userService= (List<UpdateBioServiceModel>) getIntent().getSerializableExtra("type");
        callapi();
        findViewById(R.id.backarr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); }
        });
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                userService.clear();
                String selectedTypes;
                for (Payload payload:serviceList){
                    if (payload.isSelected()){
                        sendSelectedlist.add(payload.getName());
                        UpdateBioServiceModel updateBioServiceModel= new UpdateBioServiceModel();
                        userService.add(updateBioServiceModel.setId(payload.getId().toString()));}
                }
                selectedTypes= TextUtils.join(",",sendSelectedlist);
                Intent intent = new Intent();
                intent.putExtra("serviceTypes", selectedTypes);
                intent.putExtra("servicelist", (Serializable) userService);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
    private void callapi(){
        compositeDisposable.add(HttpModule.provideRepositoryService().serviceTypeNamesss("5").
                subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<ServiceTypeNames>(){
                    @Override
                    public void accept(ServiceTypeNames serviceTypeNames) throws Exception{
                        if (serviceTypeNames != null && serviceTypeNames.getIsSuccess()) {
                            if (serviceTypeNames.getPayload().size() > 0) {
                                serviceList.addAll(serviceTypeNames.getPayload());
                                if (userService.size()>0 && userService!=null)
                                {
                                    for (Payload payload:serviceList)
                                    {
                                        for (UpdateBioServiceModel updateBioServiceModel:userService) {
                                            if (payload.getId().equals(Integer.valueOf(updateBioServiceModel.getId()))) {
                                                payload.setSelected(true);
                                            }
                                        }
                                    }
                                }
                                recjobtype.setAdapter(new ServiceTypeAdapter(ActivityEditBio.this, serviceList));
                            }
                        }
                        else {

                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("FragmentBio.accept " + throwable.toString());
                    }
                }));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
    //*********************************** Adapter Class ******************************************
    class ServiceTypeAdapter extends RecyclerView.Adapter<ServiceTypeAdapter.ServiceTypeViewHolder> {
        private Context context;
        private List<Payload> serviceList;

        public ServiceTypeAdapter(Context context, List<Payload> serviceList) {
            this.context = context;
            this.serviceList = serviceList;
        }
        @NonNull
        @Override
        public ServiceTypeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_job_type, viewGroup, false);
            return new ServiceTypeViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull ServiceTypeViewHolder serviceTypeViewHolder, int i) {
            final Payload payload = serviceList.get(i);

            if (payload.isSelected())
            {
                serviceTypeViewHolder.checkBox.setChecked(true);
            }
            serviceTypeViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                        payload. setSelected(isChecked);
                }
            });
            serviceTypeViewHolder.tvServiceName.setText(payload.getName());
        }
        @Override
        public int getItemCount() {
            return serviceList.size();
        }
        class ServiceTypeViewHolder extends RecyclerView.ViewHolder{
            private TextView tvServiceName;
            private CheckBox checkBox;
            public ServiceTypeViewHolder(@NonNull View itemView) {
                super(itemView);
                tvServiceName = itemView.findViewById(R.id.textView);
                checkBox=itemView.findViewById(R.id.imageView2);
            }
        }
    }
}
