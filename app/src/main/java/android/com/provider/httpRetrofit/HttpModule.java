package android.com.provider.httpRetrofit;


import android.com.provider15_nov_2018.BuildConfig;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpModule {

    private static OkHttpClient getOkkHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder().writeTimeout(60,TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS).build();
    }
    //http://192.168.0.3/CleanerUp/cleaning_service/public/api
// .baseUrl("http://192.168.0.42/CleanerUp/cleaning_service/public/api/")
    public static Retrofit getRetroFitClient(){
        return new Retrofit.Builder()
                //http://192.168.0.42
                .baseUrl("http://smartit.ventures/CS/cleaning_service/public/api/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .client(getOkkHttpClient())
                .build();
        //http://192.168.0.4/CleanerUp/cleaning_service/public/login
//http://smartit.ventures/CS/cleaning_service/public/api/
        //http://smartit.ventures/CS/cleaning_service/public/api/
//        192.168.0.8/CleanerUp/cleaning_service/public/api/provider/updateProviderProfile
    }

    public static RemoteRepositoryService provideRepositoryService() {
        return getRetroFitClient().create(RemoteRepositoryService.class);
    }


}

