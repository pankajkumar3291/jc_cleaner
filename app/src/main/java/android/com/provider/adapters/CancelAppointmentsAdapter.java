package android.com.provider.adapters;

import android.app.AlertDialog;
import android.com.provider.activities.ActivityCanceledAppointments;
import android.com.provider.apiResponses.cancelledJobApi.Payload;
import android.com.provider15_nov_2018.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CancelAppointmentsAdapter extends RecyclerView.Adapter<CancelAppointmentsAdapter.CancelAppointmentsViewHolder> {


    private Context mContext;
    private View view;
    private CancelAppointmentsViewHolder cancelAppointmentsViewHolder;


    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;

    private TextView tvDustingPolishing, tvCancel, tvDone;

    private List<Payload> cancelledPayload;


    public CancelAppointmentsAdapter(ActivityCanceledAppointments activity, List<Payload> cancelAppointments) {

        this.mContext = activity;
        this.cancelledPayload = cancelAppointments;

    }

    @NonNull
    @Override
    public CancelAppointmentsAdapter.CancelAppointmentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {


        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cancel_appointment_adapter, parent, false);
        cancelAppointmentsViewHolder = new CancelAppointmentsViewHolder(view);
        return cancelAppointmentsViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull CancelAppointmentsAdapter.CancelAppointmentsViewHolder holder, int position) {

        Payload payload = cancelledPayload.get(position);

        holder.tvTitle.setText(payload.getCustomerName());
        holder.tvServices.setText(payload.getServicesNames());
        holder.tvAddress.setText(payload.getCustomerAddress());
        holder.tvDate.setText(payload.getDate());
        holder.tvTime.setText(payload.getTime());

        Picasso.get().load(payload.getCustomerProfile()).resize(100,100).into(holder.profileImage);


//        holder.restore_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                askingDialog();
//
//            }
//        });


    }

    private void askingDialog() {

        LayoutInflater li = LayoutInflater.from(mContext);
        View dialogView = li.inflate(R.layout.restore_asking_dialog, null);


        findingLogoutDialodIdsHere(dialogView);


        alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder
                .setCancelable(true);
        alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();


    }

    private void findingLogoutDialodIdsHere(View dialogView) {

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

                alertDialog.dismiss();
            }
        });


    }

    @Override
    public int getItemCount() {
        return cancelledPayload.size();
    }

    public class CancelAppointmentsViewHolder extends RecyclerView.ViewHolder {


        private CircleImageView profileImage;
        private TextView tvTitle, tvServices, tvAddress, tvDate, tvTime;
        private FrameLayout restore_layout;


        public CancelAppointmentsViewHolder(@NonNull View itemView) {
            super(itemView);


            profileImage = itemView.findViewById(R.id.profileImage);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvServices = itemView.findViewById(R.id.tvServices);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
//            restore_layout = itemView.findViewById(R.id.restore_layout);


        }


    }
}
