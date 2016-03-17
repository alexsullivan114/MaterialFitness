package peoples.materialfitness.LogWorkout.LogWorkoutFragment;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.WeightSet.WeightSet;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.R;

/**
 * Created by Alex Sullivan on 1/24/16.
 */
public class ExerciseCardRecyclerAdapter extends RecyclerView.Adapter<ExerciseCardRecyclerAdapter.ExerciseCardViewHolder>
{
    private WorkoutSession mWorkoutSession;
    private ExerciseCardAdapterInterface mCallback;

    public ExerciseCardRecyclerAdapter(WorkoutSession workoutSession,
                                       ExerciseCardAdapterInterface callback)
    {
        mWorkoutSession = workoutSession;
        mCallback = callback;
    }

    @Override
    public ExerciseCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CardView cardView = (CardView)inflater.inflate(R.layout.fragment_workout_card, parent, false);

        ExerciseCardViewHolder viewHolder = new ExerciseCardViewHolder(cardView, mCallback);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ExerciseCardViewHolder holder, int position)
    {
        ExerciseSession exerciseSession = mWorkoutSession.getExercises().get(position);

        holder.mTitleView.setText(exerciseSession.getExercise().getTitle());
        holder.setExerciseSession(exerciseSession);
        // Clear our any old views.
        holder.mRepContainer.removeAllViews();
        // Now loop through our weights and add each one to our container.
        for (WeightSet weightSet : exerciseSession.getSets())
        {
            LayoutInflater inflater = LayoutInflater.from(holder.mCardView.getContext());
            TextView textView = (TextView)inflater.inflate(R.layout.workout_card_rep, holder.mRepContainer, false);

            String formattedString = holder.mRepContainer.getContext()
                    .getResources()
                    .getString(R.string.reps_at_weight, weightSet.getNumReps(), weightSet.getWeight());

            textView.setText(formattedString);
            holder.mRepContainer.addView(textView);
        }
    }

    @Override
    public int getItemCount()
    {
        return mWorkoutSession.getExercises().size();
    }

    public void updateExerciseCard(ExerciseSession exerciseSession)
    {
        if (!mWorkoutSession.containsExercise(exerciseSession.getExercise()))
        {
            mWorkoutSession.addExerciseSession(exerciseSession);
        }

        notifyItemInserted(mWorkoutSession.getExercises().size() - 1);
    }

    protected static class ExerciseCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        CardView mCardView;
        ExerciseCardAdapterInterface mCallback;
        ExerciseSession mExerciseSession;
        @Bind(R.id.exercise_title) TextView mTitleView;
        @Bind(R.id.rep_container) LinearLayout mRepContainer;

        public ExerciseCardViewHolder(CardView cardView, ExerciseCardAdapterInterface callback)
        {
            super(cardView);

            mCardView = cardView;
            mCallback = callback;

            ButterKnife.bind(this, cardView);

            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            mCallback.onExerciseClicked(mExerciseSession);
        }

        public void setExerciseSession(ExerciseSession exerciseSession)
        {
            mExerciseSession = exerciseSession;
        }
    }

    /**
     * Interface for communication between this adapter and the calling/creating class.
     */
    public interface ExerciseCardAdapterInterface
    {
        void onExerciseClicked(ExerciseSession session);
    }

    public void setWorkoutSession(WorkoutSession workoutSession)
    {
        mWorkoutSession = workoutSession;
        notifyDataSetChanged();
    }
}
