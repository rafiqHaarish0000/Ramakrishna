package RetrofitUtils;

import android.app.Activity;

import java.util.concurrent.TimeUnit;

import Utils.ConstantClass;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseApi
{
   /* private static Interface_Api I_COUNTRY_KEY;


    public static Interface_Api get() {

        OkHttpClient mOkHttp = new OkHttpClient();
        mOkHttp.setReadTimeout(150, TimeUnit.SECONDS);
        mOkHttp.setConnectTimeout(150, TimeUnit.SECONDS);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(mOkHttp))
                .setEndpoint(BuildConfig.CommonURL)
                .build();

        I_COUNTRY_KEY = restAdapter
                .create(Interface_Api.class);
        return I_COUNTRY_KEY;
    }*/

    public static Retrofit getApiUrl(Activity activity) {
        Retrofit retrofitDb = null;
      /*  final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();*/

        // Add Interceptor to HttpClient
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        retrofitDb = new Retrofit.Builder()
                .baseUrl(ConstantClass.BASE_URL)
               // .baseUrl("http://vilfresh-admin.pptssolutions.com")
                // .baseUrl(BASE_URL + SessionManager.get(activity, Constants.BASE_DB, "") + "/")
              //  .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofitDb;
    }

}
