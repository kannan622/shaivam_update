package org.shaivam.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.shaivam.R;
import org.shaivam.activities.MyFavouritesActivity;
import org.shaivam.activities.ThirumuraiSongsDetailActivity;
import org.shaivam.model.Thirumurai_songs;
import org.shaivam.utils.AppConfig;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class MyFavouritesSongsAdapter extends RecyclerView.Adapter<MyFavouritesSongsAdapter.ViewHolder> {
    private List<Thirumurai_songs> mDataset;
    private Activity context;
    private LayoutInflater inflater;
//    private static int currentPosition = -1;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView songs_text;
        LinearLayout card_songs;
        View songs_view;
        ImageView remove_fav;

        public ViewHolder(View v) {
            super(v);

            songs_text = v.findViewById(R.id.songs_text);
            card_songs = v.findViewById(R.id.card_songs);
            songs_view = v.findViewById(R.id.songs_view);
            remove_fav = v.findViewById(R.id.remove_fav);
        }
    }

    public MyFavouritesSongsAdapter(Activity context, List<Thirumurai_songs> thirumurai_songsList) {
        this.context = context;
        mDataset = thirumurai_songsList;
        inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
//        currentPosition = -1;
    }

    @Override
    public MyFavouritesSongsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        View rootView = inflater.inflate(R.layout.adapter_my_favourites_songs, parent, false);
        return new MyFavouritesSongsAdapter.ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final MyFavouritesSongsAdapter.ViewHolder holder, final int position) {
        if (position == mDataset.size() - 1)
            holder.songs_view.setVisibility(View.GONE);
        else
            holder.songs_view.setVisibility(View.VISIBLE);

        if (mDataset.size() > 0) {
            final Thirumurai_songs thirumurai_songs = mDataset.get(position);
            Typeface tf = Typeface.createFromAsset(context.getAssets(), AppConfig.FONT_KAVIVANAR);
            holder.songs_text.setTypeface(tf);
            holder.songs_text.setText(thirumurai_songs.getTitle().trim());
          /*  if (position == currentPosition) {
                holder.songs_text.setBackgroundResource(R.color.colorAccent);
                holder.songs_text.setTextColor(context.getResources().getColor(R.color.bg_white));
            } else {
                holder.songs_text.setBackgroundResource(R.color.bg_white);
                holder.songs_text.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            }*/
            holder.card_songs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   /* if (currentPosition >= 0) {
                        int prev = currentPosition;
                        notifyItemChanged(prev);
                    }
                    if (currentPosition == holder.getPosition()) {
                        currentPosition = holder.getPosition();
                        notifyItemChanged(currentPosition);
                      */
                    try {
                        Intent intent = new Intent(context, ThirumuraiSongsDetailActivity.class);
                        intent.putExtra("title", thirumurai_songs.getTitle());
                        context.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                  /*  } else {
                        currentPosition = holder.getPosition();
                        notifyItemChanged(currentPosition);
                        try {
                            Intent intent = new Intent(context, ThirumuraiSongsDetailActivity.class);
                            intent.putExtra("title", thirumurai_songs.getTitle());
                            context.startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }*/
                }
            });

            holder.remove_fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (thirumurai_songs.getFavorites()) {
                        AppConfig.showFavoritesDialog(context, thirumurai_songs, false,
                                null);
                    } else {
                        AppConfig.showFavoritesDialog(context, thirumurai_songs, true,
                                null);
                    }
                }
            });
        }

    }

    public int getItemCount() {

        if (mDataset != null)
            return mDataset.size();
        else
            return 0;
    }
}