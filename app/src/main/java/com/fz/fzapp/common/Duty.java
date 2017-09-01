package com.fz.fzapp.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
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
import com.fz.fzapp.utils.PopupMessege;
import com.fz.fzapp.utils.Preference;

import java.util.ArrayList;
import java.util.List;

public class Duty extends AppCompatActivity
{
  private PopupMessege popupMessege = new PopupMessege();
  static ProgressDialog progressDialog;

  private Activity activity = this;
  static String TAG = "[SinkronData]";
  private Context context = this;
  List<EditText> lstInput = new ArrayList<>();
  List<String> lstMsg = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.duty_lay);
    ButterKnife.bind(this);
  }

  @Override
  public void onBackPressed()
  {
    moveTaskToBack(true);
  }
}
