package dfgden.pxart.com.pxart.dialogs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import dfgden.pxart.com.pxart.R;
import dfgden.pxart.com.pxart.activities.MainActivity;
import dfgden.pxart.com.pxart.data.Pattern;
import dfgden.pxart.com.pxart.interfaces.ProgressUpdateDataListener;
import dfgden.pxart.com.pxart.interfaces.ResultUpdateDataListener;
import dfgden.pxart.com.pxart.internet.ServiceManager;
import dfgden.pxart.com.pxart.sharedpreference.PreferenceHelper;

/**
 * Created by DFGDEN on 20.11.2015.
 */
public class SaveDialog extends DialogFragment implements CompoundButton.OnCheckedChangeListener, ProgressUpdateDataListener {


    public static final String KEY_BUNDLE = "bitmap";

    private static final String FILTER_NONE = "none";
    private static final String FILTER_SMOOTHING = "smoothing";
    private static final String FILTER_TILES = "tiles";
    private static final String FILTER_EMBROIDERY = "embroidery";
    private static final String FILTER_KNITTING = "knitting";


    private static final int ZOOM_X1 = 1;
    private static final int ZOOM_X2 = 2;
    private static final int ZOOM_X3 = 3;
    private static final int ZOOM_X4 = 4;
    private int zoom = ZOOM_X1;
    private static final float WIDTH = 420f;
    private static final float HEIGHT = 400f;

    private ProgressDialog progressDialog;

    private Bitmap bitmap;
    private Bitmap serverBitmap;
    private LinearLayout mainLayout;
    private Spinner spinnerChoiceFilter;
    private RadioGroup radioGroupZoom;
    private RadioButton checkBoxZoom1, checkBoxZoom2, checkBoxZoom3, checkBoxZoom4;
    private EditText edtTxtNamePattern;
    private CheckBox checkBoxDraft;
    private Button btnSavePattern;

