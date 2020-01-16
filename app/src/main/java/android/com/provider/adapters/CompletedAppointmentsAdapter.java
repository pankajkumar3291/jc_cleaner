package android.com.provider.adapters;

import android.com.provider.activities.ActivityCompletedAppointments;
import android.com.provider.apiResponses.completedJobApiResposne.Payload;
import android.com.provider15_nov_2018.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CompletedAppointmentsAdapter extends RecyclerView.Adapter<CompletedAppointmentsAdapter.ViewHolder> {


    private Context mContext;
    private View view;
    private ViewHolder viewHolder;

    private List<Payload> cancelledPayload;

    public CompletedAppointmentsAdapter(ActivityCompletedAppointments activity, List<Payload> list) {


        this.mContext = activity;
        this.cancelledPayload = list;

    }


    @NonNull
    @Override
    public CompletedAppointmentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {

        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_completed_appointments, viewGroup, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final CompletedAppointmentsAdapter.ViewHolder viewHolder, int position) {


        Payload payload = cancelledPayload.get(position);

        viewHolder.tvTitle.setText(payload.getCustomerName());
        viewHolder.tvServices.setText(payload.getServicesNames());
        viewHolder.tvAddress.setText(payload.getCustomerAddress());
        viewHolder.tvDate.setText(payload.getDate());
        viewHolder.tvTime.setText(payload.getTime());

        Picasso.get().load(payload.getCustomerProfile()).resize(100,100).error(R.drawable.no_image).into(viewHolder.profileImage);


        Drawable drawable = viewHolder.ratingBar.getProgressDrawable();
        drawable.setColorFilter(Color.parseColor("#FFFDD433"), PorterDuff.Mode.SRC_ATOP);


        viewHolder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                Toast.makeText(mContext, "Rating changed, current rating " + ratingBar.getRating(),
                        Toast.LENGTH_SHORT).show();
                viewHolder.ratingBar.setRating(ratingBar.getRating());
            }
        });
    }

    @Override
    public int getItemCount() {
        return cancelledPayload.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private CircleImageView profileImage;
        private TextView tvTitle, tvServices, tvAddress, tvDate, tvTime;
        private RatingBar ratingBar;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profileImage);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvServices = itemView.findViewById(R.id.tvServices);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);

            ratingBar = itemView.findViewById(R.id.ratingBar);


        }


    }
}
