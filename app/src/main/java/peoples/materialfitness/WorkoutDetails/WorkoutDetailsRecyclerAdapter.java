package peoples.materialfitness.WorkoutDetails;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import peoples.materialfitness.Database.ExerciseSession;

/**
 * Created by Alex Sullivan on 2/15/16.
 */
public class WorkoutDetailsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private ExerciseSession mExerciseSession;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {

    }

    @Override
    public int getItemCount()
    {
        return mExerciseSession.getSets().size();
    }

    private static class RepViewHolder extends RecyclerView.ViewHolder
    {
        public RepViewHolder(View itemView)
        {
            super(itemView);
        }
    }
}