    private Pattern pattern = new Pattern();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(R.string.saveddialog_titlesavepattern);
        View view = inflater.inflate(R.layout.dialog_save, null);
        mainLayout = (LinearLayout) view.findViewById(R.id.mainLayout);
        spinnerChoiceFilter = (Spinner) view.findViewById(R.id.spinnerChoiceFilter);
        spinnerChoiceFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getImage();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        radioGroupZoom = (RadioGroup) view.findViewById(R.id.radioGroupZoom);
        checkBoxZoom1 = (RadioButton) view.findViewById(R.id.checkBoxZoom1);
        checkBoxZoom1.setOnCheckedChangeListener(this);
        checkBoxZoom2 = (RadioButton) view.findViewById(R.id.checkBoxZoom2);
        checkBoxZoom2.setOnCheckedChangeListener(this);
        checkBoxZoom3 = (RadioButton) view.findViewById(R.id.checkBoxZoom3);
        checkBoxZoom3.setOnCheckedChangeListener(this);
        checkBoxZoom4 = (RadioButton) view.findViewById(R.id.checkBoxZoom4);
        checkBoxZoom4.setOnCheckedChangeListener(this);
        edtTxtNamePattern = (EditText) view.findViewById(R.id.edtTxtNamePattern);
        checkBoxDraft = (CheckBox) view.findViewById(R.id.checkBoxDraft);
        if(PreferenceHelper.getInstance().token.isEmpty()){
            checkBoxDraft.setVisibility(View.GONE);
        }
        btnSavePattern = (Button) view.findViewById(R.id.btnSavePattern);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.progressdialog_dataupdate));
        btnSavePattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage();
            }
        });

        if (getBitmap() != null) {
            bitmap = getBitmap();
            serverBitmap=bitmap;
        } else {
            this.dismiss();
        }


        mainLayout.setBackgroundDrawable(createBitmapDrawable(bitmap, zoom));
        return view;
    }

    public void onResume() {
        getDialog().getWindow().setLayout(convertDpToPixel(WIDTH), convertDpToPixel(HEIGHT));
        super.onResume();
    }

    private BitmapDrawable createBitmapDrawable(Bitmap image, int zoom) {
        Bitmap resultBitmap =
                Bitmap.createScaledBitmap(image, image.getWidth() * zoom, image.getHeight() * zoom, false);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(resultBitmap);
        bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        return bitmapDrawable;
    }

    private String handleChoiceFilter(String filter) {
        String[] arrayFilter = getResources().getStringArray(R.array.savedialog_arraylayer);
        if (arrayFilter[0].equals(filter)) {
            return FILTER_NONE;
        } else if (arrayFilter[1].equals(filter)) {
            return FILTER_SMOOTHING;
        } else if (arrayFilter[2].equals(filter)) {
            return FILTER_TILES;
        } else if (arrayFilter[3].equals(filter)) {
            return FILTER_EMBROIDERY;
        } else if (arrayFilter[4].equals(filter)) {
            return FILTER_KNITTING;
        }
        return null;
    }

    private int convertDpToPixel(float dp) {
        Resources resources = getContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int) (dp * (metrics.densityDpi / 160f));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.checkBoxZoom1:
                    zoom = ZOOM_X1;
                    break;
                case R.id.checkBoxZoom2:
                    zoom = ZOOM_X2;
                    break;
                case R.id.checkBoxZoom3:
                    zoom = ZOOM_X3;
                    break;
                case R.id.checkBoxZoom4:
                    zoom = ZOOM_X4;
                    break;
            }
            mainLayout.setBackgroundDrawable(createBitmapDrawable(serverBitmap, zoom));
        }
    }

    private Bitmap getBitmap() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(KEY_BUNDLE)) {
            return (Bitmap) bundle.get(KEY_BUNDLE);
        }
        return null;
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private Bitmap base64ToBitmap(String b64) {
        String imageDataBytes = b64.substring(b64.indexOf(",") + 1);
        byte[] imageAsBytes = Base64.decode(imageDataBytes.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    private void preparePattern() {
        List<String> array = new ArrayList<String>();
        List<Boolean> arrayVisib = new ArrayList<>();
        arrayVisib.add(true);
        array.add(bitmapToBase64(bitmap));
        pattern.setIsDraft(checkBoxDraft.isChecked());
        pattern.setLayersVisibility(arrayVisib);
        pattern.setCanvases(array);
        pattern.setScale(1);
        pattern.setFilterName(handleChoiceFilter(spinnerChoiceFilter.getSelectedItem().toString()));
    }

    private void getImage() {
        preparePattern();
        ServiceManager.getInstance().sendPattern(pattern, this, new ResultUpdateDataListener<String>() {
            @Override
            public void getResult(final String result) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            serverBitmap=base64ToBitmap(result);
                            mainLayout.setBackgroundDrawable(createBitmapDrawable(serverBitmap, zoom));
                        }
                    });

                }
            }
        });

    }

    private void saveImage() {
        pattern.setScale(zoom);
        pattern.setName(edtTxtNamePattern.getText().toString());
        ServiceManager.getInstance().putPattern(pattern, this, new ResultUpdateDataListener<Pattern>() {
            @Override
            public void getResult(final Pattern result) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent();
                            intent.putExtra(MainActivity.KEY_BUNDLE_PATTERN_ID, result.getId());
                            getActivity().setResult(MainActivity.REQUEST_CODE1, intent);
                            getActivity().finish();
                        }
                    });
                }
            }
        });

    }

    @Override
    public void startUpdate() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.show();

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
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                }
            });

        }
    }

    @Override
    public void crash(final String text) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            });

        }
    }

    public static SaveDialog getInstance(Bitmap bitmap) {
        SaveDialog saveDialog = new SaveDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_BUNDLE, bitmap);
        saveDialog.setArguments(bundle);
        return saveDialog;
    }
}
