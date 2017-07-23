package in.co.rapido.assignment;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.co.rapido.assignment.model.DirectionResponses;
import in.co.rapido.assignment.model.EndLocation;
import in.co.rapido.assignment.model.Leg;
import in.co.rapido.assignment.model.Route;
import in.co.rapido.assignment.model.StartLocation;
import in.co.rapido.assignment.retrofit.RetrofitApiInterface;
import in.co.rapido.assignment.retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by anshul.nigam on 22/07/17.
 */

public class MapsActivityPresenter implements MapsContract.Presenter{
    MapsContract.View view;
    private static final String TAG = MapsActivityPresenter.class.getSimpleName();

    public void fetchDirections(@NonNull String origin, @NonNull String destination) {

        RetrofitApiInterface retrofitApiInterface = RetrofitClient.getClient().create(RetrofitApiInterface.class);
        Map<String, String> params = new HashMap<>();
        params.put("origin", origin);
        params.put("destination", destination);
        params.put("key", "AIzaSyAH1OCFIw-8smpergep08Tm8yy-j3Y2wQ8");
        params.put("alternatives","true");
        Call<DirectionResponses> responseCall = retrofitApiInterface.fetchDirections(params);
        responseCall.enqueue(new Callback<DirectionResponses>() {
            @Override
            public void onResponse(Call<DirectionResponses> call, retrofit2.Response<DirectionResponses> response) {
                view.sendLocations(response.body().getRoutes());
            }

            @Override
            public void onFailure(Call<DirectionResponses> call, Throwable t) {
                Log.d(TAG, call.toString());
            }
        });
    }

    public void setView(MapsContract.View view) {
        this.view = view;
    }


}
