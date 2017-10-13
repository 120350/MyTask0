package com.example.mohamedashour.mytask;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Mohamed Ashour on 11/10/2017.
 */
public class tibsAdapter extends RecyclerView.Adapter<tibsAdapter.ViewHolder>{

    ArrayList<String> tibsImages;
    ArrayList<String> tibsTitles;
    ArrayList<String> tibsIds;
    Context context;

    public tibsAdapter(Context context, ArrayList<String> tibsIds, ArrayList<String> tibsImages, ArrayList<String> tibsTitles) {
        this.context = context;
        this.tibsIds = tibsIds;
        this.tibsImages = tibsImages;
        this.tibsTitles = tibsTitles;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView tibsImage;
        public TextView tibsIdText, tibsTitleText;

        public ViewHolder(View v) {
            super(v);
            tibsImage = (ImageView) v.findViewById(R.id.tibsImage);
            tibsIdText = (TextView) v.findViewById(R.id.tibsIdText);
            tibsTitleText = (TextView) v.findViewById(R.id.tibsTitleText);
        }
    }
    @Override
    public tibsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tibs_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tibsIdText.setText(tibsIds.get(position));
        holder.tibsTitleText.setText(tibsTitles.get(position));
        final String imgs = tibsImages.get(position);
        final Context context = holder.tibsImage.getContext();
        Picasso.with(context)
                .load(Uri.parse("http://www.thejerb.com/jerb/public/uploads/tips/" + imgs))
                .error(R.mipmap.ic_launcher)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.tibsImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        // Try again online if cache failed
                        Picasso.with(context)
                                .load(Uri.parse("http://www.thejerb.com/jerb/public/uploads/tips/" + imgs))
                                .error(R.mipmap.ic_launcher)
                                .into(holder.tibsImage);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return tibsIds.size();
    }
}
