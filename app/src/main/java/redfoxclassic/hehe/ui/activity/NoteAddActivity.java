package redfoxclassic.hehe.ui.activity;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import redfoxclassic.hehe.R;
import redfoxclassic.hehe.data.maindb.DBManager;
import redfoxclassic.hehe.model.NoteModel;
import redfoxclassic.hehe.util.MyDate;
import redfoxclassic.hehe.util.MyMiscellaneousUtil;

public class NoteAddActivity extends AppCompatActivity {

    private final static String TAG = NoteAddActivity.class.getSimpleName();

    @BindView(R.id.add_note_etxTitle)
    EditText addNoteEtxTitle;
    @BindView(R.id.add_note_etxContent)
    EditText addNoteEtxContent;
    @BindView(R.id.add_note_dateTvOne)
    TextView addNoteDateTv;
    @BindView(R.id.add_note_dateTvTwo)
    TextView addNoteDateTv2;
    @BindView(R.id.add_note_bold)
    ImageView bold;
    @BindView(R.id.add_note_italic)
    ImageView italic;
    @BindView(R.id.add_note_textPlus)
    ImageView plusBtn;
    @BindView(R.id.add_note_textMinus)
    ImageView minusBtn;
    @BindView(R.id.add_note_edit_layout)
    LinearLayout editLayout;
    @BindView(R.id.add_note_default_layout)
    LinearLayout defaultLayout;
    @BindView(R.id.add_note_default_all)
    Button defaultAll;
    @BindView(R.id.add_note_title_btn)
    Button titleBtn;
    @BindView(R.id.add_note_toggle)
    ToggleButton toggle;

    private Toolbar toolbar;


    private float defaultTextSizeTitle = 16;
    private float defaultTextSizeContent = 14;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_add);
        ButterKnife.bind(this);

        setupToolbar();
        String date = MyDate.formatDate(System.currentTimeMillis());
        addNoteDateTv.setText(date);
        addNoteDateTv2.setText(date);
    }

    void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.add_note_toolbar);
        setSupportActionBar(toolbar);

        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.createToolBarBarPrimary));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.createStatusBarPrimary));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.create_share) {
            MyMiscellaneousUtil.shareData(
                    addNoteEtxTitle.getText().toString().trim(),
                    addNoteEtxContent.getText().toString().trim(),
                    NoteAddActivity.this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.add_note_bold, R.id.add_note_italic})
    public void onViewClickedTextStyleFormat(View view) {
        switch (view.getId()) {
            case R.id.add_note_bold:
                if (addNoteEtxContent.hasFocus()) {
                    addNoteEtxContent.setTypeface(null, Typeface.BOLD);
                }
                if (addNoteEtxTitle.hasFocus()) {
                    addNoteEtxTitle.setTypeface(null, Typeface.BOLD);
                }


                break;
            case R.id.add_note_italic:

                if (addNoteEtxContent.hasFocus()) {
                    addNoteEtxContent.setTypeface(null, Typeface.ITALIC);
                }
                if (addNoteEtxTitle.hasFocus()) {
                    addNoteEtxTitle.setTypeface(null, Typeface.ITALIC);
                }

                break;

        }
    }

    @OnClick({R.id.add_note_textPlus, R.id.add_note_textMinus})
    public void onViewClickedTextSizeFormat(View view) {
        switch (view.getId()) {
            case R.id.add_note_textPlus:
                if (addNoteEtxContent.hasFocus()) {
                    defaultTextSizeContent++;
                    addNoteEtxContent.setTextSize(defaultTextSizeContent);
                }

                if (addNoteEtxTitle.hasFocus()) {
                    defaultTextSizeTitle++;
                    addNoteEtxTitle.setTextSize(defaultTextSizeTitle);

                }

                break;
            case R.id.add_note_textMinus:
                if (addNoteEtxContent.hasFocus()) {
                    defaultTextSizeContent--;
                    addNoteEtxContent.setTextSize(defaultTextSizeContent);
                }
                if (addNoteEtxTitle.hasFocus()) {
                    defaultTextSizeTitle--;
                    addNoteEtxTitle.setTextSize(defaultTextSizeTitle);

                }
                break;
        }
    }

    @OnClick(R.id.add_note_default_all)
    public void defaultTextBehaviour() {

        addNoteEtxTitle.setTextSize(16);
        addNoteEtxContent.setTextSize(14);
        addNoteEtxTitle.setTypeface(null, Typeface.NORMAL);
        addNoteEtxContent.setTypeface(null, Typeface.NORMAL);


    }

    @OnClick({R.id.add_note_title_btn, R.id.add_note_toggle})
    public void onViewClicked(View view) {

        switch (view.getId()) {

            case R.id.add_note_title_btn:
                saveToDB();
                ActivityCompat.finishAfterTransition(NoteAddActivity.this);
                finish();

                break;
            case R.id.add_note_toggle:
                if (toggle.isChecked()) {

                    addNoteDateTv.setVisibility(View.VISIBLE);
                    toggle.setBackgroundDrawable(ContextCompat.getDrawable(NoteAddActivity.this, R.drawable.unchecked));

                    Animation slideUp = AnimationUtils.loadAnimation(NoteAddActivity.this, R.anim.slide_up);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        getWindow().setStatusBarColor(ContextCompat.getColor(NoteAddActivity.this, R.color.createStatusBarSecondary));
                        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.createToolBarBarSecondary));

                    }
                    editLayout.startAnimation(slideUp);
                    defaultLayout.setVisibility(View.GONE);
                    editLayout.setVisibility(View.VISIBLE);

                } else {
                    addNoteDateTv.setVisibility(View.GONE);
                    toggle.setBackgroundDrawable(ContextCompat.getDrawable(NoteAddActivity.this, R.drawable.checked));
                    Animation slideUp = AnimationUtils.loadAnimation(NoteAddActivity.this, R.anim.slide_up);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.createStatusBarPrimary));
                        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.createToolBarBarPrimary));
                    }

                    defaultLayout.setAnimation(slideUp);
                    editLayout.setVisibility(View.GONE);
                    defaultLayout.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    //------------------------------------------------------------------------------------------------------------

    private void saveToDB() {

        String title = addNoteEtxTitle.getText().toString().trim();
        String content = addNoteEtxContent.getText().toString().trim();
        String dateText = MyDate.formatDate(System.currentTimeMillis());


        if (title.length() > 0 || content.length() > 0) {
            DBManager dbManager = DBManager.getInstance(NoteAddActivity.this);
            dbManager.openDataBase();

            NoteModel noteModel = new NoteModel();


            noteModel.setTitle(title);
            noteModel.setContent(content);
            noteModel.setDate(dateText);

            boolean saveStatus = dbManager.insertNote(noteModel);

            if (saveStatus == true) {
                MainActivity.notifySnackbarAdd(1);

            } else {
                Toast.makeText(NoteAddActivity.this, "Unable to Save !!", Toast.LENGTH_SHORT).show();
                addNoteEtxTitle.setFocusable(true);
                addNoteEtxTitle.setError("Try Again !");
            }

            dbManager.closeDataBase();

            hideSoftKeyboard();
        } else {
            MainActivity.notifySnackbarAdd(2);
            finish();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause()");

    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    //-------------------------------------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {
        saveToDB();
        ActivityCompat.finishAfterTransition(NoteAddActivity.this);
        finish();


    }
}
