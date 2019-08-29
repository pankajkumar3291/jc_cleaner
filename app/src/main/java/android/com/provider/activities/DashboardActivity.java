package android.com.provider.activities;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.com.provider.apiResponses.displayProviderProfile.DisplayProfile;
import android.com.provider.apiResponses.getOnOffApi.OnOff;
import android.com.provider.apiResponses.providerFullDetails.ProviderFullDetails;
import android.com.provider.apiResponses.providerLogout.ProviderLogoutRequest;
import android.com.provider.apiResponses.refferalCode.RefferalCode;
import android.com.provider.fragments.FragmentAddress;
import android.com.provider.httpRetrofit.HttpModule;
import android.com.provider.rxUtils.AppConstant;
import android.com.provider.sharedPreferenceHelper.SharedPrefsHelper;
import android.com.provider15_nov_2018.R;
import android.com.provider.fragments.FragmentBio;
import android.com.provider.fragments.FragmentContactSupport;
import android.com.provider.fragments.FragmentHome;
import android.com.provider.fragments.FragmentPayout;
import android.com.provider.fragments.FragmentReferAFriend;
import android.com.provider.fragments.FragmentShop;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;
import java.util.Objects;
import am.appwise.components.ni.NoInternetDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {
    // navigation links   https://android-arsenal.com/tag/90
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private FragmentManager fragmentManager;
    private NavigationView navigationView;
    private FrameLayout frameLayout;
    private TextView logout, tvNo, tvYes;
    private CircleImageView providerProfile;
    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private ImageView dashboardlogo;
    private TextView name, email;
    private NoInternetDialog noInternetDialog;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private SharedPrefsHelper sharedPrefsHelper;
    private boolean doubleBackToExitPressedOnce = false;
    private ProgressDialog mProgressDialog;
    private Switch switchBtn;
    private RatingBar ratingBar;
    private String workingStatus;
    private boolean isWorking = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Hawk.init(DashboardActivity.this).build();
        dashboardlogo = findViewById(R.id.dashboard_logo);
        noInternetDialog = new NoInternetDialog.Builder(DashboardActivity.this).build();
        sharedPrefsHelper = new SharedPrefsHelper(DashboardActivity.this);
        mProgressDialog = new ProgressDialog(DashboardActivity.this, R.style.AppTheme_Dark_Dialog);
        getWindow().setStatusBarColor(ContextCompat.getColor(DashboardActivity.this, R.color.statusBarColor));
        fragmentManager = getSupportFragmentManager();
        setupView();
        providerFullDetailsApiGoesHere();
        if (savedInstanceState == null) showHome();
    }
    private void providerFullDetailsApiGoesHere() {
        compositeDisposable.add(HttpModule.provideRepositoryService().providerFullDetails(String.valueOf(Hawk.get("savedUserId"))).
                subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<ProviderFullDetails>(){
                    @Override
                    public void accept(ProviderFullDetails providerFullDetails) throws Exception {
                        if (providerFullDetails != null && providerFullDetails.getIsSuccess()) {
                            Hawk.put("PROVIDER_NAME_DASHBOARD", providerFullDetails.getPayLoad().getUserDetails().getFirstName());
                            name.setText(providerFullDetails.getPayLoad().getUserDetails().getFirstName());
                            email.setText(providerFullDetails.getPayLoad().getUserDetails().getEmail());
                            Hawk.put("serviceidn", providerFullDetails.getPayLoad().getUserDetails().getServices());
                            if (providerFullDetails.getPayLoad().getUserDetails().getWorking_status().equalsIgnoreCase("1")) {
                                switchBtn.setText("ON");
                                switchBtn.setChecked(true);
                                isWorking = false;
                            } else {
                                switchBtn.setText("OFF");
                                switchBtn.setChecked(false);
                                isWorking = false;
                            }
                            workingStatus = providerFullDetails.getPayLoad().getUserDetails().getWorking_status();
                            Hawk.put("WORKING_STATUS", workingStatus);
                            System.out.println("DashboardActivity.accept------------------" + workingStatus);
                            System.out.println("DashboardActivity.accept " + providerFullDetails.getMessage());
                        } else {
                            System.out.println("DashboardActivity.accept " + Objects.requireNonNull(providerFullDetails).getMessage());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("DashboardActivity.accept " + throwable.toString());
                    }
                }));
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
    private void showHome() {
        selectDrawerItem(navigationView.getMenu().getItem(0));
//        drawerLayout.openDrawer(GravityCompat.START); // its bydefault opening the drawer
    }
    private void selectDrawerItem(MenuItem menuItem) {
        boolean specialToolbarBehaviour = false;
        Class fragmentClass = null;
        switch (menuItem.getItemId()) {
            case R.id.drawer_home:
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                fragmentClass = FragmentHome.class;
                dashboardlogo.setVisibility(View.VISIBLE);
                break;
            case R.id.drawer_address:
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                dashboardlogo.setVisibility(View.GONE);
                fragmentClass = FragmentAddress.class;
//                specialToolbarBehaviour = true;
                break;
            case R.id.invite_contact_support:
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                dashboardlogo.setVisibility(View.GONE);
                fragmentClass = FragmentContactSupport.class;
                break;
            case R.id.drawer_refer_a_friend:
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                dashboardlogo.setVisibility(View.GONE);
                fragmentClass = FragmentReferAFriend.class;
                break;
//            case R.id.payout:
//                getSupportActionBar().setDisplayShowTitleEnabled(true);
//                dashboardlogo.setVisibility(View.GONE);
//                fragmentClass = FragmentPayout.class;
//                break;
            case R.id.bio:
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                dashboardlogo.setVisibility(View.GONE);
                fragmentClass = FragmentBio.class;
                break;
//            case R.id.shop:
//                getSupportActionBar().setDisplayShowTitleEnabled(true);
//                dashboardlogo.setVisibility(View.GONE);
//                fragmentClass = FragmentShop.class;
//                break;
            default:
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                fragmentClass = FragmentHome.class;
                break;
        }
        try {
            Fragment fragment = (Fragment) fragmentClass.newInstance();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        setToolbarElevation(specialToolbarBehaviour);
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drawerLayout.closeDrawers();
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setToolbarElevation(boolean specialToolbarBehaviour) {
        if (specialToolbarBehaviour) {
            toolbar.setElevation(0.0f);
            frameLayout.setElevation(getResources().getDimension(R.dimen.elevation_toolbar));
        } else {
            toolbar.setElevation(getResources().getDimension(R.dimen.elevation_toolbar));
            frameLayout.setElevation(0.0f);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        drawerToggle.syncState();
        super.onPostCreate(savedInstanceState);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
    private void setupView() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        frameLayout = findViewById(R.id.content_frame);
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(this);
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {
                name.setText((CharSequence) Hawk.get("PROVIDER_NAME_DASHBOARD"));
            }
            @Override
            public void onDrawerOpened(@NonNull View view){
//                providerFullDetailsApiGoesHere();
            }
            @Override
            public void onDrawerClosed(@NonNull View view) {

            }
            @Override
            public void onDrawerStateChanged(int i) {
                try {
                    displayProfileOnly();
                } catch (Exception exception) {
                    System.out.println("DashboardActivity.onDrawerStateChanged " + exception);
                }
            }
        });
        navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);
        name = headerView.findViewById(R.id.name);
        email = headerView.findViewById(R.id.email);
        providerProfile = headerView.findViewById(R.id.providerProfile);
        ratingBar = headerView.findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                ratingBar.setRating(3.0f);
            }
        });
        ratingBar.setIsIndicator(false);
        ratingBar.setClickable(false);
        ratingBar.setFocusable(false);
        ratingBar.setStepSize(1.0f);
        Drawable drawable = ratingBar.getProgressDrawable();
        drawable.setColorFilter(Color.parseColor("#FFFDD433"), PorterDuff.Mode.SRC_ATOP);
        switchBtn = headerView.findViewById(R.id.switchBtn);
        providerProfile.setOnClickListener(this);
