package ru.raptors.team.formzilla.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import ru.raptors.team.formzilla.R;

public class FiltersAdapter extends BaseExpandableListAdapter {
    private final Context context;

    public FiltersAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return 10;
        // TODO: здесь изменяем количество категорий
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 10;
        // TODO: здесь изменяем количество фильтров в каждой категории
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
                    .LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_view_group_view, null);
        }
        //((TextView)convertView.findViewById(R.id.tv_group_view)).setText("");
        // TODO: здесь изменяем текст категории
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
                    .LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_view_child_view, null);
        }
        //((TextView)convertView.findViewById(R.id.tv_group_view)).setText("");
        // TODO: здесь изменяем текст фильтра
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
