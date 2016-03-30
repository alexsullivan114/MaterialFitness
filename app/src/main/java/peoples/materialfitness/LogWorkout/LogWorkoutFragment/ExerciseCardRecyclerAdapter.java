package peoples.materialfitness.LogWorkout.LogWorkoutFragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.WeightSet.WeightSet;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.R;
import peoples.materialfitness.Util.HeightAnimator;

/**
 * Created by Alex Sullivan on 1/24/16.
 */
public class ExerciseCardRecyclerAdapter extends RecyclerView.Adapter<ExerciseCardRecyclerAdapter.ExerciseCardViewHolder>
{
    private WorkoutSession mWorkoutSession;
    private ExerciseCardAdapterInterface mCallback;
    private static final int EXPAND_ANIMATION_DURATION = 600;

    private static final int NUM_DISPLAY_SETS = 4;

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

        holder.mTitleView.setText(exerciseSession.getExercise().getTitle());
        // Clear our any old views.
        holder.mRepContainer.removeAllViews();
        // Now loop through our weights and add each one to our container.
        for (int i = 0; i < exerciseSession.getSets().size(); i++)
        {
            WeightSet weightSet = exerciseSession.getSets().get(i);
            int visibility = i <= NUM_DISPLAY_SETS ? View.VISIBLE : View.GONE;
            addWeightSetViewToHolder(holder, weightSet, visibility);
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

    private void addWeightSetViewToHolder(ExerciseCardViewHolder holder, WeightSet weightSet, int visibility)
    {
        LayoutInflater inflater = LayoutInflater.from(holder.mCardView.getContext());
        LinearLayout setContainer = (LinearLayout)inflater.inflate(R.layout.workout_card_rep, holder.mRepContainer, false);

        TextView repsTextView = (TextView)setContainer.findViewById(R.id.num_reps);
        TextView weightTextView = (TextView)setContainer.findViewById(R.id.weight);

        String repsString = String.valueOf(weightSet.getNumReps());
        // TODO: Use our weight units. Should be attached to the rep.
        String weightString = holder.mCardView.getContext().getResources().getString(R.string.weight_units, weightSet.getWeight(), "lbs");

        repsTextView.setText(repsString);
        weightTextView.setText(weightString);

        setContainer.setVisibility(visibility);

        holder.mRepContainer.addView(setContainer);
    }

    protected class ExerciseCardViewHolder extends RecyclerView.ViewHolder
    {
        CardView mCardView;

        @Bind(R.id.exercise_title) TextView mTitleView;
        @Bind(R.id.rep_container) LinearLayout mRepContainer;
        @Bind(R.id.dropdown_layout) LinearLayout dropDownLayout;
        @Bind(R.id.dropdown_indicator) ImageView dividerView;

        boolean isDropdownToggled = false;

        public ExerciseCardViewHolder(CardView cardView)
        {
            super(cardView);

            mCardView = cardView;

            ButterKnife.bind(this, cardView);
        }

        @OnClick(R.id.card_view)
        public void onCardClicked(View v)
        {
            ExerciseCardRecyclerAdapter.this.onCardClicked(this);
        }

        @OnClick(R.id.dropdown_layout)
        public void onDropDownClicked(View v)
        {
            ExerciseCardRecyclerAdapter.this.onDropdownClicked(this);
        }
    }

    private void onDropdownClicked(ExerciseCardViewHolder viewHolder)
    {
        boolean isDropdownToggled = viewHolder.isDropdownToggled;

        if (isDropdownToggled)
        {
            removeWeightSets(viewHolder);
        }
        else
        {
            addWeightSets(viewHolder);
        }

        viewHolder.isDropdownToggled = !viewHolder.isDropdownToggled;
    }

    private void addWeightSets(ExerciseCardViewHolder viewHolder)
    {
        ExerciseSession exerciseSession = mWorkoutSession.getExercises().get(viewHolder.getAdapterPosition());
        int spilloverSetsNum = exerciseSession.getSets().size() - NUM_DISPLAY_SETS;
        // Note: We know we have at least NUM_DISPLAY_SETS children here, which is why this is safe.
        int heightOfSetsToAdd = viewHolder.mRepContainer.getChildAt(0).getHeight() * spilloverSetsNum;
        int heightOfContainer = viewHolder.mCardView.getHeight();

        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(viewHolder.dropDownLayout, "translationY", heightOfSetsToAdd);
        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(viewHolder.dividerView, "rotation", 0f, 180f);
        HeightAnimator heightAnimator = new HeightAnimator(viewHolder.mCardView, heightOfContainer + heightOfSetsToAdd);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(heightAnimator, translationAnimator, rotationAnimator);
        animatorSet.setDuration(EXPAND_ANIMATION_DURATION);
        animatorSet.setInterpolator(new FastOutSlowInInterpolator());
        animatorSet.start();
    }

    private void removeWeightSets(ExerciseCardViewHolder viewHolder)
    {
        ExerciseSession exerciseSession = mWorkoutSession.getExercises().get(viewHolder.getAdapterPosition());
        int spilloverSetsNum = exerciseSession.getSets().size() - NUM_DISPLAY_SETS;
        // Note: We know we have at least NUM_DISPLAY_SETS children here, which is why this is safe.
        int heightOfSetsToRemove = viewHolder.mRepContainer.getChildAt(0).getHeight() * spilloverSetsNum;
        int heightOfContainer = viewHolder.mCardView.getHeight();
        int desiredHeight = heightOfContainer - heightOfSetsToRemove;

        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(viewHolder.dropDownLayout, "translationY", 0);
        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(viewHolder.dividerView, "rotation", 180f, 360f);
        HeightAnimator heightAnimator = new HeightAnimator(viewHolder.mCardView, desiredHeight);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(heightAnimator, translationAnimator, rotationAnimator);
        animatorSet.setDuration(EXPAND_ANIMATION_DURATION);
        animatorSet.setInterpolator(new FastOutSlowInInterpolator());
        animatorSet.start();
    }

    private void animateDividerView(boolean isDropdownToggled, ImageView dividerView)
    {

        int pivotX = dividerView.getWidth()/2;
        int pivotY = dividerView.getHeight()/2;
        int startDegrees = isDropdownToggled ? 180 : 0;
        int endDegrees = isDropdownToggled ? 360 : 180;

        RotateAnimation rotateAnimation = new RotateAnimation(startDegrees, endDegrees, pivotX, pivotY);
        rotateAnimation.setInterpolator(new FastOutSlowInInterpolator());
        rotateAnimation.setDuration(500);
        rotateAnimation.setFillAfter(true);
        dividerView.startAnimation(rotateAnimation);
    }

    private void onCardClicked(ExerciseCardViewHolder viewHolder)
    {
        ExerciseSession exerciseSession = mWorkoutSession.getExercises().get(viewHolder.getAdapterPosition());
        mCallback.onExerciseClicked(exerciseSession);
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
