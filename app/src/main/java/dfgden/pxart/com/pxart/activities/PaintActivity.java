package dfgden.pxart.com.pxart.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import com.github.danielnilsson9.colorpickerview.dialog.ColorPickerDialogFragment;

import java.util.ArrayList;
import java.util.HashMap;

import dfgden.pxart.com.pxart.R;
import dfgden.pxart.com.pxart.dialogs.ClearDialog;
import dfgden.pxart.com.pxart.dialogs.SaveDialog;
import dfgden.pxart.com.pxart.dialogs.SettingDialog;
import dfgden.pxart.com.pxart.models.BitmapModel;
import dfgden.pxart.com.pxart.customviews.PaintView;
import dfgden.pxart.com.pxart.sharedpreference.PreferenceHelper;


public class PaintActivity extends AppCompatActivity implements ColorPickerDialogFragment.ColorPickerDialogListener, View.OnClickListener, View.OnLongClickListener {


    public static final String CURRENT_PATTERN = "current_pattern";
    private static final String SAVE_PATTERN_MODEL = "save_pattern_model";


    private SaveDialog saveDialog;
    private SettingDialog settingDialog;
    private ClearDialog clearDialog;
    private PaintView paintView;
    private ColorPickerDialogFragment colorPickerDialogFragment;
    private ToggleButton imgBrush, imgBtnEyedropper, imgBtnErase, imgBtnFormatPaint, imgBtnCursorMove;
    private Button imgBtnBaseChangeColor, imgBtnUndo, imgBtnRedo, imgBtnSetting, imgBtnChangeColor1, imgBtnChangeColor2, imgBtnChangeColor3, imgBtnSave, imgBtnDel;

    private ArrayList<ToggleButton> imageButtonArrayList = new ArrayList<>();
    private ArrayList<Button> arrayListColorButton = new ArrayList<>();
    private ArrayList<Integer> arrayListColor = new ArrayList<>();

    {
        arrayListColor.add(Color.BLUE);
        arrayListColor.add(Color.YELLOW);
        arrayListColor.add(Color.GREEN);
        arrayListColor.add(Color.RED);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);
        paintView = (PaintView) findViewById(R.id.paintModel);
        imgBtnBaseChangeColor = (Button) findViewById(R.id.imgBtnBaseChangeColor);
        imgBtnBaseChangeColor.setOnClickListener(this);
        arrayListColorButton.add(imgBtnBaseChangeColor);
        imgBtnChangeColor1 = (Button) findViewById(R.id.imgBtnChangeColor1);
        imgBtnChangeColor1.setOnClickListener(this);
        imgBtnChangeColor1.setOnLongClickListener(this);
        arrayListColorButton.add(imgBtnChangeColor1);
        imgBtnChangeColor2 = (Button) findViewById(R.id.imgBtnChangeColor2);
        imgBtnChangeColor2.setOnClickListener(this);
        imgBtnChangeColor2.setOnLongClickListener(this);
        arrayListColorButton.add(imgBtnChangeColor2);
        imgBtnChangeColor3 = (Button) findViewById(R.id.imgBtnChangeColor3);
        imgBtnChangeColor3.setOnClickListener(this);
        imgBtnChangeColor3.setOnLongClickListener(this);
        arrayListColorButton.add(imgBtnChangeColor3);
        for (int i = 0; i <arrayListColor.size() ; i++) {
            arrayListColorButton.get(i).setBackgroundColor(arrayListColor.get(i));
        }

        imgBtnEyedropper = (ToggleButton) findViewById(R.id.imgBtnEyedropper);
        imgBtnEyedropper.setOnClickListener(this);
        imageButtonArrayList.add(imgBtnEyedropper);

        imgBrush = (ToggleButton) findViewById(R.id.imgBrush);
        imgBrush.setOnClickListener(this);
        imgBrush.setChecked(true);
        paintView.setIsDraw(true);
        imageButtonArrayList.add(imgBrush);

        imgBtnErase = (ToggleButton) findViewById(R.id.imgBtnErase);
        imgBtnErase.setOnClickListener(this);
        imageButtonArrayList.add(imgBtnErase);

        imgBtnFormatPaint = (ToggleButton) findViewById(R.id.imgBtnFormatPaint);
        imgBtnFormatPaint.setOnClickListener(this);
        imageButtonArrayList.add(imgBtnFormatPaint);

        imgBtnCursorMove = (ToggleButton) findViewById(R.id.imgBtnCursorMove);
        imgBtnCursorMove.setOnClickListener(this);
        imageButtonArrayList.add(imgBtnCursorMove);

        imgBtnUndo = (Button) findViewById(R.id.imgBtnUndo);
        imgBtnUndo.setOnClickListener(this);

        imgBtnRedo = (Button) findViewById(R.id.imgBtnRedo);
        imgBtnRedo.setOnClickListener(this);

        imgBtnDel = (Button) findViewById(R.id.imgBtnDel);
        imgBtnDel.setOnClickListener(this);

        imgBtnSetting = (Button) findViewById(R.id.imgBtnSetting);
        imgBtnSetting.setOnClickListener(this);

        imgBtnSave = (Button) findViewById(R.id.imgBtnSave);
        imgBtnSave.setOnClickListener(this);

        paintView.setColor(arrayListColor.get(0));

        settingDialog = new SettingDialog();
        settingDialog.getColorListener(new SettingDialog.GetParamsListener() {
            @Override
            public void getParams(int cellToLine, int cellToColumn, int line, int column) {
                paintView.setProperty(cellToLine, cellToColumn, column, line);
            }
        });

