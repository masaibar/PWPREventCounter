package com.masaibar.eventbeforeaftercounter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.masaibar.eventbeforeaftercounter.EventCharacter;
import com.masaibar.eventbeforeaftercounter.HighSchool;
import com.masaibar.eventbeforeaftercounter.R;

import java.util.List;

/**
 * Created by masaibar on 2016/02/28.
 */
public class EventCharacterAdapter extends ArrayAdapter<EventCharacter>{

    public EventCharacterAdapter(Context context, int resource, List<EventCharacter> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getViewAndDropDownView(position, convertView);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
       return getViewAndDropDownView(position, convertView);
    }

    private View getViewAndDropDownView(int position, View convertView) {
        final Context context = getContext();

        if (convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.spinner_item, null);
        }

        TextView label = (TextView) convertView.findViewById(R.id.checked_text);

        if(label != null) {
            EventCharacter ec = getItem(position);
            label.setText(ec.getName());
        }

        return convertView;
    }
}
