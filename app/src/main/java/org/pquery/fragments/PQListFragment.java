package org.pquery.fragments;

import android.R.color;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;

import org.pquery.R;
import org.pquery.dao.DownloadablePQ;
import org.pquery.dao.PQListItem;
import org.pquery.dao.PQListItemSection;
import org.pquery.dao.RepeatablePQ;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Fragment that shows a list of Pocket Query that the user can download
 * <p/>
 * It shows some help when first opened
 */
public class PQListFragment extends SherlockListFragment {

    public interface PQClickedListener {
        public void onPQClicked(DownloadablePQ pq);
    }

    private PQClickedListener listener;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (PQClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement PQListFragment");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pq_list_fragment, null);

        return view;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        PQListItem pqListItem = (PQListItem) l.getItemAtPosition((int) id);
        if (pqListItem instanceof DownloadablePQ) {
            listener.onPQClicked((DownloadablePQ)pqListItem);
        } else if (pqListItem instanceof RepeatablePQ) {
            // TODO: open popup to select weekdays
        }
    }


    /**
     * Change list of Pocket Query
     * <p/>
     * Called from Activity
     *
     * @param pqs - null = list unknown
     *            empty array = user has no downloadable DownloadablePQ
     */
    public void updateList(DownloadablePQ[] pqs, RepeatablePQ[] repeatables) {


        if (pqs == null && repeatables == null) {

            // We have don't have a pocket query list to display
            // so show some help instead

            WebView wv = ((WebView) getListView().getEmptyView());

            String html2 = "<html>" +
                    "<body bgcolor='#000000'>" +
                    "<table style='height:100%;width:100%;'>" +
                    "<tr>" +
                    "<td align='center' valign='center'>" +
                    "<font color='grey'>" +
                    "This app allows the easy creation of Pocket Queries<p>" +
                    "It requires a premium geocaching.com account<p>" +
                    "Press <img style='vertical-align: middle' width='20px' src='content_new.png'> to create new pocket query<br>" +
                    "Press <img style='vertical-align: middle' width='20px' src='navigation_refresh.png'> to download existing pocket query" +
                    "<p>" +
                    "Press <img style='vertical-align: middle' width='20px' src='action_help.png'> to get more help" +
                    "</font>" +
                    "</td>" +
                    "</tr>" +
                    "</table>" +
                    "</body>" +
                    "</html>";

            wv.setBackgroundColor(getResources().getColor(color.black));
            wv.loadDataWithBaseURL("file:///android_asset/", html2, "text/html", "utf-8", "");


            setListAdapter(new IconicAdapter(getSherlockActivity(), new PQListItem[0]));        // have to set empty list so help is displayed

        } else {

            // If list is empty, display a message
            // else display list

            ((WebView) getListView().getEmptyView()).setBackgroundColor(getResources().getColor(color.black));
            ((WebView) getListView().getEmptyView()).loadDataWithBaseURL("file:///android_asset/", "<html><body bgcolor='#000000'><table style='height:100%;width:100%;'><tr><td align='center'><font color='white'>No downloadable DownloadablePQ</font></td></tr></table></body></html>", "text/html", "utf-8", "");

            List<PQListItem> listItems = new ArrayList<PQListItem>();
            if (pqs != null) {
                listItems.add(new PQListItemSection("Downloadable PQs"));
                listItems.addAll(Arrays.asList(pqs));
            }

            if (repeatables != null) {
                listItems.add(new PQListItemSection("Repeatable PQs"));
                listItems.addAll(Arrays.asList(repeatables));
            }

            setListAdapter(new IconicAdapter(getSherlockActivity(), listItems.toArray(new PQListItem[0])));
        }

    }

    private class IconicAdapter extends ArrayAdapter<PQListItem> {

        Activity context;

        IconicAdapter(Activity context, PQListItem[] pqs) {
            super(context, 0, pqs);
            this.context = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View row = null;

            PQListItem pqListItem = getItem(position);

            if (pqListItem instanceof DownloadablePQ) {
                DownloadablePQ pq = (DownloadablePQ) pqListItem;
                row = inflater.inflate(R.layout.dpq_list_row, parent, false);

                TextView name = (TextView) row.findViewById(R.id.name);
                name.setText(pq.name);

                TextView size = (TextView) row.findViewById(R.id.size);
                size.setText(pq.size);

                TextView waypoints = (TextView) row.findViewById(R.id.waypoints);
                waypoints.setText(pq.waypoints);

                TextView generated = (TextView) row.findViewById(R.id.generated);
                generated.setText(pq.age);
            } else if (pqListItem instanceof RepeatablePQ) {
                RepeatablePQ pq = (RepeatablePQ)pqListItem;
                row = inflater.inflate(R.layout.rpq_list_row, parent, false);

                TextView name = (TextView) row.findViewById(R.id.name);
                name.setText(pq.name);

                TextView waypoints = (TextView) row.findViewById(R.id.waypoints);
                waypoints.setText(pq.waypoints);

                TextView weekdays = (TextView) row.findViewById(R.id.weekdays);
                weekdays.setText(pq.getCheckedWeekdays());
            } else if (pqListItem instanceof PQListItemSection) {
                PQListItemSection pq = (PQListItemSection)pqListItem;
                row = inflater.inflate(R.layout.spq_list_row, parent, false);

                TextView name = (TextView) row.findViewById(R.id.name);
                name.setText(pq.getName());
            }

            return row;
        }
    }


}
