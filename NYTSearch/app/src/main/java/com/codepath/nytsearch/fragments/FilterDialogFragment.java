package com.codepath.nytsearch.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.codepath.nytsearch.R;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;
import com.codetroopers.betterpickers.datepicker.DatePickerBuilder;
import com.codetroopers.betterpickers.datepicker.DatePickerDialogFragment;

import org.joda.time.format.DateTimeFormatter;
import org.parceler.Parcel;
import org.joda.time.DateTime;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

public class FilterDialogFragment extends DialogFragment
        implements DatePickerDialogFragment.DatePickerDialogHandler {
    TextView tvDate;
    Spinner spinnerSort;
    CheckBox checkArt;
    CheckBox checkStyle;
    CheckBox checkSports;
    ArrayAdapter<CharSequence> spinnderAdapter;
    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_begin";

    public FilterDialogFragment() { }

    public static FilterDialogFragment newInstance(String title) {
        FilterDialogFragment frag = new FilterDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpCategoryListener(view);
        setUpSortListener(view);
        setUpDateListener(view);
        view.findViewById(R.id.btnDone).setOnClickListener(v -> {
            dismiss();
        });
    }


    @Override
    public void onDialogDateSet(int reference, int year, int monthOfYear, int dayOfMonth) {
       // tvDate.setText(getString(R.string.date_picker_result_value, year, monthOfYear, dayOfMonth));
    }

    CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton view, boolean checked) {
            switch(view.getId()) {
                case R.id.checkbox_arts:
                    if (checked) {
                        Filter.addCategory(Categories.ARTS);
                    } else {
                        Filter.removeCategory(Categories.ARTS);
                    }
                    break;
                case R.id.checkbox_style:
                    if (checked) {
                        Filter.addCategory(Categories.STYLE);
                    } else {
                        Filter.removeCategory(Categories.STYLE);
                    }
                    break;
                case R.id.checkbox_sports:
                    if (checked) {
                        Filter.addCategory(Categories.SPORTS);
                    } else {
                        Filter.removeCategory(Categories.SPORTS);
                    }
                    break;
            }
        }
    };

    private void setUpCategoryListener(View view) {
        checkArt = (CheckBox) view.findViewById(R.id.checkbox_arts);
        checkStyle = (CheckBox) view.findViewById(R.id.checkbox_style);
        checkSports = (CheckBox) view.findViewById(R.id.checkbox_sports);
        checkArt.setOnCheckedChangeListener(checkListener);
        checkStyle.setOnCheckedChangeListener(checkListener);
        checkSports.setOnCheckedChangeListener(checkListener);
        if (Filter.categories.contains(Categories.ARTS)) checkArt.setChecked(true);
        if (Filter.categories.contains(Categories.STYLE)) checkStyle.setChecked(true);
        if (Filter.categories.contains(Categories.SPORTS)) checkSports.setChecked(true);
    }

    private void setUpSortListener(View view) {
        // Set up adapter
        spinnerSort = (Spinner) view.findViewById(R.id.spinSort);
        spinnderAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.sorts_array, android.R.layout.simple_spinner_item);
        spinnderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(spinnderAdapter);

        // Set default selection
        int newestPos = spinnderAdapter.getPosition(SortOrder.NEWEST.toString());
        int oldestPos = spinnderAdapter.getPosition(SortOrder.OLDEST.toString());
        if (Filter.sortOrder == SortOrder.NEWEST) {
            spinnerSort.setSelection(newestPos);
        } else {
            spinnerSort.setSelection(oldestPos);
        }

        // Set listener
        spinnerSort.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selection = adapterView.getItemAtPosition(i).toString();
                if (selection.equals(SortOrder.NEWEST.toString())) {
                    Filter.sortOrder = SortOrder.NEWEST;
                } else {
                    Filter.sortOrder = SortOrder.OLDEST;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setUpDateListener(View view) {
        tvDate = (TextView) view.findViewById(R.id.start_time_date);
        DateTime selectedDate = Filter.beginTime;
        if (selectedDate != null) {
            tvDate.setText(selectedDate.getYear() + " " + selectedDate.getMonthOfYear() + " " +
                    selectedDate.getDayOfMonth());
        }
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTime now = DateTime.now();
                MonthAdapter.CalendarDay maxDate = new MonthAdapter.CalendarDay(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth());

                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                    .setOnDateSetListener((dialog, year, monthOfYear, dayOfMonth) -> {
                        Filter.beginTime = new DateTime(year, (monthOfYear + 1), dayOfMonth, 0, 0);
                        tvDate.setText(year + " " + (monthOfYear + 1) + " " + dayOfMonth);
                    })
                    .setFirstDayOfWeek(Calendar.SUNDAY)
                    .setDateRange(null, maxDate)
                    .setDoneText("Select")
                    .setCancelText("Cancel");
                cdp.show(getChildFragmentManager(), FRAG_TAG_DATE_PICKER);
            }
        });
    }

    @Parcel
    public static class Filter {
        public static DateTime beginTime;
        public static Set<Categories> categories = new HashSet<>();
        public static SortOrder sortOrder;

        public static void addCategory(Categories c ) {
            categories.add(c);
        }

        public static void removeCategory(Categories c ) {
            categories.remove(c);
        }
    }

    public enum Categories {
        STYLE {
            public String toString() {
                return "Style";
            }
        },
        ARTS {
            public String toString() {
                return "Arts";
            }
        },
        SPORTS {
            public String toString() {
                return "Sports";
            }
        },
    }

    public enum SortOrder {
        NEWEST {
            public String toString() {
                return "Newest";
            }
        },
        OLDEST {
            public String toString() {
                return "Oldest";
            }
        },
    }
}
