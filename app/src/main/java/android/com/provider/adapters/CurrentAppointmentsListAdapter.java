package android.com.provider.adapters;
import android.app.AlertDialog;
import android.com.provider.activities.ActivityChat;
import android.com.provider.activities.ActivityCurrentAppointments;
import android.com.provider.activities.ActivityCurrentAppointmentsList;
import android.com.provider.apiResponses.cancelledJobByProvider.CancelledJobByProvider;
import android.com.provider.apiResponses.currentJobApiResponse.Payload;
import android.com.provider.httpRetrofit.HttpModule;
import android.com.provider15_nov_2018.R;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
public class CurrentAppointmentsListAdapter extends RecyclerView.Adapter<CurrentAppointmentsListAdapter.ViewHolder> {
    private Context mContext;
    private View view;
    private ViewHolder viewHolder;
    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private TextView tvDustingPolishing, tvCancel, tvDone;
    private List<Payload> currentJobPayload;
    private String jobId, providerId;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public CurrentAppointmentsListAdapter(ActivityCurrentAppointmentsList context, List<Payload> list) {
        this.mContext = context;
        this.currentJobPayload = list;
    }
    @NonNull
    @Override
    public CurrentAppointmentsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_current_appointment_list, viewGroup, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull CurrentAppointmentsListAdapter.ViewHolder viewHolder, final int position) {
        final Payload payload = currentJobPayload.get(position);
        viewHolder.tvTitle.setText(payload.getCustomerName());
        viewHolder.tvServices.setText(payload.getServicesNames());
        viewHolder.tvAddress.setText(payload.getCustomerAddress());
        viewHolder.tvDate.setText(payload.getDate());
        viewHolder.tvTime.setText(payload.getTime());
        Picasso.get().load(payload.getCustomerProfile()).resize(100, 100).error(R.drawable.noimagenew).into(viewHolder.profileImage);
        jobId = payload.getJobId().toString();
        viewHolder.cancel_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askingDialog(position);
            }
        });


        viewHolder.tvChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ActivityChat.class).putExtra("jobid",payload.getJobId()));
            }
        });

        viewHolder.reParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ActivityCurrentAppointments.class);
                intent.putExtra("jobId", payload.getJobId());
                intent.putExtra("Title", payload.getCustomerName());
                intent.putExtra("Address", payload.getCustomerAddress());
                intent.putExtra("Profile", payload.getCustomerProfile());
                intent.putExtra("Date", payload.getDate());
                intent.putExtra("Time", payload.getTime());
                mContext.startActivity(intent);
            }
        });
    }
    private void askingDialog(int position) {
        LayoutInflater li = LayoutInflater.from(mContext);
        View dialogView = li.inflate(R.layout.cancel_asking_dialog, null);
        findingLogoutDialodIdsHere(dialogView, position);
        alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder
                .setCancelable(true);
        alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }
    private void findingLogoutDialodIdsHere(View dialogView, final int position) {
        tvDustingPolishing = dialogView.findViewById(R.id.tvDustingPolishing);
        tvCancel = dialogView.findViewById(R.id.tvCancel);
        tvDone = dialogView.findViewById(R.id.tvDone);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });



        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compositeDisposable.add(HttpModule.provideRepositoryService().cancelledJobByProvider(jobId, String.valueOf(Hawk.get("savedUserId"))).
                        subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                        observeOn(AndroidSchedulers.mainThread()).
                        subscribe(new Consumer<CancelledJobByProvider>() {
                            @Override
                            public void accept(CancelledJobByProvider cancelledJobByProvider) throws Exception {
                                if (cancelledJobByProvider != null && cancelledJobByProvider.getIsSuccess()) {
                                    TastyToast.makeText(mContext, cancelledJobByProvider.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                                    currentJobPayload.remove(position);
                                    notifyDataSetChanged();
                                } else {
                                    TastyToast.makeText(mContext, Objects.requireNonNull(cancelledJobByProvider).getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                TastyToast.makeText(mContext, throwable.toString(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                            }
                        }));
                alertDialog.dismiss();
            }
        });
    }
    @Override
    public int getItemCount() {
        return currentJobPayload.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView profileImage;
        private TextView tvTitle, tvServices, tvAddress, tvDate, tvTime, tvCancel, tvChat;
        private LinearLayout cancel_layout, frontLayout;
        private RelativeLayout reParent;
        private CardView cardview;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvServices = itemView.findViewById(R.id.tvServices);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            cancel_layout = itemView.findViewById(R.id.cancel_layout);
            tvCancel = itemView.findViewById(R.id.swipe_layout);
            tvChat = itemView.findViewById(R.id.tv_chat);
            reParent = itemView.findViewById(R.id.reParent);
            cardview = itemView.findViewById(R.id.cardview);
        }
    }
}
