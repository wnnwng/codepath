package com.codepath.simpletodo;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by winnie_wang on 7/12/16.
 */
@Table(name = "TodoItem")
public class TodoItem extends Model {
    @Column(name = "Text")
    public String text;

    public TodoItem() {
        super();
    }

    public TodoItem(String text) {
        super();
        this.text = text;
        this.save();
    }

    public static List<TodoItem> getAll() {
        return new Select()
                .from(TodoItem.class)
                .execute();
    }
}
