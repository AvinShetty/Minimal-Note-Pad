package redfoxclassic.hehe.ui.activity;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import redfoxclassic.hehe.util.MyMiscellaneousUtil;
import redfoxclassic.hehe.util.MyRecyclerTouchListener;

public class NoteFavoriteActivity extends AppCompatActivity {

    @BindView(R.id.fav_main_rv)
    RecyclerView recyclerView;
    @BindView(R.id.activity_note_imvEmpty)
    ImageView activityNoteImvEmpty;
    @BindView(R.id.fab_note_fab)
    FloatingActionButton fabNoteFab;
    private Toolbar toolbar;

    private NoteAdapter noteAdapter;
    private StaggeredGridLayoutManager layoutManager;
    private List<NoteModel> noteModelList = new ArrayList<>();

    private final static String TAG = NoteFavoriteActivity.class.getSimpleName();
    private int adapterClickedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_favorite);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.fav_note_toolbar);
        setSupportActionBar(toolbar);

        NoteFavoriteActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setUpAnimations();
        }

        noteAdapter = new NoteAdapter(noteModelList, NoteFavoriteActivity.this);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(noteAdapter);

        touchListenerRecycler();
        loadDataAll();


    }

    @OnClick(R.id.fab_note_fab)
    public void onViewClicked() {
    }

    public void loadDataAll() {
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        moreGenericLoadData();

    }


    private void touchListenerRecycler() {

        recyclerView.addOnItemTouchListener(new MyRecyclerTouchListener(NoteFavoriteActivity.this, recyclerView,
                new MyRecyclerTouchListener.ClickListenerTouch() {
                    @Override
                    public void onClickTouch(View view, int position) {
                        adapterClickedPosition = position;

                    }

                    @Override
                    public void onLongClickTouch(View view, int position) {

                        registerForContextMenu(view);
                        adapterClickedPosition = position;


                    }
                }));
    }

    private void moreGenericLoadData() {
        noteModelList.clear();

        DBManagerFav dbManagerFav = DBManagerFav.getInstance(NoteFavoriteActivity.this);
        dbManagerFav.openDataBase();
        noteModelList = dbManagerFav.getAllNoteList();

        if (noteModelList.size() == 0) {

            recyclerView.setVisibility(View.GONE);
            activityNoteImvEmpty.setVisibility(View.VISIBLE);

        } else {
            recyclerView.setVisibility(View.VISIBLE);
            activityNoteImvEmpty.setVisibility(View.GONE);
            noteAdapter = new NoteAdapter(noteModelList, NoteFavoriteActivity.this);
            recyclerView.setAdapter(noteAdapter);
            noteAdapter.notifyDataSetChanged();
        }

        dbManagerFav.closeDataBase();

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.context_menu2, menu);


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.context_delete2:
                deleteDialogOperation();

                break;
            case R.id.context_share2:
                MyMiscellaneousUtil.shareDataOnClick(adapterClickedPosition, noteModelList, NoteFavoriteActivity.this);
                break;
            default:

        }

        return true;
    }

    private void deleteDialogOperation() {

        new AlertDialog.Builder(NoteFavoriteActivity.this).setCancelable(false)
                .setMessage("Delete Selected Note ?").
                setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        deleteOperation();
                        dialogInterface.dismiss();

                    }
                }).setNeutralButton("DISMISS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

            }
        }).show();
    }

    private void deleteOperation() {

        //have the adapter position
        DBManagerFav dbManagerFav = DBManagerFav.getInstance(NoteFavoriteActivity.this);
        dbManagerFav.openDataBase();


        NoteModel noteModel = new NoteModel();
        noteModel.setId(noteModelList.get(adapterClickedPosition).getId());


        int status = dbManagerFav.deleteNote(noteModelList.get(adapterClickedPosition).getId());
        if (status > 0) {
            noteAdapter.removeAt(adapterClickedPosition);


        }
        dbManagerFav.closeDataBase();
        //loadDataAll(); //solved the issues about stagger render problems
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadDataAll();
        Log.d(TAG, "onResume()");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
        MainActivity.notifyFavourite(1);
        hideSoftKeyboard();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setUpAnimations() {
        Slide slide = new Slide();
        slide.setDuration(800);
        slide.setSlideEdge(Gravity.END);
        getWindow().setEnterTransition(slide);
    }
}
