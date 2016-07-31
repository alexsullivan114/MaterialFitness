package peoples.materialfitness.Schedule.ScheduleDay;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import peoples.materialfitness.Model.Exercise.Exercise;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.R;
import peoples.materialfitness.View.SwipeToReveal.SwipeToRevealLayout;

/**
 * Created by Alex Sullivan on 6/6/2016.
 */
public class ScheduleDayRecyclerAdapter extends RecyclerView.Adapter<ScheduleDayRecyclerAdapter.ScheduleDayViewHolder>
{
    private List<Exercise> exercises;
    private final ScheduleDayAdapterCallback callback;

    public ScheduleDayRecyclerAdapter(List<Exercise> exercises, ScheduleDayAdapterCallback callback)
    {
        this.exercises= exercises;
        this.callback = callback;
    }

    @Override
    public ScheduleDayViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.schedule_day_adapter_layout, parent, false);
        return new ScheduleDayViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ScheduleDayViewHolder holder, int position)
    {
        Exercise exercise = exercises.get(position);
        holder.textView.setText(exercise.getTitle());
    }

    @Override
    public int getItemCount()
    {
        return exercises.size();
    }

    public void addExercise(Exercise exercise)
    {
        exercises.add(exercise);
        notifyItemInserted(exercises.size());
    }

    public void removeExercise(int position)
    {
        exercises.remove(position);
        notifyItemRemoved(position);
    }

    protected class ScheduleDayViewHolder extends RecyclerView.ViewHolder implements SwipeToRevealLayout.SwipeLayoutCallback
    {
        @Bind(R.id.swipeRevealLayout)
        SwipeToRevealLayout layout;
        @Bind(R.id.textView)
        TextView textView;

        public ScheduleDayViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void contentTouched()
        {

        }

        @Override
        public void leftButtonClicked(View v)
        {
            callback.itemDeleted(getAdapterPosition());
        }

        @Override
        public void rightButtonClicked(View v)
        {

        }

        @Override
        public void itemRevealed(View v)
        {

        }
    }

    public interface ScheduleDayAdapterCallback
    {
        void itemDeleted(int position);
    }
}
