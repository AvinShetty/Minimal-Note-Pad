package redfoxclassic.hehe.ui.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
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
import redfoxclassic.hehe.util.MyConstants;
import redfoxclassic.hehe.util.MyDate;
import redfoxclassic.hehe.util.MyMiscellaneousUtil;

public class NoteUpdateActivity extends AppCompatActivity {

    private final static String TAG = NoteUpdateActivity.class.getSimpleName();

    @BindView(R.id.update_note_etxTitle)
    EditText updateNoteEtxTitle;
    @BindView(R.id.update_note_etxContent)
    EditText updateNoteEtxContent;
    @BindView(R.id.update_note_dateTvOne)
    TextView updateNoteDateTv;
    @BindView(R.id.update_note_dateTvTwo)
    TextView updateNoteDateTv2;
    @BindView(R.id.update_note_bold)
    ImageView bold;
    @BindView(R.id.update_note_italic)
    ImageView italic;
    @BindView(R.id.update_note_textPlus)
    ImageView plusBtn;
    @BindView(R.id.update_note_textMinus)
    ImageView minusBtn;
    @BindView(R.id.update_note_edit_layout)
    LinearLayout editLayoutUpdate;
    @BindView(R.id.update_note_default_layout)
    LinearLayout defaultLayoutUpdate;
    @BindView(R.id.update_note_default_all)
    Button defaultAll;
    @BindView(R.id.update_note_title_btn)
    Button titleBtn;
    @BindView(R.id.update_note_toggle)
    ToggleButton toggle;
    @BindView(R.id.update_note_fab)
    FloatingActionButton fabUpdateBtn;

    private Toolbar toolbar;


    private float defaultTextSizeTitle = 16;
    private float defaultTextSizeContent = 14;

    private int recordPos;

