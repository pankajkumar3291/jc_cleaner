package android.com.provider.activities;

import android.com.provider.adapters.FaqAdapter;
import android.com.provider.apiResponses.getFAQ.FAQ;
import android.com.provider.httpRetrofit.HttpModule;
import android.com.provider15_nov_2018.R;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.sdsmdg.tastytoast.TastyToast;

import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityFAQ extends AppCompatActivity {


    private RecyclerView faqRecycler;
    ImageView backarr;


    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    private NoInternetDialog noInternetDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);


        noInternetDialog = new NoInternetDialog.Builder(this).build();

        changingStatusBarColorHere();

        backarr = findViewById(R.id.backarr);

        faqRecycler = findViewById(R.id.faqRecycler);


        callingTheFAQApisHere();
        backarr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void callingTheFAQApisHere() {


        compositeDisposable.add(HttpModule.provideRepositoryService().getFAQAPI().
                subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<FAQ>() {


                    @Override
                    public void accept(FAQ faq) throws Exception {

                        if (faq != null && faq.getIsSuccess()) {


                            TastyToast.makeText(ActivityFAQ.this, "You can now able to see your question and answers", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

                            FaqAdapter expandableListAdapter = new FaqAdapter(ActivityFAQ.this, faq.getPayload());
                            faqRecycler.setHasFixedSize(true);
                            faqRecycler.setLayoutManager(new LinearLayoutManager(ActivityFAQ.this));
                            faqRecycler.setAdapter(expandableListAdapter);

                        } else {

                            TastyToast.makeText(ActivityFAQ.this, "500 Internal server error", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        }


                    }


                }, new Consumer<Throwable>() {


                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        TastyToast.makeText(ActivityFAQ.this, throwable.toString(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();

                    }


                }));


    }

    private void changingStatusBarColorHere() {
        getWindow().setStatusBarColor(ContextCompat.getColor(ActivityFAQ.this, R.color.statusBarColor));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }
}
