package com.example.dictionaryapp;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Locale;



public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPref;

    VocabDataBase vocabDataBase;
    SQLiteDatabase db;

    TextView textView;
    TextView resusltTxt;
    SearchView searchView;
    LinearLayout resultView;
    String JSONSTR;
    private boolean FLAG = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = getPreferences(Context.MODE_PRIVATE);

        vocabDataBase = new VocabDataBase(this);
        db = vocabDataBase.getWritableDatabase();
        db = vocabDataBase.getReadableDatabase();

        if (savedInstanceState == null && !FLAG){
            Log.d("FLAG", String.valueOf(FLAG));
            FLAG = true;
            GetWordOfTheDayTask getWordOfTheDayTask = new GetWordOfTheDayTask();
            getWordOfTheDayTask.execute();
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("Flag", FLAG);
            editor.commit();
            Log.d("SavedInstance : ", "null");
        }
        else{
            Log.d("SavedInstance : ", "not null");
            String jsonStr = sharedPref.getString("JSONStr",null);
            DisplayWordOfTheDayResult(jsonStr);
        }

        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        resusltTxt = (TextView) findViewById(R.id.meaning);
        resultView = (LinearLayout) findViewById(R.id.resultView);

        // Get the SearchView and set the searchable configuration
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) findViewById(R.id.searchView);

        // Current activity is not the searchable activity
       //  searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchableActivity.class)));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        EditText txtSearch = ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
        txtSearch.setHintTextColor(Color.LTGRAY);
        txtSearch.setHint("Search a word");
        txtSearch.setTextColor(Color.GRAY);

//        try {
//            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
//            mCursorDrawableRes.setAccessible(true);
//            mCursorDrawableRes.set(txtSearch, 0);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public class GetWordOfTheDayTask extends AsyncTask<String, Void, String> {

        private final String LOG_TAG = MainActivity.GetWordOfTheDayTask.class.getSimpleName();

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            String JsonStr = null;

            try {

                final String BASE_URL1 = "http://api.wordnik.com:80/v4/words.json/wordOfTheDay?date=";
                final String BASE_URL2 = "&api_key=54d58ad1400c9bc39d59b4c5dae05829cb6d4086bcfe2c886";
                final String BASE_URL = BASE_URL1 + date + BASE_URL2;

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
            JSONSTR = JsonStr;
            DisplayWordOfTheDayResult(JsonStr);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("JSONStr", JsonStr);
            editor.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.vocab) {
            Intent intent = new Intent(this, VocabActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void DisplayWordOfTheDayResult(String jsonStr){
        if(jsonStr != null){

            final String WORD_MEANING = "text";
            final String PART_OF_SPEECH = "partOfSpeech";
            String resultStr ="";
            JSONObject jsonObject;

            try {
                jsonObject = new JSONObject(jsonStr);
                textView.setText(jsonObject.getString("word"));
                Typeface titleTypeFace = Typeface.createFromAsset(this.getAssets(), "fonts/JosefinSans-SemiBoldItalic.ttf");
                textView.setTypeface(titleTypeFace);
                JSONArray jsonArray = jsonObject.getJSONArray("definitions");
                JSONObject jsonObj = jsonArray.getJSONObject(0);

                TextView textView = new TextView(getApplicationContext());
                textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                textView.setTextColor(ContextCompat.getColor(this,R.color.black));
                textView.setPadding(10,10,10,10);
                textView.setTextSize(20);
                textView.setText("Part Of Speech" + " : " +jsonObj.getString(PART_OF_SPEECH));
                Typeface typeface = Typeface.createFromAsset(this.getAssets(), "fonts/JosefinSans-Regular.ttf");
                textView.setTypeface(typeface);
                resultView.addView(textView);

                resultStr = "Part Of Speech" + " : " +jsonObj.getString(PART_OF_SPEECH) + "\n\n" + "Meaning : \n";

                TextView textView2 = new TextView(getApplicationContext());
                textView2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                textView2.setTextColor(ContextCompat.getColor(this,R.color.black));
                textView2.setPadding(10,10,10,10);
                textView2.setTextSize(20);
                textView2.setText("Meaning :");
                Typeface typeface2 = Typeface.createFromAsset(this.getAssets(), "fonts/JosefinSans-Regular.ttf");
                textView2.setTypeface(typeface2);
                resultView.addView(textView2);

                TextView textView3 = new TextView(getApplicationContext());
                textView3.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                textView3.setTextColor(ContextCompat.getColor(this,R.color.black));
                textView3.setPadding(10,10,10,10);
                textView3.setTextSize(20);
                Typeface typeface3 = Typeface.createFromAsset(this.getAssets(), "fonts/JosefinSans-Regular.ttf");
                textView3.setTypeface(typeface3);
                String meaning = "";
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonOBJECT = jsonArray.getJSONObject(i);
                    int j = i + 1;
                    resultStr = resultStr + "  " + j + ". " +jsonOBJECT.getString(WORD_MEANING) + "\n\n";
                    if (i == jsonArray.length()-1){
                        meaning = meaning + "  " + j + ". " +jsonOBJECT.getString(WORD_MEANING);
                    }
                    else {
                        meaning = meaning + "  " + j + ". " +jsonOBJECT.getString(WORD_MEANING) + "\n\n";
                    }
                }

                textView3.setText(meaning);
                resultView.addView(textView3);

                TextView textView4 = new TextView(getApplicationContext());
                textView4.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                textView4.setTextColor(ContextCompat.getColor(this,R.color.black));
                textView4.setPadding(10,10,10,10);
                textView4.setTextSize(20);
                textView4.setText("Examples");
                Typeface typeface4 = Typeface.createFromAsset(this.getAssets(), "fonts/JosefinSans-Regular.ttf");
                textView4.setTypeface(typeface4);
                resultView.addView(textView4);

                jsonArray = jsonObject.getJSONArray("examples");
                resultStr = resultStr + "Examples" + "\n";

                TextView textView5 = new TextView(getApplicationContext());
                textView5.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                textView5.setTextColor(ContextCompat.getColor(this,R.color.black));
                textView5.setPadding(10,10,10,10);
                textView5.setTextSize(20);
                String examples = "";
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonOBJECT = jsonArray.getJSONObject(i);
                    int j = i+1;
                    resultStr = resultStr + "  "+ j + ". " +jsonOBJECT.getString("text") + "\n\n";
                    examples = examples + "  "+ j + ". " +jsonOBJECT.getString("text") + "\n\n";
                }
                textView5.setText(examples);
                Typeface typeface5 = Typeface.createFromAsset(this.getAssets(), "fonts/JosefinSans-Regular.ttf");
                textView5.setTypeface(typeface5);
                resultView.addView(textView5);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.v("Word meaning: ", resultStr);
            //resusltTxt.setText(resultStr);
        }


    }

}
