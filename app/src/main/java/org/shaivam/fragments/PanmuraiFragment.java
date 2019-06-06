package org.shaivam.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.shaivam.R;
import org.shaivam.activities.ThirumuraiListActivity;
import org.shaivam.adapter.PanmuraiParentAdapter;
import org.shaivam.model.Thirumurai;
import org.shaivam.utils.AppConfig;
import org.shaivam.utils.MyApplication;
import org.shaivam.utils.SnappingLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class PanmuraiFragment extends Fragment {
    private static PanmuraiParentAdapter panmuraiParentAdapter;
    public RecyclerView recycle_panmurai_parent;
    List<Thirumurai> thirumuraiList = new ArrayList<Thirumurai>();

    public PanmuraiFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_panmurai, container, false);
        thirumuraiList.clear();
        try {
            recycle_panmurai_parent = view.findViewById(R.id.recycle_panmurai_parent);
            if (ThirumuraiListActivity.selected_page.equalsIgnoreCase(AppConfig.thevaram)) {
                thirumuraiList.add(MyApplication.repo.repoThirumurais.getById("1"));
                thirumuraiList.add(MyApplication.repo.repoThirumurais.getById("2"));
                thirumuraiList.add(MyApplication.repo.repoThirumurais.getById("3"));
                thirumuraiList.add(MyApplication.repo.repoThirumurais.getById("4"));
                thirumuraiList.add(MyApplication.repo.repoThirumurais.getById("5"));
                thirumuraiList.add(MyApplication.repo.repoThirumurais.getById("6"));
                thirumuraiList.add(MyApplication.repo.repoThirumurais.getById("7"));
            } else if (ThirumuraiListActivity.selected_page.equalsIgnoreCase(AppConfig.thiruvasagam)) {
                thirumuraiList.add(MyApplication.repo.repoThirumurais.getById("8"));
            } else if (ThirumuraiListActivity.selected_page.equalsIgnoreCase(AppConfig.thirukovaiyar)) {
                thirumuraiList.add(MyApplication.repo.repoThirumurais.getById("9"));
            } else if (ThirumuraiListActivity.selected_page.equalsIgnoreCase(AppConfig.thiruvisaipa)) {
                thirumuraiList.add(MyApplication.repo.repoThirumurais.getById("10"));
                thirumuraiList.add(MyApplication.repo.repoThirumurais.getById("11"));
            } else if (ThirumuraiListActivity.selected_page.equalsIgnoreCase(AppConfig.thirumanthiram)) {
                thirumuraiList.add(MyApplication.repo.repoThirumurais.getById("12"));
            } else if (ThirumuraiListActivity.selected_page.equalsIgnoreCase(AppConfig.prabandham)) {
                thirumuraiList.add(MyApplication.repo.repoThirumurais.getById("13"));
            } else if (ThirumuraiListActivity.selected_page.equalsIgnoreCase(AppConfig.periyapuranam)) {
                thirumuraiList.add(MyApplication.repo.repoThirumurais.getById("14"));
            }
            panmuraiParentAdapter = new PanmuraiParentAdapter(getContext(),thirumuraiList);
            recycle_panmurai_parent.setLayoutManager(new SnappingLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            recycle_panmurai_parent.setAdapter(panmuraiParentAdapter);
            recycle_panmurai_parent.setNestedScrollingEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }
}
