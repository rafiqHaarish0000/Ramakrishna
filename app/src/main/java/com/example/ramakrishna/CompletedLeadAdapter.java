package com.example.ramakrishna;

import static Utils.ConstantClass.Changeformat;
import static Utils.ConstantClass.Simpleformat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ramakrishna.databinding.CompletedleadadapterBinding;
import com.example.ramakrishna.databinding.LeadadapterBinding;

import java.util.List;

import RetrofitUtils.Response.ActivityDoneResponse;
import RetrofitUtils.Response.DashboardResponse;
import Utils.CommonFunction;
import Utils.SharedPrefsUtils;

public class CompletedLeadAdapter extends RecyclerView.Adapter<CompletedLeadAdapter.ViewHolder> {

    private Context context;
    List<ActivityDoneResponse.Result> mLeadsResponse;
    CommonFunction obj_commonfunction;
    private Activity mActicity;
    String  mNotificationDate;
    String mfrom;

    public CompletedLeadAdapter(Context context, Activity mActicity, List<ActivityDoneResponse.Result> mLeadsResponse, String mfrom) {
        this.context = context;
        this.mActicity = mActicity;
        this.mLeadsResponse = mLeadsResponse;
        this.mfrom = mfrom;
        obj_commonfunction = new CommonFunction(mActicity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        CompletedleadadapterBinding
                mLeadsadapterBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext())
                , R.layout.completedleadadapter, parent, false);

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


        holder.mLeadsadapterBinding.moppname.setText(mLeadsResponse.get(position).getRecord_name());




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
        public CompletedleadadapterBinding
                mLeadsadapterBinding;


        public ViewHolder(CompletedleadadapterBinding
                                          mLeadsadapterBinding) {
            super(mLeadsadapterBinding.getRoot());
            this.mLeadsadapterBinding = mLeadsadapterBinding;

        }
    }



}
