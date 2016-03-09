package th.or.nectec.tanrabad.survey.presenter;

import android.widget.AdapterView;

import java.util.List;

interface ListViewAdapter<T> {
    void updateData(List<T> dataList);

    void clearData();

    T getItem(int position);

    void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener);

    void setOnItemLongClickListener(AdapterView.OnItemLongClickListener onItemLongClickListener);
}
