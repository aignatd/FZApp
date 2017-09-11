package com.fz.fzapp.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.fz.fzapp.R;
import com.fz.fzapp.adapter.AllTaskList_adapter;
import com.fz.fzapp.adapter.Database_adapter;
import com.fz.fzapp.service.AllFunction;
import com.fz.fzapp.utils.PopupMessege;
import com.fz.fzapp.utils.Preference;
import com.fz.fzapp.utils.SaveToSQLite;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Duty extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
	GoogleApiClient.OnConnectionFailedListener, LocationListener
{
	@BindView(R.id.btnDestinyTask)
	Button btnDestinyTask;
	@BindView(R.id.tvEstTime)
	TextView tvEstTime;
	@BindView(R.id.tvActTime)
	TextView tvActTime;
	@BindView(R.id.tvCountTime)
	TextView tvCountTime;
	@BindView(R.id.tvTrackTest)
	TextView tvTrackTest;
	@BindView(R.id.tvCountTimeSecond)
	TextView tvCountTimeSecond;

	private PopupMessege popupMessege = new PopupMessege();
	static ProgressDialog progressDialog;

	private Activity activity = this;
	static String TAG = "[DutyData]";
	private Context context = this;
	private Integer intDutyTask;
	private CountDownClass CountingTimer;

	private SimpleDateFormat tf, df;
	private Calendar calendar;
	private String strEstTime;
	private Integer onDuty = 3;

	private Database_adapter database_adapter;
	private LocationRequest mLocationRequest;
	private GoogleApiClient mGoogleApiClient;
	private Location mLastLocation;
	private CountDownTimerToTrackLocation TimerToTrackLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.duty_lay);
		ButterKnife.bind(this);

		database_adapter = new Database_adapter(context);
		df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		tf = new SimpleDateFormat("HH:mm:ss", Locale.US);

		StartDutyCheck();

		if(checkPlayServices())
		{
			buildGoogleApiClient();
			createLocationRequest();
		}

		long TimeTrackLocation = AllFunction.getLongFromSharedPref(context, Preference.prefTimeTrack);
		TimerToTrackLocation = new CountDownTimerToTrackLocation(TimeTrackLocation, 1000);
		TimerToTrackLocation.cancel();
	}

	@Override
	public void onBackPressed()
	{
		moveTaskToBack(true);
	}

	@OnClick({R.id.btnDutyDone, R.id.btnDutyFail})
	public void onViewClicked(View view)
	{
		switch(view.getId())
		{
			case R.id.btnDutyFail:
				onDuty = 1;
			case R.id.btnDutyDone:
				calendar = Calendar.getInstance();
				tvActTime.setText(context.getString(R.string.strStartFinish, tf.format(calendar.getTime())));

				if(onDuty == 3)
				{
					SaveToSQLite saveToSQLite = new SaveToSQLite(context.getResources().getString(R.string.strOnSchedule), activity, context, onDuty);
					saveToSQLite.ProcessToSQLite();
				}
				else
				{
					Intent ReasonIntent = new Intent(Duty.this, Reason.class);
					ReasonIntent.putExtra("onDuty", onDuty);
					startActivity(ReasonIntent);
					finish();
				}
			break;
		}
	}

	private class CountDownClass extends CountDownTimer
	{
		public CountDownClass(long millisInFuture, long countDownInterval)
		{
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished)
		{
			long millis = millisUntilFinished;
			String hms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
				TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)));
			tvCountTime.setText(hms);


			hms = String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(millis) -
				TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
			tvCountTimeSecond.setText(hms);
		}

		@Override
		public void onFinish()
		{
			CountingTimer.cancel();
			onDuty = 2;
			tvCountTime.setText("00:00");
		}
	}

	private void StartDutyCheck()
	{
		intDutyTask = AllFunction.getIntFromSharedPref(context, Preference.prefDutyTask);

		strEstTime = AllTaskList_adapter.getInstance().getAlltaskList().get(intDutyTask - 1).getEstimateTime();
		btnDestinyTask.setText(AllTaskList_adapter.getInstance().getAlltaskList().get(intDutyTask - 1).getDisplayName());
		tvEstTime.setText(context.getString(R.string.strStartTask, strEstTime));
		tvActTime.setText(context.getString(R.string.strStartFinish, ""));

		calendar = Calendar.getInstance();
		Date StartTime = null;
		Date EndTime = null;
		long countdown = 0;

		try
		{
			StartTime = tf.parse(tf.format(calendar.getTime()));
			EndTime = tf.parse(strEstTime);
			countdown = EndTime.getTime() - StartTime.getTime();
		}
		catch(ParseException e)
		{
			//e.printStackTrace();
		}

		if(countdown > 0)
		{
			onDuty = 3;
			CountingTimer = new CountDownClass(countdown, 1000);
			CountingTimer.start();
		}
		else
		{
			onDuty = 2;
		}
	}

	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

		if (resultCode != ConnectionResult.SUCCESS)
		{
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
			{
				GooglePlayServicesUtil.getErrorDialog(resultCode, this, 1000).show();
			}
			else
			{
				Toast.makeText(getApplicationContext(), context.getResources().getString(R.string.strDeviceNotSupported),
					Toast.LENGTH_LONG).show();
				finish();
			}

			return false;
		}

		return true;
	}

	protected synchronized void buildGoogleApiClient()
	{
		mGoogleApiClient = new GoogleApiClient.Builder(this)
			.addConnectionCallbacks(this)
			.addOnConnectionFailedListener(this)
			.addApi(LocationServices.API)
			.build();
	}

	private class CountDownTimerToTrackLocation extends CountDownTimer
	{
		public CountDownTimerToTrackLocation(long millisInFuture, long countDownInterval)
		{
			super(millisInFuture, countDownInterval);
		}

		public void onTick(long l)
		{
			// Log.d(TAG, "seconds remaining -> " + l / 1000);
		}

		public void onFinish()
		{
			TimerToTrackLocation.cancel();

			try
			{
				mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
			}
			catch (SecurityException e)
			{
				Log.d(TAG, "onFinish:");
			}

			if (mLastLocation != null)
				SaveTrackToSQLite(String.valueOf(mLastLocation.getLatitude()), String.valueOf(mLastLocation.getLongitude()), null, null);

			TimerToTrackLocation.start();
		}
	}

	private void SaveTrackToSQLite(String Latitude, String Longitude, String Date, String Time)
	{
		String strDate;
		String strTime;

		if(Date == null)
		{
			strDate = df.format(calendar.getTime());
			strTime = tf.format(calendar.getTime());
		}
		else
		{
			strDate = Date;
			strTime = Time;
		}

		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.clear();

		hashMap.put("Latitude", Latitude);
		hashMap.put("Longitude", Longitude);
		hashMap.put("Date", strDate);
		hashMap.put("Time", strTime);

		database_adapter.SaveTrackingData(hashMap);
	}

	protected void createLocationRequest() {
		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(1000);
		mLocationRequest.setFastestInterval(1000);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setSmallestDisplacement(10);
	}

	@Override
	public void onConnected(@Nullable Bundle bundle)
	{
		startLocationUpdates();
		getCurrentLocation();
	}

	@Override
	public void onConnectionSuspended(int i) {
		mGoogleApiClient.connect();
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
		Toast.makeText(getApplicationContext(),
			context.getResources().getString(R.string.strConnectionFailed), Toast.LENGTH_LONG)
			.show();
	}

	@Override
	public void onLocationChanged(Location location)
	{
		mLastLocation = location;
		TimerToTrackLocation.cancel();
		tvTrackTest.setText("Lat : " + String.valueOf(mLastLocation.getLatitude()) + ", Long : " + String.valueOf(mLastLocation.getLongitude()));
		SaveTrackToSQLite(String.valueOf(mLastLocation.getLatitude()), String.valueOf(mLastLocation.getLongitude()), null, null);
		TimerToTrackLocation.start();
	}

	protected void startLocationUpdates()
	{
		try
		{
			LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
		}
		catch (SecurityException e)
		{
			Toast.makeText(getApplicationContext(),
				context.getResources().getString(R.string.strStartLocation), Toast.LENGTH_LONG)
				.show();
		}
	}

	private boolean getCurrentLocation()
	{
		try
		{
			mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
		}
		catch (SecurityException e)
		{
			Toast.makeText(getApplicationContext(), context.getResources().getString(R.string.strCurrentLocation),
				Toast.LENGTH_LONG).show();

			return false;
		}

		if(mLastLocation == null) return false;

		return true;
	}

	@Override
	protected void onStart() {
		super.onStart();

		if (mGoogleApiClient != null)
			mGoogleApiClient.connect();
	}

	@Override
	public void onResume() {
		super.onResume();

		if(!checkPlayServices())
		{
			if (mGoogleApiClient.isConnected())
				mGoogleApiClient.disconnect();
		}
	}
}
