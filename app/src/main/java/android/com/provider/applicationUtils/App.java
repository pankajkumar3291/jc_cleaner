package android.com.provider.applicationUtils;

import android.app.Application;
import android.com.provider.models.OpenApportunity;
import android.com.provider.rxUtils.RxBus;
import android.com.provider15_nov_2018.R;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class App extends Application {


    public RxBus getRxBus() {
        return rxBus;
    }

    private RxBus rxBus;
    public static ArrayList<OpenApportunity> listOfValues = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/headingbrew.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );


        rxBus = new RxBus();

    }

}
