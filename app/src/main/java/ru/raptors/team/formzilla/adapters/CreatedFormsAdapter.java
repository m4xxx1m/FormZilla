package ru.raptors.team.formzilla.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.raptors.team.formzilla.R;
import ru.raptors.team.formzilla.activities.MainActivity;
import ru.raptors.team.formzilla.models.Form;
import ru.raptors.team.formzilla.models.Helper;

public class CreatedFormsAdapter extends RecyclerView.Adapter<CreatedFormsAdapter.ViewHolder>{

    // внимание! на строчке снизу КОСТЫЛЬ
    public static int[] userAnswerCount = new int[1500];

    private LayoutInflater inflater;
    private List<Form> createdForms;
    private MainActivity mainActivity;
    private Context context;

    public CreatedFormsAdapter(Context context, List<Form> createdForms, MainActivity mainActivity) {
        this.createdForms = createdForms;
        this.inflater = LayoutInflater.from(context);
        this.mainActivity = mainActivity;
        this.context = context;
    }
    @Override
    public CreatedFormsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.created_form_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CreatedFormsAdapter.ViewHolder holder, int position) {
        Form form = createdForms.get(position);
        holder.title.setText(form.title);
        holder.questionsCount.setText(Integer.toString(form.questions.size()) + " " + Helper.getWordQuestionInRightForm(form.questions.size()));
        holder.passedFormAccountsCount.setText(Integer.toString(userAnswerCount[position]));
        holder.createdFormHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                form.getStaffAnswersAndDoAction(context, () -> {
                    mainActivity.openFormAnswersFragment(form);
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return createdForms.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView questionsCount;
        final TextView passedFormAccountsCount;
        final View createdFormHolder;
        ViewHolder(View view){
            super(view);
            title = view.findViewById(R.id.createdFormTitle);
            questionsCount = view.findViewById(R.id.createdFormQuestionsCount);
            passedFormAccountsCount = view.findViewById(R.id.passedFormAccountsCount);
            createdFormHolder = view.findViewById(R.id.created_form_holder);
        }
    }
}
