package dfgden.pxart.com.pxart.adaptors;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import dfgden.pxart.com.pxart.R;

import dfgden.pxart.com.pxart.data.Comment;
import dfgden.pxart.com.pxart.data.Pattern;


public class CommentAdaptor extends BasePatternAdaptor {

    private Activity activity;
    private ArrayList<Pattern> patterns;
    private List<Comment> comments;

    public CommentAdaptor(ArrayList<Pattern> patterns, List<Comment> comments , Activity activity) {
        this.patterns = patterns;
        this.activity = activity;
        this.comments =comments;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeader() {
        View view = LayoutInflater.from(activity).inflate(R.layout.adaptor_comments_header, null);
        return new BasePattern(view);
    }

    @Override
    public RecyclerView.ViewHolder onCreateItem() {
        View view = LayoutInflater.from(activity).inflate(R.layout.adaptor_comments_items, null);
        return new CommentPattern(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof BasePattern) {
            final BasePattern header = (BasePattern) holder;
            prepareBaseItem(header, patterns,position,activity);
        } else if (holder instanceof CommentPattern) {
            final CommentPattern item = (CommentPattern) holder;
            prepareCommentItem(item,comments,position);
        }
    }

    @Override
    protected boolean isClickableAvaBaseItem() {
        return true;
    }

    @Override
    public int getItemCount() {
        return comments.size() + 1;
    }

}