    private int fabValue = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_update);
        NoteUpdateActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setupToolbar();
        ButterKnife.bind(this);

        fabUpdateBtn.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.update_note_fab_color), PorterDuff.Mode.SRC_IN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setUpAnimations();
        }

        Intent intent = getIntent();
        rcvData(intent);

        updateNoteEtxTitle.setEnabled(false);
        updateNoteEtxContent.setEnabled(false);


        toggle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                NoteUpdateActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                NoteUpdateActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                return false;
            }
        });


    }


    @OnClick(R.id.update_note_fab)
    public void onFabUpdate() {
        toggle.setVisibility(View.VISIBLE);


        Animation slideRight = AnimationUtils.loadAnimation(NoteUpdateActivity.this, R.anim.slide_right);
        fabUpdateBtn.setAnimation(slideRight);
        fabUpdateBtn.setVisibility(View.GONE);


        updateNoteEtxTitle.setEnabled(true);
        updateNoteEtxContent.setEnabled(true);

        fabValue = 1;
        updateNoteEtxTitle.requestFocus();
      /*  editTextTouch();
        editTextTouch2();*/


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setUpAnimations() {
        Explode explode = new Explode();
        explode.setDuration(800);
        getWindow().setEnterTransition(explode);

    }


    void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.update_note_toolbar);
        setSupportActionBar(toolbar);

        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.update_note_toolbar_color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.update_note_statusbar));
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
                    updateNoteEtxTitle.getText().toString().trim(),
                    updateNoteEtxContent.getText().toString().trim(),
                    NoteUpdateActivity.this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void updateToDB() {
        String title = updateNoteEtxTitle.getText().toString().trim();
        String content = updateNoteEtxContent.getText().toString().trim();
        String dateText = MyDate.formatDate(System.currentTimeMillis());

        System.out.println(title);
        System.out.println(content);
        DBManager dbManager = DBManager.getInstance(NoteUpdateActivity.this);

        dbManager.openDataBase();

        NoteModel noteModel = new NoteModel();


        noteModel.setTitle(title);
        noteModel.setContent(content);
        noteModel.setDate(dateText);
        noteModel.setId(recordPos);


        long rowsAffected = dbManager.updateNote(noteModel, recordPos);

        if (rowsAffected > 0) {

            MainActivity.notifySnackbarUpdate(1);


        } else {
            Toast.makeText(NoteUpdateActivity.this, "Unable to Update !!", Toast.LENGTH_SHORT).show();
        }

        dbManager.closeDataBase();

        hideSoftKeyboard();


    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop()");
        hideSoftKeyboard();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause()");
    }

    private void rcvData(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {

            String rcvTitle = bundle.getString(MyConstants.WISH_TITLE);
            String rcvContent = bundle.getString(MyConstants.WISH_CONTENT);
            recordPos = bundle.getInt(MyConstants.WISH_POSITION);
            String rcvDate = bundle.getString(MyConstants.WISH_DATE);

            updateNoteEtxTitle.setText(rcvTitle);
            updateNoteEtxContent.setText(rcvContent);
            updateNoteDateTv.setText("modified : ".concat(rcvDate));
            updateNoteDateTv2.setText("modified : ".concat(rcvDate));
        }
    }


    @OnClick({R.id.update_note_bold, R.id.update_note_italic})
    public void onViewClickedTextStyleFormat(View view) {
        switch (view.getId()) {
            case R.id.update_note_bold:
                if (updateNoteEtxContent.hasFocus()) {
                    updateNoteEtxContent.setTypeface(null, Typeface.BOLD);
                }
                if (updateNoteEtxTitle.hasFocus()) {
                    updateNoteEtxTitle.setTypeface(null, Typeface.BOLD);
                }


                break;
            case R.id.update_note_italic:

                if (updateNoteEtxContent.hasFocus()) {
                    updateNoteEtxContent.setTypeface(null, Typeface.ITALIC);
                }
                if (updateNoteEtxTitle.hasFocus()) {
                    updateNoteEtxTitle.setTypeface(null, Typeface.ITALIC);
                }

                break;

        }
    }

    @OnClick({R.id.update_note_textPlus, R.id.update_note_textMinus})
    public void onViewClickedTextSizeFormat(View view) {
        switch (view.getId()) {
            case R.id.update_note_textPlus:
                if (updateNoteEtxContent.hasFocus()) {
                    defaultTextSizeContent++;
                    updateNoteEtxContent.setTextSize(defaultTextSizeContent);
                }

                if (updateNoteEtxTitle.hasFocus()) {
                    defaultTextSizeTitle++;
                    updateNoteEtxTitle.setTextSize(defaultTextSizeTitle);

                }

                break;
            case R.id.update_note_textMinus:
                if (updateNoteEtxContent.hasFocus()) {
                    defaultTextSizeContent--;
                    updateNoteEtxContent.setTextSize(defaultTextSizeContent);
                }
                if (updateNoteEtxTitle.hasFocus()) {
                    defaultTextSizeTitle--;
                    updateNoteEtxTitle.setTextSize(defaultTextSizeTitle);

                }
                break;
        }
    }

    @OnClick(R.id.update_note_default_all)
    public void defaultTextBehaviour() {

        updateNoteEtxTitle.setTextSize(16);
        updateNoteEtxContent.setTextSize(14);
        updateNoteEtxTitle.setTypeface(null, Typeface.NORMAL);
        updateNoteEtxContent.setTypeface(null, Typeface.NORMAL);


    }

    @OnClick({R.id.update_note_title_btn, R.id.update_note_toggle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.update_note_title_btn:

                updateToDB();

                ActivityCompat.finishAfterTransition(NoteUpdateActivity.this);
                finish();

                break;
            case R.id.update_note_toggle:

                if (toggle.isChecked()) {


                    updateNoteDateTv.setVisibility(View.VISIBLE);
                    toggle.setBackgroundDrawable(ContextCompat.getDrawable(NoteUpdateActivity.this, R.drawable.unchecked));

                    Animation slideUp = AnimationUtils.loadAnimation(NoteUpdateActivity.this, R.anim.slide_up);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        getWindow().setStatusBarColor(ContextCompat.getColor(NoteUpdateActivity.this, R.color.createStatusBarSecondary));
                        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.createToolBarBarSecondary));

                    }
                    editLayoutUpdate.startAnimation(slideUp);
                    NoteUpdateActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    defaultLayoutUpdate.setVisibility(View.GONE);
                    editLayoutUpdate.setVisibility(View.VISIBLE);


                    NoteUpdateActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                    NoteUpdateActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


                } else {
                    updateNoteDateTv.setVisibility(View.GONE);
                    toggle.setBackgroundDrawable(ContextCompat.getDrawable(NoteUpdateActivity.this, R.drawable.checked));
                    Animation slideUp = AnimationUtils.loadAnimation(NoteUpdateActivity.this, R.anim.slide_up);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.update_note_statusbar));
                        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.update_note_toolbar_color));
                    }

                    defaultLayoutUpdate.setAnimation(slideUp);
                    showSoftKeyboard(toggle);
                    NoteUpdateActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    editLayoutUpdate.setVisibility(View.GONE);
                    defaultLayoutUpdate.setVisibility(View.VISIBLE);


                    /*NoteUpdateActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                    NoteUpdateActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);*/
                }

                break;
        }
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    private void editTextTouch() {
        updateNoteEtxTitle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                NoteUpdateActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                NoteUpdateActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                return false;
            }
        });
    }

    private void editTextTouch2() {
        updateNoteEtxContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                NoteUpdateActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                NoteUpdateActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                return false;
            }
        });
    }

    @OnClick({R.id.update_note_etxTitle, R.id.update_note_etxContent})
    public void onViewClicked2(View view) {
        switch (view.getId()) {
            case R.id.update_note_etxTitle:
                NoteUpdateActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                NoteUpdateActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                break;
            case R.id.update_note_etxContent:
                NoteUpdateActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                NoteUpdateActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                break;
        }
    }

    //-------------------------------------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {

        if (fabValue == 1) {
            toggle.setVisibility(View.GONE);

            updateNoteEtxTitle.setEnabled(false);
            updateNoteEtxContent.setEnabled(false);

            Animation slideLeft = AnimationUtils.loadAnimation(NoteUpdateActivity.this, R.anim.slide_left);
            fabUpdateBtn.setVisibility(View.VISIBLE);
            fabUpdateBtn.setAnimation(slideLeft);

            updateToDB();
            fabValue = 0;


        } else {
            ActivityCompat.finishAfterTransition(NoteUpdateActivity.this);
            super.onBackPressed();

        }
    }


}
