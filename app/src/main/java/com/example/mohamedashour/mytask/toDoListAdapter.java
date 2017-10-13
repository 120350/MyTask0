package com.example.mohamedashour.mytask;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mohamed Ashour on 11/10/2017.
 */
public class toDoListAdapter extends RecyclerView.Adapter<toDoListAdapter.ViewHolder>{

    ArrayList<String> toDoListTitles;
    ArrayList<String> toDoListIds;
    Context context;

    public toDoListAdapter(Context context, ArrayList<String> toDoListIds, ArrayList<String> toDoListTitles) {
        this.context = context;
        this.toDoListIds = toDoListIds;
        this.toDoListTitles = toDoListTitles;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView  toDoListTitleText;
        public CheckBox checkBox;

        public ViewHolder(View v) {
            super(v);
            toDoListTitleText = (TextView) v.findViewById(R.id.toDoListTitleText);
            checkBox = (CheckBox) v.findViewById(R.id.toDoListCheckBox);
        }
    }

    @Override
    public toDoListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todolist_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.toDoListTitleText.setText(toDoListTitles.get(position));
        int num = Integer.parseInt(toDoListIds.get(position));
        if (num % 2 == 0){
            holder.checkBox.setChecked(true);
        }else {
            holder.checkBox.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return toDoListIds.size();
    }
}
