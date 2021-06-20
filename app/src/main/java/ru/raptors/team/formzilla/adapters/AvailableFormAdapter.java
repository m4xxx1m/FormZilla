package ru.raptors.team.formzilla.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.raptors.team.formzilla.R;
import ru.raptors.team.formzilla.models.Form;

public class AvailableFormAdapter extends RecyclerView.Adapter<AvailableFormAdapter.ViewHolder>{

    private LayoutInflater inflater;
    private List<Form> availableForms;

    public AvailableFormAdapter(Context context, List<Form> states) {
        this.availableForms = states;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public AvailableFormAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.available_form_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AvailableFormAdapter.ViewHolder holder, int position) {
        Form form = availableForms.get(position);
        holder.title.setText(form.title);
        holder.questionsCount.setText(Integer.toString(form.questions.size()));
    }

    @Override
    public int getItemCount() {
        return availableForms.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView questionsCount;
        ViewHolder(View view){
            super(view);
            title = view.findViewById(R.id.availableFormTitle);
            questionsCount = view.findViewById(R.id.availableFormQuestionsCount);
        }
    }
}
