package org.pquery.dao;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RepeatablePQ implements Parcelable, Serializable, PQListItem {

    private static final long serialVersionUID = -6540850109992510771L;
    public String name;
    public String waypoints;
    private String checkedWeekdays;
    private Map<String, String> weekdays = new HashMap<String, String>();

    public RepeatablePQ(Parcel in) {
        name = in.readString();
        waypoints = in.readString();
    }

    public RepeatablePQ() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(waypoints);
    }

    /**
     * Implementing the Parcelable interface must also have a static field called CREATOR, which is an object
     * implementing the Parcelable.Creator interface.
     */
    public static final Creator<RepeatablePQ> CREATOR = new Creator<RepeatablePQ>() {
        public RepeatablePQ createFromParcel(Parcel in) {
            return new RepeatablePQ(in);
        }

        public RepeatablePQ[] newArray(int size) {
            return new RepeatablePQ[size];
        }
    };

    @Override
    public String getName() {
        return name;
    }

    public String getCheckedWeekdays() {
        if (checkedWeekdays == null) {
            return "-";
        }
        return checkedWeekdays;
    }

    public void addWeekday(String weekday, String href) {
        weekdays.put(weekday, href);
        if (href.endsWith("opt=0")) {
            if (checkedWeekdays == null) {
                checkedWeekdays = weekday;
            } else {
                checkedWeekdays += ", " + weekday;
            }
        }
    }

    public Map<String,String> getWeekdays() {
        return weekdays;
    }

}
