package com.example.dictionaryapp;

import android.app.SearchManager;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.dictionaryapp.VocabDBContract.VocabDbEntry;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class SearchableActivity extends AppCompatActivity {

    VocabDataBase vocabDataBase;
    SQLiteDatabase db;

    TextView wordTxt, meaningTxt;
    String word;
    boolean STAR_CHECKED = false;

    private SlidingUpPanelLayout mLayout;
    private static final String TAG = "MainAcitvity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);
        wordTxt = (TextView) findViewById(R.id.word);
        meaningTxt = (TextView) findViewById(R.id.meaning);

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

        Typeface titleTypeFace = Typeface.createFromAsset(this.getAssets(), "fonts/JosefinSans-SemiBoldItalic.ttf");
        wordTxt.setTypeface(titleTypeFace);
        Typeface titleTypeFace2 = Typeface.createFromAsset(this.getAssets(), "fonts/JosefinSans-Regular.ttf");
        meaningTxt.setTypeface(titleTypeFace2);
        meaningTxt.setTextColor(ContextCompat.getColor(this,R.color.black));

        vocabDataBase = new VocabDataBase(this);
        db = vocabDataBase.getWritableDatabase();

        handleIntent(getIntent());
        panelListener();


    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            word = query;
            wordTxt.setText(word);
            STAR_CHECKED = checkWordExists(word);
            GetWordMeaningTask getWordMeaningTask = new GetWordMeaningTask();
            getWordMeaningTask.execute(word.trim().toLowerCase());
        }
    }

    public class GetWordMeaningTask extends AsyncTask<String, Void, String> {

        private final String LOG_TAG = SearchableActivity.GetWordMeaningTask.class.getSimpleName();

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String JsonStr = null;

            try {

                final String BASE_URL1 = "http://api.wordnik.com:80/v4/word.json/";
                final String BASE_URL2 = "/definitions?limit=200&includeRelated=true&useCanonical=false&includeTags=false&api_key=54d58ad1400c9bc39d59b4c5dae05829cb6d4086bcfe2c886";
                final String BASE_URL = BASE_URL1 + params[0] + BASE_URL2;

                URL url = new URL(BASE_URL);

                Log.v(LOG_TAG, "URi"+ BASE_URL);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                JsonStr = buffer.toString();

                Log.v(LOG_TAG,"JSON String: "+ JsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            return JsonStr;
        }

        @Override
        protected void onPostExecute(String JsonStr) {
            if(JsonStr != null){

                final String WORD_MEANING = "text";
                final String PART_OF_SPEECH = "partOfSpeech";
                String resultStr = "No word found.";
                JSONArray jsonArray;
                try {
                    jsonArray = new JSONArray(JsonStr);
                    if(jsonArray.length() <= 0) {
                        meaningTxt.setText(resultStr);
                        return;
                    }
                    JSONObject jsonObj = jsonArray.getJSONObject(0);
                    resultStr = "Part Of Speech" + " : " +jsonObj.getString(PART_OF_SPEECH) + "\n\n" + "Meaning : \n";
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int j = i + 1;
                        resultStr = resultStr + "  " + j + ". " + jsonObject.getString(WORD_MEANING) + "\n\n";
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.v(LOG_TAG, "Word meaning: " + resultStr);
                meaningTxt.setText(resultStr);
            }

        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.star_menu, menu);
        if (STAR_CHECKED){
            menu.findItem(R.id.star).setIcon(R.drawable.ic_action_action_yellow);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.star:
                if (STAR_CHECKED == false){
                    STAR_CHECKED = true;
                    item.setIcon(R.drawable.ic_action_action_yellow);
                    ContentValues values = new ContentValues();
                    values.put(VocabDbEntry.COLUMN_WORD, wordTxt.getText().toString());
                    values.put(VocabDbEntry.COLUMN_DETAILS, meaningTxt.getText().toString());

                    db.insert(VocabDbEntry.TABLE_NAME, null, values);
                    Toast.makeText(this,"Added word to Vocab", Toast.LENGTH_LONG).show();

                }
                else {
                    STAR_CHECKED = false;
                    item.setIcon(R.drawable.ic_action_action_grade);
                    db = vocabDataBase.getReadableDatabase();

                    String selection = VocabDbEntry.COLUMN_WORD + " LIKE ?";
                    String[] selectionArgs = { word };

                    db.delete(VocabDbEntry.TABLE_NAME, selection, selectionArgs);

                    Toast.makeText(this,"Word removed from Vocab", Toast.LENGTH_LONG).show();

                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    public boolean checkWordExists(String word)
    {
        db = vocabDataBase.getReadableDatabase();
        Cursor cursor = db.query(VocabDbEntry.TABLE_NAME,
                new String[] { VocabDbEntry.COLUMN_WORD},
                VocabDbEntry.COLUMN_WORD + " = ?" ,
                new String[] {word},
                null, null, null, null);
        Log.d("Word check","done");
        if(cursor.moveToFirst())

            return true; //row exists
        else
            return false;

    }
}
