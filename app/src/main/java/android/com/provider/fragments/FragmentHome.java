package android.com.provider.fragments;

import android.com.provider.activities.ActivityCanceledAppointments;
import android.com.provider.activities.ActivityCompletedAppointments;
import android.com.provider.activities.ActivityCurrentAppointments;
import android.com.provider.activities.ActivityCurrentAppointmentsList;
import android.com.provider.activities.ActivityOpenApportunity;
import android.com.provider15_nov_2018.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sdsmdg.tastytoast.TastyToast;

public class FragmentHome extends Fragment implements View.OnClickListener {

    private View view;
    private LinearLayout linearOpenApportunities, linearUpcomingAppointments,
            linearCompletedAppointments, linearCurrentAppointments, linearCanceledAppointments;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        findingIdsHere(view);
        eventListener();
        return view;
    }

    private void eventListener() {
        linearOpenApportunities.setOnClickListener(this);
        linearUpcomingAppointments.setOnClickListener(this);
        linearCompletedAppointments.setOnClickListener(this);
        linearCurrentAppointments.setOnClickListener(this);
        linearCanceledAppointments.setOnClickListener(this);

    }


    private void findingIdsHere(View view) {

        linearOpenApportunities = view.findViewById(R.id.linearOpenApportunities);
        linearUpcomingAppointments = view.findViewById(R.id.linearUpcomingAppointments);
        linearCompletedAppointments = view.findViewById(R.id.linearCompletedAppointments);
        linearCurrentAppointments = view.findViewById(R.id.linearCurrentAppointments);
        linearCanceledAppointments = view.findViewById(R.id.linearCanceledAppointments);


    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.linearOpenApportunities:

                callTheOpenApportunityActivityHere();
                break;


            case R.id.linearUpcomingAppointments:
                TastyToast.makeText(getActivity(), "Coming soon", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                break;


            case R.id.linearCompletedAppointments:
                callTheCompletedAppointmentsActivityHere();
                break;

            case R.id.linearCurrentAppointments:

                callTheCurrentAppointmentsActivityHere();
                break;


            case R.id.linearCanceledAppointments:
                callTheCancelAppointmentsActivityHere();

                break;


        }

    }

    private void callTheCompletedAppointmentsActivityHere() {

        Intent compledtedAppointmentsIntent = new Intent(getActivity(), ActivityCompletedAppointments.class);
        startActivity(compledtedAppointmentsIntent);

    }

    private void callTheCancelAppointmentsActivityHere() {
        Intent cancelAppointmentsIntent = new Intent(getActivity(), ActivityCanceledAppointments.class);
        startActivity(cancelAppointmentsIntent);

    }

    private void callTheCurrentAppointmentsActivityHere() {


        Intent currentAppointmentsIntent = new Intent(getActivity(), ActivityCurrentAppointmentsList.class);
        startActivity(currentAppointmentsIntent);


    }

    private void callTheOpenApportunityActivityHere() {

        Intent openApportunityIntent = new Intent(getActivity(), ActivityOpenApportunity.class);
        startActivity(openApportunityIntent);
    }


}
