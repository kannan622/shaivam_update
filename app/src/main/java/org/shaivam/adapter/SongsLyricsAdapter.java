package org.shaivam.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.util.FloatMath;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.shaivam.R;
import org.shaivam.activities.ThirumuraiSongsDetailActivity;
import org.shaivam.model.Thirumurai_songs;
import org.shaivam.utils.AppConfig;
import org.shaivam.utils.MyApplication;
import org.shaivam.utils.PrefManager;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class SongsLyricsAdapter extends RecyclerView.Adapter<SongsLyricsAdapter.ViewHolder> {
    private List<Thirumurai_songs> mDataset;
    private Context context;
    private String search_item;
    private LayoutInflater inflater;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView songs_lyrics_text, songs_lyrics_no;
        LinearLayout card_songs_lyrics;

        public ViewHolder(View v) {
            super(v);

            songs_lyrics_no = v.findViewById(R.id.songs_lyrics_no);
            songs_lyrics_text = v.findViewById(R.id.songs_lyrics_text);
            card_songs_lyrics = v.findViewById(R.id.card_songs_lyrics);
        }
    }

    public SongsLyricsAdapter(Context context, String search_item, List<Thirumurai_songs> thirumurai_songsList) {
        this.context = context;
        mDataset = thirumurai_songsList;
        inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        this.search_item = search_item;
    }

    @Override
    public SongsLyricsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View rootView = inflater.inflate(R.layout.adapter_song_lyrics, parent, false);
        return new SongsLyricsAdapter.ViewHolder(rootView);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public void onBindViewHolder(final SongsLyricsAdapter.ViewHolder holder, final int position) {
        if (mDataset.size() > 0) {
            final Thirumurai_songs thirumurai_songs = mDataset.get(position);
            Typeface tf = Typeface.createFromAsset(context.getAssets(), AppConfig.FONT_KAVIVANAR);
            holder.songs_lyrics_text.setTypeface(tf);
//            holder.songs_lyrics_no.setText();
          if(thirumurai_songs.getType()!=null && !thirumurai_songs.getType().equalsIgnoreCase(""))
            holder.songs_lyrics_text.setText(thirumurai_songs.getType().trim() + "\n\n"
                + thirumurai_songs.getSong().replace("\\n", "\n").trim()
                + " " + String.valueOf(thirumurai_songs.getSongNo()));
           else
            holder.songs_lyrics_text.setText(thirumurai_songs.getSong().replace("\\n", "\n").trim()
                + " " + String.valueOf(thirumurai_songs.getSongNo()));

          holder.songs_lyrics_text.setTextSize(MyApplication.prefManager.getInt(PrefManager
                .SONG_FONT_SIZE, AppConfig.SMALL_SONG_FONT_SIZE));
            if (!search_item.equalsIgnoreCase("") && thirumurai_songs.getSong().contains(search_item)) {
                holder.card_songs_lyrics.setBackgroundResource(R.color.Yellow);
                holder.songs_lyrics_no.setBackgroundResource(R.color.Yellow);
                holder.songs_lyrics_text.setBackgroundResource(R.color.Yellow);
                holder.songs_lyrics_no.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                holder.songs_lyrics_text.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }
        }
    }

    private void highlightString(TextView textView, String input) {
//Get the text from text view and create a spannable string
        SpannableString spannableString = new SpannableString(textView.getText());
//Get the previous spans and remove them
        BackgroundColorSpan[] backgroundSpans = spannableString.getSpans(0, spannableString.length(), BackgroundColorSpan.class);

        for (BackgroundColorSpan span : backgroundSpans) {
            spannableString.removeSpan(span);
        }

//Search for all occurrences of the keyword in the string
        int indexOfKeyword = spannableString.toString().indexOf(input);

        while (indexOfKeyword > 0) {
            //Create a background color span on the keyword
            spannableString.setSpan(new BackgroundColorSpan(Color.YELLOW), indexOfKeyword, indexOfKeyword + input.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            //Get the next index of the keyword
            indexOfKeyword = spannableString.toString().indexOf(input, indexOfKeyword + input.length());
        }

//Set the final text on TextView
        textView.setText(spannableString);
    }

    public int getItemCount() {

        if (mDataset != null)
            return mDataset.size();
        else
            return 0;
    }
}