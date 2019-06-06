package org.shaivam.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import org.shaivam.R;
import org.shaivam.activities.ThirumuraiListActivity;
import org.shaivam.adapter.SongsAdapter;
import org.shaivam.model.Thirumurai_songs;
import org.shaivam.utils.AppConfig;
import org.shaivam.utils.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class AkarathiFragment extends Fragment {
    private SongsAdapter adapter;
    public RecyclerView akarathi_recyclerView;
    List<Thirumurai_songs> thirumurai_songs = new ArrayList<>();
    AutoCompleteTextView edit_search;
    Button search_clear_btn;

    public AkarathiFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_akarathi, container, false);
        thirumurai_songs.clear();
        try {
            edit_search = view.findViewById(R.id.edit_search);
            search_clear_btn = view.findViewById(R.id.search_clear_btn);
            akarathi_recyclerView = view.findViewById(R.id.akarathi_recyclerView);
            if (ThirumuraiListActivity.selected_page.equalsIgnoreCase(AppConfig.thevaram)) {
                thirumurai_songs.addAll(MyApplication.repo.repoThirumurai_songs.getByThirumurai_Id(1));
                thirumurai_songs.addAll(MyApplication.repo.repoThirumurai_songs.getByThirumurai_Id(2));
                thirumurai_songs.addAll(MyApplication.repo.repoThirumurai_songs.getByThirumurai_Id(3));
                thirumurai_songs.addAll(MyApplication.repo.repoThirumurai_songs.getByThirumurai_Id(4));
                thirumurai_songs.addAll(MyApplication.repo.repoThirumurai_songs.getByThirumurai_Id(5));
                thirumurai_songs.addAll(MyApplication.repo.repoThirumurai_songs.getByThirumurai_Id(6));
                thirumurai_songs.addAll(MyApplication.repo.repoThirumurai_songs.getByThirumurai_Id(7));
            } else if (ThirumuraiListActivity.selected_page.equalsIgnoreCase(AppConfig.thiruvasagam)) {
                thirumurai_songs.addAll(MyApplication.repo.repoThirumurai_songs.getByThirumurai_Id(8));
            } else if (ThirumuraiListActivity.selected_page.equalsIgnoreCase(AppConfig.thirukovaiyar)) {
                thirumurai_songs.addAll(MyApplication.repo.repoThirumurai_songs.getByThirumurai_Id(9));
            } else if (ThirumuraiListActivity.selected_page.equalsIgnoreCase(AppConfig.thiruvisaipa)) {
                thirumurai_songs.addAll(MyApplication.repo.repoThirumurai_songs.getByThirumurai_Id(10));
                thirumurai_songs.addAll(MyApplication.repo.repoThirumurai_songs.getByThirumurai_Id(11));
            } else if (ThirumuraiListActivity.selected_page.equalsIgnoreCase(AppConfig.thirumanthiram)) {
                thirumurai_songs.addAll(MyApplication.repo.repoThirumurai_songs.getByThirumurai_Id(12));
            } else if (ThirumuraiListActivity.selected_page.equalsIgnoreCase(AppConfig.prabandham)) {
                thirumurai_songs.addAll(MyApplication.repo.repoThirumurai_songs.getByThirumurai_Id(13));
            } else if (ThirumuraiListActivity.selected_page.equalsIgnoreCase(AppConfig.periyapuranam)) {
                thirumurai_songs.addAll(MyApplication.repo.repoThirumurai_songs.getByThirumurai_Id(14));
            }
            adapter = new SongsAdapter(getContext(), thirumurai_songs);
            akarathi_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            akarathi_recyclerView.setAdapter(adapter);
            akarathi_recyclerView.setNestedScrollingEnabled(false);
            adapter.notifyDataSetChanged();
            edit_search.setHint(AppConfig.getTextString(getContext(), AppConfig.search_hint_thirumurai));
            edit_search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    //after the change calling the method and passing the search input
                    filter(editable.toString());
                }
            });
            search_clear_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    edit_search.setText("");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    private void filter(String text) {
        List<Thirumurai_songs> filterdNames = new ArrayList<>();

        for (Thirumurai_songs s : thirumurai_songs) {
            //if the existing elements contains the search input
            if (s.getTitle().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }
        //calling a method of the adapter class and passing the filtered list
        if (filterdNames.size() > 0) {
            adapter.filterList(filterdNames);
        }
    }
}
