package com.fz.fzapp.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.fz.fzapp.R;
import com.fz.fzapp.adapter.AllTaskList_adapter;
import com.fz.fzapp.data.TaskList;
import com.fz.fzapp.pojo.TaskListPojo;
import com.fz.fzapp.popup.OtherOption;
import com.fz.fzapp.sending.TaskListHolder;
import com.fz.fzapp.service.AllFunction;
import com.fz.fzapp.service.DataLink;
import com.fz.fzapp.utils.FixValue;
import com.fz.fzapp.utils.PopupMessege;
import com.fz.fzapp.utils.Preference;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncData extends AppCompatActivity
{
  @BindView(R.id.ivOtherMenu)
  ImageView ivOtherMenu;
  @BindView(R.id.tvMsg)
  TextView tvMsg;
  @BindView(R.id.btnCancelGo)
  Button btnCancelGo;
  @BindView(R.id.ivGo)
  ImageView ivGo;

  private PopupMessege popupMessege = new PopupMessege();
  private Activity activity = this;
  static String TAG = "[SyncData]";
  private Context context = this;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.syncdata_lay);
    ButterKnife.bind(this);
  }

  @Override
  public void onBackPressed()
  {
    moveTaskToBack(true);
  }

  private void getTaskList()
  {
    tvMsg.setText(context.getResources().getString(R.string.strWait));
    btnCancelGo.setVisibility(View.VISIBLE);
    ivGo.setImageResource(R.drawable.buttongo);

    if(AllFunction.isNetworkAvailable(context) == FixValue.TYPE_NONE)
    {
      tvMsg.setText(context.getResources().getString(R.string.titleKlik));
      btnCancelGo.setVisibility(View.GONE);
      ivGo.setBackgroundResource(R.drawable.buttonthick);
      popupMessege.ShowMessege1(context, context.getResources().getString(R.string.msgServerResponse));
      return;
    }

    TaskList taskList = new TaskList();
    taskList.setUserID(AllFunction.getIntFromSharedPref(context, Preference.prefUserID));
    taskList.setRoleID(AllFunction.getIntFromSharedPref(context, Preference.prefRoleID));
    taskList.setSyncDate(AllFunction.getDateFromSharedPref(context, Preference.prefSyncDate));
    taskList.setSyncTime(AllFunction.getTimeFromSharedPref(context, Preference.prefSyncTime));

    TaskListHolder taskListHolder = new TaskListHolder(taskList);
    DataLink dataLink = AllFunction.BindingData();

    final Call<TaskListPojo> ReceivePojo = dataLink.TaskListService(taskListHolder);

    ReceivePojo.enqueue(new Callback<TaskListPojo>()
    {
      @Override
      public void onResponse(Call<TaskListPojo> call, Response<TaskListPojo> response)
      {
        if(response.isSuccessful())
        {
          if(response.body().getCoreResponse().getCode() == FixValue.intFail)
          {
            Log.d(TAG, "onResponse: 1");
            tvMsg.setText(context.getResources().getString(R.string.titleKlik));
            btnCancelGo.setVisibility(View.GONE);
            ivGo.setBackgroundResource(R.drawable.buttonthick);
            popupMessege.ShowMessege1(context, response.body().getCoreResponse().getMsg());
          }
          else if(response.body().getCoreResponse().getCode() == FixValue.intSuccess)
          {
            AllFunction.storeToSharedPref(context, 1, Preference.prefDutyTask);

            AllTaskList_adapter.initAllTaskList();
            AllTaskList_adapter.getInstance().setAlltaskList(response.body().getTaskListResponse());

            Intent NamaUserIntent = new Intent(SyncData.this, Planning.class);
            startActivity(NamaUserIntent);
            finish();
          }
        }
        else
        {
          Log.d(TAG, "onResponse: 3");
          tvMsg.setText(context.getResources().getString(R.string.titleKlik));
          btnCancelGo.setVisibility(View.GONE);
          ivGo.setBackgroundResource(R.drawable.buttonthick);
          popupMessege.ShowMessege1(context, getResources().getString(R.string.msgServerData));
        }
      }

      @Override
      public void onFailure(Call<TaskListPojo> call, Throwable t)
      {
        tvMsg.setText(context.getResources().getString(R.string.titleKlik));
        btnCancelGo.setVisibility(View.GONE);
        ivGo.setBackgroundResource(R.drawable.buttonthick);
        popupMessege.ShowMessege1(context, getResources().getString(R.string.msgServerFailure));
      }
    });
  }

  @OnClick({R.id.ivOtherMenu, R.id.ivGo})
  public void onViewClicked(View view)
  {
    switch(view.getId())
    {
      case R.id.ivGo:
        getTaskList();
      break;
      case R.id.ivOtherMenu:
        OtherOption otherOption = new OtherOption(context, ivOtherMenu, activity, Username.class);
        otherOption.ShowOtherOptions();
      break;
    }
  }
}
