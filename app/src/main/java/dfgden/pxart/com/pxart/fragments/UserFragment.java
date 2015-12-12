package dfgden.pxart.com.pxart.fragments;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

import dfgden.pxart.com.pxart.R;
import dfgden.pxart.com.pxart.adaptors.BasePatternAdaptor;
import dfgden.pxart.com.pxart.adaptors.UserAdaptor;
import dfgden.pxart.com.pxart.data.Author;
import dfgden.pxart.com.pxart.data.Pattern;
import dfgden.pxart.com.pxart.interfaces.ProgressUpdateDataListener;
import dfgden.pxart.com.pxart.interfaces.ResultUpdateDataListener;
import dfgden.pxart.com.pxart.internet.ServiceManager;
import dfgden.pxart.com.pxart.internet.URLList;


public class UserFragment  extends BasePatternFragment implements ProgressUpdateDataListener, ResultUpdateDataListener, BasePatternAdaptor.OnCurrentPositionListener {

    public static final String BUNDLE_KEY_USER = "key_user";
    private int skipCount = 0;
    private UserAdaptor userAdaptor;
    private ArrayList<Pattern> patternModelArrayList = new ArrayList<>(60);
    private ArrayList<Author> authorArrayList = new ArrayList<>();
    private String authorName;

    @Override
    public void joinAdaptor() {
        if (userAdaptor != null){
            recyclerView.setAdapter(userAdaptor);
        }
    }

    @Override
    public void updateData() {
        ServiceManager.getInstance().getUserInfo(createAuthorUrl(authorName), createPatternUrl(authorName), this, this);
    }

   protected void initAdaptor() {
        userAdaptor = new UserAdaptor( getActivity(), authorArrayList, patternModelArrayList);
        userAdaptor.getCurrentItemPosition(this);
        userAdaptor.setOnItemRecycleViewClickListener(new BasePatternAdaptor.OnItemRecycleViewListener() {
            @Override
            public void onItemClick(String result) {
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom).replace(R.id.container, CommentsFragment.getInstance(result)).addToBackStack(null).commit();
            }
        });
        userAdaptor.setOnFollowerClickListener(new BasePatternAdaptor.OnFollowerListener() {
                                                   @Override
                                                   public void getFollower(Author author) {
                                                       getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, FollowerFragment.getInstance(FollowerFragment.KEY_BUNDLE_FOLLOWER, author)).addToBackStack(null).commit();
                                                   }

                                                   @Override
                                                   public void getFollowing(Author author) {
                                                       getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, FollowerFragment.getInstance(FollowerFragment.KEY_BUNDLE_FOLLOWING, author)).addToBackStack(null).commit();
                                                   }
                                               }
        );
    }

    protected void getBundleData() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(BUNDLE_KEY_USER)) {
            authorName =  bundle.getString(BUNDLE_KEY_USER);
        }
    }

    private String createPatternUrl(String authorName) {
        return getPatternUrl() +  authorName + "/patterns?skip=" + skipCount;
    }

    private String createAuthorUrl(String authorName) {
        return getPatternUrl() +  authorName;
    }


    private String getPatternUrl() {
        return URLList.PAGE_FOLLOWER.getUrl();
    }

    private void addSkipCount() {
        skipCount = skipCount + URLList.DEFAULT_SKIP;
    }

    @Override
    public void getResult(Object result) {
            if (result instanceof  Author) {
                authorArrayList.clear();
                authorArrayList.add((Author)result);
            } else {
                if (skipCount == 0){
                    patternModelArrayList.clear();
                }
                patternModelArrayList.addAll((ArrayList<Pattern>) result);
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (userAdaptor == null){
                                initAdaptor();
                                joinAdaptor();
                            }
                            userAdaptor.notifyDataSetChanged();
                        }
                    });
                }
            }
    }

    @Override
    public void onRefresh() {
        skipCount = 0;
        updateData();
    }

    @Override
    public void crash(final String text) {
        if (getActivity() != null ) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    }

    public static UserFragment getInstance(String authorName) {
        UserFragment userFragment = new UserFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_USER, authorName);
        userFragment.setArguments(bundle);
        return userFragment;
    }

    @Override
    public void currentPosition(int pos) {
        if (pos == (skipCount-1) + URLList.DEFAULT_SKIP) {
            addSkipCount();
            updateData();
        }
    }
}
