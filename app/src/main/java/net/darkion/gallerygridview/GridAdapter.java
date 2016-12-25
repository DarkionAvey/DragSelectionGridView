package net.darkion.gallerygridview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by DarkionAvey
 */

public class GridAdapter extends BaseAdapter {
    private Context context;
    int numberOfItems;
    ArrayList<Boolean> checkedItems = new ArrayList<>();

    public GridAdapter(Context context, int numberOfItems) {
        this.context = context;
        this.numberOfItems = numberOfItems;
        for (int i = 0; i < numberOfItems; i++) {
            checkedItems.add(false);
        }
    }



    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_view_adapter_item, parent, false);
        }

        ((GridViewItem) convertView).setChecked(checkedItems.get(position), false);
  ////      ImageView imageView = (ImageView) convertView.findViewById(R.id.image);

        convertView.setTag(position);
        convertView.requestLayout();
        return convertView;
    }

    public ArrayList<Integer> getCheckedItems() {
        ArrayList<Integer> results = new ArrayList<>();

        for (int x = 0; x < checkedItems.size(); x++) {
            if (checkedItems.get(x)) {
                results.add(x);
            }
        }
        return results;
    }

    public int getCheckedItemsNumber() {
        int i = 0;
        for (int x = 0; x < checkedItems.size(); x++) {
            if (checkedItems.get(x)) {
                i++;
            }
        }
        return i;
    }

    public boolean getCheckedItem(int position) {
        return checkedItems.get(position);
    }

    public void setCheckedItem(int position, boolean checked) {
        checkedItems.set(position, checked);
    }

    @Override
    public int getCount() {
        return numberOfItems;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
