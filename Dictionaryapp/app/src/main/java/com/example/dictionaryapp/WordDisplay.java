package com.example.dictionaryapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class WordDisplay extends AppCompatActivity {

    TextView wordTxt, meaningTxt;
    private SlidingUpPanelLayout mLayout;

    String word;
    VocabDataBase vocabDataBase;
    SQLiteDatabase db;

    private static final String TAG = "WordDisplay";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_display);

        wordTxt = (TextView) findViewById(R.id.word);
        meaningTxt = (TextView) findViewById(R.id.meaning);

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

        Typeface titleTypeFace = Typeface.createFromAsset(this.getAssets(), "fonts/JosefinSans-SemiBoldItalic.ttf");
        wordTxt.setTypeface(titleTypeFace);
        Typeface titleTypeFace2 = Typeface.createFromAsset(this.getAssets(), "fonts/JosefinSans-Regular.ttf");
        meaningTxt.setTypeface(titleTypeFace2);
        meaningTxt.setTextColor(ContextCompat.getColor(this,R.color.black));
        Intent intent = getIntent();
        word = intent.getStringExtra("word");
        wordTxt.setText(word);

        panelListener();

        vocabDataBase = new VocabDataBase(this);
        db = vocabDataBase.getReadableDatabase();
        Cursor cursor = db.query(VocabDBContract.VocabDbEntry.TABLE_NAME,
                null,
                VocabDBContract.VocabDbEntry.COLUMN_WORD + " = ?" ,
                new String[] {word},
                null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
            meaningTxt.setText(cursor.getString(2));
        }
        cursor.close();
    }

    public void panelListener(){

        mLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {

            // During the transition of expand and collapse onPanelSlide function will be called.
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.e(TAG, "onPanelSlide, offset " + slideOffset);
            }

            // This method will be call after slide up layout
            @Override
            public void onPanelExpanded(View panel) {
                Log.e(TAG, "onPanelExpanded");

            }

            // This method will be call after slide down layout.
            @Override
            public void onPanelCollapsed(View panel) {
                Log.e(TAG, "onPanelCollapsed");

            }

            @Override
            public void onPanelAnchored(View panel) {
                Log.e(TAG, "onPanelAnchored");
            }

            @Override
            public void onPanelHidden(View panel) {
                Log.e(TAG, "onPanelHidden");
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

}
