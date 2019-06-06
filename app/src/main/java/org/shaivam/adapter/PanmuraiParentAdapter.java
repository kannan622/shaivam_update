package org.shaivam.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.shaivam.R;
import org.shaivam.activities.ThirumuraiSongsListActivity;
import org.shaivam.model.Thirumurai;
import org.shaivam.model.Thirumurai_songs;
import org.shaivam.utils.AppConfig;
import org.shaivam.utils.MyApplication;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class PanmuraiParentAdapter extends RecyclerView.Adapter<PanmuraiParentAdapter.ViewHolder> {
    private List<Thirumurai> mDataset;
    private Context context;
    private LayoutInflater inflater;
    private static PanmuraiChildAdapter adapter;
    private static int currentPosition = -1;
    public static List<Thirumurai_songs> thirumuraiDetails = new ArrayList<Thirumurai_songs>();


    public class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout card_thirumurai;
        public TextView thirumurai_text;
        public RecyclerView recycle_thirumurai_child;

        public ViewHolder(View v) {
            super(v);
            card_thirumurai = v.findViewById(R.id.card_thirumurai);
            thirumurai_text = v.findViewById(R.id.thirumurai_text);
            recycle_thirumurai_child = v.findViewById(R.id.recycle_thirumurai_child);

        }
    }

    public void add(int position, Thirumurai item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(Thirumurai item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    public PanmuraiParentAdapter(Context context, List<Thirumurai> thirumuraiDetails) {
        this.context = context;
        mDataset = thirumuraiDetails;
        currentPosition = -1;
        inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View rootView = inflater.inflate(R.layout.adapter_panmurai_parent, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Thirumurai ds = mDataset.get(position);
        if (ds != null) {
            adapter = new PanmuraiChildAdapter(context, thirumuraiDetails);
            holder.recycle_thirumurai_child.setLayoutManager(new LinearLayoutManager(context));
            holder.recycle_thirumurai_child.setAdapter(adapter);
            if (position == currentPosition) {
                holder.recycle_thirumurai_child.setVisibility(View.VISIBLE);
                holder.thirumurai_text.setBackgroundResource(R.color.bg_white);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.thirumurai_text.setElevation(8);
                }
                holder.thirumurai_text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_drop_up, 0);
            } else {
                holder.recycle_thirumurai_child.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.thirumurai_text.setElevation(0);
                }
                holder.thirumurai_text.setBackgroundResource(R.color.bg_white);
                holder.thirumurai_text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_drop_down, 0);
            }
            holder.thirumurai_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentPosition >= 0) {
                        int prev = currentPosition;
                        notifyItemChanged(prev);
                    }
                    if (currentPosition == holder.getPosition()) {
                        currentPosition = -1;
                        notifyItemChanged(currentPosition);
                    } else {
                        currentPosition = holder.getPosition();
                        try {
                            thirumuraiDetails.clear();
                            List<Thirumurai_songs> thirumuraisData = null;
                            thirumuraisData = MyApplication.repo.repoThirumurai_songs.getPanbyThirumurai_Id(ds.getId());
                            for (int i = thirumuraisData.size() - 1; i >= 0; i--) {
                                if (!thirumuraisData.get(i).getPann().equalsIgnoreCase(""))
                                    thirumuraiDetails.add(thirumuraisData.get(i));
                            }
                            if (thirumuraiDetails.size() > 0)
                                adapter.notifyDataSetChanged();
                            else {
                                Intent intent = new Intent(context, ThirumuraiSongsListActivity.class);
                                intent.putExtra("thirumurai_id", mDataset.get(position).getId());
                                context.startActivity(intent);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        notifyItemChanged(currentPosition);
                    }
                }
            });

            Typeface tf = Typeface.createFromAsset(context.getAssets(), AppConfig.FONT_KAVIVANAR);
            holder.thirumurai_text.setTypeface(tf);

            if (ds.getId() == 10)
                holder.thirumurai_text.setText(context.getResources().getText(R.string.thiruvisaipa_title));
            else if (ds.getId() == 11)
                holder.thirumurai_text.setText(context.getResources().getText(R.string.thirupalandu_title));
            else
                holder.thirumurai_text.setText(ds.getThirumuraiName());


        }
    }

    public int getItemCount() {

        if (mDataset != null)
            return mDataset.size();
        else
            return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
