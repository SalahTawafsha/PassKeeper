package com.example.passkeeper.models;


import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.passkeeper.R;
import com.example.passkeeper.controllers.AppInfoPage;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder> {

    private final List<App> apps;

    public AppAdapter(List<App> apps) {
        this.apps = apps;
    }

    @NonNull
    @Override
    public AppAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.app_card_view,
                parent,
                false);

        return new AppAdapter.ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(AppAdapter.ViewHolder holder, int position) {
        CardView cardView = holder.cardView;

        App app = apps.get(position);

        ImageView imageView = cardView.findViewById(R.id.app_icon_view);
        Log.e("AppAdapter", app.getImageUrl());
        Picasso.get().load(app.getImageUrl()).into(imageView);


        TextView appName = cardView.findViewById(R.id.app_name_view);
        appName.setText(app.getName());

        TextView appTag = cardView.findViewById(R.id.app_tag_name_view);
        appTag.setText(getTag(app));

        View appTagView = cardView.findViewById(R.id.app_tag_view);
        appTagView.setBackgroundResource(getBackgroundColor(app));

        cardView.setOnClickListener(v -> {
            Intent intent = new Intent(cardView.getContext(), AppInfoPage.class);
            intent.putExtra("AppPosition", position);
            cardView.getContext().startActivity(intent);
        });
    }

    private String getTag(App app) {
        switch (app.getTag()) {
            case SOCIAL_MEDIA:
                return "Social Media";
            case WEBSITE:
                return "Website";
            case EMAIL:
                return "Email Account";
            case BANK:
                return "Bank Account";
        }
        return "";
    }

    private int getBackgroundColor(App app) {
        switch (app.getTag()) {
            case SOCIAL_MEDIA:
                return R.drawable.view_purpule;
            case WEBSITE:
                return R.drawable.view_blue;
            case EMAIL:
                return R.drawable.view_red_color;
            case BANK:
                return R.drawable.view_green;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;

        public ViewHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;
        }

    }
}