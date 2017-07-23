package in.co.rapido.assignment;

import android.content.Context;

import java.util.List;

import in.co.rapido.assignment.model.Route;

/**
 * Created by anshul.nigam on 23/07/17.
 */

public class MapsContract {
    public interface View{
        Context getContext();

        void sendLocations(List<Route> integerLatLngMap);
    }
    public interface Presenter{
        void fetchDirections(String s, String toString);
    }
}
