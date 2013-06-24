package in.wptrafficanalyzer.speechrecognitiondemo;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

//import org.ndeftools.boilerplate.AndroidNfcActivity;
//import org.ndeftools.boilerplate.DefaultNfcReaderActivity;

import com.google.gson.Gson;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	
	private static final String TAG = "Speak";
	
	public static String channel;
	

	@SuppressWarnings("unused")
	private SpeechRecognizer speechRecognizer; 
	private final int SPEECHTOTEXT = 1;
	
	
	Pubnub pubnub = new Pubnub("demo", "demo", "", false);
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Getting an instance of SpeechRecognizerduration
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getBaseContext());
        
        // Setting Speech Recognition Listener
        //speechRecognizer.setRecognitionListener(this);
        
       
        // Getting an instance of btn_speak
        Button btnSpeak = (Button) findViewById(R.id.btn_speak);
        
        // Defining button click listener
        OnClickListener onClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				
				// Getting an instance of PackageManager
				PackageManager pm = getPackageManager();
				
				// Querying Package Manager
				List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);
				
				if(activities.size()<=0){
					Toast.makeText(getBaseContext(), 
							"No Activity found to handle the action ACTION_RECOGNIZE_SPEECH", 
							Toast.LENGTH_SHORT).show();
					return;
				}
				
				
				
		        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
		                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice recognition Demo...");
		        startActivityForResult(intent, SPEECHTOTEXT);
			}
		};
		
		//Setting a click event handler for the button
		btnSpeak.setOnClickListener(onClickListener);
		
        
    }
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        //EditText etText = (EditText) findViewById(R.id.et_text);
        ListView lvText = (ListView) findViewById(R.id.lv_text);
        
        switch (requestCode) {
	        case SPEECHTOTEXT: 
	            if (resultCode == RESULT_OK && null != data) {	 
	                ArrayList<String> text = data
	                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);	
	                
	                Gson gson = new Gson();
	                String jsonNames = gson.toJson(text);
	                Log.i("JSON CMD : ", jsonNames);
	                String arraysize = String.valueOf(text.size());
	                Log.i("JSON SIZE : ", arraysize);
	                
	                //channel = "Robert2";
	                if (channel != null){
	                	Log.i("Channel: ",  channel);
	                }
	         
	                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, text);
	                
	                lvText.setAdapter(adapter);	   
	                
	                //Log.i("JSON GET First", text.get(0));
	                
	               /* List<String> list = new ArrayList<String>() {
	              	  {
	              		add("String 1");
	              		add("String 2");
	              		add("String 3");
	              	  }
	              	};*/
	                	     
	                if (channel != null){
	                	for(int k = 0; k < text.size(); k++){
	                		Log.i("JSON GET First", text.get(k));
	                	
	                		String result = String.valueOf(text.get(k).toString().compareTo("subscribe"));
	                		Log.i("JSON RESULT", result);
	                		                		
	                		if (text.get(k).toString().compareTo("subscribe") ==0){
	                			Log.i("JSON IN SUBSCRIBE", " ");
	                			subscribe(channel);
	                		}else{
	                			Log.i("JSON NO MATCH", " ");
	                		}

	                	}
	                }else{
	                	Log.i("Channel: ",  "No Channel");
	                }
	            }
	            break;
	    }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }   
    
    public void reader(View view) {
    	Log.d(TAG, "Show reader");
    	
    	Intent intent = new Intent(this, DefaultNfcReaderActivity.class);
    	startActivity(intent);
    }


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		Log.i(TAG,"Speech Pause");
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		Log.i(TAG,"Speech Resumed");
	}
    
	private void notifyUser(Object message) {
		try {
			if (message instanceof JSONObject) {
				final JSONObject obj = (JSONObject) message;
				this.runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(getApplicationContext(), obj.toString(),
								Toast.LENGTH_LONG).show();

						Log.i("Received msg : ", String.valueOf(obj));
					}
				});

			} else if (message instanceof String) {
				final String obj = (String) message;
				this.runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(getApplicationContext(), obj,
								Toast.LENGTH_LONG).show();
						Log.i("Received msg : ", obj.toString());
					}
				});

			} else if (message instanceof JSONArray) {
				final JSONArray obj = (JSONArray) message;
				this.runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(getApplicationContext(), obj.toString(),
								Toast.LENGTH_LONG).show();
						Log.i("Received msg : ", obj.toString());
					}
				});
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void subscribe(String inChannel) {
//		
		
		Hashtable<String, String> args = new Hashtable<String, String>(1);

		//String message = input.getText().toString();
		args.put("channel", inChannel);

		try {
			pubnub.subscribe(args, new Callback() {
				public void connectCallback(String channel,
						Object message) {
					notifyUser("SUBSCRIBE : CONNECT on channel:"
							+ channel
							+ " : "
							+ message.getClass()
							+ " : "
							+ message.toString());
				}

				public void disconnectCallback(String channel,
						Object message) {
					notifyUser("SUBSCRIBE : DISCONNECT on channel:"
							+ channel
							+ " : "
							+ message.getClass()
							+ " : "
							+ message.toString());
				}

				@Override
				public void reconnectCallback(String channel,
						Object message) {
					notifyUser("SUBSCRIBE : RECONNECT on channel:"
							+ channel
							+ " : "
							+ message.getClass()
							+ " : "
							+ message.toString());
				}

				public void successCallback(String channel,
						Object message) {
					notifyUser("SUBSCRIBE : " + channel + " : "
							+ message.getClass() + " : "
							+ message.toString());
				}

				public void errorCallback(String channel,
						Object message) {
					notifyUser("SUBSCRIBE : ERROR on channel "
							+ channel + " : "
							+ message.getClass() + " : "
							+ message.toString());
				}
			});

		} catch (Exception e) {

		}
	}

}
    

