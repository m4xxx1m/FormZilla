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
import ru.raptors.team.formzilla.models.Helper;

public class PassedFormsAdapter extends RecyclerView.Adapter<PassedFormsAdapter.ViewHolder>{

    private LayoutInflater inflater;
    private List<Form> passedForms;

    public PassedFormsAdapter(Context context, List<Form> passedForms) {
        this.passedForms = passedForms;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public PassedFormsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.passed_form_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PassedFormsAdapter.ViewHolder holder, int position) {
        Form form = passedForms.get(position);
        holder.title.setText(form.title);
        holder.questionsCount.setText(Integer.toString(form.questions.size()) + " " + Helper.getWordQuestionInRightForm(form.questions.size()));
    }

    @Override
    public int getItemCount() {
        return passedForms.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView questionsCount;
        ViewHolder(View view){
            super(view);
            title = view.findViewById(R.id.passed_form_title);
            questionsCount = view.findViewById(R.id.passed_form_questions_count);
        }
    }
}
