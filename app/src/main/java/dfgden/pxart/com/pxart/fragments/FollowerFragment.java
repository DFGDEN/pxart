package dfgden.pxart.com.pxart.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import dfgden.pxart.com.pxart.R;
import dfgden.pxart.com.pxart.adaptors.BasePatternAdaptor;
import dfgden.pxart.com.pxart.adaptors.FollowerAdaptor;
import dfgden.pxart.com.pxart.data.Author;
import dfgden.pxart.com.pxart.data.Follower;
import dfgden.pxart.com.pxart.data.Following;


public class FollowerFragment extends Fragment {

    public static final String KEY_BUNDLE_FOLLOWER = "key_follower";
    public static final String KEY_BUNDLE_FOLLOWING = "key_following";

    private ArrayList<Follower> followerArrayList ;
    private ArrayList<Following> followingArrayList;
    private FollowerAdaptor followerAdaptor;
    private RecyclerView recyclerView;
    private TextView txtFollowers;
    private String title;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_follower, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewFollower);
        txtFollowers = (TextView) view.findViewById(R.id.txtFollowers);
        initRecyclerView();
        return view;
    }
    private  void initRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(itemAnimator);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBundleData();
    }

    @Override
    public void onStart() {
        super.onStart();
        initAdaptor();
        txtFollowers.setText(title);
    }

    protected void getBundleData() {
        Bundle bundle = getArguments();
        Author author =null;
        if(bundle!=null) {
            if (bundle.containsKey(KEY_BUNDLE_FOLLOWER)) {
                 author = (Author) bundle.getSerializable(KEY_BUNDLE_FOLLOWER);
                followerArrayList=(author.getFollowers());
                title = getString(R.string.followerfragment_titlefollowing)+ author.getName()+":";

            } else {
                if (bundle.containsKey(KEY_BUNDLE_FOLLOWING)) {
                    author = (Author) bundle.getSerializable(KEY_BUNDLE_FOLLOWING);
                    followingArrayList=(author.getFollowings());
                    title = author.getName() +getString(R.string.followerfragment_titlefollower);
                }
            }

        }
    }

    private void initAdaptor() {
        if (followerArrayList!=null){
            followerAdaptor = new FollowerAdaptor<Follower>( getContext(),followerArrayList);
        } else {
            if (followingArrayList!=null){
                followerAdaptor = new FollowerAdaptor<Following>(getContext(),followingArrayList);
            }
        }

        recyclerView.setAdapter(followerAdaptor);
        followerAdaptor.setOnAuthorNameClickListener(new BasePatternAdaptor.OnAuthorNameListener() {
            @Override
            public void onNameClickListener(String author) {
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom).replace(R.id.container, UserFragment.getInstance(author)).addToBackStack(null).commit();
            }
        }
       );
    }

    public static FollowerFragment getInstance(String key, Author author) {
        FollowerFragment followerFragment = new FollowerFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(key,author);
        followerFragment.setArguments(bundle);
        return followerFragment;
    }

}