//        //obtain  Intent Object send  from SenderActivity
//        Intent intent = this.getIntent();
//
//        /* Obtain String from Intent  */
//        if (intent != null) {
//            String strdata = intent.getExtras().getString("Uniqid");
//
//            if (strdata.equals("ActivitySignUp")) {
////                name.setText((CharSequence) Hawk.get("FIRST_NAME"));
////                email.setText((CharSequence) Hawk.get("EMAIL_ID"));
//            }
//            if (strdata.equals("ActivitySignIn")) {
////                name.setText((CharSequence) Hawk.get("USER_NAME"));
////                email.setText((CharSequence) Hawk.get("EMAIL_ID_SIGNIN"));
//            }
//
//        } else {
//            TastyToast.makeText(getApplicationContext(), "Nothing Matched", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
//        }
//
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                selectDrawerItem(menuItem);
                return true;
            }
        });
        switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (!isWorking) {
                    if (isChecked) {
                        activeInActiveApi();
                    } else {
                        activeInActiveApi();
                    }
                }
            }
        });
    }
    private void activeInActiveApi(){
        compositeDisposable.add(HttpModule.provideRepositoryService().
                onOffApi(String.valueOf(Hawk.get("savedUserId"))).
                subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<OnOff>() {
                    @Override
                    public void accept(OnOff onOff) throws Exception {
                        if (onOff != null && onOff.getIsSuccess()) {
                            if (onOff.getWorkingStatus() == 1) {
                                switchBtn.setText("ON");
                                switchBtn.setTextColor(Color.parseColor("#FFFFFF"));
//                                switchBtn.setChecked(true);
                            } else {
                                switchBtn.setText("OFF");
                                switchBtn.setTextColor(Color.parseColor("#FFFFFF"));
//                                switchBtn.setChecked(false);
                            }
                            TastyToast.makeText(DashboardActivity.this, Objects.requireNonNull(onOff).getMessage(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                        } else{
                            TastyToast.makeText(DashboardActivity.this, Objects.requireNonNull(onOff).getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        }
                    }
                }, new Consumer<Throwable>(){
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("DashboardActivity.accept " + throwable.toString());
                    }
                }));
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.logout:
                logoutAlertDialog();
                break;
            case R.id.providerProfile:
                break;
        }
    }
    private void displayProfileOnly() {
        HttpModule.provideRepositoryService().displayProviderImage(String.valueOf(Hawk.get("savedUserId"))).enqueue(new Callback<DisplayProfile>() {
            @Override
            public void onResponse(Call<DisplayProfile> call, Response<DisplayProfile> response) {
                if (response.body() != null && Objects.requireNonNull(response.body()).getIsSuccess()) {
//                    TastyToast.makeText(DashboardActivity.this, response.body().getMessage(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                    Picasso.get()
                            .load(Objects.requireNonNull(response.body()).getPayLoad())
                            .placeholder(R.drawable.provider_default)
                            .error(R.drawable.provider_default)
                            .into(providerProfile);
                    Hawk.put(AppConstant.SAVE_IMAGE_DASHBOARD, response.body().getPayLoad());
                } else {
                    Picasso.get()
                            .load(R.drawable.provider_default)
                            .placeholder(R.drawable.provider_default)
                            .error(R.drawable.provider_default)
                            .into(providerProfile);
//                    TastyToast.makeText(DashboardActivity.this, Objects.requireNonNull(response.body()).getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                }
            }
            @Override
            public void onFailure(Call<DisplayProfile> call, Throwable t) {
                TastyToast.makeText(DashboardActivity.this, t.toString(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
            }
        });
    }
    private void logoutAlertDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View dialogView = li.inflate(R.layout.dialog_logout, null);
        findingLogoutDialodIdsHere(dialogView);
        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(false);
        alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }
    private void findingLogoutDialodIdsHere(View dialogView) {
        tvNo = dialogView.findViewById(R.id.tvNo);
        tvYes = dialogView.findViewById(R.id.tvYes);
        takingClickOfLogout();
    }
    private void takingClickOfLogout() {
        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                TastyToast.makeText(DashboardActivity.this, "Thank u for staying with us!", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
            }
        });
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
//                SharedPreferences settings = getSharedPreferences("PREFS_NAME", 0);
//                isChecked = settings.getBoolean("isChecked", false);
//                SharedPreferences.Editor editor = settings.edit();
//                editor.clear();
//                editor.apply();
//                sharedPrefsHelper.deleteSavedData(AppConstant.KEEP_ME_LOGGED_IN);

             callLogoutApi();
            }
        });
    }
    private void callLogoutApi() {
        final ProgressDialog mProgressDialog;
        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        mProgressDialog.setMessage("wait...");

       try
       {
           compositeDisposable.add(HttpModule.provideRepositoryService().logoutApi(Hawk.get("savedUserId",0))
                   .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .subscribe(new Consumer<ProviderLogoutRequest>() {
                       @Override
                       public void accept(ProviderLogoutRequest refferalCode) throws Exception {
                           mProgressDialog.dismiss();
                           if (refferalCode.getIsSuccess())
                           {
                               Hawk.put("keepMeLoggedInCheck", false);
                               Intent intent = new Intent(DashboardActivity.this, ActivityMain.class);
                               startActivity(intent);
                               finish();
                           }
                           else
                           {
                               Toast.makeText(DashboardActivity.this,refferalCode.getMessage(),Toast.LENGTH_SHORT).show();
                           }
                       }
                   }, new Consumer<Throwable>() {
                       @Override
                       public void accept(Throwable throwable) throws Exception {
                           mProgressDialog.dismiss();
                       }
                   }));
       }
       catch (Exception e)
       {
           mProgressDialog.dismiss();
           System.out.println("Exception :"+e.getMessage());

       }
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        TastyToast.makeText(DashboardActivity.this, "Please click back again to exit from the app", TastyToast.LENGTH_SHORT, TastyToast.WARNING).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
