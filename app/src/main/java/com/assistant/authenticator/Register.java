package com.assistant.authenticator;

import java.util.Locale;

import com.assistant.authenticator.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends Activity {
	
	QandA qa[]=new QandA[20];
	EditText et[]=new EditText[20];
	TextView tv[]=new TextView[20];
	EditText uname;
	Button btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		et[0]=(EditText)findViewById(R.id.et1);
		et[1]=(EditText)findViewById(R.id.et2);
		et[2]=(EditText)findViewById(R.id.et3);
		et[3]=(EditText)findViewById(R.id.et4);
		et[4]=(EditText)findViewById(R.id.et5);
		et[5]=(EditText)findViewById(R.id.et6);
		et[6]=(EditText)findViewById(R.id.et7);
		et[7]=(EditText)findViewById(R.id.et8);
		et[8]=(EditText)findViewById(R.id.et9);
		et[9]=(EditText)findViewById(R.id.et10);
		et[10]=(EditText)findViewById(R.id.et11);
		et[11]=(EditText)findViewById(R.id.et12);
		et[12]=(EditText)findViewById(R.id.et13);
		et[13]=(EditText)findViewById(R.id.et14);
		et[14]=(EditText)findViewById(R.id.et15);
		et[15]=(EditText)findViewById(R.id.et16);
		et[16]=(EditText)findViewById(R.id.et17);
		et[17]=(EditText)findViewById(R.id.et18);
		et[18]=(EditText)findViewById(R.id.et19);
		et[19]=(EditText)findViewById(R.id.et20);
		uname=(EditText)findViewById(R.id.et0);
		et[0].setText("pass");
		et[1].setText("pass");
		et[2].setText("pass");
		et[3].setText("pass");
		et[4].setText("pass");
		et[5].setText("pass");
		et[6].setText("pass");
		et[7].setText("pass");
		et[8].setText("pass");
		et[9].setText("pass");
		et[10].setText("pass");
		et[11].setText("pass");
		et[12].setText("pass");
		et[13].setText("pass");
		et[14].setText("pass");
		et[15].setText("pass");
		et[16].setText("pass");
		et[17].setText("pass");
		et[18].setText("pass");
		et[19].setText("pass");

		btn=(Button)findViewById(R.id.btn1);
		tv[0]=(TextView)findViewById(R.id.tv1);
		tv[1]=(TextView)findViewById(R.id.tv2);
		tv[2]=(TextView)findViewById(R.id.tv3);
		tv[3]=(TextView)findViewById(R.id.tv4);
		tv[4]=(TextView)findViewById(R.id.tv5);
		tv[5]=(TextView)findViewById(R.id.tv6);
		tv[6]=(TextView)findViewById(R.id.tv7);
		tv[7]=(TextView)findViewById(R.id.tv8);
		tv[8]=(TextView)findViewById(R.id.tv9);
		tv[9]=(TextView)findViewById(R.id.tv10);
		tv[10]=(TextView)findViewById(R.id.tv11);
		tv[11]=(TextView)findViewById(R.id.tv12);
		tv[12]=(TextView)findViewById(R.id.tv13);
		tv[13]=(TextView)findViewById(R.id.tv14);
		tv[14]=(TextView)findViewById(R.id.tv15);
		tv[15]=(TextView)findViewById(R.id.tv16);
		tv[16]=(TextView)findViewById(R.id.tv17);
		tv[17]=(TextView)findViewById(R.id.tv18);
		tv[18]=(TextView)findViewById(R.id.tv19);
		tv[19]=(TextView)findViewById(R.id.tv20);
		final DBase db=new DBase(getApplicationContext());
		btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
            	//Toast.makeText(getApplicationContext(),db.checkUser(uname.getText().toString())+"", Toast.LENGTH_LONG).show();
            	String usr=uname.getText().toString().toLowerCase(Locale.ENGLISH);
            	if(!db.checkUser(usr)){
            		db.createUser(new User(usr)); 
            		for(int i=0;i<20;i++){
                		qa[i]=new QandA(usr,i+1,tv[i].getText().toString().toLowerCase(Locale.ENGLISH),
                							et[i].getText().toString().toLowerCase(Locale.ENGLISH));
                		db.createQandA(qa[i]);
                	}
            		db.closeDB();
                	finish();
            	}
            	else{
            		Toast.makeText(getApplicationContext(), "User with same username already exists!",Toast.LENGTH_LONG).show();
            	}
            	
            	
            }
        });
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_register, menu);
		return true;
	}

}
