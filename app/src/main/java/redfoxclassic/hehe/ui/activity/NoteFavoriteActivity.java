package redfoxclassic.hehe.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import redfoxclassic.hehe.R;
import redfoxclassic.hehe.data.favdb.DBManagerFav;
import redfoxclassic.hehe.model.NoteModel;
import redfoxclassic.hehe.ui.adapters.NoteAdapter;

public class NoteFavoriteActivity extends AppCompatActivity {

    @BindView(R.id.fav_main_rv)
    RecyclerView recyclerView;
    @BindView(R.id.activity_note_imvEmpty)
    ImageView activityNoteImvEmpty;
    @BindView(R.id.fab_note_fab)
    FloatingActionButton fabNoteFab;
    private Toolbar toolbar;

    private NoteAdapter noteAdapter;
    private LinearLayoutManager layoutManager;
    private List<NoteModel> noteModelList = new ArrayList<>();

    private final static String TAG = NoteFavoriteActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_favorite);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.fav_note_toolbar);
        setSupportActionBar(toolbar);

        NoteFavoriteActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupWindowAnimations();
        }

        noteAdapter = new NoteAdapter(noteModelList, NoteFavoriteActivity.this);
        layoutManager = new LinearLayoutManager(NoteFavoriteActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(noteAdapter);

        loadDataAll();


    }

    @OnClick(R.id.fab_note_fab)
    public void onViewClicked() {
    }

    public void loadDataAll() {
        Log.i(TAG, "loadDataAll()");
        moreGenericLoadData();

    }

    private void moreGenericLoadData() {
        noteModelList.clear();

        DBManagerFav dbManagerFav = DBManagerFav.getInstance(NoteFavoriteActivity.this);
        dbManagerFav.openDataBase();
        noteModelList = dbManagerFav.getAllNoteList();

        if (noteModelList.size() == 0) {

            recyclerView.setVisibility(View.GONE);
            activityNoteImvEmpty.setVisibility(View.VISIBLE);
            Log.i(TAG, " size : " + noteModelList.size());

        } else {
            recyclerView.setVisibility(View.VISIBLE);
            activityNoteImvEmpty.setVisibility(View.GONE);
            Log.i(TAG, " size : " + noteModelList.size());
            noteAdapter = new NoteAdapter(noteModelList, NoteFavoriteActivity.this);
            recyclerView.setAdapter(noteAdapter);
            noteAdapter.notifyDataSetChanged();
        }

        dbManagerFav.closeDataBase();

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataAll();
        NoteFavoriteActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    @Override
    protected void onStop() {
        super.onStop();
        MainActivity.hideSheetPlease();
        NoteFavoriteActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        // Log.e(TAG, "setupWindowAnimations()");
        Slide slideTransition = new Slide();
        slideTransition.setSlideEdge(Gravity.START);
        slideTransition.setDuration(800);
        getWindow().setReenterTransition(slideTransition);
        getWindow().setExitTransition(slideTransition);
    }

}
