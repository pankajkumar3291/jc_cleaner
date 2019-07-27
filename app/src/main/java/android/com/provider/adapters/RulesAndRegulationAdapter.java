package android.com.provider.adapters;

import android.com.provider.activities.ActivityRulesRegulations;
import android.com.provider.models.RulesAndRegulation;
import android.com.provider15_nov_2018.R;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RulesAndRegulationAdapter extends RecyclerView.Adapter<RulesAndRegulationAdapter.ViewHolder> {


    private Context mCtx;
    View view;
    List<RulesAndRegulation> andRegulations;


    public RulesAndRegulationAdapter(ActivityRulesRegulations activityPrivacyPolicy, List<RulesAndRegulation> policyList) {


        this.mCtx = activityPrivacyPolicy;
        this.andRegulations = policyList;
    }


    @NonNull
    @Override
    public RulesAndRegulationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rules_regulation_adapter_row, viewGroup, false);
        return new RulesAndRegulationAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RulesAndRegulationAdapter.ViewHolder viewHolder, int i) {


        RulesAndRegulation regulation = andRegulations.get(i);

        viewHolder.tvTitle.setText(regulation.getTitle());
        viewHolder.tvDescription.setText(regulation.getDescription());

    }


    @Override
    public int getItemCount() {
        return andRegulations.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView tvTitle, tvDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }


    }
}
