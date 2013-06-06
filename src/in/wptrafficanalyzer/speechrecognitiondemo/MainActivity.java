package in.wptrafficanalyzer.speechrecognitiondemo;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import android.app.Activity;
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
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	@SuppressWarnings("unused")
	private SpeechRecognizer speechRecognizer; 
	private final int SPEECHTOTEXT = 1;
		
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
	         
	                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, text);
	                
	                lvText.setAdapter(adapter);	                
	                
	            }
	            break;
	    }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }   
}
