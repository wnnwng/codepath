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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.Unbinder;

public class FilterDialogFragment extends DialogFragment {
    @BindView(R.id.start_time_date) TextView tvDate;
    @BindView(R.id.spinSort) Spinner spinnerSort;
    @BindView(R.id.checkbox_arts) CheckBox checkArt;
    @BindView(R.id.checkbox_style) CheckBox checkStyle;
    @BindView(R.id.checkbox_sports) CheckBox checkSports;
    private ArrayAdapter<CharSequence> spinnerAdapter;
    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_begin";
    private Unbinder unbinder;

    public FilterDialogFragment() { }

    public static FilterDialogFragment newInstance(String title) {
        FilterDialogFragment frag = new FilterDialogFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpCategories(view);
        setUpSorts(view);
        setUpDates(view);
    }

    @OnClick (R.id.btnDone)
    public void doneWithFilters(View v) {
        dismiss();
    }

    private void setUpCategories(View view) {
        if (Filter.categories.contains(Categories.ARTS)) checkArt.setChecked(true);
        if (Filter.categories.contains(Categories.STYLE)) checkStyle.setChecked(true);
        if (Filter.categories.contains(Categories.SPORTS)) checkSports.setChecked(true);
    }

    private void setUpSorts(View view) {
        // Set up adapter
        spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.sorts_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(spinnerAdapter);

        // Set default selection
        int newestPos = spinnerAdapter.getPosition(SortOrder.NEWEST.toString());
        int oldestPos = spinnerAdapter.getPosition(SortOrder.OLDEST.toString());
        if (Filter.sortOrder == SortOrder.NEWEST) {
            spinnerSort.setSelection(newestPos);
        } else {
            spinnerSort.setSelection(oldestPos);
        }
    }

    private void setUpDates(View view) {
        DateTime selectedDate = Filter.beginTime;
        if (selectedDate != null) {
            tvDate.setText(selectedDate.getYear() + " " + selectedDate.getMonthOfYear() + " " +
                    selectedDate.getDayOfMonth());
        }
    }

    @OnCheckedChanged (R.id.checkbox_arts)
    public void checkListenerArts(boolean checked) {
        if (checked) {
            Filter.addCategory(Categories.ARTS);
        } else {
            Filter.removeCategory(Categories.ARTS);
        }
    }

    @OnCheckedChanged (R.id.checkbox_sports)
    public void checkListenerSports(boolean checked) {
        if (checked) {
            Filter.addCategory(Categories.SPORTS);
        } else {
            Filter.removeCategory(Categories.SPORTS);
        }
    }
    @OnCheckedChanged (R.id.checkbox_style)
    public void checkListenerStyle(boolean checked) {
        if (checked) {
            Filter.addCategory(Categories.STYLE);
        } else {
            Filter.removeCategory(Categories.STYLE);
        }
    }

    @OnItemSelected (R.id.spinSort)
    public void selectSortItem(AdapterView<?> adapterView, View view, int i, long l) {
        String selection = adapterView.getItemAtPosition(i).toString();
        if (selection.equals(SortOrder.NEWEST.toString())) {
            Filter.sortOrder = SortOrder.NEWEST;
        } else {
            Filter.sortOrder = SortOrder.OLDEST;
        }
    }

    @OnClick (R.id.start_time_date)
    public void clickDateSelector(View v) {
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

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
