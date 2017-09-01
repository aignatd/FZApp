package com.fz.fzapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.fz.fzapp.R;
import com.fz.fzapp.data.TaskListData;
import com.fz.fzapp.model.TaskListResponse;

import java.util.List;

/**
 * Dibuat oleh : ignat
 * Tanggal : 20-Jul-17
 * HP/WA : 0857 7070 6 777
 */
public class adapter_AllTaskList
{
  private List<TaskListResponse> alltaskList;

  public List<TaskListResponse> getAlltaskList()
  {
    return alltaskList;
  }

  public void setAlltaskList(List<TaskListResponse> alltaskList)
  {
    this.alltaskList = alltaskList;
  }

  private static adapter_AllTaskList UserInstance = new adapter_AllTaskList();

  public static adapter_AllTaskList getInstance()
  {
    return UserInstance;
  }

  private adapter_AllTaskList()
  {
  }

  public static void initAllTaskList()
  {
    UserInstance = new adapter_AllTaskList();
  }
}
