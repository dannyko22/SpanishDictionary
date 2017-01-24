package com.spanishdictionary.app;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class DictionaryItemActivity extends ActionBarActivity {

    String spanishChars;
    String englishChars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dictionary_item_activity);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Set the strings in the class declaration.
        Intent intent = getIntent();


        spanishChars = intent.getStringExtra("spanishWords");
        spanishChars = "•" + spanishChars;
        spanishChars = spanishChars.replaceAll(", ","\n•");
        TextView textViewSpanishChars = (TextView) findViewById(R.id.spanishTextView);
        Button spanishButton = (Button) findViewById(R.id.spanishButton);

        textViewSpanishChars.setText(spanishChars);

        englishChars = intent.getStringExtra("englishWords");
        englishChars = "•" + englishChars;
        TextView textViewEnglishChars = (TextView) findViewById(R.id.englishTextView);
        textViewEnglishChars.setText(englishChars);
    }

    public void copyEnglish(View v)
    {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("english", englishChars);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(), "English copied to Clipboard",
                Toast.LENGTH_LONG).show();
    }

    public void copySpanish(View v)
    {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("spanish", spanishChars);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(), "Spanish copied to Clipboard",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.dictionary_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
