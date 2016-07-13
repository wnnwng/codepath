package com.codepath.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<String> items;
    List<TodoItem> itemsSql;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<>();
        itemsSql = new ArrayList<>();
        readItems2();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
        setupEditItemListener();
        ActiveAndroid.initialize(this);
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        if (itemText.length() > 0) {
            addItem(itemText);
            etNewItem.setText("");
        }
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteItem(position);
                itemsAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private void setupEditItemListener() {
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                i.putExtra("todoText", items.get(position));
                i.putExtra("todoPos", position);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
    }
//
//    private void readItems() {
//        File filesDir = getFilesDir();
//        File todoFile = new File(filesDir, "todo.txt");
//        try {
//            items = new ArrayList<String>(FileUtils.readLines(todoFile));
//        } catch (IOException e) {
//            items = new ArrayList<String>();
//        }
//    }

    // Using SQL
    private void readItems2() {
        itemsSql = TodoItem.getAll();
        for (TodoItem item: itemsSql) {
            items.add(item.text);
        }
    }
//
//    private void writeItems() {
//        File filesDir = getFilesDir();
//        File todoFile = new File(filesDir, "todo.txt");
//        try {
//            FileUtils.writeLines(todoFile, items);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private void addItem(String text) {
        itemsAdapter.add(text);
        itemsSql.add(new TodoItem(text));
    }


    private void deleteItem(int pos) {
        items.remove(pos);
        TodoItem itemSql = itemsSql.remove(pos);
        itemSql.delete();
    }

    private void updateItem(String text, int pos) {
        TodoItem itemSql = itemsSql.get(pos);
        items.set(pos, text);
        itemSql.text = text;
        itemSql.save();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String todoText = data.getExtras().getString("todoText").toString();
            int position = data.getExtras().getInt("todoPos");
            updateItem(todoText, position);
            itemsAdapter.notifyDataSetChanged();
        }
    }
}
