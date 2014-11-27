package org.pquery.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import net.htmlparser.jericho.Source;

import org.pquery.R;
import org.pquery.util.Logger;
import org.pquery.webdriver.FailurePermanentException;
import org.pquery.webdriver.RetrievePageTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Dialog with a list of weekdays to change the scheduling of a PQ
 */
public class SchedulePQFragment extends DialogFragment {

    private List<String> mSelectedItems = new ArrayList<String>();
    private List<String> weekdays = new ArrayList<String>();
    private boolean[] selectedWeekdays;
    private Map<String, String> weekdayLinks;

    public void setWeekdays(Map<String, String> weekdayLinks) {
        this.weekdayLinks = weekdayLinks;
        this.weekdays.addAll(weekdayLinks.keySet());
        selectedWeekdays = new boolean[weekdays.size()];
        int index = 0;
        for (Map.Entry<String, String> weekdayEntry : weekdayLinks.entrySet()) {
            if (weekdayEntry.getValue().endsWith("opt=0")) {
                mSelectedItems.add(weekdayEntry.getKey());
                selectedWeekdays[index] = true;
            } else {
                selectedWeekdays[index] = false;
            }
            index++;
        }

    }

    @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            // Set the dialog title
            builder.setTitle(R.string.change_schedule)
                    // Specify the list array, the items to be selected by default (null for none),
                    // and the listener through which to receive callbacks when items are selected
                    .setMultiChoiceItems(weekdays.toArray(new String[0]), selectedWeekdays,
                            new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which,
                                                    boolean isChecked) {
                                    if (isChecked) {
                                        // If the user checked the item, add it to the selected items
                                        mSelectedItems.add(weekdays.get(which));
                                        String href = weekdayLinks.get(weekdays.get(which));

                                        // TODO: call URL to change the schedule for the selected weekday
/*                                        RetrievePageTask schedulePQTask = new RetrievePageTask(5, 5, 30, this, this, ctx, href);
                                        try {
                                            Source parsedHtml = schedulePQTask.call();
                                        } catch (Exception e) {
                                            Logger.e("Couldn't change Schedule", e);
                                        }
*/
                                    } else if (mSelectedItems.contains(weekdays.get(which))) {
                                        // Else, if the item is already in the array, remove it
                                        mSelectedItems.remove(weekdays.get(which));
                                        // TODO: call URL to change the schedule for unselected weekday
                                    }
                                }
                            })
                            // Set the action buttons
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK, so save the mSelectedItems results somewhere
                            // or return them to the component that opened the dialog
                            // TODO: implement
                        }
                    });

            return builder.create();
        }

}
