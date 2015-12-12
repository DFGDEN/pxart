package dfgden.pxart.com.pxart.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import dfgden.pxart.com.pxart.R;

import dfgden.pxart.com.pxart.interfaces.ProgressUpdateDataListener;


public abstract class BasePatternFragment extends Fragment implements ProgressUpdateDataListener, SwipeRefreshLayout.OnRefreshListener {

    protected Parcelable state;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected RecyclerView recyclerView;
    protected LinearLayoutManager layoutManager;
    protected LinearLayout commentLayout;
    protected ImageView btnPostComment;
    protected EditText edtPostComment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_pattern, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        initRecyclerView();
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        initSwipeRefreshLayout();
        commentLayout =(LinearLayout) view.findViewById(R.id.commentLayout);
        if(isVisibleComment()) {
            commentLayout.setVisibility(View.VISIBLE);
            edtPostComment = (EditText) view.findViewById(R.id.edtPostComment);
            TextInputLayout tilPostComment=(TextInputLayout) view.findViewById(R.id.tilPostComment);
            tilPostComment.setHint(getString(R.string.basepatternfragment_tilpostcomment));
            btnPostComment = (ImageView) view.findViewById(R.id.btnPostComment);
            sendComment();
        }
        return view;
    }

    protected boolean isVisibleComment(){
        return false;
    }

    protected void sendComment(){};


    @Override
    public void onStart() {
        super.onStart();
        getBundleData();
        if (state != null) {
            layoutManager.onRestoreInstanceState(state);
        } else  {

            updateData();
        }
        joinAdaptor();

    }

    public abstract void joinAdaptor();

    @Override
    public void onPause() {
        super.onPause();
        mSwipeRefreshLayout.setRefreshing(false);
        state = recyclerView.getLayoutManager().onSaveInstanceState();
    }

    protected abstract void getBundleData();

    private void initRecyclerView() {
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(itemAnimator);
    }


    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(
                Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);
    }

    @Override
    public void startUpdate() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
            });
        }
    }

    @Override
    public void stopUpdate() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    }



    public abstract void updateData();

    @Override
    public void crash(final String text) {
        if (getActivity() != null ) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                    loadSaveData();
                }
            });

        }
    }
    protected void loadSaveData(){}
}
