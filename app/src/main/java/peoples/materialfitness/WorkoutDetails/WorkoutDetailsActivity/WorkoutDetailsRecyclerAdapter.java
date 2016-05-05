package peoples.materialfitness.WorkoutDetails.WorkoutDetailsActivity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.base.Optional;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.WeightSet.WeightSet;
import peoples.materialfitness.R;
import peoples.materialfitness.Util.AnimationHelpers.SwipeToRevealItemTouchHelper;

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
        else
        {
            holder.prImageView.setVisibility(View.GONE);
        }

        if (holder.contentView.getX() != 0)
        {
            holder.contentView.setX(0);
        }
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
            implements SwipeToRevealItemTouchHelper.ItemInteractionCallback
    {
        @Bind(R.id.position)
        TextView positionTextView;
        @Bind(R.id.num_reps)
        TextView numRepsTextView;
        @Bind(R.id.weight)
        TextView weightTextView;
        @Bind(R.id.pr_image)
        ImageView prImageView;
        @Bind(R.id.contentView)
        LinearLayout contentView;
        @Bind(R.id.trashButton)
        ImageButton trashButton;
        @Bind(R.id.editButton)
        ImageButton editButton;

        public RepViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);

            if (allowTouchEvents)
            {
                setSwipeHelper();
            }
        }

        @OnClick(R.id.trashButton)
        protected void trashButtonClicked()
        {
            callback.deleteButtonClicked(getAdapterPosition());
        }

        @OnClick(R.id.editButton)
        protected void editButtonClicked()
        {
            callback.editButtonClicked(getAdapterPosition());
        }

        @Override
        public void itemTouched(View v)
        {
            viewholderTouched();
        }

        @Override
        public void itemRevealed(View v)
        {
            revealedViewHolder = Optional.of(this);
        }

        private void setSwipeHelper()
        {
            SwipeToRevealItemTouchHelper helper =
                    new SwipeToRevealItemTouchHelper(this,
                                                     itemView,
                                                     trashButton,
                                                     editButton);
            contentView.setOnTouchListener(helper);
        }

        protected void hideOptions()
        {
            contentView.animate()
                    .x(0)
                    .setDuration(200)
                    .setInterpolator(new FastOutLinearInInterpolator())
                    .start();
        }
    }
}
