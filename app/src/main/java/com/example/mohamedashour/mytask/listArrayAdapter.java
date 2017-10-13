package com.example.mohamedashour.mytask;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import me.tankery.lib.circularseekbar.CircularSeekBar;


/**
 * Created by Mohamed Ashour on 12/10/2017.
 */
public class listArrayAdapter extends ArrayAdapter<Plans> {

    ArrayList<Plans> plans;
    Context context;
    public listArrayAdapter(Context context, int textViewResourceId, ArrayList<Plans> objects) {
        super(context, textViewResourceId, objects);
        this.plans = objects;
        this.context = context;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.plans_layout, null,true);
        TextView planNameText = (TextView) view.findViewById(R.id.planNameText);
        TextView planLocationText = (TextView) view.findViewById(R.id.planLocationText);
        TextView planNOGuestsText = (TextView) view.findViewById(R.id.planNOGuestsText);
        TextView planBudgetText = (TextView) view.findViewById(R.id.planBudgetText);
        CircularSeekBar circularSeekBar = (CircularSeekBar) view.findViewById(R.id.circularSeekBar);
        final TextView circularSeekBarVal = (TextView) view.findViewById(R.id.circularSeekBarVal);

        circularSeekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                circularSeekBarVal.setText((int)progress+"%");
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });

        Random r = new Random();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        planNameText.setText("Plan "+alphabet.charAt(r.nextInt(alphabet.length())));

        planLocationText.setText(plans.get(position).getLocation());
        planNOGuestsText.setText(plans.get(position).getNoFriends()+ "+ Family & friends");
        planBudgetText.setText("Planned Cost: "+plans.get(position).getMoney()+" L.E");

        return view;
    }
}
