package com.fz.fzapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.fz.fzapp.R;
import com.fz.fzapp.model.ReasonResponse;
import com.fz.fzapp.popup.ReasonStatus;
import com.fz.fzapp.service.AllFunction;
import com.fz.fzapp.utils.Preference;
import com.fz.fzapp.utils.SaveToSQLite;

import java.util.List;

/**
 * Dibuat oleh : ignat
 * Tanggal : 20-Jul-17
 * HP/WA : 0857 7070 6 777
 */
public class ReasonList_adapter extends BaseAdapter
{
	private static String TAG = "[adapter_TaskList]";
	private static Activity activity;
	private static Context mContext;
	static private List<ReasonResponse> reasonResponses;
	private static Integer intDuty;

	public ReasonList_adapter(Activity mActivity, Context mContext, List<ReasonResponse> reasonResponses, Integer intDuty)
	{
		this.mContext = mContext;
		this.reasonResponses = reasonResponses;
		this.activity = mActivity;
		this.intDuty = intDuty;
	}

	@Override
	public int getCount()
	{
		return reasonResponses.size();
	}

	@Override
	public Object getItem(int i)
	{
		return reasonResponses.get(i).getReasonid();
	}

	@Override
	public long getItemId(int i)
	{
		return reasonResponses.get(i).getReasonid();
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup)
	{
		if(view == null)
		{
			LayoutInflater lInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			view = lInflater.inflate(R.layout.reasondetail_lay, null);
		}

		ViewHolder vhView = new ViewHolder(view);
		vhView.tvReasonName.setText(reasonResponses.get(i).getReasonname() + " / " + reasonResponses.get(i).getReasondesc());
		vhView.tvReasonName.setTag(i);

		return view;
	}

	static class ViewHolder
	{
		@BindView(R.id.tvReasonName)
		TextView tvReasonName;

		ViewHolder(View view)
		{
			ButterKnife.bind(this, view);
		}

		@OnClick({R.id.rlDetailReason, R.id.tvReasonName})
		public void onViewClicked(View view)
		{
			switch(view.getId())
			{
				case R.id.rlDetailReason:
				case R.id.tvReasonName:
					Integer idxView = (Integer) tvReasonName.getTag();
					final String[] strReason = {""};

					if(reasonResponses.get(idxView).getReasonname() == "Alasan Lain")
					{
						ReasonStatus reasonStatus = new ReasonStatus(activity);
						reasonStatus.show();

						reasonStatus.setOnDismissListener(new DialogInterface.OnDismissListener()
						{
							@Override
							public void onDismiss(DialogInterface dialogInterface)
							{
								strReason[0] = AllFunction.getStringFromSharedPref(mContext, Preference.prefOtherReason);
							}
						});
					}
					else
						strReason[0] = reasonResponses.get(idxView).getReasonname();

					SaveToSQLite saveToSQLite = new SaveToSQLite(strReason[0], activity, mContext, intDuty);
					saveToSQLite.ProcessToSQLite();
				break;
			}
		}
	}
}
