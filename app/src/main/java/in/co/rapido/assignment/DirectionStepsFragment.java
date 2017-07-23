package in.co.rapido.assignment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.List;

import in.co.rapido.assignment.model.Step;

/**
 * Created by farhad on 1/13/17.
 */

public class DirectionStepsFragment extends BottomSheetDialogFragment {

    private static final String TAG = DirectionStepsFragment.class.getSimpleName();
    private DirectionStepsAdapter directionStepsAdapter;
    List<Step> stepList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       stepList= getArguments().getParcelableArrayList(getString(R.string.steps_list));
    }

    public DirectionStepsFragment() {
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            switch (newState) {

                case BottomSheetBehavior.STATE_COLLAPSED:{

                    Log.d(TAG,"collapsed") ;
                }
                case BottomSheetBehavior.STATE_SETTLING:{

                    Log.d(TAG,"settling") ;
                }
                case BottomSheetBehavior.STATE_EXPANDED:{

                    Log.d(TAG,"expanded") ;
                }
                case BottomSheetBehavior.STATE_HIDDEN: {

                    Log.d(TAG , "hidden") ;
                    dismiss();
                }
                case BottomSheetBehavior.STATE_DRAGGING: {

                    Log.d(TAG,"dragging") ;
                }
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            Log.d(TAG,"sliding " + slideOffset ) ;
        }
    };

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.direction_steps_layout, null);
        dialog.setContentView(contentView);
        RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        directionStepsAdapter = new DirectionStepsAdapter(stepList,getContext());
        recyclerView.setAdapter(directionStepsAdapter);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if( behavior != null && behavior instanceof BottomSheetBehavior ) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }
}
