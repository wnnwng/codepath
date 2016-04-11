package com.codepath.simpletodo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.support.v4.app.DialogFragment;
import android.widget.TextView;

/**
 * Created by winnie_wang on 7/13/16.
 */
public class EditItemDialogFragment extends DialogFragment implements TextView.OnEditorActionListener {

    private EditText mEditText;
    private int position;
    private String originalText;

    // 1. Defines the listener interface with a method passing back data result.
    public interface EditItemDialogListener {
        void onFinishEditDialog(String inputText, int position);
    }

    public static EditItemDialogFragment newInstance(String title, int newPosition, String newOriginalText) {
        EditItemDialogFragment frag = new EditItemDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("originalText", newOriginalText);
        args.putInt("position", newPosition);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_item, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        mEditText = (EditText) view.findViewById(R.id.editText);
        mEditText.setOnEditorActionListener(this);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Edit your item:");
        originalText = getArguments().getString("originalText");
        position = getArguments().getInt("position");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        mEditText.setText(originalText);
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    // Fires whenever the textfield has an action performed
    // In this case, when the "Done" button is pressed
    // REQUIRES a 'soft keyboard' (virtual keyboard)
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            // Return input text back to activity through the implemented listener
            EditItemDialogListener listener = (EditItemDialogListener) getActivity();
            listener.onFinishEditDialog(mEditText.getText().toString(), position);
            // Close the dialog and return back to the parent activity
            dismiss();
            return true;
        }
        return false;
    }
}
