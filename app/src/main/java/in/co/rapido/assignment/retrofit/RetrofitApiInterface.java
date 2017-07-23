package in.co.rapido.assignment.retrofit;

import java.util.Map;

import in.co.rapido.assignment.model.DirectionResponses;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by anshul.nigam on 22/07/17.
 */

public interface RetrofitApiInterface {
    @GET("directions/json")
    Call<DirectionResponses> fetchDirections(@QueryMap Map<String, String> params);
}
