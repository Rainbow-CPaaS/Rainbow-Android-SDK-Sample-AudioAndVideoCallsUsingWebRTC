package webrtccall.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ale.infra.contact.IRainbowContact;
import com.ale.infra.list.ArrayItemList;
import com.ale.rainbowsdk.RainbowSdk;

import webrtccall.callapplication.R;

public class ContactsTabAdapter extends BaseAdapter {

    private ArrayItemList<IRainbowContact> m_roster = new ArrayItemList<>();
    private Context m_context;

    public ContactsTabAdapter(Context context) {
        m_context = context;
        updateContacts();
    }

    @Override
    public int getCount() {
        if (m_roster != null)
        {
            return m_roster.getCount();
        }
        return 0;

    }

    @Override
    public Object getItem(int position) {
        if (m_roster.getCount() > position) {
            return m_roster.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MyViewHolder myViewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)m_context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_list_contact, parent, false);

            myViewHolder = new MyViewHolder();
            myViewHolder.displayName = (TextView)convertView.findViewById(R.id.display_name);
            myViewHolder.company = (TextView)convertView.findViewById(R.id.company);
            myViewHolder.photo = (ImageView)convertView.findViewById(R.id.photo);
            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (MyViewHolder)convertView.getTag();
        }

        IRainbowContact contact = (IRainbowContact)getItem(position);
        myViewHolder.displayName.setText(contact.getFirstName() + " " + contact.getLastName());
        myViewHolder.company.setText(contact.getCompanyName());
        if (contact.getPhoto() == null) {
            myViewHolder.photo.setImageResource(R.drawable.contact);
        } else {
            myViewHolder.photo.setImageBitmap(contact.getPhoto());
        }

        return convertView;
    }

    public void updateContacts() {
        m_roster.replaceAll(RainbowSdk.instance().contacts().getRainbowContacts().getCopyOfDataList());
        notifyDataSetChanged();
    }

    private class MyViewHolder {
        TextView displayName;
        TextView company;
        ImageView photo;
    }
}

