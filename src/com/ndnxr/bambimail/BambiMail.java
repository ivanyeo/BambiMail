package com.ndnxr.bambimail;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.ndnxr.bambi.BambiLib;
import com.ndnxr.bambi.BambiLib.TASK_TYPE;
import com.ndnxr.bambi.BambiLib.URGENCY;
import com.ndnxr.bambi.Email;
import com.ndnxr.bambi.Task;
import com.ndnxr.bambi.mailtest.R;

public class BambiMail extends ActionBarActivity implements
		DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		// Create Spinner items
		Spinner spinner = (Spinner) findViewById(R.id.urgencySpinner);
		List<String> list = new ArrayList<String>();
		list.add("Normal");
		list.add("Schedule");
		list.add("Urgent");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void send_email(View v) {
		// Create email with attachment
		String attachment = null;
		String filename = "smurf2.png";

		if (this.getFileStreamPath(filename).exists()) {
			attachment = this.getFileStreamPath(filename).getAbsolutePath();
		}

		Email email = new Email("cs246rocks@gmail.com", "cs202rocks",
				"smtp.gmail.com", "465", "woot", "subject here", "message",
				new String[] { "cs246rocks@gmail.com" },
				new String[] { attachment });

		// Create Deadline
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MINUTE, 1);

		Date deadline = calendar.getTime();

		// Create a task
		Task task = new Task(TASK_TYPE.EMAIL, URGENCY.SCHEDULE, deadline, email);

		// Create instance of BambiLib
		BambiLib bambiLib = new BambiLib(this);

		// Send email
		boolean output = bambiLib.sendEmail(task);

		// Shutdown Lib
		bambiLib.shutdown();

		G.Log("send_email() DONE!");
	}

	public void schedule_email(View v) {
		// Intent intent = new Intent(this, ScheduleActivity.class);
		// startActivity(intent);
	}

	public void showTimePickerDialog(View v) {
		DialogFragment newFragment = new TimePickerFragment();
		newFragment.show(getSupportFragmentManager(), "timePicker");
	}

	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getSupportFragmentManager(), "datePicker");
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		// Local Variable
		String month = "";

		// Set button's date
		Button dateButton = (Button) this.findViewById(R.id.dateButton);

		// Get month
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] months = dfs.getMonths();
		month = months[monthOfYear];

		dateButton.setText(String.format("%s %d, %d", month, dayOfMonth, year));
	}

	@Override
	public void onTimeSet(TimePicker arg0, int hourOfDay, int minute) {
		// Set button's date
		Button timeButton = (Button) this.findViewById(R.id.timeButton);
		
		timeButton.setText(String.format("%d:%d", hourOfDay, minute));
	}

}
