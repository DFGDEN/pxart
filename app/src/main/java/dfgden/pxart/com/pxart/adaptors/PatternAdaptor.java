package dfgden.pxart.com.pxart.adaptors;


import android.app.Activity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import java.util.ArrayList;
import dfgden.pxart.com.pxart.R;
import dfgden.pxart.com.pxart.data.Pattern;



public class PatternAdaptor extends BasePatternAdaptor {

    private ArrayList<Pattern> patternModels;
    private Activity activity;

    public PatternAdaptor(ArrayList<Pattern> patternModels, Activity activity) {
        this.patternModels = patternModels;
        this.activity =activity;

    }

    @Override
    public RecyclerView.ViewHolder onCreateHeader() {
        return null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateItem() {
        View view = LayoutInflater.from(activity).inflate(R.layout.adaptor_base_pattern, null);
        return new BasePattern(view);
    }


    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    prepareBaseItem((BasePattern) holder, patternModels,position,activity);
    }

    @Override
    public int getItemCount() {
        return patternModels.size();
    }


}
