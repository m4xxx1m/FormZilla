package ru.raptors.team.formzilla.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import ru.raptors.team.formzilla.R;
import ru.raptors.team.formzilla.models.Form;
import ru.raptors.team.formzilla.models.User;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.ViewHolder>{
    private LayoutInflater inflater;
    private List<User> staff;
    private Context context;

    public StaffAdapter(Context context, List<User> staff) {
        this.staff = staff;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }
    @Override
    public StaffAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.staff_item, parent, false);
        return new StaffAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StaffAdapter.ViewHolder holder, int position) {
        User user = staff.get(position);
        holder.firstLastName.setText(user.getFirstName() + " " + user.getLastName());
        holder.login.setText(user.getLogin());
        holder.password.setText(user.getPassword());
        holder.placeHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    String infoToCopy = holder.firstLastName.getText() + "\nЛогин: " + holder.login.getText() +
                            "\nПароль: " + holder.password.getText();
                    ClipData clip = ClipData.newPlainText("copy information about employee", infoToCopy);
                    clipboard.setPrimaryClip(clip);
                    Snackbar.make((View) holder.placeHolder, "Скопировано", Snackbar.LENGTH_SHORT).show();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return staff.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView firstLastName;
        final TextView login;
        final TextView password;
        final RelativeLayout placeHolder;
        ViewHolder(View view){
            super(view);
            firstLastName = view.findViewById(R.id.firstname_lastname_text_view);
            login = view.findViewById(R.id.login_text_view);
            password = view.findViewById(R.id.password_text_view);
            placeHolder = view.findViewById(R.id.employee);
        }
    }
}
