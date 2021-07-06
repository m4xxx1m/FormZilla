package ru.raptors.team.formzilla.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.raptors.team.formzilla.R;
import ru.raptors.team.formzilla.activities.AnswerQuestionActivity;
import ru.raptors.team.formzilla.activities.EnterFormNameActivity;
import ru.raptors.team.formzilla.activities.MainActivity;
import ru.raptors.team.formzilla.models.Form;

public class AvailableFormAdapter extends RecyclerView.Adapter<AvailableFormAdapter.ViewHolder>{

    private LayoutInflater inflater;
    private List<Form> availableForms;
    private  Context context;

    public AvailableFormAdapter(Context context, List<Form> availableForms) {
        this.availableForms = availableForms;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
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
        holder.availableFormItem.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Form availableForm = availableForms.get(position);
                Intent passFormIntent = new Intent(context, AnswerQuestionActivity.class);
                passFormIntent.putExtra(AnswerQuestionActivity.FORMTOPASS, availableForm);
                context.startActivity(passFormIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return availableForms.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView questionsCount;
        final View availableFormItem;
        ViewHolder(View view){
            super(view);
            title = view.findViewById(R.id.availableFormTitle);
            questionsCount = view.findViewById(R.id.availableFormQuestionsCount);
            availableFormItem = view.findViewById(R.id.available_form_layout);
        }
    }
}
