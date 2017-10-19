package com.example.mohamedashour.mytask;

import android.content.Context;
import android.support.v7.widget.CardView;
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

    ArrayList<toDoListModel> models;
    Context context;

    public toDoListAdapter(Context context, ArrayList<toDoListModel> models) {
        this.context = context;
        this.models = models;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView  toDoListTitleText;
        CheckBox checkBox;
        CardView todolistCardView;
        public ViewHolder(View v) {
            super(v);
            toDoListTitleText = (TextView) v.findViewById(R.id.toDoListTitleText);
            checkBox = (CheckBox) v.findViewById(R.id.toDoListCheckBox);
            todolistCardView = (CardView) v.findViewById(R.id.todolistCardView);
        }
    }

    @Override
    public toDoListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //define recyclerview layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todolist_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // update view
        holder.toDoListTitleText.setText(models.get(position).getTitle());
        holder.checkBox.setChecked(false);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
