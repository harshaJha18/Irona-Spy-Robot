package com.irona.spyrobotrpi;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
public class MainActivity extends ActionBarActivity {
private String str;
public static final String TAG = "Irona";
public static String URL = "http://192.168.0.150";
public static String postReceiverUrl = URL+"/php2.php";
private PostDataAsyncTask postDataAsyncTask;
Button fwd_btn,bwd_btn,lft_btn,rgt_btn,btndist;
private Button btnSpeak;
private final int REQ_CODE_SPEECH_INPUT = 100;
private TextView txtSpeechInput,txtdist;
int btnflag=0;
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_main);
Log.d(TAG, "onCreate Function");
fwd_btn = (Button) findViewById(R.id.fwd_button);
bwd_btn = (Button) findViewById(R.id.bwd_button);
lft_btn = (Button) findViewById(R.id.left_button);
rgt_btn = (Button) findViewById(R.id.right_button);
btnSpeak = (Button) findViewById(R.id.btnSpeak);
btndist = (Button) findViewById(R.id.button_dist);
txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
txtdist = (TextView) findViewById(R.id.tvdist);
WebView browser = (WebView) findViewById(R.id.webView);
browser.setWebViewClient(new WebViewClient());
browser.loadUrl(URL+":8081");
browser.getSettings().setUseWideViewPort(true);
browser.getSettings().setLoadWithOverviewMode(true);
fwd_btn.setOnTouchListener(new View.OnTouchListener() {
@Override
public boolean onTouch(View v, MotionEvent event) {
if (event.getAction() == MotionEvent.ACTION_DOWN) {
btnflag=0;
Log.d(TAG, "Forward Button Pressed");
postDataAsyncTask = new PostDataAsyncTask();
postDataAsyncTask.execute("[FWD]");
} else if (event.getAction() == MotionEvent.ACTION_UP ||
event.getAction() == MotionEvent.ACTION_CANCEL) {
btnflag=0;
Log.d(TAG, "Forward Button Released");
postDataAsyncTask = new PostDataAsyncTask();
postDataAsyncTask.execute("[STP]");
}
return false;
}
});
bwd_btn.setOnTouchListener(new View.OnTouchListener() {
@Override
public boolean onTouch(View v, MotionEvent event) {
if (event.getAction() == MotionEvent.ACTION_DOWN) {
btnflag=0;
Log.d(TAG, "Backward Button Pressed");
postDataAsyncTask = new PostDataAsyncTask();
postDataAsyncTask.execute("[BWD]");
} else if (event.getAction() == MotionEvent.ACTION_UP ||
event.getAction() == MotionEvent.ACTION_CANCEL) {
btnflag=0;
Log.d(TAG, "Backward Button Released");
postDataAsyncTask = new PostDataAsyncTask();
postDataAsyncTask.execute("[STP]");
}
return false;
}
});
btndist.setOnTouchListener(new View.OnTouchListener() {
@Override
public boolean onTouch(View v, MotionEvent event) {
if (event.getAction() == MotionEvent.ACTION_DOWN) {
btnflag=1;
Log.d(TAG, "Distance Button Pressed");
postDataAsyncTask = new PostDataAsyncTask();
postDataAsyncTask.execute("[DIST]");
}
return false;
}
});
lft_btn.setOnTouchListener(new View.OnTouchListener() {
@Override
public boolean onTouch(View v, MotionEvent event) {
if (event.getAction() == MotionEvent.ACTION_DOWN) {
btnflag=0;
Log.d(TAG, "Left Button Pressed");
postDataAsyncTask = new PostDataAsyncTask();
postDataAsyncTask.execute("[LFT]");
} else if (event.getAction() == MotionEvent.ACTION_UP ||
event.getAction() == MotionEvent.ACTION_CANCEL) {
btnflag=0;
Log.d(TAG, "Left Button Released");
postDataAsyncTask = new PostDataAsyncTask();
postDataAsyncTask.execute("[STP]");
}
return false;
}
});
postDataAsyncTask = new PostDataAsyncTask();
postDataAsyncTask.execute("[DIST]");
}
return false;
}
});
lft_btn.setOnTouchListener(new View.OnTouchListener() {
@Override
public boolean onTouch(View v, MotionEvent event) {
if (event.getAction() == MotionEvent.ACTION_DOWN) {
btnflag=0;
Log.d(TAG, "Left Button Pressed");
postDataAsyncTask = new PostDataAsyncTask();
postDataAsyncTask.execute("[LFT]");
} else if (event.getAction() == MotionEvent.ACTION_UP ||
event.getAction() == MotionEvent.ACTION_CANCEL) {
btnflag=0;
Log.d(TAG, "Left Button Released");
postDataAsyncTask = new PostDataAsyncTask();
postDataAsyncTask.execute("[STP]");
}
return false;
}
});
rgt_btn.setOnTouchListener(new View.OnTouchListener() {
@Override
public boolean onTouch(View v, MotionEvent event) {
if (event.getAction() == MotionEvent.ACTION_DOWN) {
btnflag=0;
Log.d(TAG, "Right Button Pressed");
postDataAsyncTask = new PostDataAsyncTask();
postDataAsyncTask.execute("[RGT]");
} else if (event.getAction() == MotionEvent.ACTION_UP ||
event.getAction() == MotionEvent.ACTION_CANCEL) {
btnflag=0;
Log.d(TAG, "Right Button Released");
postDataAsyncTask = new PostDataAsyncTask();
postDataAsyncTask.execute("[STP]");
}
return false;
}
});
btnSpeak.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
promptSpeechInput();
}
});
}
/**
* Showing google speech input dialog
* */
private void promptSpeechInput() {
Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
getString(R.string.speech_prompt));
try {
startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
} catch (ActivityNotFoundException a) {
Toast.makeText(getApplicationContext(),
getString(R.string.speech_not_supported),
Toast.LENGTH_SHORT).show();
}
}
/**
* Receiving speech input
* */
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
super.onActivityResult(requestCode, resultCode, data);
switch (requestCode) {
case REQ_CODE_SPEECH_INPUT: {
if (resultCode == RESULT_OK && null != data) {
ArrayList<String> result = data
.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
str = result.get(0);
txtSpeechInput.setText(str);
if(str.contains("forward") || str.contains("up"))
{
btnflag=0;
Toast.makeText(getApplicationContext(),"Forward...Ok",Toast.LENGTH_SHORT).show();
postDataAsyncTask = new PostDataAsyncTask();
postDataAsyncTask.execute("[FWD]");
}
else if(str.contains("backward") || str.contains("back") || str.contains("down"))
{
btnflag=0;
Toast.makeText(getApplicationContext(),"Backward...Ok",Toast.LENGTH_SHORT).show();
postDataAsyncTask = new PostDataAsyncTask();
postDataAsyncTask.execute("[BWD]");
}
else if(str.contains("left") || str.contains("Left"))
{
btnflag=0;
Toast.makeText(getApplicationContext(),"Left...Ok",Toast.LENGTH_SHORT).show();
postDataAsyncTask = new PostDataAsyncTask();
postDataAsyncTask.execute("[LFT]");
}
else if(str.contains("right") || str.contains("Right"))
{
btnflag=0;
Toast.makeText(getApplicationContext(),"Right...Ok",Toast.LENGTH_SHORT).show();
postDataAsyncTask = new PostDataAsyncTask();
postDataAsyncTask.execute("[RGT]");
}
else if(str.contains("stop") || str.contains("stop"))
{
btnflag=0;
Toast.makeText(getApplicationContext(),"Stop...Ok",Toast.LENGTH_SHORT).show();
postDataAsyncTask = new PostDataAsyncTask();
postDataAsyncTask.execute("[STP]");
}
}
break;
}
}
}
@Override
public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
getMenuInflater().inflate(R.menu.menu_main, menu);
return true;
}
@Override
public boolean onOptionsItemSelected(MenuItem item) {
// Handle action bar item clicks here. The action bar will
// automatically handle clicks on the Home/Up button, so long
// as you specify a parent activity in AndroidManifest.xml.
int id = item.getItemId();
//noinspection SimplifiableIfStatement
if (id == R.id.action_settings) {
return true;
}
return super.onOptionsItemSelected(item);
}
****************
private class PostDataAsyncTask extends AsyncTask<String, String, String> {
protected void onPreExecute() {
super.onPreExecute();
// do stuff before posting data
}
@Override
protected String doInBackground(String... strings) {
try {
// post a text data
postText(strings[0]);
} catch (NullPointerException e) {
e.printStackTrace();
} catch (Exception e) {
e.printStackTrace();
}
return null;
}
@Override
protected void onPostExecute(String lenghtOfFile) {
// do stuff after posting data
}
}
//*********************************************************************************************
private void postText(String str){
try{
Log.d(TAG, "postURL: " + postReceiverUrl);
List nameValuePairs = new ArrayList();
// HttpClient
HttpClient httpClient = new DefaultHttpClient();
// post header
HttpPost httpPost = new HttpPost(postReceiverUrl);
nameValuePairs.add(new BasicNameValuePair("action", str));
httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
// execute HTTP post request
HttpResponse response = httpClient.execute(httpPost);
HttpEntity resEntity = response.getEntity();
if (resEntity != null) {
String responseStr = EntityUtils.toString(resEntity).trim();
Log.d(TAG, "Response: " + responseStr);
if(btnflag==1)
{
btnflag=0;
txtdist.setText(responseStr.toString());
}
// you can add an if statement here and do other actions based on the response
}
} catch (ClientProtocolException e) {
e.printStackTrace();
} catch (IOException e) {
e.printStackTrace();
}
}
}
