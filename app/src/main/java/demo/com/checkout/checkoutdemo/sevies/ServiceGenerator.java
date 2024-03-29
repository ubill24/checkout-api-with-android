package demo.com.checkout.checkoutdemo.sevies;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ServiceGenerator {

//    public static final String API_BASE_URL = "https://365d76d2.ngrok.io/";
    public static final String API_BASE_URL = "https://checkoutapi-demo.bill24.net/";
    public static final String TOKEN = "a8024ffe355342ef890fcebed5ad3009";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass ){

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request original = chain.request();

                Request.Builder requestBuilder = original.newBuilder()
                        .header("token" , TOKEN)
                        .header("Content-Type" , "application/json")
                        .header("Accept" , "application/json")
                        .method(original.method() , original.body()) ;

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }
}
