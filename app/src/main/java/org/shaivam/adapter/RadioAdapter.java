package org.shaivam.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.shaivam.R;
import org.shaivam.activities.RadioActivity;
import org.shaivam.activities.RadioDetailActivity;
import org.shaivam.model.Selected_songs;
import org.shaivam.playback.PlaybackInfoListener;
import org.shaivam.utils.AppConfig;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class RadioAdapter extends RecyclerView.Adapter<RadioAdapter.ViewHolder> {
    private List<Selected_songs> mDataset;
    private Context context;
    private LayoutInflater inflater;
    private static int currentPosition = -1;
    public static int currentRadioPosition = -1;
    public int drawable = R.drawable.ic_shaivam_fm_play;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView radio_text, recent_radio_title, recent_radio_value;
        ConstraintLayout radio_linear;
        ImageView radio_player, radio_direction;
        LinearLayout recent_radio_linear;

        public ViewHolder(View v) {
            super(v);

            radio_text = v.findViewById(R.id.radio_text);
            recent_radio_title = v.findViewById(R.id.recent_radio_title);
            recent_radio_value = v.findViewById(R.id.recent_radio_value);
            radio_linear = v.findViewById(R.id.radio_linear);
            radio_player = v.findViewById(R.id.radio_player);
            radio_direction = v.findViewById(R.id.radio_direction);
            recent_radio_linear = v.findViewById(R.id.recent_radio_linear);
        }
    }

    public RadioAdapter(Context context, List<Selected_songs> selected_songsList) {
        this.context = context;
        mDataset = selected_songsList;
        inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        currentPosition = -1;
    }

    @Override
    public RadioAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View rootView = inflater.inflate(R.layout.adapter_radio, parent, false);
        return new RadioAdapter.ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final RadioAdapter.ViewHolder holder, final int position) {
        if (mDataset.size() > 0) {
            final Selected_songs selected_songs = mDataset.get(position);
            Typeface tf = Typeface.createFromAsset(context.getAssets(), AppConfig.FONT_KAVIVANAR);
            holder.radio_text.setTypeface(tf);
            holder.recent_radio_title.setTypeface(tf);
            holder.recent_radio_value.setTypeface(tf);
            if (selected_songs.getAuthor_name().equalsIgnoreCase("radio-thiruneriya-thamizhosai"))
                holder.radio_text.setText(AppConfig.capitalizeWord(context.getResources().getString(R.string.tamil_radio_tilte)));
            else
                holder.radio_text.setText(AppConfig.capitalizeWord(selected_songs.getAuthor_name().trim().replaceAll("-", " ")));

            holder.recent_radio_value.setText(selected_songs.getTitle().trim());
            if (selected_songs.getTitle().equalsIgnoreCase(""))
                holder.recent_radio_linear.setVisibility(View.GONE);
            else
                holder.recent_radio_linear.setVisibility(View.VISIBLE);

            if (selected_songs.getThalam().equalsIgnoreCase("Tamil"))
                holder.recent_radio_title.setText(context.getResources().getString(R.string.tamil_radio_recent_tilte));
            else
                holder.recent_radio_title.setText(context.getResources().getString(R.string.lahari_radio_recent_tilte));

            if (position == currentPosition) {
                holder.radio_linear.setBackground(context.getResources().getDrawable(R.drawable.background_button));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.radio_linear.setElevation(context.getResources().getDimension(R.dimen._4sdp));
                }
            } else {
                holder.radio_linear.setBackground(context.getResources().getDrawable(R.drawable.background_tirumurai));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.radio_linear.setElevation(0);
                }
            }
           /* if (position == currentRadioPosition) {
                holder.radio_direction.post(new Runnable() {
                    @Override
                    public void run() {
                        holder.radio_direction.setImageResource(drawable);
                    }
                });
            } else {
                holder.radio_direction.post(new Runnable() {
                    @Override
                    public void run() {
                        holder.radio_direction.setImageResource(R.drawable.ic_shaivam_fm_play);
                    }
                });
            }*/
            if (RadioActivity.mPlayerAdapter != null && RadioActivity.mPlayerAdapter.getCurrentSong() != null
                    && RadioActivity.mPlayerAdapter.getCurrentSong().getAudioUrl().equalsIgnoreCase(selected_songs.getAudioUrl())
                    && RadioActivity.mPlayerAdapter.isPlaying()) {
                holder.radio_direction.setImageResource(R.drawable.ic_shaivam_fm_stop);

            } else {
                holder.radio_direction.setImageResource(R.drawable.ic_shaivam_fm_play);
            }
            holder.radio_linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentPosition >= 0) {
                        int prev = currentPosition;
                        notifyItemChanged(prev);
                    }
                    if (currentPosition == holder.getPosition()) {
                        currentPosition = holder.getPosition();
                        notifyItemChanged(currentPosition);
                        RadioDetailActivity.selected_songsList = mDataset;
                        RadioDetailActivity.selected_songs = selected_songs;

                        Intent intent = new Intent(context, RadioDetailActivity.class);
                        context.startActivity(intent);
                    } else {
                        currentPosition = holder.getPosition();
                        notifyItemChanged(currentPosition);

                        RadioDetailActivity.selected_songsList = mDataset;
                        RadioDetailActivity.selected_songs = selected_songs;

                        Intent intent = new Intent(context, RadioDetailActivity.class);
                        context.startActivity(intent);
                    }
                }
            });

            holder.radio_direction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentRadioPosition >= 0) {
                        int prev = currentRadioPosition;
                        notifyItemChanged(prev);
                    }
                    if (currentRadioPosition == holder.getPosition()) {
                        currentRadioPosition = holder.getPosition();
                        notifyItemChanged(currentRadioPosition);

                        if (RadioActivity.mPlayerAdapter != null && RadioActivity.mPlayerAdapter.getCurrentSong() != null
                                && RadioActivity.mPlayerAdapter.getCurrentSong().getAudioUrl().equalsIgnoreCase(selected_songs.getAudioUrl())) {
                            RadioActivity.mPlayerAdapter.resumeOrPause();
                        } else {
                            AppConfig.showProgDialiog(context);
                            RadioActivity.mPlayerAdapter.setCurrentSong(selected_songs, mDataset);
                            RadioActivity.mPlayerAdapter.initMediaPlayer(context);
                        }
                    } else {
                        currentRadioPosition = holder.getPosition();
                        notifyItemChanged(currentRadioPosition);
                        AppConfig.showProgDialiog(context);
                        if (RadioActivity.mPlayerAdapter != null && RadioActivity.mPlayerAdapter.getCurrentSong() != null
                                && RadioActivity.mPlayerAdapter.getCurrentSong().getAudioUrl().equalsIgnoreCase(selected_songs.getAudioUrl())) {
                            RadioActivity.mPlayerAdapter.resumeOrPause();
                        } else {
                            AppConfig.showProgDialiog(context);
                            RadioActivity.mPlayerAdapter.setCurrentSong(selected_songs, mDataset);
                            RadioActivity.mPlayerAdapter.initMediaPlayer(context);
                        }
                    }
                }
            });
        }

    }

    public void updatePlayingStatus() {
        drawable = RadioActivity.mPlayerAdapter.getState() != PlaybackInfoListener.State.PAUSED ? R.drawable.ic_shaivam_fm_stop : R.drawable.ic_shaivam_fm_play;
        notifyItemChanged(currentRadioPosition);
    }

    public void updatePlayingNotificationStatus() {
        drawable = RadioActivity.mPlayerAdapter.getState() != PlaybackInfoListener.State.PAUSED ? R.drawable.ic_shaivam_fm_stop : R.drawable.ic_shaivam_fm_play;
        notifyItemChanged(currentRadioPosition);
        RadioActivity.mPlayerAdapter.setCurrentSong(mDataset.get(currentRadioPosition), mDataset);
        RadioActivity.mPlayerAdapter.initMediaPlayer(context);
    }

    public int getItemCount() {

        if (mDataset != null)
            return mDataset.size();
        else
            return 0;
    }
}