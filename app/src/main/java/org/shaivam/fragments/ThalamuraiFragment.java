package org.shaivam.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.shaivam.R;
import org.shaivam.activities.CountryActivity;
import org.shaivam.activities.ThalamActivity;
import org.shaivam.activities.ThirumuraiListActivity;
import org.shaivam.model.Thirumurai_songs;
import org.shaivam.utils.AppConfig;

import java.util.ArrayList;
import java.util.List;

public class ThalamuraiFragment extends Fragment {
    LinearLayout nadu_linear, thalam_linear;
    List<Thirumurai_songs> thirumurai_songs = new ArrayList<>();
int thirumurai_id = 0 ;
    public ThalamuraiFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thalaimurai, container, false);
        nadu_linear = view.findViewById(R.id.nadu_linear);
        thalam_linear = view.findViewById(R.id.thalam_linear);
        thirumurai_songs.clear();
        try {
            if (ThirumuraiListActivity.selected_page.equalsIgnoreCase(AppConfig.thevaram)) {
               thirumurai_id = 1;
               thirumurai_id = 2;
               thirumurai_id = 3;
               thirumurai_id = 4;
               thirumurai_id = 5;
               thirumurai_id = 6;
               thirumurai_id = 7;
            } else if (ThirumuraiListActivity.selected_page.equalsIgnoreCase(AppConfig.thiruvasagam)) {
               thirumurai_id = 8;
            } else if (ThirumuraiListActivity.selected_page.equalsIgnoreCase(AppConfig.thirukovaiyar)) {
               thirumurai_id = 9;
            } else if (ThirumuraiListActivity.selected_page.equalsIgnoreCase(AppConfig.thiruvisaipa)) {
               thirumurai_id = 10;
               thirumurai_id = 11;
            } else if (ThirumuraiListActivity.selected_page.equalsIgnoreCase(AppConfig.thirumanthiram)) {
               thirumurai_id = 12;
            } else if (ThirumuraiListActivity.selected_page.equalsIgnoreCase(AppConfig.prabandham)) {
               thirumurai_id = 13;
            } else if (ThirumuraiListActivity.selected_page.equalsIgnoreCase(AppConfig.periyapuranam)) {
               thirumurai_id = 14;
            }
            nadu_linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent  = new Intent(getContext(), CountryActivity.class);
                    intent.putExtra("thirumurai_id",thirumurai_id);
                    getContext().startActivity(intent);
                }
            });
            thalam_linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent  = new Intent(getContext(), ThalamActivity.class);
                    intent.putExtra("thirumurai_id",thirumurai_id);
                    getContext().startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }
}
