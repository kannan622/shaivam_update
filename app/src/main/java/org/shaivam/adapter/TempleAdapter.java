package org.shaivam.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.shaivam.R;
import org.shaivam.activities.TempleActivity;
import org.shaivam.model.Temple;
import org.shaivam.utils.AppConfig;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class TempleAdapter extends RecyclerView.Adapter<TempleAdapter.ViewHolder> {
    private List<Temple> mDataset;
    private Context context;
    private LayoutInflater inflater;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView temple_text, location_text;
        View temple_view;
        ConstraintLayout temple_list_card;
        LinearLayout temple_location_linear;
        ImageView temple_image;

        public ViewHolder(View v) {
            super(v);

            temple_text = v.findViewById(R.id.temple_text);
            location_text = v.findViewById(R.id.location_text);
            temple_view = v.findViewById(R.id.temple_view);
            temple_list_card = v.findViewById(R.id.temple_list_card);
            temple_location_linear = v.findViewById(R.id.temple_location_linear);
            temple_image = v.findViewById(R.id.temple_image);
        }
    }

    public TempleAdapter(Context context, List<Temple> templeList) {
        this.context = context;
        mDataset = templeList;
        inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public TempleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View rootView = inflater.inflate(R.layout.adapter_temple, parent, false);
        return new TempleAdapter.ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final TempleAdapter.ViewHolder holder, final int position) {
        if (position == mDataset.size() - 1)
            holder.temple_view.setVisibility(View.GONE);
        else
            holder.temple_view.setVisibility(View.VISIBLE);

        if (mDataset.size() > 0) {
            final Temple temple = mDataset.get(position);
            Typeface tf = Typeface.createFromAsset(context.getAssets(), AppConfig.FONT_KAVIVANAR);
            holder.temple_text.setTypeface(tf);
            holder.location_text.setTypeface(tf);

            holder.temple_text.setText(temple.getName().trim());
            holder.location_text.setText(temple.getDescription().trim());
            if (temple.getDescription() != null && !temple.getDescription().equalsIgnoreCase("")) {
                holder.temple_location_linear.setVisibility(View.VISIBLE);
            } else {
                holder.temple_location_linear.setVisibility(View.GONE);
            }
            if (!temple.getImgpath().equalsIgnoreCase(""))
                Glide.with(context)
                        .load(temple.getImgpath())
                        .into(holder.temple_image);

            holder.temple_list_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    double latitude = Double.parseDouble(temple.getLatitude());
                    double longitude = Double.parseDouble(temple.getLongitude());
                    String label = temple.getName();
                    String uriBegin = "geo:" + latitude + "," + longitude;
                    String query = latitude + "," + longitude + "(" + label + ")";
                    String encodedQuery = Uri.encode(query);
                    String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                    Uri uri = Uri.parse(uriString);
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);

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


    public void filterList(List<Temple> filterdNames) {
        this.mDataset = filterdNames;
        notifyDataSetChanged();
    }
}