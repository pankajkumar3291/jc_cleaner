package android.com.provider.adapters;

import android.com.provider.activities.ActivityCompanyInfo;
import android.com.provider.models.CompanyInfo;
import android.com.provider15_nov_2018.R;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CompanyInfoAdapter extends RecyclerView.Adapter<CompanyInfoAdapter.ViewHolder> {


    Context mCtx;
    View view;

    List<CompanyInfo> companyInfos;

    public CompanyInfoAdapter(ActivityCompanyInfo activityCompanyInfo, List<CompanyInfo> infoList) {


        this.mCtx = activityCompanyInfo;
        this.companyInfos = infoList;


    }

    @NonNull
    @Override
    public CompanyInfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.company_info_adapter_row, parent, false);
        return new CompanyInfoAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CompanyInfoAdapter.ViewHolder holder, int position) {

        CompanyInfo companyInfo = companyInfos.get(position);

        holder.tvTitle.setText(companyInfo.getTitle());
        holder.tvDescription.setText(companyInfo.getDescription());

    }

    @Override
    public int getItemCount() {
        return companyInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView tvTitle, tvDescription;


        public ViewHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);

        }
    }


}
