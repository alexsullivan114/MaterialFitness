package peoples.materialfitness.View.Components.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.ButterKnife;
import peoples.materialfitness.Database.Exercise;

/**
 * Created by Alex Sullivan on 1/3/16.
 */
public class ExerciseTitleAutoCompleteAdapter extends ArrayAdapter<Exercise>
{
    private List<Exercise> mExercises;
    public ExerciseTitleAutoCompleteAdapter(Context context, List<Exercise> exercises)
    {
        super(context, android.R.layout.simple_list_item_1, exercises);
        mExercises = new ArrayList<>(exercises.size());
        mExercises.addAll(exercises);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView textView = ButterKnife.findById(convertView, android.R.id.text1);
        textView.setText(getItem(position).getTitle());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    /**
     * <p>An array filter constrains the content of the array adapter with
     * a prefix. Each item that does not start with the supplied prefix
     * is removed from the list.</p>
     */
    private Filter mFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            return ((Exercise)resultValue).getTitle();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null) {
                ArrayList<Exercise> suggestions = new ArrayList<>();
                for (Exercise exercise : mExercises) {
                    if (exercise.getTitle().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(exercise);
                    }
                }

                results.values = suggestions;
                results.count = suggestions.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            if (results != null && results.count > 0) {
                // we have filtered results
                addAll((ArrayList<Exercise>) results.values);
            } else {
                // no filter, add entire original list back in
                addAll(mExercises);
            }
            notifyDataSetChanged();
        }
    };
}
