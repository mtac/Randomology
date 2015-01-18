package org.jumpingtree.randomology.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.jumpingtree.randomology.R;

/**
 * Created by Miguel on 15/01/2015.
 */
public class AdFragment extends Fragment {

    private AdView mAdView;
    private AdRequest.Builder adRequestBuilder;
    private AdRequest adRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ad, container, false);
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        mAdView = (AdView) getView().findViewById(R.id.adView);
        adRequestBuilder = new AdRequest.Builder();
        //adRequestBuilder.addTestDevice("24BEB71D9938C9EF293DFD186C0695E6");
        adRequest = adRequestBuilder.build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onDestroy() {
        mAdView.destroy();
        super.onDestroy();
    }
}
