package dfgden.pxart.com.pxart.adaptors;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;

import dfgden.pxart.com.pxart.R;

import dfgden.pxart.com.pxart.data.Follower;
import dfgden.pxart.com.pxart.data.Following;


public class FollowerAdaptor<T> extends BasePatternAdaptor {

    private T type;
    private Context context;
    private ArrayList<T> followerArrayList;
    private LayoutInflater layoutInflater;

    public FollowerAdaptor(Context context, ArrayList<T> followerArrayList) {
        this.type = followerArrayList.size()>0 ?followerArrayList.get(0) : null ;
        this.context = context;
        this.followerArrayList = followerArrayList;

    }

    @Override
    public RecyclerView.ViewHolder onCreateHeader() {
        return null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateItem() {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.adaptor_follower, null);
        return new FollowerPattern(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if ( type instanceof Follower ){
            prepareFollowerItem((ArrayList<Follower>) followerArrayList,(FollowerPattern)holder,position);
        } else {
            if (type instanceof Following){
                prepareFollowingItem((ArrayList<Following>) followerArrayList,(FollowerPattern)holder,position);
        }
    }
    }

    @Override
    public int getItemCount() {
        return followerArrayList.size();
    }


}
