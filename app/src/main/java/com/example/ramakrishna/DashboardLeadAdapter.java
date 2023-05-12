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

import com.example.ramakrishna.databinding.DashboardleadadapterBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import RetrofitUtils.Response.DashboardResponse;
import Utils.CommonFunction;
import Utils.SharedPrefsUtils;

public class DashboardLeadAdapter  extends RecyclerView.Adapter<DashboardLeadAdapter.ViewHolder> {

    private Context context;
    List<DashboardResponse.Result> mLeadsResponse;
    CommonFunction obj_commonfunction;
    private Activity mActicity;
    String  mNotificationDate;
    String mfrom;

    public DashboardLeadAdapter(Context context,Activity mActicity, List<DashboardResponse.Result> mLeadsResponse,String mfrom) {
        this.context = context;
        this.mActicity = mActicity;
        this.mLeadsResponse = mLeadsResponse;
        this.mfrom = mfrom;
        obj_commonfunction = new CommonFunction(mActicity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        DashboardleadadapterBinding mLeadsadapterBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext())
                , R.layout.dashboardleadadapter, parent, false);

        return new ViewHolder(mLeadsadapterBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {


      /*  if(mLeadsResponse.get(position).getContact_name() == null)
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
        holder.mLeadsadapterBinding.moppname.setText(mLeadsResponse.get(position).getEnquiry_name());






        holder.mLeadsadapterBinding.mClickRow.setOnClickListener(view -> {

//                KEY_OPPORTUNITYNAME,KEY_CONTACTNAME,KEY_EMAIL,KEY_PHONE,KEY_NEXTACTIVITY,KEY_ACTIVITYTYPE,KEY_DUEDATE,KEY_USERACTIVITYID
            Gson gson = new Gson();
            List<DashboardResponse.Result> Senduserdetails = new ArrayList<DashboardResponse.Result>();
            Senduserdetails.add(mLeadsResponse.get(position));
            String Struserdetails = gson.toJson(Senduserdetails);
            SharedPrefsUtils.putString(SharedPrefsUtils.PREF_KEY.LEAD_DETAILS, Struserdetails);

            if(mfrom.contentEquals("cal"))
            {
                SharedPrefsUtils.putInt(SharedPrefsUtils.PREF_KEY.MFROM, 3);
            }
            else
            {
                SharedPrefsUtils.putInt(SharedPrefsUtils.PREF_KEY.MFROM, 1);
            }


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
        public DashboardleadadapterBinding mLeadsadapterBinding;


        public ViewHolder(DashboardleadadapterBinding mLeadsadapterBinding) {
            super(mLeadsadapterBinding.getRoot());
            this.mLeadsadapterBinding = mLeadsadapterBinding;

        }
    }



}