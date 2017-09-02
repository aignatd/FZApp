package com.fz.fzapp.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.fz.fzapp.R;
import com.fz.fzapp.adapter.adapter_AllTaskList;
import com.fz.fzapp.service.AllFunction;
import com.fz.fzapp.utils.PopupMessege;
import com.fz.fzapp.utils.Preference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Duty extends AppCompatActivity
{
    @BindView(R.id.btnDestinyTask)
    Button btnDestinyTask;
    @BindView(R.id.tvEstTime)
    TextView tvEstTime;
    @BindView(R.id.tvActTime)
    TextView tvActTime;
    @BindView(R.id.tvCountTime)
    TextView tvCountTime;

    private PopupMessege popupMessege = new PopupMessege();
    static ProgressDialog progressDialog;

    private Activity activity = this;
    static String TAG = "[DutyData]";
    private Context context = this;
    private Integer intDutyTask;
    private CountDownClass CountingTimer;

    private SimpleDateFormat tf;
    private Calendar calendar;
    String strEstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.duty_lay);
        ButterKnife.bind(this);

        StartDutyCheck();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private class CountDownClass extends CountDownTimer
    {
        public CountDownClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long millis = millisUntilFinished;
            String hms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)));
            tvCountTime.setText(hms);
        }

        @Override
        public void onFinish()
        {
            tvCountTime.setText("00:00");
        }
    }

    private void StartDutyCheck()
    {
        intDutyTask = AllFunction.getIntFromSharedPref(context, Preference.prefDutyTask);

        strEstTime = adapter_AllTaskList.getInstance().getAlltaskList().get(intDutyTask - 1).getEstimateTime();
        btnDestinyTask.setText(adapter_AllTaskList.getInstance().getAlltaskList().get(intDutyTask - 1).getDisplayName());
        tvEstTime.setText(context.getString(R.string.strStartTask, strEstTime));
        tvActTime.setText(context.getString(R.string.strStartFinish, ""));

        calendar = Calendar.getInstance();
        tf = new SimpleDateFormat("HH:mm:ss", Locale.US);
        Date StartTime = null;
        Date EndTime = null;
        long countdown = 0;

        try
        {
            StartTime = tf.parse(tf.format(calendar.getTime()));
            EndTime = tf.parse(strEstTime);
            countdown = EndTime.getTime() - StartTime.getTime();
        }
        catch (ParseException e)
        {
            //e.printStackTrace();
        }

        int days = (int) (countdown / (1000*60*60*24));
        int hours = (int) ((countdown - (1000*60*60*24*days)) / (1000*60*60));
        int min = (int) (countdown - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);

        Log.d(TAG, "onCreate: 1 " + countdown);
        Log.d(TAG, "onCreate: 2 " + hours + " " + min);
        CountingTimer = new CountDownClass(countdown, 1000);
        CountingTimer.start();
    }
}
