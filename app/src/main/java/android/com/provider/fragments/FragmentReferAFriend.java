package android.com.provider.fragments;

import android.app.ProgressDialog;
import android.com.provider.apiResponses.refferalCode.RefferalCode;
import android.com.provider.httpRetrofit.HttpModule;
import android.com.provider15_nov_2018.R;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class FragmentReferAFriend extends Fragment implements View.OnClickListener {

    private View view;
    private TextView tvReferNow, tvRefferalCode;
    private ProgressDialog mProgressDialog;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_refered_a_friend, container, false);

        findingIdsHere(view);
        eventsListenerHere();

        loginProgressing();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                refferalCodeApiGoesHere();
            }
        }, 3000);


        return view;
    }

    private void loginProgressing() {

        mProgressDialog.setMessage("Loading..");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();

    }

    private void refferalCodeApiGoesHere() {


        compositeDisposable.add(HttpModule.provideRepositoryService().referralCode(String.valueOf(Hawk.get("savedUserId"))).subscribeOn(io.reactivex.schedulers.Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<RefferalCode>() {

            @Override
            public void accept(RefferalCode refferalCode) throws Exception {


                if (refferalCode != null && refferalCode.getIsSuccess()) {

                    mProgressDialog.dismiss();
                    tvRefferalCode.setText(refferalCode.getPayload());
                    TastyToast.makeText(getActivity(), refferalCode.getPayload(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();

                } else {

                    mProgressDialog.dismiss();
                    TastyToast.makeText(getActivity(), Objects.requireNonNull(refferalCode).getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();

                }


            }

        }, new Consumer<Throwable>() {

            @Override
            public void accept(Throwable throwable) throws Exception {

                mProgressDialog.dismiss();
                TastyToast.makeText(getActivity(), throwable.toString(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();

            }

        }));


    }

    private void eventsListenerHere() {
        tvReferNow.setOnClickListener(this);
    }


    private void findingIdsHere(View view) {

        tvReferNow = view.findViewById(R.id.tvReferNow);
        tvRefferalCode = view.findViewById(R.id.tvRefferalCode);

        mProgressDialog = new ProgressDialog(getActivity(), R.style.AppTheme_Dark_Dialog);

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tvReferNow:
                shareWithSocial();
                break;

        }

    }

    private void shareWithSocial() {


        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "The app");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "https://play.google.com/sto...");
        startActivity(Intent.createChooser(sharingIntent, "Share via"));

    }


}
