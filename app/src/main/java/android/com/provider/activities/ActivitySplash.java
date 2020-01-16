package android.com.provider.activities;

import android.com.provider.sharedPreferenceHelper.SharedPrefsHelper;
import android.com.provider15_nov_2018.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import com.orhanobut.hawk.Hawk;
import am.appwise.components.ni.NoInternetDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class ActivitySplash extends AppCompatActivity {
    private boolean isChecked = false;
    private NoInternetDialog noInternetDialog;
    SharedPrefsHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Hawk.init(this).build();

        noInternetDialog = new NoInternetDialog.Builder(this).build();
        helper = new SharedPrefsHelper(ActivitySplash.this);

        changingStatusBarColorHere();
        splashTiming();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    private void splashTiming() {

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
//                SharedPreferences settings = getSharedPreferences("PREFS_NAME", 0);
//                isChecked = settings.getBoolean("isChecked", false);
//                if (isChecked){
//                    Intent i = new Intent(ActivitySplash.this, DashboardActivity.class);
//                    startActivity(i);
//                } else {
//                    Intent i = new Intent(ActivitySplash.this, ActivitySelectLanguages.class);
//                    startActivity(i);
//                }
//                if (helper.get(AppConstant.KEEP_ME_LOGGED_IN, false)) {
//
//                    Toast.makeText(ActivitySplash.this, "True", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(ActivitySplash.this, DashboardActivity.class);
//                    startActivity(intent);
//                    finish();
//
//                } else {
//                    Toast.makeText(ActivitySplash.this, "False", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(ActivitySplash.this, ActivitySelectLanguages.class);
//                    startActivity(intent);
//                    finish();
//                }
                if (Hawk.get("keepMeLoggedInCheck", false)){
//                    if (Hawk.get("sp").equals(true))
//                    {
//                        LocaleHelper.setLocale(ActivitySplash.this,"es");
//                    }
//                    else
//                    {
//                        LocaleHelper.setLocale(ActivitySplash.this,"en");
//                    }
                    Intent intent = new Intent(ActivitySplash.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(ActivitySplash.this, ActivitySelectLanguages.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);
    }
    private void changingStatusBarColorHere(){
        getWindow().setStatusBarColor(ContextCompat.getColor(ActivitySplash.this, R.color.statusBarColor));

    }
}