        prepareClearDialog();
        if (savedInstanceState == null) {
            if (PreferenceHelper.getInstance().getPaintModel() != null) {
                BitmapModel bitmapModel = PreferenceHelper.getInstance().getPaintModel();
                paintView.loadPattern(bitmapModel.getBitmapArray(), bitmapModel.getWidth(), bitmapModel.getHeight(), bitmapModel.getFragmentX(), bitmapModel.getFragmentY());
            }
        }

        paintView.setOnColorTouchListener(new PaintView.ColorTouchListener() {
            @Override
            public void getColor(int color) {
                if (imgBtnEyedropper.isChecked()) {
                    arrayListColorButton.get(0).setBackgroundColor(color);
                    paintView.setColor(color);
                    arrayListColor.set(0,color);
                }
            }
        });
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey(SAVE_PATTERN_MODEL)) {
            BitmapModel bitmapModel = (BitmapModel) savedInstanceState.getSerializable(SAVE_PATTERN_MODEL);
            paintView.loadPattern(bitmapModel.getBitmapArray(), bitmapModel.getWidth(), bitmapModel.getHeight(), bitmapModel.getFragmentX(), bitmapModel.getFragmentY());
        }
        for (ToggleButton btn : imageButtonArrayList) {
            btn.setChecked(false);
        }
        imgBrush.setChecked(true);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable(SAVE_PATTERN_MODEL, saveBitmap());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getIntent().getParcelableExtra(CURRENT_PATTERN) != null) {
            paintView.setBitmap((Bitmap) getIntent().getParcelableExtra(CURRENT_PATTERN));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        PreferenceHelper.getInstance().putPaintModel(saveBitmap());
    }

    @Override
    public void onColorSelected(int dialogId, int color) {
        for (int i = 0; i < arrayListColor.size(); i++) {
            if (dialogId== i){
                arrayListColor.set(i, color);
                arrayListColorButton.get(i).setBackgroundColor(color);
                arrayListColorButton.get(0).setBackgroundColor(color);
                arrayListColor.set(0, color);
                if (!imgBtnErase.isChecked()) {
                    paintView.setColor(color);

                }
            }
        }
    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtnBaseChangeColor :
                colorPickerDialogFragment = ColorPickerDialogFragment.newInstance(0, null, null, arrayListColor.get(0), true);
                colorPickerDialogFragment.show(getFragmentManager(), "colorpicker");
                break;

            case R.id.imgBtnSave:
                saveDialog = SaveDialog.getInstance(paintView.getBitmap());
                if (!saveDialog.isAdded()) {
                    saveDialog.show(getSupportFragmentManager(), "savedialog");
                }
                break;
            case R.id.imgBtnRedo:
                paintView.bufferNext();
                break;
            case R.id.imgBtnUndo:
                paintView.bufferBack();
                break;
            case R.id.imgBtnSetting:
                settingDialog.show(getSupportFragmentManager(), "setting");
                break;
            case R.id.imgBtnDel:
                clearDialog.show(getSupportFragmentManager(), "cleardialog");

                break;

            default:
                for (int i = 1; i < arrayListColorButton.size() ; i++) {
                    if(arrayListColorButton.get(i).getId() == v.getId()){
                        if (!imgBtnErase.isChecked()) {
                            paintView.setColor(arrayListColor.get(i));
                            arrayListColorButton.get(0).setBackgroundColor(arrayListColor.get(i));
                            arrayListColor.set(0, arrayListColor.get(i));
                        }
                    }
                }

                for (int i = 0; i < imageButtonArrayList.size(); i++) {
                    if (imageButtonArrayList.get(i).getId() == v.getId()){
                        for (int j = 0; j < imageButtonArrayList.size(); j++) {
                            imageButtonArrayList.get(j).setChecked(imageButtonArrayList.get(j).getId() == v.getId());
                        }
                        paintView.setIsScrolling(false);
                        paintView.setIsFilling(false);
                        paintView.setIsDraw(false);
                        break;
                    }
                }

                switch (v.getId()) {
                    case R.id.imgBrush:
                        paintView.setIsDraw(true);
                        paintView.setColor(arrayListColor.get(0));
                        break;
                    case R.id.imgBtnErase:
                        paintView.setIsDraw(true);
                        paintView.setColor(Color.WHITE);
                        break;
                    case R.id.imgBtnFormatPaint:
                        paintView.setIsDraw(true);
                        paintView.setIsFilling(true);
                        paintView.setColor(arrayListColor.get(0));
                        break;
                    case R.id.imgBtnCursorMove:
                        paintView.setIsScrolling(true);
                        break;
                }
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        for (int i = 1; i < arrayListColorButton.size() ; i++) {
                if(v.getId() == arrayListColorButton.get(i).getId()){
                    colorPickerDialogFragment = ColorPickerDialogFragment.newInstance(i, null, null, arrayListColor.get(i), true);
                    colorPickerDialogFragment.show(getFragmentManager(), "colorpicker");
                }
        }
        return false;
    }

    private void prepareClearDialog(){
        clearDialog = new ClearDialog() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        paintView.clearField();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        clearDialog.dismiss();
                        break;
                    default:
                        clearDialog.dismiss();
                        break;
                }
            }
        };
    }

    private BitmapModel saveBitmap() {
        BitmapModel bitmapModel = new BitmapModel();
        bitmapModel.setBitmapArray(paintView.getColorArray());
        bitmapModel.setFragmentX(paintView.getCountPaintFragmentX());
        bitmapModel.setFragmentY(paintView.getCountPaintFragmentY());
        bitmapModel.setHeight(paintView.getCountCellHeight());
        bitmapModel.setWidth(paintView.getCountCellWidth());
        return bitmapModel;
    }
}
