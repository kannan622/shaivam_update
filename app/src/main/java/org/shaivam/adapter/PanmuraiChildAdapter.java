package org.shaivam.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.shaivam.R;
import org.shaivam.activities.ThirumuraiSongsListActivity;
import org.shaivam.model.Thirumurai_songs;
import org.shaivam.utils.AppConfig;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class PanmuraiChildAdapter extends RecyclerView.Adapter<PanmuraiChildAdapter.ViewHolder> {
    private List<Thirumurai_songs> mDataset;
    private Context context;
    private LayoutInflater inflater;
    private static int currentPosition = -1;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView thirumurai_child_text;
        LinearLayout card_thirumurai_child;
        View thirumurai_child_view;

        public ViewHolder(View v) {
            super(v);

            card_thirumurai_child = v.findViewById(R.id.card_thirumurai_child);
            thirumurai_child_text = v.findViewById(R.id.thirumurai_child_text);
            thirumurai_child_view = v.findViewById(R.id.thirumurai_child_view);
        }
    }

    public PanmuraiChildAdapter(Context context, List<Thirumurai_songs> thirumurai_songsList) {
        this.context = context;
        mDataset = thirumurai_songsList;
        inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        currentPosition = -1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View rootView = inflater.inflate(R.layout.adapter_panmurai_child, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (position == mDataset.size() - 1)
            holder.thirumurai_child_view.setVisibility(View.GONE);
        else
            holder.thirumurai_child_view.setVisibility(View.VISIBLE);

        if (mDataset.size() > 0) {
           final Thirumurai_songs thirumurai_songs = mDataset.get(position);
            Typeface tf = Typeface.createFromAsset(context.getAssets(), AppConfig.FONT_KAVIVANAR);
            holder.thirumurai_child_text.setTypeface(tf);
            holder.thirumurai_child_text.setText(thirumurai_songs.getPann().trim());
            if (position == currentPosition) {
                holder.thirumurai_child_text.setBackgroundResource(R.color.colorAccent);
                holder.thirumurai_child_text.setTextColor(context.getResources().getColor(R.color.bg_white));
            } else {
                holder.thirumurai_child_text.setBackgroundResource(R.color.adapter_child_bg);
                holder.thirumurai_child_text.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            }
            holder.card_thirumurai_child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentPosition >= 0) {
                        int prev = currentPosition;
                        notifyItemChanged(prev);
                    }
                    if (currentPosition == holder.getPosition()) {
                        currentPosition = holder.getPosition();
                        notifyItemChanged(currentPosition);
                        try {
                            Intent intent = new Intent(context, ThirumuraiSongsListActivity.class);
                            intent.putExtra("thirumurai_id", thirumurai_songs.getThirumuraiId());
                            intent.putExtra("pann", thirumurai_songs.getPann());
                            context.startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }  } else {
                        currentPosition = holder.getPosition();
                        notifyItemChanged(currentPosition);
                        try {
                            Intent intent = new Intent(context, ThirumuraiSongsListActivity.class);
                            intent.putExtra("thirumurai_id", thirumurai_songs.getThirumuraiId());
                            intent.putExtra("pann", thirumurai_songs.getPann());
                            context.startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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