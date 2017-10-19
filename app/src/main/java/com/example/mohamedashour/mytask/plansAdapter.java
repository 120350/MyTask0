package com.example.mohamedashour.mytask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Mohamed Ashour on 17/10/2017.
 */
public class plansAdapter extends RecyclerView.Adapter<plansAdapter.ViewHolder>{
    ArrayList<Plans> plans;
    Context context;

    public plansAdapter(Context context, ArrayList<Plans> plans) {
        this.context = context;
        this.plans = plans;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView planNameText;
        TextView planLocationText;
        TextView planNOGuestsText;
        TextView planBudgetText;
        ImageView imgPlans;
        public ViewHolder(View view) {
            super(view);
            planNameText = (TextView) view.findViewById(R.id.planNameText);
            planLocationText = (TextView) view.findViewById(R.id.planLocationText);
            planNOGuestsText = (TextView) view.findViewById(R.id.planNOGuestsText);
            planBudgetText = (TextView) view.findViewById(R.id.planBudgetText);
            imgPlans = (ImageView) view.findViewById(R.id.imgPlans);
        }
    }
    @Override
    public plansAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //define recyclerview layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plans_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // update view
        holder.planNameText.setText("Plan "+plans.get(position).getPlanName());
        holder.planLocationText.setText(plans.get(position).getLocation());
        holder.planNOGuestsText.setText(plans.get(position).getNoFriends()+ "+ Family & friends");
        holder.planBudgetText.setText("Planned Cost: "+plans.get(position).getMoney()+" L.E");
        circularImageBar(holder.imgPlans, 50);
    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    // this method for drawing circle view
    private void circularImageBar(ImageView iv2, int i) {

        Bitmap b = Bitmap.createBitmap(300, 300,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(b);
        Paint paint = new Paint();

        paint.setColor(Color.parseColor("#c4c4c4"));
        paint.setStrokeWidth(10);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(150, 150, 140, paint);
        paint.setColor(Color.parseColor("#904799"));
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.FILL);
        final RectF oval = new RectF();
        paint.setStyle(Paint.Style.STROKE);
        oval.set(10,10,290,290);
        canvas.drawArc(oval, 270, ((i*360)/100), false, paint);
        paint.setStrokeWidth(0);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.parseColor("#8E8E93"));
        paint.setTextSize(140);
        //canvas.drawText(""+i, 150, 150+(paint.getTextSize()/3), paint);
        iv2.setImageBitmap(b);
    }
}
