package dfgden.pxart.com.pxart.fragments;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import dfgden.pxart.com.pxart.R;
import dfgden.pxart.com.pxart.adaptors.BasePatternAdaptor;
import dfgden.pxart.com.pxart.adaptors.CommentAdaptor;
import dfgden.pxart.com.pxart.data.Comment;
import dfgden.pxart.com.pxart.data.Pattern;
import dfgden.pxart.com.pxart.interfaces.ProgressUpdateDataListener;
import dfgden.pxart.com.pxart.interfaces.ResultUpdateDataListener;
import dfgden.pxart.com.pxart.internet.ServiceManager;
import dfgden.pxart.com.pxart.internet.URLList;
import dfgden.pxart.com.pxart.sharedpreference.PreferenceHelper;


/**
 * Created by DFGDEN on 30.11.2015.
 */
public class CommentsFragment extends BasePatternFragment {

    public static final String BUNDLE_KEY_COMMENTS = "key_comments";
    private String patternId;
    private Pattern pattern;
    private CommentAdaptor commentAdaptor;
    private ArrayList<Pattern> patternArrayList = new ArrayList<>();
    private ArrayList<Comment> commentArrayList = new ArrayList<>();

    protected void initAdaptor() {
        commentAdaptor = new CommentAdaptor(patternArrayList,commentArrayList, getActivity());
        commentAdaptor.setOnAuthorNameClickListener(new BasePatternAdaptor.OnAuthorNameListener() {
            @Override
            public void onNameClickListener(String author) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, UserFragment.getInstance(author)).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public void joinAdaptor() {
        if (commentAdaptor != null){
            recyclerView.setAdapter(commentAdaptor);
        }
    }

    @Override
    public void updateData() {
        ServiceManager.getInstance().getImage(getUrl(), this, new ResultUpdateDataListener<Pattern>() {
            @Override
            public void getResult(final Pattern result) {
        patternArrayList.clear();
                commentArrayList.clear();
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                patternArrayList.add(result);
                                commentArrayList.addAll(result.getComments());
                                if (commentAdaptor == null) {
                                    initAdaptor();
                                    joinAdaptor();
                                }
                                commentAdaptor.notifyDataSetChanged();
                            }
                        });
                    }
            }
        });
    }

    @Override
    public void onRefresh() {
updateData();
    }

    public String getUrl() {
      return URLList.PAGE_NEW_PATTERN.getUrl()+patternId;
    }

    protected void getBundleData() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(BUNDLE_KEY_COMMENTS)) {
            patternId = bundle.getString(BUNDLE_KEY_COMMENTS);

        }

    }

    @Override
    protected boolean isVisibleComment(){
        return !PreferenceHelper.getInstance().token.isEmpty();
    }

    @Override
    protected void sendComment() {

        btnPostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            ServiceManager.getInstance().postComment(getCommentUrl(), edtPostComment.getText().toString(), new ProgressUpdateDataListener() {
                @Override
                public void startUpdate() {

                }

                @Override
                public void stopUpdate() {
                    updateData();
                    if(getActivity()!=null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                edtPostComment.setText("");
                            }
                        });
                    }

                }

                @Override
                public void crash(final String text) {
                    if(getActivity()!=null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),text,Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
            }
        });
    }

    private String getCommentUrl(){
        return URLList.PAGE_NEW_PATTERN.getUrl()+patternId+"/comments";
    }


    public static CommentsFragment getInstance(String patternId) {
        CommentsFragment commentsFragment = new CommentsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_COMMENTS, patternId);
        commentsFragment.setArguments(bundle);
        return commentsFragment;
    }
}
