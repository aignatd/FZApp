package com.fz.fzapp.popup;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.fz.fzapp.R;
import com.fz.fzapp.adapter.adapter_Database;
import com.fz.fzapp.service.AllFunction;
import com.fz.fzapp.utils.PopupMessege;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Dibuat oleh : ignat
 * Tanggal : 08-Dec-16
 * HP/WA : 0857 7070 6 777
 */
public class ReasonStatus extends Dialog
{
  @BindView(R.id.etReason)
  EditText etReason;
  @BindView(R.id.tvTitleReason)
  TextView tvTitleReason;

  private String TAG = "[Reason]";
  private ProgressDialog progressDialog;
  private PopupMessege pesan = new PopupMessege();
  List<EditText> lstInput = new ArrayList<>();
  List<String> lstMsg = new ArrayList<>();
  private List<String> lstLogin = new ArrayList<>();

  private Activity ParentAct;
  private Context localcontext;
  private String strReason;
  private String strNotes;
  private int lTaskID;
  private int iLocationID;
  private int iUserID;

  private int viewShow;

  public ReasonStatus(Activity parentAct, Context context, int UserID, int TaskID, int LocationID, String Reason, int ViewShow)
  {
    super(parentAct);
    this.ParentAct = parentAct;
    this.localcontext = context;
    this.iUserID = UserID;
    this.lTaskID = TaskID;
    this.iLocationID = LocationID;
    this.strReason = Reason;
    this.viewShow = ViewShow;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.reasonstatus_lay);
    ButterKnife.bind(this);

    etReason.setVisibility(viewShow);
    tvTitleReason.setText(strReason);
  }

  @OnClick({R.id.btnSubmitReason, R.id.btnCancelReason})
  public void onViewClicked(View view)
  {
    switch(view.getId())
    {
      case R.id.btnSubmitReason:
        lstInput.clear();
        lstMsg.clear();
        lstInput.add(etReason);
        lstMsg.add(ParentAct.getResources().getString(R.string.strReason));

        if(etReason.getVisibility() == View.VISIBLE)
        {
          if(AllFunction.InputCheck(lstInput, lstMsg, getContext()))
          {
            strNotes = etReason.getText().toString().trim();
            SaveToSQLite();
          }
        }
        else
        {
          strNotes = ParentAct.getResources().getString(R.string.strAccurateTime);
          SaveToSQLite();
        }
      break;
      case R.id.btnCancelReason:
        cancel();
      break;
    }
  }

  private void SaveToSQLite()
  {
    HashMap<String, String> hashMap = new HashMap<>();
    hashMap.clear();

    hashMap.put("lnkUserID", String.valueOf(iUserID));
    hashMap.put("lnkTaskID", String.valueOf(lTaskID));
    hashMap.put("lnkLocationID", String.valueOf(iLocationID));
    hashMap.put("Notes", strNotes);

    adapter_Database adapter_database = new adapter_Database(localcontext);
    if(adapter_database.SaveReportData(hashMap)) dismiss();
  }
}

