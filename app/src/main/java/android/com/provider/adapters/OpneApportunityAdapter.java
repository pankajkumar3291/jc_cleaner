package android.com.provider.adapters;

import android.app.AlertDialog;
import android.com.provider.activities.ActivityCurrentAppointmentsList;
import android.com.provider.activities.ActivityOpenApportunity;
import android.com.provider.activities.ActivitySchedule;
import android.com.provider.apiResponses.acceptInstantBookingApi.AcceptInstantBooking;
import android.com.provider.httpRetrofit.HttpModule;
import android.com.provider.models.OpenApportunity;
import android.com.provider15_nov_2018.R;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

import static android.com.provider.applicationUtils.App.listOfValues;

public class OpneApportunityAdapter extends RecyclerView.Adapter<OpneApportunityAdapter.OpenApportunityViewHolder> {

    private Context mContext;
    private View view;
    private OpenApportunityViewHolder openApportunityViewHolder;

    private List<OpenApportunity> list;


    private TextView tvPackingInsntructions, tvSpecialRequestCleaner;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private String jobId, customerId;


    public OpneApportunityAdapter(ActivityOpenApportunity activityOpenApportunity, ArrayList<OpenApportunity> apportunityList) {

        this.mContext = activityOpenApportunity;
        this.list = apportunityList;


        Hawk.init(mContext).build();

    }


    @NonNull
    @Override
    public OpneApportunityAdapter.OpenApportunityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {


        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_open_apportunity_adapter, parent, false);
        openApportunityViewHolder = new OpenApportunityViewHolder(view);
        return openApportunityViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull OpneApportunityAdapter.OpenApportunityViewHolder holder, final int position) {

        OpenApportunity apportunity = list.get(position);
        System.out.println("OpneApportunityAdapter.onBindViewHolder before reverse" + list.size());

        Collections.reverse(list);

        System.out.println("OpneApportunityAdapter.onBindViewHolder after reverse " + list.size());


        if (listOfValues.size() != 0) {

            holder.tvServicesText.setText(list.get(position).getServices());
            holder.tvLocationText.setText(list.get(position).getAddress());
            holder.tvDate.setText(list.get(position).getDate());
            holder.tvTime.setText(list.get(position).getTime());
            jobId = list.get(position).getJobId();
            customerId = list.get(position).getCustometId();
            TastyToast.makeText(mContext, list.get(position).getJobId(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();


        } else {

            holder.tvServicesText.setVisibility(View.GONE);
            holder.tvDistanceInKmText.setVisibility(View.GONE);
            holder.tvLocationText.setVisibility(View.GONE);
            holder.tvDate.setVisibility(View.GONE);
            holder.tvTime.setVisibility(View.GONE);
            holder.noDataAvailable.setVisibility(View.VISIBLE);
            holder.imgTime.setVisibility(View.GONE);
            holder.imgDate.setVisibility(View.GONE);
            holder.imgAddress.setVisibility(View.GONE);
            holder.tvAccept.setVisibility(View.GONE);
            holder.tvDeny.setVisibility(View.GONE);


        }


        holder.tvPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                personalInfoOfUserDialog();

            }
        });


        holder.tvAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                compositeDisposable.add(HttpModule.provideRepositoryService().acceptInstantBooking(list.get(position).getJobId(), String.valueOf(Hawk.get("savedUserId"))).
                        subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                        observeOn(AndroidSchedulers.mainThread()).
                        subscribe(new Consumer<AcceptInstantBooking>() {

                            @Override
                            public void accept(AcceptInstantBooking acceptInstantBooking) throws Exception {

                                if (acceptInstantBooking != null && acceptInstantBooking.getIsSuccess()) {


                                    TastyToast.makeText(mContext, acceptInstantBooking.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                                    removeAt(position);


                                } else {

                                    TastyToast.makeText(mContext, Objects.requireNonNull(acceptInstantBooking).getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                                }


                            }


                        }, new Consumer<Throwable>() {

                            @Override
                            public void accept(Throwable throwable) throws Exception {

                                TastyToast.makeText(mContext, throwable.toString(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();

                            }

                        }));


            }
        });


        holder.tvDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(mContext, ActivitySchedule.class);
                mContext.startActivity(intent);


//                removeAt(position);


            }
        });


    }

    private void removeAt(int position) {

        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());


    }


    private void personalInfoOfUserDialog() {


        LayoutInflater li = LayoutInflater.from(mContext);
        View dialogView = li.inflate(R.layout.dialog_personal_info, null);


        findingCleanerInfoIdsHere(dialogView);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder
                .setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();


    }

    private void findingCleanerInfoIdsHere(View dialogView) {

        tvPackingInsntructions = dialogView.findViewById(R.id.tvPackingInsntructions);
        tvSpecialRequestCleaner = dialogView.findViewById(R.id.tvSpecialRequestCleaner);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class OpenApportunityViewHolder extends RecyclerView.ViewHolder {

        private TextView tvServicesText, tvDistanceInKmText, tvLocationText,
                tvDate, tvTime, tvPersonalInfo, tvAccept, tvDeny, noDataAvailable;


        private ImageView imgTime, imgDate, imgAddress;

        public OpenApportunityViewHolder(@NonNull View itemView) {
            super(itemView);

            tvServicesText = itemView.findViewById(R.id.tvServicesText);
            tvDistanceInKmText = itemView.findViewById(R.id.tvDistanceInKmText);
            tvLocationText = itemView.findViewById(R.id.tvLocationText);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);

            tvPersonalInfo = itemView.findViewById(R.id.tvPersonalInfo);
            tvAccept = itemView.findViewById(R.id.tvAccept);
            tvDeny = itemView.findViewById(R.id.tvDeny);
            noDataAvailable = itemView.findViewById(R.id.noDataAvailable);

            imgAddress = itemView.findViewById(R.id.imgAddress);
            imgDate = itemView.findViewById(R.id.imgDate);
            imgTime = itemView.findViewById(R.id.imgTime);


        }
    }


}
