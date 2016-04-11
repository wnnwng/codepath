package com.codepath.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class EditItemActivity extends AppCompatActivity {
    EditText textArea;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        String todoText = getIntent().getStringExtra("todoText");
        position = getIntent().getIntExtra("todoPos", 0);
        textArea = (EditText) findViewById(R.id.editText);
        textArea.setText(todoText);
        textArea.setSelection(textArea.getText().length());
        setupSaveListener();
    }

    private void setupSaveListener() {
        Button saveBtn = (Button) findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("todoText", textArea.getText().toString());
                i.putExtra("todoPos", position);
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }
}
