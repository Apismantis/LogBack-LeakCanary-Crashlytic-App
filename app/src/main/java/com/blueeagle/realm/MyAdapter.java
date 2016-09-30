package com.blueeagle.realm;

        import android.content.Context;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.ListAdapter;
        import android.widget.TextView;

        import java.util.Random;

        import Model.Contact;
        import io.realm.RealmBaseAdapter;
        import io.realm.RealmResults;

public class MyAdapter extends RealmBaseAdapter<Contact> implements ListAdapter {

    public static class MyViewHolder {
        private TextView tvFullName;
        private TextView tvPhoneNumber;
    }

    public MyAdapter(Context context, int resID, RealmResults<Contact> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder viewHolder;

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.item_contact, parent, false);
            viewHolder = new MyViewHolder();

            viewHolder.tvFullName = (TextView) convertView.findViewById(R.id.tvFullName);
            viewHolder.tvPhoneNumber = (TextView) convertView.findViewById(R.id.tvPhoneNumber);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MyViewHolder) convertView.getTag();
        }

        Contact item = realmResults.get(position);

        viewHolder.tvFullName.setText(item.getFullName());
        viewHolder.tvPhoneNumber.setText(item.getPhone());

        return convertView;
    }

    public RealmResults<Contact> getRealmResults() {
        return realmResults;
    }

    public void setData(RealmResults<Contact> contacts) {
        realmResults = contacts;
    }
}
