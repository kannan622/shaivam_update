package org.shaivam.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.shaivam.R;
import org.shaivam.model.Selected_songs;
import org.shaivam.utils.AppConfig;

import java.util.List;

public class SongsSpinnerCustomAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflter;
    private List<Selected_songs> mDataset;

    public SongsSpinnerCustomAdapter(Context context, List<Selected_songs> myDataset) {
        this.context = context;
        mDataset = myDataset;
        inflter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mDataset.size();    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner_selected_songs, null);
        final Selected_songs ds =mDataset.get(i);
        TextView names = (TextView) view.findViewById(R.id.txt_dname);
        Typeface tf = Typeface.createFromAsset(context.getAssets(), AppConfig.FONT_KAVIVANAR);
        names.setTypeface(tf);
        names.setText(ds.getAuthor_name());
        return view;
    }
}
