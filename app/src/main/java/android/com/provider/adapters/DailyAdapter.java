package android.com.provider.adapters;

import android.com.provider.models.Time;
import android.com.provider15_nov_2018.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.DailyViewHolder> {


    private Context mCtx;
    private List<Time> list;
    View view;

    private boolean isDailyBtnClicked = false;

    public DailyAdapter(Context mCtx, List<Time> timeList) {

        this.mCtx = mCtx;
        this.list = timeList;

    }


    @NonNull
    @Override
    public DailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_adapter_row, parent, false);
        return new DailyAdapter.DailyViewHolder(view);


    }


    @Override
    public void onBindViewHolder(@NonNull final DailyViewHolder holder, int position) {

        Time time = list.get(position);
        holder.checkDaily.setText(time.getName());

        holder.checkDaily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (holder.checkDaily.isChecked()) {

                    Toast.makeText(view.getContext(), "Daily", Toast.LENGTH_SHORT).show();

                    holder.tvDaily.setTextColor(Color.parseColor("#303F9F"));
                    setTextViewDrawableColor(holder.tvDaily, R.color.colorPrimaryDark);


                } else {

                    Toast.makeText(view.getContext(), "Daily ELSE", Toast.LENGTH_SHORT).show();

                    holder.tvDaily.setTextColor(Color.parseColor("#FFFFFF"));
                    setTextViewDrawableColor(holder.tvDaily, R.color.white);
                }

            }
        });

        holder.tvDaily.setText(time.getTime());


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    private void setTextViewDrawableColor(TextView tvWeekly, int colorAccent) {

        for (Drawable drawable : tvWeekly.getCompoundDrawables()) {
            if (drawable != null) {

                drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(tvWeekly.getContext(), colorAccent), PorterDuff.Mode.SRC_IN));
            }
        }
    }


    public class DailyViewHolder extends RecyclerView.ViewHolder {


        public CheckBox checkDaily;
        public TextView tvDaily;


        public DailyViewHolder(View itemView) {
            super(itemView);


            checkDaily = itemView.findViewById(R.id.checkDaily);
            tvDaily = itemView.findViewById(R.id.tvDaily);


        }


    }
}
