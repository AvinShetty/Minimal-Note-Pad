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
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
    FloatingActionButton fabUpdate;

    private Toolbar toolbar;


    private float defaultTextSizeTitle = 16;
    private float defaultTextSizeContent = 14;

    private String rcvTitle;
    private String rcvContent;
    private int recordPos;
    private String rcvDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_update);
        NoteUpdateActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabUpdate.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.update_note_fab_color), PorterDuff.Mode.SRC_IN);
        MainActivity.hideSheetPlease();

        setupToolbar();
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
                Log.e(TAG, "onTOuch");
                NoteUpdateActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                NoteUpdateActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                return false;
            }
        });


    }

    @OnClick(R.id.update_note_fab)
    public void onFabUpdate() {
        Log.e(TAG, "fab update");

        toggle.setVisibility(View.VISIBLE);


        Animation slideRight = AnimationUtils.loadAnimation(NoteUpdateActivity.this, R.anim.slide_right);
        fabUpdate.setAnimation(slideRight);
        fabUpdate.setVisibility(View.GONE);


        updateNoteEtxTitle.setEnabled(true);
        updateNoteEtxContent.setEnabled(true);

        updateNoteEtxTitle.requestFocus();
        editTextTouch();
        editTextTouch2();


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setUpAnimations() {
        Log.w(TAG, "setUpAnimations()");
        Transition transition = new Explode();
        transition.setDuration(800);
        getWindow().setEnterTransition(transition);
    }


    void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.update_note_toolbar);
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
            Log.w(TAG, "note_update_share btn clicked");
            MyMiscellaneousUtil.shareData(
                    updateNoteEtxTitle.getText().toString().trim(),
                    updateNoteEtxContent.getText().toString().trim(),
                    NoteUpdateActivity.this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {

        if (fabUpdate.isShown()) {
            Log.e(TAG, "shown------------------------");
            updateToDB();
            ActivityCompat.finishAfterTransition(NoteUpdateActivity.this);
            NoteUpdateActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            finish();

        } else {

            toggle.setVisibility(View.GONE);

            Log.e(TAG, "hidden------------------------");
            updateNoteEtxTitle.setEnabled(false);
            updateNoteEtxContent.setEnabled(false);

            Animation slideLeft = AnimationUtils.loadAnimation(NoteUpdateActivity.this, R.anim.slide_left);
            fabUpdate.setVisibility(View.VISIBLE);
            fabUpdate.setAnimation(slideLeft);

        }

    }

    private void updateToDB() {
        Log.e(TAG, "updateToDB()");

        DBManager dbManager = DBManager.getInstance(NoteUpdateActivity.this);

        dbManager.openDataBase();

        NoteModel noteModel = new NoteModel();

        String title = updateNoteEtxTitle.getText().toString().trim();
        String content = updateNoteEtxContent.getText().toString().trim();
        String dateText = MyDate.formatDate(System.currentTimeMillis());

        noteModel.setTitle(title);
        noteModel.setContent(content);
        noteModel.setDate(dateText);
        noteModel.setId(recordPos);

        long rowsAffected = dbManager.updateNote(noteModel, recordPos);

        if (rowsAffected > 0) {

            MainActivity.notifySnackbarUpdate(1);

        } else {
            Log.e(TAG, " oops ! Not able to update ");
            Toast.makeText(NoteUpdateActivity.this, "Unable to Update !!", Toast.LENGTH_SHORT).show();
        }

        dbManager.closeDataBase();

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop()");
        NoteUpdateActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

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

            rcvTitle = bundle.getString(MyConstants.WISH_TITLE);
            rcvContent = bundle.getString(MyConstants.WISH_CONTENT);
            recordPos = bundle.getInt(MyConstants.WISH_POSITION);
            rcvDate = bundle.getString(MyConstants.WISH_DATE);

            Log.e(TAG, " getting Bundle + " + bundle.toString());

            updateNoteEtxTitle.setText(rcvTitle);
            updateNoteEtxContent.setText(rcvContent);
            updateNoteDateTv.setText("modified : " + rcvDate);
            updateNoteDateTv2.setText("modified : " + rcvDate);
        }
    }

    @OnClick({R.id.update_note_bold, R.id.update_note_italic})
    public void onViewClickedTextStyleFormat(View view) {
        switch (view.getId()) {
            case R.id.update_note_bold:
                if (updateNoteEtxContent.hasFocus() == true) {
                    updateNoteEtxContent.setTypeface(null, Typeface.BOLD);
                }
                if (updateNoteEtxTitle.hasFocus() == true) {
                    updateNoteEtxTitle.setTypeface(null, Typeface.BOLD);
                }


                break;
            case R.id.update_note_italic:

                if (updateNoteEtxContent.hasFocus() == true) {
                    updateNoteEtxContent.setTypeface(null, Typeface.ITALIC);
                }
                if (updateNoteEtxTitle.hasFocus() == true) {
                    updateNoteEtxTitle.setTypeface(null, Typeface.ITALIC);
                }

                break;

        }
    }

    @OnClick({R.id.update_note_textPlus, R.id.update_note_textMinus})
    public void onViewClickedTextSizeFormat(View view) {
        switch (view.getId()) {
            case R.id.update_note_textPlus:
                if (updateNoteEtxContent.hasFocus() == true) {
                    defaultTextSizeContent++;
                    updateNoteEtxContent.setTextSize(defaultTextSizeContent);
                }

                if (updateNoteEtxTitle.hasFocus() == true) {
                    defaultTextSizeTitle++;
                    updateNoteEtxTitle.setTextSize(defaultTextSizeTitle);

                }

                break;
            case R.id.update_note_textMinus:
                if (updateNoteEtxContent.hasFocus() == true) {
                    defaultTextSizeContent--;
                    updateNoteEtxContent.setTextSize(defaultTextSizeContent);
                }
                if (updateNoteEtxTitle.hasFocus() == true) {
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
//-------------------------------------------------------------------------------------------------------------

    @OnClick({R.id.update_note_title_btn, R.id.update_note_toggle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.update_note_title_btn:
                Log.w(TAG, "note_update_title btn clicked");

                updateToDB();

                ActivityCompat.finishAfterTransition(NoteUpdateActivity.this);
                NoteUpdateActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                finish();

                break;
            case R.id.update_note_toggle:

                if (toggle.isChecked()) {


                    updateNoteDateTv.setVisibility(View.VISIBLE);
                    toggle.setBackgroundDrawable(ContextCompat.getDrawable(NoteUpdateActivity.this, R.drawable.unchecked));

                    Animation slideUp = AnimationUtils.loadAnimation(NoteUpdateActivity.this, R.anim.slide_up);
                    // Animation slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        getWindow().setStatusBarColor(ContextCompat.getColor(NoteUpdateActivity.this, R.color.createStatusBarSecondary));
                        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.createToolBarBarSecondary));

                    }
                    editLayoutUpdate.startAnimation(slideUp);
                    defaultLayoutUpdate.setVisibility(View.GONE);
                    editLayoutUpdate.setVisibility(View.VISIBLE);
                    NoteUpdateActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                    NoteUpdateActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

                } else {
                    updateNoteDateTv.setVisibility(View.GONE);
                    toggle.setBackgroundDrawable(ContextCompat.getDrawable(NoteUpdateActivity.this, R.drawable.checked));
                    Animation slideUp = AnimationUtils.loadAnimation(NoteUpdateActivity.this, R.anim.slide_up);
                    // Animation slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.createStatusBarPrimary));
                        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.createToolBarBarPrimary));
                    }

                    defaultLayoutUpdate.setAnimation(slideUp);
                    editLayoutUpdate.setVisibility(View.GONE);
                    defaultLayoutUpdate.setVisibility(View.VISIBLE);
                    NoteUpdateActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                    NoteUpdateActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                }

                break;
        }
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

}
