package peoples.materialfitness.WorkoutDetails.PastWorkoutDialog;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.WeightSet.WeightSet;
import peoples.materialfitness.R;

/**
 * Created by Alex Sullivan on 5/4/2016.
 */
public class PastWorkoutDialogRecyclerAdapter extends RecyclerView.Adapter<PastWorkoutDialogRecyclerAdapter.RepViewHolder>
{
    private final ExerciseSession exerciseSession;
    private List<WeightSet> prs = new ArrayList<>();

    public PastWorkoutDialogRecyclerAdapter(ExerciseSession exerciseSession)
    {
        this.exerciseSession = exerciseSession;
    }

    @Override
    public RepViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.historical_popup_rep, parent, false);
        return new RepViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RepViewHolder holder, int position)
    {
        WeightSet set = exerciseSession.getSets().get(position);

        // TODO: Weight unit stuff.
        // TODO TOD: Pretty sure this is wrong.
        String formattedString = holder.weight.getContext()
                .getResources()
                .getString(R.string.weight_units, set.getUserUnitsWeight(), "lbs");

        holder.weight.setText(formattedString);
        holder.numReps.setText(String.valueOf(set.getNumReps()));

        for (int i = 0; i < exerciseSession.getSets().size(); i++)
        {
            WeightSet weightSet = exerciseSession.getSets().get(i);

            if (weightSet.getId().equals(set.getId()))
            {
                holder.prImage.setVisibility(View.VISIBLE);
                break;
            }
            else
            {
                holder.prImage.setVisibility(View.GONE);
            }
        }
    }

    public void setWeightSetAsPr(WeightSet weightSet)
    {
        prs.add(weightSet);
        for (int i = 0; i < exerciseSession.getSets().size(); i++)
        {
            WeightSet set = exerciseSession.getSets().get(i);
            if (set.getId().equals(weightSet.getId()))
            {
                notifyItemChanged(i);
            }
        }
    }

    @Override
    public int getItemCount()
    {
        return exerciseSession.getSets().size();
    }

    protected static final class RepViewHolder extends RecyclerView.ViewHolder
    {
        @Bind(R.id.num_reps)
        TextView numReps;
        @Bind(R.id.weight)
        TextView weight;
        @Bind(R.id.pr_image)
        ImageView prImage;

        public RepViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
