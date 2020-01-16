package android.com.provider.activities;

import android.com.provider15_nov_2018.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.sdsmdg.tastytoast.TastyToast;

import am.appwise.components.ni.NoInternetDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityMain extends AppCompatActivity implements View.OnClickListener {


    private Button btnLogin, btnRegister;
    private TextView tvAboutUs;

    private NoInternetDialog noInternetDialog;
    private TextView tvCompanyInfo, tvOffredServices, tvFAQ, tvTermsAndConditions, tvRulesRegulation, tvAppyToWork;
    private ImageView down_arrow;
    DialogPlus dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noInternetDialog = new NoInternetDialog.Builder(this).build();
        changingStatusBarColorHere();
        findingIdsHere();
        eventsListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }

    private void changingStatusBarColorHere() {
        getWindow().setStatusBarColor(ContextCompat.getColor(ActivityMain.this, R.color.statusBarColor));
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    private void eventsListener() {

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        tvAboutUs.setOnClickListener(this);
    }

    private void findingIdsHere() {
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        tvAboutUs = findViewById(R.id.tvAboutUs);
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.btnLogin:
                Intent intentBtnLogin = new Intent(ActivityMain.this, ActivitySignIn.class);
                startActivity(intentBtnLogin);
                finish();
//                setupWindowAnimations();

                break;


            case R.id.btnRegister:

                Intent intentBtnRegister = new Intent(ActivityMain.this, ActivitySignUp.class);
                startActivity(intentBtnRegister);

                break;


            case R.id.tvAboutUs:


                // https://github.com/orhanobut/dialogplus
                dialog = DialogPlus.newDialog(this)
                        .setContentHolder(new ViewHolder(R.layout.dialog_footer))
                        .setExpanded(true, 700).setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(DialogPlus dialog, View view) {

                                findingDialogIdsHere();

                                if (view == down_arrow) {

                                    down_arrow.setRotation(370);
                                    dialog.dismiss();

                                } else if (view == tvCompanyInfo) {
                                    callTheActivityHere(view);

                                } else if (view == tvOffredServices) {
                                    callTheActivityHere(view);

                                } else if (view == tvFAQ) {
                                    callTheActivityHere(view);

                                } else if (view == tvTermsAndConditions) {
                                    callTheActivityHere(view);

                                } else if (view == tvRulesRegulation) {
                                    callTheActivityHere(view);

                                }

                            }
                        })
                        .create();

                dialog.show();


                break;


        }


    }

    private void callTheActivityHere(View view) {

        if (view == tvCompanyInfo) {

            Intent intent = new Intent(ActivityMain.this, ActivityCompanyInfo.class);
            startActivity(intent);

        } else if (view == tvOffredServices) {

            Intent intent = new Intent(ActivityMain.this, ActivityOffredServices.class);
            startActivity(intent);
        } else if (view == tvTermsAndConditions) {
            Intent intent = new Intent(ActivityMain.this, ActivityTermsConditions.class);
            startActivity(intent);
        } else if (view == tvRulesRegulation) {
            Intent intent = new Intent(ActivityMain.this, ActivityRulesRegulations.class);
            startActivity(intent);
        } else if (view == tvFAQ) {
            Intent intent = new Intent(ActivityMain.this, ActivityFAQ.class);
            startActivity(intent);
        }


    }


//    private void setupWindowAnimations() {
//        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);


    private void findingDialogIdsHere() {

        tvCompanyInfo = findViewById(R.id.tvCompanyInfo);
        tvOffredServices = findViewById(R.id.tvOffredServices);
        tvFAQ = findViewById(R.id.tvFAQ);
        tvTermsAndConditions = findViewById(R.id.tvTermsAndConditions);
        tvRulesRegulation = findViewById(R.id.tvRulesRegulation);
        down_arrow = findViewById(R.id.down_arrow);
        tvAppyToWork = findViewById(R.id.tvAppyToWork);

    }


}
