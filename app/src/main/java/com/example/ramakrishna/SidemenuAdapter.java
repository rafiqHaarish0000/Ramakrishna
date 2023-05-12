package com.example.ramakrishna;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.drawerlayout.widget.DrawerLayout;

import com.example.ramakrishna.UI_Testing.activities.CustomTodo;

import Utils.CommonFunction;
import Utils.SharedPrefsUtils;

public class SidemenuAdapter  extends ArrayAdapter<String> {
    String[] mSideMenuList;
    private int listItemLayout;
    Integer[] mSideMenuimages;
    DrawerLayout mNavicationDrawer;
    Context context;
    CommonFunction obj_commonfunction;
    Activity activity;

    public SidemenuAdapter(Activity activity, Context context, int layoutId, String[] mSideMenuList, Integer[] mSideMenuimages, DrawerLayout mNavicationDrawer) {
        super(context, layoutId, mSideMenuList);
        listItemLayout = layoutId;
        this.context = context;
        this.mSideMenuList = mSideMenuList;
        this.mSideMenuimages = mSideMenuimages;
        this.mNavicationDrawer = mNavicationDrawer;
        this.activity = activity;
        obj_commonfunction = new CommonFunction(activity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final String item = getItem(pos);

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(listItemLayout, parent, false);
            viewHolder.SideMenuListName = (TextView) convertView.findViewById(R.id.sidemenu_text);
            viewHolder.SideMenuImage = (ImageView) convertView.findViewById(R.id.sidemenuimage);
            viewHolder.ll_sidemenuclick = (LinearLayout) convertView.findViewById(R.id.ll_sidemenuclick);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.SideMenuListName.setText(item);
        viewHolder.SideMenuImage.setImageResource(mSideMenuimages[pos]);

        viewHolder.ll_sidemenuclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (pos == 0) {
                    Intent i = new Intent(context, Dashboard.class);
                    context.startActivity(i);
                    ((Activity) context).finish();
                    mNavicationDrawer.closeDrawer(Gravity.LEFT);
                } else if (pos == 1) {

                    Intent i = new Intent(context, CalendarActivity.class);
                    context.startActivity(i);
                    ((Activity) context).finish();
                    mNavicationDrawer.closeDrawer(Gravity.LEFT);

                } else if (pos == 2) {
                    SharedPrefsUtils.putInt(SharedPrefsUtils.PREF_KEY.VIEWPAGERCURRENTPOSTION, 0);
                    Intent i = new Intent(context, CustomTodo.class);
                    context.startActivity(i);
                    ((Activity) context).finish();
                    mNavicationDrawer.closeDrawer(Gravity.LEFT);
                }
                else if (pos == 3) {
                    Intent i = new Intent(context, CompletedActivity.class);
                    context.startActivity(i);
                    ((Activity) context).finish();
                    mNavicationDrawer.closeDrawer(Gravity.LEFT);
                }

                else if(pos == 4){
                    Intent i = new Intent(context, Profile.class);
                    context.startActivity(i);
                    ((Activity) context).finish();
                    mNavicationDrawer.closeDrawer(Gravity.LEFT);
                }

                else if (pos == 5) {
                    Intent i = new Intent(context, Profile.class);
                    context.startActivity(i);
                    ((Activity) context).finish();
                    mNavicationDrawer.closeDrawer(Gravity.LEFT);
                }

            }
        });
        return convertView;
    }


    class ViewHolder {
        TextView SideMenuListName;
        ImageView SideMenuImage;
        LinearLayout ll_sidemenuclick;
    }
}
