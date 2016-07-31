package peoples.materialfitness.WorkoutDetails.WorkoutDetailsActivity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.base.Optional;

import butterknife.Bind;
import butterknife.ButterKnife;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.WeightSet.WeightSet;
import peoples.materialfitness.Model.WeightUnits.WeightUnit;
import peoples.materialfitness.R;
import peoples.materialfitness.Util.PreferenceManager;
import peoples.materialfitness.View.SwipeToReveal.SwipeToRevealLayout;

/**
 * Created by Alex Sullivan on 2/15/16.
 */
public class WorkoutDetailsRecyclerAdapter extends RecyclerView.Adapter<WorkoutDetailsRecyclerAdapter.RepViewHolder>
{
    private static final String TAG = WorkoutDetailsRecyclerAdapter.class.getSimpleName();

    @NonNull
    private ExerciseSession exerciseSession;
    @Nullable
    private final SetInteractionCallback callback;
    private final boolean allowTouchEvents;
    private Optional<RepViewHolder> revealedViewHolder = Optional.absent();

    public WorkoutDetailsRecyclerAdapter(final @NonNull ExerciseSession exerciseSession,
                                         final @Nullable SetInteractionCallback callback,
                                         final boolean allowTouchEvents)
    {
        this.exerciseSession = exerciseSession;
        this.allowTouchEvents = allowTouchEvents;
        this.callback = callback;
    }

    public void setExerciseSession(final @NonNull ExerciseSession exerciseSession)
    {
        this.exerciseSession = exerciseSession;
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
        WeightSet set = exerciseSession.getSets().get(position);
        WeightUnit userWeightUnit = PreferenceManager.getInstance().getUnits();

        // TODO: Weight unit stuff.
        String formattedString = holder.positionTextView.getContext()
                .getResources()
                .getString(R.string.weight_units, set.getUserUnitsWeight(), userWeightUnit.getUnitString());
        holder.weightTextView.setText(formattedString);
        holder.positionTextView.setText(String.valueOf(position + 1));
        holder.numRepsTextView.setText(String.valueOf(set.getNumReps()));

        if (set.getIsPr())
        {
            holder.prImageView.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.prImageView.setVisibility(View.GONE);
        }

        holder.swipeToRevealLayout.returnToDefaultPositioning(false);
    }

    @Override
    public int getItemCount()
    {
        return exerciseSession.getSets().size();
    }

    protected void viewholderTouched()
    {
        hideSetOptions();
    }

    public void hideSetOptions()
    {
        if (revealedViewHolder.isPresent())
        {
            revealedViewHolder.get().hideOptions();
            revealedViewHolder = Optional.absent();
        }
    }

    public interface SetInteractionCallback
    {
        void deleteButtonClicked(int position);

        void editButtonClicked(int position);
    }

    protected final class RepViewHolder extends RecyclerView.ViewHolder
            implements SwipeToRevealLayout.SwipeLayoutCallback
    {
        @Bind(R.id.position)
        TextView positionTextView;
        @Bind(R.id.num_reps)
        TextView numRepsTextView;
        @Bind(R.id.weight)
        TextView weightTextView;
        @Bind(R.id.pr_image)
        ImageView prImageView;
        @Bind(R.id.swipeLayout)
        SwipeToRevealLayout swipeToRevealLayout;

        public RepViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);

            swipeToRevealLayout.setSwipeable(allowTouchEvents);
            swipeToRevealLayout.setCallback(this);
        }

        @Override
        public void itemRevealed(View v)
        {
            revealedViewHolder = Optional.of(this);
        }

        @Override
        public void contentTouched()
        {
            viewholderTouched();
        }

        @Override
        public void leftButtonClicked(View v)
        {
            callback.deleteButtonClicked(getAdapterPosition());
        }

        @Override
        public void rightButtonClicked(View v)
        {
            callback.editButtonClicked(getAdapterPosition());
        }

        protected void hideOptions()
        {
            swipeToRevealLayout.returnToDefaultPositioning(true);
        }
    }
}
