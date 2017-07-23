package in.co.rapido.assignment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.co.rapido.assignment.model.Step;

/**
 * Created by anshul.nigam on 23/07/17.
 */

class DirectionStepsAdapter extends RecyclerView.Adapter<DirectionStepsAdapter.StepsHolder>{
   private  List<Step> stepList;
   private Context mContext;

    public DirectionStepsAdapter(List<Step> stepList, Context context) {
        this.stepList = stepList;
        this.mContext = context;
    }

    @Override
    public StepsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.steps_item_view,parent,false);
        return new StepsHolder(v);
    }

    @Override
    public void onBindViewHolder(StepsHolder holder, int position) {
        if(null == stepList || stepList.size() ==0)
            return;
        Step step = stepList.get(position);
        holder.mTiming.setText("Time :"+step.getDuration().getText());
        holder.mDistance.setText("Distance: " +step.getDistance().getText() );
        holder.mInstructions.setText(Html.fromHtml(step.getHtmlInstructions()));
    }


    @Override
    public int getItemCount() {
        if(null != stepList && !stepList.isEmpty())
             return stepList.size();
        else
            return 0;
    }
    public class StepsHolder extends RecyclerView.ViewHolder{
        private TextView mInstructions;
        private  TextView mDistance;
        private TextView mTiming;

        public StepsHolder(View itemView) {
            super(itemView);
            mInstructions = (TextView) itemView.findViewById(R.id.step_instruction);
            mDistance = (TextView) itemView.findViewById(R.id.step_distance);
            mTiming = (TextView) itemView.findViewById(R.id.step_time);
        }
    }
}
