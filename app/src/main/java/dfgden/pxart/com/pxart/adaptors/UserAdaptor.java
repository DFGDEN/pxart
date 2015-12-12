package dfgden.pxart.com.pxart.adaptors;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import dfgden.pxart.com.pxart.R;
import dfgden.pxart.com.pxart.data.Author;

import dfgden.pxart.com.pxart.data.Pattern;

public class UserAdaptor extends BasePatternAdaptor {

    private Activity activity;
    private List<Author> authorList;
    private ArrayList<Pattern> patternList;

    public UserAdaptor(Activity activity, List<Author> authorList, ArrayList<Pattern> patternList) {
        this.activity = activity;
        this.authorList = authorList;
        this.patternList = patternList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeader() {
        View view = LayoutInflater.from(activity).inflate(R.layout.adaptor_user_header, null);
        return new UserPattern(view);
    }

    @Override
    public RecyclerView.ViewHolder onCreateItem() {
        View view = LayoutInflater.from(activity).inflate(R.layout.adaptor_base_pattern, null);
        return new BasePattern(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UserPattern) {
            UserPattern header = (UserPattern) holder;
           prepareUserHeader(header, authorList.get(0),activity);

        } else if (holder instanceof BasePattern) {
            BasePattern item = (BasePattern) holder;
            prepareBaseItem(item, patternList,getItem(position),activity);
        }
    }

    @Override
    public int getItemCount() {
        return patternList.size()+1;
    }

}
