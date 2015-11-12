package th.or.nectec.tanrabad.survey;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import th.or.nectec.tanrabad.entity.Place;

public class PlaceAdapter extends BaseAdapter {

    Context context;

    ArrayList<Place> places = new ArrayList<>();

    public PlaceAdapter(Context context, List<Place> places) {
        this.context = context;
        this.places.addAll(places);
    }

    @Override
    public int getCount() {
        return places.size();

    }

    @Override
    public Object getItem(int i) {
        return places.get(i);

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final ViewHolder holder;
        if(view == null || view.getTag() == null){
            view = inflater.inflate(R.layout.place_list_item, parent, false);
            holder = new ViewHolder();
            holder.placeTextView = (TextView)view.findViewById(R.id.place_name);
            holder.placeIcon = (ImageView)view.findViewById(R.id.place_icon);

            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        holder.placeTextView.setText(places.get(i).getName());
        view.setTag(holder);

        return view;
    }
    public  class ViewHolder {
        TextView placeTextView;
        ImageView placeIcon;
    }

}
