package android.com.provider.adapters;

import android.com.provider.activities.ActivityOffredServices;
import android.com.provider.apiResponses.getOffredServices.OffrredServices;
import android.com.provider.apiResponses.getOffredServices.Payload;
import android.com.provider15_nov_2018.R;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class OffredServicesAdapter extends RecyclerView.Adapter<OffredServicesAdapter.ViewHolder> {


    private Context mCtx;
    View view;

    List<Payload> offeredServices;


    public OffredServicesAdapter(ActivityOffredServices activityOffredServices, List<Payload> jobsList) {


        this.mCtx = activityOffredServices;
        this.offeredServices = jobsList;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_offered_services_adapter, parent, false);
        return new OffredServicesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Payload services = offeredServices.get(position);

        holder.tvTitle.setText(services.getName());
        holder.tvDescription.setText(Html.fromHtml(Html.fromHtml(services.getDescription()).toString()));
        holder.tvDescription.setMovementMethod(new ScrollingMovementMethod());


        Picasso.get().load(services.getImage())
                .placeholder(R.drawable.placeholder)   // optional
                .error(R.drawable.no_image)      // optional
                .resize(400, 400)                        // optional
                .into(holder.profileImage);


//        if (position % 2 == 0) {
//            holder.reParent.setBackgroundColor(Color.parseColor("#9dc209"));
//        } else {
//            holder.reParent.setBackgroundColor(Color.parseColor("#cb00cb"));
//        }


        String[] mColors = {"#3F51B5", "#FF9800", "#009688", "#673AB7", "#999999", "#454545", "#00FF00",
                "#FF0000", "#0000FF", "#800000", "#808000", "#00FF00", "#008000", "#00FFFF",
                "#000080", "#800080", "#f40059", "#0080b8", "#350040", "#650040", "#750040",
                "#45ddc0", "#dea42d", "#b83800", "#dd0244", "#c90000", "#465400",
                "#ff004d", "#ff6700", "#5d6eff", "#3955ff", "#0a24ff", "#004380", "#6b2e53",
                "#a5c996", "#f94fad", "#ff85bc", "#ff906b", "#b6bc68", "#296139"};

        holder.reParent.setBackgroundColor(Color.parseColor(mColors[position % 40]));

    }

    @Override
    public int getItemCount() {

        return offeredServices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDescription, tvTitle;
        private CircleImageView profileImage;
        private RelativeLayout reParent;

        public ViewHolder(View itemView) {
            super(itemView);

            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            profileImage = itemView.findViewById(R.id.profileImage);
            reParent = itemView.findViewById(R.id.reParent);

        }
    }


}
