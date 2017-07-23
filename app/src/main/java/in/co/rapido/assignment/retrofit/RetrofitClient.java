package in.co.rapido.assignment.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by anshul.nigam on 22/07/17.
 */

public class RetrofitClient {
    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().
                    addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL).
                    build();
        }
        return retrofit;
    }
}
