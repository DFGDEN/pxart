package dfgden.pxart.com.pxart.fragments;

import android.os.Bundle;

import java.util.ArrayList;

import dfgden.pxart.com.pxart.R;
import dfgden.pxart.com.pxart.adaptors.BasePatternAdaptor;
import dfgden.pxart.com.pxart.adaptors.PatternAdaptor;
import dfgden.pxart.com.pxart.data.Pattern;

import dfgden.pxart.com.pxart.interfaces.ResultUpdateDataListener;
import dfgden.pxart.com.pxart.internet.ServiceManager;
import dfgden.pxart.com.pxart.internet.URLList;
import dfgden.pxart.com.pxart.sharedpreference.PreferenceHelper;


public class BaseTitlePatternFragment extends BasePatternFragment implements  ResultUpdateDataListener<ArrayList<Pattern>>,BasePatternAdaptor.OnCurrentPositionListener {

    public static final String KEY_BUNDLE_URL="request_url";
    private String url;
    private int skipCount = 0;
    private PatternAdaptor patternAdaptor;
    private ArrayList<Pattern> patternModelArrayList = new ArrayList<>(60);


    protected void initAdaptor() {
        patternAdaptor = new PatternAdaptor(patternModelArrayList, getActivity());
        patternAdaptor.getCurrentItemPosition(this);
        patternAdaptor.setOnItemRecycleViewClickListener(new BasePatternAdaptor.OnItemRecycleViewListener() {
            @Override
            public void onItemClick(String result) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, CommentsFragment.getInstance(result)).addToBackStack("comment").commit();
            }
        });
    }

    @Override
    public void joinAdaptor() {
        if (patternAdaptor!=null){
            recyclerView.setAdapter(patternAdaptor);
        }
    }

    @Override
    protected void getBundleData() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(KEY_BUNDLE_URL)) {
            url= bundle.getString(KEY_BUNDLE_URL);
        }
    }

    public void updateData() {
        ServiceManager.getInstance().getPatterns(createUrl(), this, this);
    }

    private String createUrl() {
        return url + "?skip=" + skipCount;
    }

    private void addSkipCount() {
        skipCount = skipCount + URLList.DEFAULT_SKIP;
    }

    @Override
    public void currentPosition(int pos) {
        if (pos == (skipCount - 1) + URLList.DEFAULT_SKIP) {
            addSkipCount();
            updateData();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        PreferenceHelper.getInstance().putPatterns(patternModelArrayList);

    }

    @Override
    public void getResult(ArrayList<Pattern> result) {
        if (skipCount == 0){
            patternModelArrayList.clear();
        }
            patternModelArrayList.addAll(result);
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (patternAdaptor == null) {
                            initAdaptor();
                            joinAdaptor();
                        }
                        patternAdaptor.notifyDataSetChanged();
                    }
                });
            }
    }

    @Override
    public void onRefresh() {
        skipCount = 0;
        updateData();
    }

    @Override
    protected void loadSaveData() {
            if(patternModelArrayList.size() ==0 && PreferenceHelper.getInstance().getPatterns()!=null){
                if (patternAdaptor == null) {
                    initAdaptor();
                    joinAdaptor();
                }
                patternModelArrayList.addAll(PreferenceHelper.getInstance().getPatterns());
                patternAdaptor.notifyDataSetChanged();
            }
    }

    public static BaseTitlePatternFragment getInstance(String patternId) {
        BaseTitlePatternFragment baseTitlePatternFragment = new BaseTitlePatternFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_BUNDLE_URL, patternId);
        baseTitlePatternFragment.setArguments(bundle);
        return baseTitlePatternFragment;

    }
}
