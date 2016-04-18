package peoples.materialfitness.WorkoutDetails;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.WeightSet.WeightSet;
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

        // TODO: Weight unit stuff.
        String formattedString = holder.positionTextView.getContext()
                .getResources()
                .getString(R.string.weight_units, set.getWeight(), "lbs");
        holder.weightTextView.setText(formattedString);
        holder.positionTextView.setText(String.valueOf(position + 1));
        holder.numRepsTextView.setText(String.valueOf(set.getNumReps()));

        if (set.getIsPr())
        {
            holder.prImageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount()
    {
        return mExerciseSession.getSets().size();
    }

    protected static final class RepViewHolder extends RecyclerView.ViewHolder
    {
        @Bind(R.id.position) TextView positionTextView;
        @Bind(R.id.num_reps) TextView numRepsTextView;
        @Bind(R.id.weight) TextView weightTextView;
        @Bind(R.id.pr_image) ImageView prImageView;

        public RepViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
