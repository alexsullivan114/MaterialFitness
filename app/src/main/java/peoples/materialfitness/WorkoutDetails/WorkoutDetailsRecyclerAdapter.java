package peoples.materialfitness.WorkoutDetails;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import peoples.materialfitness.Database.ExerciseSession;
import peoples.materialfitness.Database.WeightSet;
import peoples.materialfitness.R;

/**
 * Created by Alex Sullivan on 2/15/16.
 */
public class WorkoutDetailsRecyclerAdapter extends RecyclerView.Adapter<WorkoutDetailsRecyclerAdapter.RepViewHolder>
{
    private ExerciseSession mExerciseSession;

    public WorkoutDetailsRecyclerAdapter(ExerciseSession exerciseSession)
    {
        mExerciseSession = exerciseSession;
    }

    @Override
    public RepViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View repLayout = inflater.inflate(R.layout.rep_adapter_layout, parent, false);
        return new RepViewHolder(repLayout);
    }

    @Override
    public void onBindViewHolder(RepViewHolder holder, int position)
    {
        WeightSet set = mExerciseSession.getSets().get(position);

        String formattedString = holder.repDetailsView.getContext()
                .getResources()
                .getString(R.string.reps_at_weight, set.getNumReps(), set.getWeight());
        holder.repDetailsView.setText(formattedString);
        holder.positionTextView.setText(String.valueOf(position + 1));
    }

    @Override
    public int getItemCount()
    {
        return mExerciseSession.getSets().size();
    }

    protected static final class RepViewHolder extends RecyclerView.ViewHolder
    {
        @Bind(R.id.position) TextView positionTextView;
        @Bind(R.id.rep_details) TextView repDetailsView;

        public RepViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
