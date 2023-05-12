package com.example.ramakrishna;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import Utils.CustomToast;

public class CalendarAdapter extends BaseAdapter {
	private Context context;

	private Calendar mCurrentMonth;
	public GregorianCalendar mGregorianMonth;
	TextView dayView,leadscount;
	public GregorianCalendar mMonthMaxSet;
	private GregorianCalendar mSelectedDate;
	int mFirstDayofMonth,mMaxWeeknumber,mMaxPrevWeeknumber,calMaxP,mMonthlength;
	String mDateValue, mCurentDate;
	DateFormat df;
	String[] mGetArrayValue;
	String mLeaveDate, mLeaveStatus;
	LinearLayout mCalDateClick;
	public static List<String> mdays;
	public ArrayList<String> mDeliveryBoyLeaveDetails;
	public CalendarAdapter(Context context, GregorianCalendar mGetMonth, ArrayList<String> mGetDeliveryBoyLeaveDetails) {
		mDeliveryBoyLeaveDetails = new ArrayList<String>();
		mDeliveryBoyLeaveDetails = mGetDeliveryBoyLeaveDetails;
		mdays = new ArrayList<String>();
		Locale.setDefault(Locale.US);
		mCurrentMonth = mGetMonth;
		mSelectedDate = (GregorianCalendar) mGetMonth.clone();
		this.context = context;
		mCurrentMonth.set(GregorianCalendar.DAY_OF_MONTH, 1);
		df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		Date date = new Date();
		mCurentDate = df.format(date);
		refreshDays();
	}



	public int getCount() {
		return mdays.size();
	}

	public Object getItem(int position) {
		return mdays.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		View v = convertView;
		if (convertView == null)
		{

			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.calendar_item, null);

		}

		dayView = (TextView) v.findViewById(R.id.date);
		leadscount = (TextView) v.findViewById(R.id.leadscount);
		mCalDateClick = (LinearLayout) v.findViewById(R.id.mCalDateClick);

		String[] separatedTime = mdays.get(position).split("-");
		String gridvalue = separatedTime[2].replaceFirst("^0*", "");
		if ((Integer.parseInt(gridvalue) > 1) && (position < mFirstDayofMonth)) {
			dayView.setTextColor(Color.GRAY);
			dayView.setClickable(false);
			dayView.setFocusable(false);
		} else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {
			dayView.setTextColor(Color.GRAY);
			dayView.setClickable(false);
			dayView.setFocusable(false);
		} else {

			dayView.setTextColor(Color.BLACK);
		}


		if (mdays.get(position).equals(mCurentDate)) {

		//	v.setBackgroundColor(Color.RED);
			dayView.setTextColor(Color.parseColor("#6f2e8d"));

		} else {
			v.setBackgroundColor(Color.parseColor("#FFFFFF"));
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date strDate = null;
		try {
			strDate = sdf.parse(mdays.get(position));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (mdays.get(position).equals(mCurentDate)) {
			leadscount.setBackground(context.getDrawable(R.drawable.cricle_today));
		}

		else if (new Date().after(strDate)) {
			leadscount.setBackground(context.getDrawable(R.drawable.cricle_overdue));
		}
		else if (new Date().before(strDate)) {
			leadscount.setBackground(context.getDrawable(R.drawable.cricle_feture));
		}




		dayView.setText(gridvalue);


		if (position % 7 == 0)
		{
			dayView.setTextColor(Color.parseColor("#ffffff"));
		}

		mCalDateClick.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {



				String[] separatedTime = mdays.get(position).split("-");
				String gridvalue = separatedTime[2].replaceFirst("^0*", "");
				if ((Integer.parseInt(gridvalue) > 1) && (position < mFirstDayofMonth)) {
					CustomToast.makeText(context, "You can view only current month leads ", CustomToast.LENGTH_SHORT, 0).show();
				} else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {

					CustomToast.makeText(context, "You can view only current month leads ", CustomToast.LENGTH_SHORT, 0).show();
				} else {

					((ItemClickListenerEvent) context).onClickEvent(view,position,mdays.get(position));
				}


			}
		});

		setEventView(v, position,dayView,leadscount);
		return v;
	}




	public void refreshDays() {

		mdays.clear();
		Locale.setDefault(Locale.US);
		mGregorianMonth = (GregorianCalendar) mCurrentMonth.clone();
		mFirstDayofMonth = mCurrentMonth.get(GregorianCalendar.DAY_OF_WEEK);
		mMaxWeeknumber = mCurrentMonth.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
		mMonthlength = mMaxWeeknumber * 7;
		mMaxPrevWeeknumber = getMaxP(); // previous month maximum day 31,30....
		calMaxP = mMaxPrevWeeknumber - (mFirstDayofMonth - 1);// calendar offday starting 24,25 ...
		mMonthMaxSet = (GregorianCalendar) mGregorianMonth.clone();
		mMonthMaxSet.set(GregorianCalendar.DAY_OF_MONTH, calMaxP + 1);
		for (int n = 0; n < mMonthlength; n++) {

			mDateValue = df.format(mMonthMaxSet.getTime());
			mMonthMaxSet.add(GregorianCalendar.DATE, 1);
			mdays.add(mDateValue);

		}
	}

	private int getMaxP() {
		int maxP;
		if (mCurrentMonth.get(GregorianCalendar.MONTH) == mCurrentMonth
				.getActualMinimum(GregorianCalendar.MONTH)) {
			mGregorianMonth.set((mCurrentMonth.get(GregorianCalendar.YEAR) - 1),
					mCurrentMonth.getActualMaximum(GregorianCalendar.MONTH), 1);
		} else {
			mGregorianMonth.set(GregorianCalendar.MONTH,
					mCurrentMonth.get(GregorianCalendar.MONTH) - 1);
		}
		maxP = mGregorianMonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

		return maxP;
	}


	public void setEventView(View v, final int pos,TextView dayView,TextView leadscount) {
		if (pos % 7 == 0){
			v.setBackgroundColor(Color.parseColor("#999696"));

		}
		else
		{

			v.setBackgroundColor(Color.parseColor("#ffffff"));

		}

		if (mdays.get(pos).equals(mCurentDate)) {
			v.setBackgroundColor(Color.parseColor("#6f2e8d"));
			dayView.setTextColor(Color.WHITE);
			leadscount.setBackground(context.getDrawable(R.drawable.cricle_today));
		}


		for (int i = 0; i < mDeliveryBoyLeaveDetails.size(); i++)
		{
			if (mdays.get(pos).equals(mDeliveryBoyLeaveDetails.get(i))) {
				leadscount.setVisibility(View.VISIBLE);

				int mcount = Integer.parseInt(leadscount.getText().toString()) + 1;
				leadscount.setText(String.valueOf(mcount));
			}
			else
			{
				//leadscount.setVisibility(View.INVISIBLE);
			}


		}


	}

}