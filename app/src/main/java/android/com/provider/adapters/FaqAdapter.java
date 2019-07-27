package android.com.provider.adapters;

import android.com.provider.apiResponses.getFAQ.Payload;
import android.com.provider15_nov_2018.R;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.ViewHolder> {

    private Context context;
    private List<Payload> expandableListTitle;
    private boolean isClicked = false;

    public FaqAdapter(Context context, List<Payload> expandableListTitle) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
    }

    @NonNull
    @Override
    public FaqAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View view = LayoutInflater.from(context).inflate(R.layout.faq_row, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final FaqAdapter.ViewHolder viewHolder, int position) {


        final Payload payload = expandableListTitle.get(position);


        viewHolder.tvQues.setText(Html.fromHtml(payload.getQuestion()));
        viewHolder.tvAns.setText(Html.fromHtml(payload.getAnswer()));


        viewHolder.tvQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (isClicked) {

                    viewHolder.tvAns.setVisibility(View.GONE);
                    isClicked = false;

                } else {

                    isClicked = true;
                    viewHolder.tvAns.setVisibility(View.VISIBLE);
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return expandableListTitle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView tvQues, tvAns;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            tvQues = itemView.findViewById(R.id.tvQues);
            tvAns = itemView.findViewById(R.id.tvAns);


        }
    }

}