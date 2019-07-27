package android.com.provider.adapters;

import android.com.provider.models.ContactSupport;
import android.com.provider15_nov_2018.R;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ContactSupportAdapter extends RecyclerView.Adapter<ContactSupportAdapter.ViewHolder> {


    private View view;
    private Context context;
    private ViewHolder viewHolder;
    private List<ContactSupport> supportList;

    public ContactSupportAdapter(Context mContext, List<ContactSupport> contactSupportList) {

        this.context = mContext;
        this.supportList = contactSupportList;

    }


    @NonNull
    @Override
    public ContactSupportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_contact_support, viewGroup, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;


    }


    @Override
    public void onBindViewHolder(@NonNull ContactSupportAdapter.ViewHolder holder, int i) {

        ContactSupport list = supportList.get(i);
        holder.tvDescription.setText(list.getSupportDescription());


    }


    @Override
    public int getItemCount() {
        return supportList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDescription;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDescription = itemView.findViewById(R.id.tvDescription);
        }

    }
}
