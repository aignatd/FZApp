package com.fz.fzapp.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.fz.fzapp.R;
import com.fz.fzapp.adapter.adapter_AllTaskList;
import com.fz.fzapp.adapter.adapter_TaskList;
import com.fz.fzapp.data.TaskListData;
import com.fz.fzapp.popup.OtherOption;
import com.fz.fzapp.service.AllFunction;
import com.fz.fzapp.utils.Preference;

public class Planning extends AppCompatActivity
{
  @BindView(R.id.ivOtherMenuPlan)
  ImageView ivOtherMenuPlan;
  @BindView(R.id.lvListAll)
  ListView lvListAll;

  private Activity activity = this;
  static String TAG = "[SinkronData]";
  private Context context = this;
  private Integer iTotalTask;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.planning_lay);
    ButterKnife.bind(this);

    getAllTaskList();
  }

  @Override
  public void onBackPressed()
  {
    moveTaskToBack(true);
  }

  @OnClick({R.id.ivOtherMenuPlan, R.id.btnStartTask})
  public void onViewClicked(View view)
  {
    switch(view.getId())
    {
      case R.id.ivOtherMenuPlan:
        OtherOption otherOption = new OtherOption(context, ivOtherMenuPlan, activity, Username.class);
        otherOption.ShowOtherOptions();
      break;
      case R.id.btnStartTask:
        if(AllFunction.getIntFromSharedPref(context, Preference.prefDutyTask) <= iTotalTask)
        {
          Intent DutyIntent = new Intent(Planning.this, Duty.class);
          startActivity(DutyIntent);
          finish();
        }
      break;
    }
  }

  private void getAllTaskList()
  {
    lvListAll.invalidateViews();
    adapter_TaskList adapter_taskList = new adapter_TaskList(activity, context, adapter_AllTaskList.getInstance().getAlltaskList());
    lvListAll.setAdapter(adapter_taskList);

    TaskListData taskListData = (TaskListData) adapter_taskList.getItem(adapter_taskList.getCount() - 1);
    iTotalTask = adapter_taskList.getCount();

    AllFunction.storeToSharedPref(context, taskListData.getSyncDate(), Preference.prefSyncDate);
    AllFunction.storeToSharedPref(context, taskListData.getSyncTime(), Preference.prefSyncTime);
  }
}
