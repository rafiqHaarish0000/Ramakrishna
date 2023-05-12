package com.example.ramakrishna;

import static Utils.ConstantClass.Changeformat;
import static Utils.ConstantClass.Simpleformat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ramakrishna.databinding.LeadadapterBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import RetrofitUtils.Response.DashboardResponse;
import RetrofitUtils.Response.LoginResponse;
import Utils.CommonFunction;
import Utils.SharedPrefsUtils;

public class LeadAdapter  extends RecyclerView.Adapter<LeadAdapter.ViewHolder> {

    private Context context;
    List<DashboardResponse.Result> mLeadsResponse;
    CommonFunction obj_commonfunction;
    private Activity mActicity;
    String  mNotificationDate;
    String mfrom;

    public LeadAdapter(Context context,Activity mActicity, List<DashboardResponse.Result> mLeadsResponse,String mfrom) {
        this.context = context;
        this.mActicity = mActicity;
        this.mLeadsResponse = mLeadsResponse;
        this.mfrom = mfrom;
        obj_commonfunction = new CommonFunction(mActicity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        LeadadapterBinding mLeadsadapterBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext())
                , R.layout.leadadapter, parent, false);

        return new ViewHolder(mLeadsadapterBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {


       /* if(mLeadsResponse.get(position).getContact_name() == null)
        {
            holder.mLeadsadapterBinding.mcustomer.setText("-");
            SharedPrefsUtils.putString(SharedPrefsUtils.PREF_KEY.KEY_CONTACTNAME, "-");
        }
        else
        {
            if(mLeadsResponse.get(position).getContact_name().contentEquals(""))
            {
                holder.mLeadsadapterBinding.mcustomer.setText("-");
                SharedPrefsUtils.putString(SharedPrefsUtils.PREF_KEY.KEY_CONTACTNAME, "-");
            }
            else
            {
                holder.mLeadsadapterBinding.mcustomer.setText(mLeadsResponse.get(position).getContact_name());
                SharedPrefsUtils.putString(SharedPrefsUtils.PREF_KEY.KEY_CONTACTNAME, mLeadsResponse.get(position).getContact_name());
            }
        }*/



        holder.mLeadsadapterBinding.mcustomer.setText(mLeadsResponse.get(position).getLocation());

        holder.mLeadsadapterBinding.mactivitytype.setText(mLeadsResponse.get(position).getActivity_type());


        if(mfrom.contentEquals("Overdue"))
        {
            holder.mLeadsadapterBinding.mDuedate.setTextColor(Color.parseColor("#DC3545"));
        }
        else if(mfrom.contentEquals("Feature"))
        {
            holder.mLeadsadapterBinding.mDuedate.setTextColor(Color.parseColor("#28A745"));
        }
        else if(mfrom.contentEquals("Today"))
        {
            holder.mLeadsadapterBinding.mDuedate.setTextColor(Color.parseColor("#FFAC00"));
        }

        if(mLeadsResponse.get(position).getDue_date() == null)
        {
            holder.mLeadsadapterBinding.mDuedate.setText("");

        }
        else
        {
            holder.mLeadsadapterBinding.mDuedate.setText("My Deadline  " + CommonFunction.formatDate(mLeadsResponse.get(position).getDue_date(), Simpleformat, Changeformat));

        }

        holder.mLeadsadapterBinding.moppname.setText(mLeadsResponse.get(position).getEnquiry_name());


        holder.mLeadsadapterBinding.mClickRow.setOnClickListener(view -> {

//                KEY_OPPORTUNITYNAME,KEY_CONTACTNAME,KEY_EMAIL,KEY_PHONE,KEY_NEXTACTIVITY,KEY_ACTIVITYTYPE,KEY_DUEDATE,KEY_USERACTIVITYID

            /*
            *  SharedPrefsUtils.putString(SharedPrefsUtils.PREF_KEY.KEY_OPPORTUNITYNAME, mLeadsResponse.get(position).getEnquiry_name());

            SharedPrefsUtils.putString(SharedPrefsUtils.PREF_KEY.KEY_EMAIL, "");
            SharedPrefsUtils.putString(SharedPrefsUtils.PREF_KEY.KEY_PHONE, mLeadsResponse.get(position).getContact_mobile());
            SharedPrefsUtils.putString(SharedPrefsUtils.PREF_KEY.KEY_NEXTACTIVITY, mLeadsResponse.get(position).getSummary());
            SharedPrefsUtils.putString(SharedPrefsUtils.PREF_KEY.KEY_ACTIVITYTYPE, mLeadsResponse.get(position).getActivity_type());

            SharedPrefsUtils.putInt(SharedPrefsUtils.PREF_KEY.KEY_USERACTIVITYID, mLeadsResponse.get(position).getUser_activity_id());
*/

            Gson gson = new Gson();
            List<DashboardResponse.Result> Senduserdetails = new ArrayList<DashboardResponse.Result>();
            Senduserdetails.add(mLeadsResponse.get(position));
            String Struserdetails = gson.toJson(Senduserdetails);
            SharedPrefsUtils.putString(SharedPrefsUtils.PREF_KEY.LEAD_DETAILS, Struserdetails);

            SharedPrefsUtils.putInt(SharedPrefsUtils.PREF_KEY.MFROM, 2);
            obj_commonfunction.navigation(context, AddLocation.class); //IntroPage
          // (context).finish();

        });

    }



    @Override
    public int getItemCount() {
        return mLeadsResponse.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        public LeadadapterBinding mLeadsadapterBinding;


        public ViewHolder(LeadadapterBinding mLeadsadapterBinding) {
            super(mLeadsadapterBinding.getRoot());
            this.mLeadsadapterBinding = mLeadsadapterBinding;

        }
    }



}
