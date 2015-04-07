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
        adRequest = adRequestBuilder.build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdView.resume();
    }

    @Override
    public void onPause() {
        mAdView.pause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mAdView.destroy();
        super.onDestroy();
    }
}
