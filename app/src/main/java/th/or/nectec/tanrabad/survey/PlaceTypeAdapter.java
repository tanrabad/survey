package th.or.nectec.tanrabad.survey;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import th.or.nectec.tanrabad.entity.Place;

public class PlaceTypeAdapter extends BaseAdapter {

    Context context;

    ArrayList<PlaceType> placeTypes = new ArrayList<>();

    public PlaceTypeAdapter(Context context) {
        this.context = context;

        ArrayList<PlaceType> placeTypes = new ArrayList<>();
        placeTypes.add(new PlaceType(-1, "ทั้งหมด"));
        placeTypes.add(new PlaceType(Place.TYPE_VILLAGE_COMMUNITY, "หมู่บ้าน/ชุมชน"));
        placeTypes.add(new PlaceType(Place.TYPE_WORSHIP, "ศาสนสถาน"));
        placeTypes.add(new PlaceType(Place.TYPE_SCHOOL, "โรงเรียน"));
        placeTypes.add(new PlaceType(Place.TYPE_HOSPITAL, "โรงพยาบาล"));
        placeTypes.add(new PlaceType(Place.TYPE_FACTORY, "โรงงาน"));

        this.placeTypes.addAll(placeTypes);
    }

    @Override
    public int getCount() {
        return placeTypes.size();
    }

    @Override
    public PlaceType getItem(int i) {

        return placeTypes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return placeTypes.get(i).id;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final ViewHolder holder;
        if (view == null || view.getTag() == null) {
            view = inflater.inflate(R.layout.spinner_list_item, parent, false);
            holder = new ViewHolder();
            holder.nameView = (TextView) view.findViewById(R.id.text_item);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.nameView.setText(placeTypes.get(i).name);
        view.setTag(holder);

        return view;
    }

    public class ViewHolder {
        TextView nameView;
    }

    public class PlaceType {
        int id;
        String name;

        public PlaceType(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
