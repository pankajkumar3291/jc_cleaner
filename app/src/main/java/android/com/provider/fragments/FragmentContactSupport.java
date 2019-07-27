package android.com.provider.fragments;

import android.com.provider.adapters.ContactSupportAdapter;
import android.com.provider.models.ContactSupport;
import android.com.provider15_nov_2018.R;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FragmentContactSupport extends Fragment {


    private View view;
    private Context mContext;
    private RecyclerView recyclerContactSupport;
    private ContactSupportAdapter adapter;
    private List<ContactSupport> contactSupportList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_contact_support, container, false);

        findingIdsHere(view);
        callTheAdapterHere();


        return view;
    }

    private void callTheAdapterHere() {

        contactSupportList = new ArrayList<>();
        contactSupportList.add(new ContactSupport("\"But I must explain to you how all this mistaken idea of denouncing pleasure and praising pain was born and I will give you a complete account of the system, and expound the actual teachings of the great explorer of the truth, the master-builder of human happiness. No one rejects, dislikes, or avoids pleasure itself, because it is pleasure, but because those who do not know how to pursue pleasure rationally encounter consequences that are extremely painful. Nor again is there anyone who loves or pursues or desires to obtain pain of itself, because it is pain, but because occasionally circumstances occur in which toil and pain can procure him some great pleasure. To take a trivial example, which of us ever undertakes laborious physical exercise, except to obtain some advantage from it? But who has any right to find fault with a man who chooses to enjoy a pleasure that has no annoying consequences, or one who avoids a pain that produces no resultant pleasure?\""));

        adapter = new ContactSupportAdapter(mContext, contactSupportList);
        recyclerContactSupport.setHasFixedSize(true);
        recyclerContactSupport.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerContactSupport.setAdapter(adapter);


    }


    private void findingIdsHere(View view) {

        recyclerContactSupport = view.findViewById(R.id.recyclerContactSupport);
    }


}
