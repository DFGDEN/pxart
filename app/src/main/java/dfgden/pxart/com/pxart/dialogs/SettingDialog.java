package dfgden.pxart.com.pxart.dialogs;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;

import dfgden.pxart.com.pxart.R;


public class SettingDialog extends DialogFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private static final float WIDTH =395f;
    private static final float HEIGHT =240f;

    public interface GetParamsListener{
        void getParams(int cellToLine, int cellToColumn, int line, int column);
    }
    private GetParamsListener getParamsListener;

    private static int MIN_COUNT_CELL = 10;
    private static int MAX_COUNT_CELL_HEIGHT_OR_WIDTH = 100;
    private static int MAX_COUNT_CELL = 10000;
    private static int MIN_COUNT_COLUMN_OR_LINE = 1;
    private static int MAX_COUNT_COLUMN_OR_LINE = 3;

    private int maxCurrentCellToLine = MAX_COUNT_CELL_HEIGHT_OR_WIDTH;
    private int maxCurrentCellToColumn = MAX_COUNT_CELL_HEIGHT_OR_WIDTH;
    private int maxCurrentColumn = MAX_COUNT_COLUMN_OR_LINE;
    private int maxCurrentLine = MAX_COUNT_COLUMN_OR_LINE;

    private int getCellToLine = MIN_COUNT_CELL;
    private int getCellToColumn = MIN_COUNT_CELL;
    private int getLine = MIN_COUNT_COLUMN_OR_LINE;
    private int getColumn = MIN_COUNT_COLUMN_OR_LINE;


    private Button btnCancel, btnOk;

    private ViewGroup.LayoutParams layoutParams;

    private Spinner spinnerCountCellHeight, spinnerCountCellWidth, spinnerCountColumn, spinnerCountLine;
    private LinearLayout linLayoutPlan;
    private ArrayList<Integer> arrayListSpinnerCountCellHeight = new ArrayList();
    private ArrayList<Integer> arrayListSpinnerCountCellWidth = new ArrayList();
    private ArrayList<Integer> arrayListSpinnerCountColumn = new ArrayList();
    private ArrayList<Integer> arrayListSpinnerCountLine = new ArrayList();
    ArrayAdapter<Integer> adapterSpinnerCountCellHeight,adapterSpinnerCountCellWidth,adapterSpinnerCountColumn,adapterSpinnerCountLine;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_setting, container, false);
        getDialog().setTitle(R.string.settingdialog_title);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);
        btnOk = (Button) view.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);
        spinnerCountCellHeight = (Spinner) view.findViewById(R.id.spinnerCountCellHeight);
        spinnerCountCellWidth = (Spinner) view.findViewById(R.id.spinnerCountCellWidth);
        spinnerCountColumn = (Spinner) view.findViewById(R.id.spinnerCountColumn);
        spinnerCountLine = (Spinner) view.findViewById(R.id.spinnerCountLine);
        linLayoutPlan = (LinearLayout) view.findViewById(R.id.linLayoutPlan);

        layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        initSpinnerCountCellHeightAdaptor();
        initSpinnerCountCellWidthAdaptor();
        initSpinnerSpinnerCountColumnAdaptor();
        initSpinnerSpinnerCountLineAdaptor();
        adapterSpinnerCountCellHeight = new ArrayAdapter<Integer>(getContext(), R.layout.support_simple_spinner_dropdown_item, arrayListSpinnerCountCellHeight);
        adapterSpinnerCountCellWidth = new ArrayAdapter<Integer>(getContext(), R.layout.support_simple_spinner_dropdown_item, arrayListSpinnerCountCellWidth);
        adapterSpinnerCountColumn = new ArrayAdapter<Integer>(getContext(), R.layout.support_simple_spinner_dropdown_item, arrayListSpinnerCountColumn);
        adapterSpinnerCountLine = new ArrayAdapter<Integer>(getContext(), R.layout.support_simple_spinner_dropdown_item, arrayListSpinnerCountLine);
        spinnerCountCellWidth.setAdapter(adapterSpinnerCountCellWidth);
        spinnerCountCellHeight.setAdapter(adapterSpinnerCountCellHeight);
        spinnerCountColumn.setAdapter(adapterSpinnerCountColumn);
        spinnerCountLine.setAdapter(adapterSpinnerCountLine);
        spinnerCountCellHeight.setOnItemSelectedListener(this);
        spinnerCountCellWidth.setOnItemSelectedListener(this);
        spinnerCountColumn.setOnItemSelectedListener(this);
        spinnerCountLine.setOnItemSelectedListener(this);
        drawPlan();
        return view;
    }


    public void onResume() {
        getDialog().getWindow().setLayout(convertDpToPixel(WIDTH), convertDpToPixel(HEIGHT));
        super.onResume();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    public void getColorListener (GetParamsListener getParamsListener){
        this.getParamsListener = getParamsListener;
    }

    private void initSpinnerCountCellHeightAdaptor() {
        maxCurrentCellToColumn = (MAX_COUNT_CELL / getColumn / getLine / getCellToLine >= MAX_COUNT_CELL_HEIGHT_OR_WIDTH) ?
                MAX_COUNT_CELL_HEIGHT_OR_WIDTH : MAX_COUNT_CELL / getColumn / getLine / getCellToLine;
        arrayListSpinnerCountCellHeight.clear();
        for (int i = MIN_COUNT_CELL; i <= maxCurrentCellToColumn; i++) {
            arrayListSpinnerCountCellHeight.add(i);
        }
    }

    private void initSpinnerCountCellWidthAdaptor() {
        maxCurrentCellToLine = (MAX_COUNT_CELL / getLine / getColumn / getCellToColumn >= MAX_COUNT_CELL_HEIGHT_OR_WIDTH) ?
                MAX_COUNT_CELL_HEIGHT_OR_WIDTH : MAX_COUNT_CELL / getLine / getColumn / getCellToColumn;
        arrayListSpinnerCountCellWidth.clear();
        for (int i = MIN_COUNT_CELL; i <= maxCurrentCellToLine; i++) {
            arrayListSpinnerCountCellWidth.add(i);
        }
    }

    private void initSpinnerSpinnerCountColumnAdaptor() {
        maxCurrentColumn = (MAX_COUNT_CELL / getCellToColumn / getLine / getCellToLine >= MAX_COUNT_COLUMN_OR_LINE) ?
                MAX_COUNT_COLUMN_OR_LINE : MAX_COUNT_CELL / getCellToColumn / getLine / getCellToLine;
        arrayListSpinnerCountColumn.clear();
        for (int i = MIN_COUNT_COLUMN_OR_LINE; i <= maxCurrentColumn; i++) {
            arrayListSpinnerCountColumn.add(i);
        }
    }

    private void initSpinnerSpinnerCountLineAdaptor() {
        maxCurrentLine = (MAX_COUNT_CELL / getCellToColumn / getColumn / getCellToLine >= MAX_COUNT_COLUMN_OR_LINE) ?
                MAX_COUNT_COLUMN_OR_LINE : MAX_COUNT_CELL / getCellToColumn / getColumn / getCellToLine;
        arrayListSpinnerCountLine.clear();
        for (int i = MIN_COUNT_COLUMN_OR_LINE; i <= maxCurrentLine; i++) {
            arrayListSpinnerCountLine.add(i);
        }
    }

    private void drawPlan() {
        linLayoutPlan.removeAllViews();
        for (int i = 0; i < getColumn; i++) {
            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setLayoutParams(layoutParams);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linLayoutPlan.addView(linearLayout);
            for (int j = 0; j < getLine; j++) {
                ImageView imageView = new ImageView(getContext());
                imageView.setLayoutParams(layoutParams);
                imageView.setImageResource(R.drawable.ic_grid_black_36dp);
                linearLayout.addView(imageView);
            }
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        getCellToColumn = (Integer) spinnerCountCellHeight.getSelectedItem();
        getCellToLine = (Integer) spinnerCountCellWidth.getSelectedItem();
        if (getLine != (Integer) spinnerCountLine.getSelectedItem()){
            getLine =(Integer) spinnerCountLine.getSelectedItem();
            drawPlan();
        }
        if (getColumn != (Integer) spinnerCountColumn.getSelectedItem()){
            getColumn =(Integer) spinnerCountColumn.getSelectedItem();
            drawPlan();
        }
        initSpinnerCountCellHeightAdaptor();
        initSpinnerCountCellWidthAdaptor();
        initSpinnerSpinnerCountColumnAdaptor();
        initSpinnerSpinnerCountLineAdaptor();
        adapterSpinnerCountCellHeight.notifyDataSetChanged();
        adapterSpinnerCountCellWidth.notifyDataSetChanged();
        adapterSpinnerCountColumn.notifyDataSetChanged();
        adapterSpinnerCountLine.notifyDataSetChanged();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCancel:
                getDialog().dismiss();

                break;
            case R.id.btnOk:
                if (getParamsListener != null){
                    getParamsListener.getParams(getCellToLine,getCellToColumn,getLine,getColumn);
                }
                getDialog().dismiss();

                break;
        }
    }

    private int convertDpToPixel(float dp) {
        Resources resources = getContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int)(dp * (metrics.densityDpi / 160f));
    }
}

