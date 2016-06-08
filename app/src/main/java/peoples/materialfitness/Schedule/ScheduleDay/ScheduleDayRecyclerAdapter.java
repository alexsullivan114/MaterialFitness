package peoples.materialfitness.Schedule.ScheduleDay;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import peoples.materialfitness.Model.Exercise.Exercise;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.R;

/**
 * Created by Alex Sullivan on 6/6/2016.
 */
public class ScheduleDayRecyclerAdapter extends RecyclerView.Adapter<ScheduleDayRecyclerAdapter.ScheduleDayViewHolder>
{
    private List<Exercise> exercises;

    public ScheduleDayRecyclerAdapter(List<Exercise> exercises)
    {
        this.exercises= exercises;
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

    protected static class ScheduleDayViewHolder extends RecyclerView.ViewHolder
    {
        @Bind(R.id.textView)
        TextView textView;

        public ScheduleDayViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
