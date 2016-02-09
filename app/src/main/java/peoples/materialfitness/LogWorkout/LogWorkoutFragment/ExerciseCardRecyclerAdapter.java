package peoples.materialfitness.LogWorkout.LogWorkoutFragment;

import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import peoples.materialfitness.Database.ExerciseSession;
import peoples.materialfitness.Database.WorkoutSession;
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

        ExerciseCardViewHolder viewHolder = new ExerciseCardViewHolder(cardView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ExerciseCardViewHolder holder, int position)
    {
        ExerciseSession exerciseSession = mWorkoutSession.getExercises().get(position);

        holder.mTextView.setText(exerciseSession.getExercise().getTitle());
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

    public class ExerciseCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        CardView mCardView;
        TextView mTextView;

        public ExerciseCardViewHolder(CardView cardView)
        {
            super(cardView);

            mCardView = cardView;
            mTextView = ButterKnife.findById(cardView, R.id.exercise_title);

            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            mCallback.onExerciseClicked(mWorkoutSession.getExercises().get(this.getLayoutPosition()));
        }
    }

    public interface ExerciseCardAdapterInterface
    {
        void onExerciseClicked(ExerciseSession session);
    }
}
