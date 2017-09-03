package com.fz.fzapp.common;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.fz.fzapp.R;
import com.fz.fzapp.adapter.AllTaskList_adapter;
import com.fz.fzapp.adapter.TaskList_adapter;
import com.fz.fzapp.data.TaskListData;
import com.fz.fzapp.popup.OtherOption;
import com.fz.fzapp.service.AllFunction;
import com.fz.fzapp.service.UploadData;
import com.fz.fzapp.utils.Preference;

import java.util.HashMap;

public class Planning extends AppCompatActivity
{
  @BindView(R.id.ivOtherMenuPlan)
  ImageView ivOtherMenuPlan;
  @BindView(R.id.lvListAll)
  ListView lvListAll;
  @BindView(R.id.tvPlanning1)
  TextView tvPlanning1;
  @BindView(R.id.tvPlanning2)
  TextView tvPlanning2;
  @BindView(R.id.btnStartTask)
  Button btnStartTask;
  @BindView(R.id.btnEndTask)
  Button btnEndTask;

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

  @OnClick({R.id.ivOtherMenuPlan, R.id.btnStartTask, R.id.btnEndTask})
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
      case R.id.btnEndTask:
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
          .setTitle(R.string.titleMessege)
          .setMessage(context.getResources().getString(R.string.msgContinueSync))
          .setIcon(android.R.drawable.ic_dialog_alert)
          .setCancelable(false)
          .setPositiveButton(R.string.strBtnOK, new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface dialog, int which)
            {
              onProcessSyncData();
            }
          })
          .setNegativeButton(R.string.strBtnCancel, new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface dialog, int id)
            {
              dialog.cancel();
            }
          });

        AlertDialog alert = builder.create();
        alert.show();
      break;
    }
  }

  private void getAllTaskList()
  {
    lvListAll.invalidateViews();
    TaskList_adapter taskList_adapter = new TaskList_adapter(activity, context, AllTaskList_adapter.getInstance().getAlltaskList());
    lvListAll.setAdapter(taskList_adapter);

    TaskListData taskListData = (TaskListData) taskList_adapter.getItem(taskList_adapter.getCount() - 1);
    iTotalTask = taskList_adapter.getCount();

    AllFunction.storeToSharedPref(context, taskListData.getSyncDate(), Preference.prefSyncDate);
    AllFunction.storeToSharedPref(context, taskListData.getSyncTime(), Preference.prefSyncTime);

    if(AllFunction.getIntFromSharedPref(context, Preference.prefDutyTask) > 1)
    {
      tvPlanning1.setText(context.getResources().getString(R.string.titlePlan3));
      tvPlanning2.setText(context.getResources().getString(R.string.titlePlan4));
    }
    else
      tvPlanning2.setText(context.getResources().getString(R.string.titlePlan2));

    if(AllFunction.getIntFromSharedPref(context, Preference.prefDutyTask) > 1)
      btnEndTask.setVisibility(View.VISIBLE);

    if(AllFunction.getIntFromSharedPref(context, Preference.prefDutyTask) > iTotalTask)
      btnStartTask.setVisibility(View.GONE);
  }

  private void onProcessSyncData()
  {
    HashMap<String, String> listSyncTable = new HashMap<>();
    listSyncTable.clear();

    listSyncTable.put("Tracking", "tracking");
    listSyncTable.put("TaskList", "trxtasklist");

    new UploadData(activity, context, listSyncTable).execute();
  }
}
