package com.example.dictionaryapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;
import android.content.DialogInterface;
import java.util.ArrayList;
import java.util.List;

public class VocabActivity extends AppCompatActivity {

    VocabDataBase vocabDataBase;
    SQLiteDatabase db;
    ListView listView;
    ArrayList<Word> wordsList;
    ListViewAdapter listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocab);

        vocabDataBase = new VocabDataBase(this);
        db = vocabDataBase.getReadableDatabase();

        listView = (ListView) findViewById(R.id.listView);
        wordsList = new ArrayList<>();

        Cursor cursor = db.query(
                VocabDBContract.VocabDbEntry.TABLE_NAME,                     // The table to query
                null,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            Word word = new Word();
            word.setWord(cursor.getString(1));
            word.setMeaning(cursor.getString(2));
            wordsList.add(word);
            cursor.moveToNext();
        }
        cursor.close();

        listViewAdapter = new ListViewAdapter(this,wordsList);
        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),WordDisplay.class);
                Word word = wordsList.get(position);
                intent.putExtra("word", word.getWord());
                startActivity(intent);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(VocabActivity.this);
                builder.setTitle("DELETE");
                builder.setCancelable(true);
                builder.setMessage("Do you want to delete word "+wordsList.get(i).getWord()+ " from vocabulary.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String selection = VocabDBContract.VocabDbEntry.COLUMN_WORD + " LIKE ?";
                                String[] selectionArgs = { wordsList.get(i).getWord() };

                                db.delete(VocabDBContract.VocabDbEntry.TABLE_NAME, selection, selectionArgs);

                                wordsList.remove(i);
                                listViewAdapter = new ListViewAdapter(VocabActivity.this,wordsList);
                                listView.setAdapter(listViewAdapter);
                                Toast.makeText(getApplicationContext(),"Word removed from Vocab", Toast.LENGTH_LONG).show();

                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                builder.show();
                return false;
            }
        });
    }
}
