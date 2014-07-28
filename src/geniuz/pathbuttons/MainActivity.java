package geniuz.pathbuttons;

import geniuz.myPathbutton.composerLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class MainActivity extends Activity {

	int PtoTop;
	int PtoLeft;
	boolean changed=false;
	static  CloudLed camera2=new CloudLed();
	composerLayout clayout;
	@Override
	public void onDestroy() {
		Log.i("miao3","onDestroy");	
		super.onDestroy();
		this.unregisterReceiver(ss);

	}
	@Override
	public void onRestart() {
		Log.i("miao3","onRestart");	
		super.onRestart();

	}
	@Override
	public void onResume() {
		Log.i("miao3","onResume");	
		super.onResume();

	}
	@Override
	public void onPause() {
		Log.i("miao3","onPause");
		//sendMessage(null);
		super.onPause();


	}
	@Override
	public void onStart() {

		super.onStart();
		Log.i("miao3","onRestart ");
		Intent intent=getIntent();
		if(intent.getIntExtra("finish",0)==22)
		{
			sendMessage(null);
		}

	}

	private void showToast( String message ){
		Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0); 

		toast.show();
	}
	public int volume_img;
	public int lighting_img;
	public int liuliang_img;
	public int wifi_img;
	public int blue_img;
	private boolean isEnabled[]=new boolean[5];
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("miao3","onCreate");
		setContentView(R.layout.activity_main);
		overridePendingTransition(0,0);
		//���ÿؼ�
		clayout=(composerLayout)findViewById(R.id.test);
		AudioManager aManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		boolean isMute = ( aManager.getStreamVolume(AudioManager.STREAM_RING)==0 &&(aManager.getRingerMode()==AudioManager.RINGER_MODE_SILENT) );
		boolean isNextMute = !isMute;
		if( isNextMute )
		{
			volume_img=R.drawable.composer_music;
			isEnabled[1]=true;
		}
		else
		{
			volume_img=R.drawable.composer_music_shut;		
			isEnabled[1]=false;

		}


		if(camera2.getIsOn())
		{
			lighting_img=R.drawable.composer_lighting;
			isEnabled[2]=true;
			lighting_set();
			System.exit(0);
			//return ;
		}	
		else {
			lighting_img=R.drawable.composer_lighting_shut;
			isEnabled[2]=false;
		}
		

		WifiManager wifiManager = (WifiManager)(this.getSystemService(Context.WIFI_SERVICE));

		if(wifiManager != null){

			if(wifiManager.getWifiState()==WifiManager.WIFI_STATE_ENABLED||(wifiManager.getWifiState()==WifiManager.WIFI_STATE_ENABLING))
			{
				wifi_img=R.drawable.composer_wifi;
				isEnabled[0]=true;

			}
			else {
				wifi_img=R.drawable.composer_wifi_shut;
				isEnabled[0]=false;

			}

		}


		ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(  
				Context.CONNECTIVITY_SERVICE);  

		Class<?> cmClass = connectivityManager.getClass();  
		Class<?>[] argClasses = new Class[1];  
		argClasses[0] = boolean.class;  

		// ����ConnectivityManager��hide�ķ���setMobileDataEnabled�����Կ����͹ر�GP
		Method method = null;

		try {
			method = cmClass.getMethod("getMobileDataEnabled");
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if(method.invoke(connectivityManager).toString().equals(new String("true")))
			{
				liuliang_img=R.drawable.composer_liuliang;  

				isEnabled[3]=true;
			}
			else {
				liuliang_img=R.drawable.composer_liuliang_shut; 
				isEnabled[3]=false;
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		int blue_state=mBluetoothAdapter.getState();
		if(blue_state==BluetoothAdapter.STATE_OFF||blue_state==BluetoothAdapter.STATE_TURNING_OFF)
		{
			blue_img=R.drawable.blue_shut;
			isEnabled[4]=false;

		}
		else {
			blue_img=R.drawable.blue;
			isEnabled[4]=true;

		}





		clayout.init(
				new int[]{
						wifi_img,
						volume_img,
						lighting_img,
						blue_img,
						liuliang_img,


				}, 
				R.drawable.quan, 
				R.drawable.cha, 
				composerLayout.LEFTBOTTOM ,
				180, 
				300);

		if(!isEnabled[1])
			clayout.findViewById(101).setAlpha((float) 0.8);	
		if(!isEnabled[0])
			clayout.findViewById(100).setAlpha((float) 0.8);
		if(!isEnabled[3])	
			clayout.findViewById(104).setAlpha((float) 0.8);
		if(!isEnabled[2])	
			clayout.findViewById(102).setAlpha((float) 0.8);
		if(!isEnabled[4])	
			clayout.findViewById(103).setAlpha((float) 0.8);

		//�ӂ��c���O ��100+0����composer_camera��100+1����composer_music�������������Ў׶������o�ͼӎ׶�����
		OnLongClickListener clicklong=new OnLongClickListener(){
			@Override
			public boolean onLongClick(View v) {
				wifi_toggle();
				// TODO Auto-generated method stub

				return true;
			}

		};
		OnLongClickListener clicklong_blue=new OnLongClickListener(){
			@Override
			public boolean onLongClick(View v) {
				blue_toggle();
				// TODO Auto-generated method stub

				return true;
			}

		};
		OnClickListener clickit=new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(v.getId()==100+0){
					System.out.println("composer_camera");
					open_wifi();

				}else if(v.getId()==100+1){
					System.out.println("composer_music");
					volume_set(true);
				}else if(v.getId()==100+2){
					System.out.println("composer_place");
					lighting_set();
				}else if(v.getId()==100+3){
					System.out.println("composer_sleep");
					blue_set();


				}else if(v.getId()==100+4){
					System.out.println("composer_thought");
					liuliang_set();
				}
			}
		};
		clayout.findViewById(100).setLongClickable(true);
		clayout.findViewById(100).setOnLongClickListener(clicklong);
		clayout.findViewById(103).setOnLongClickListener(clicklong_blue);

		clayout.setButtonsOnClickListener(clickit);
		//clayout.setOnLongClickListener(clicklong);

		//�����؎׾伃��{���yԇ�¸��ؼ��c���c�������H�Æ��r�����ȥ����
		//		RelativeLayout rl=(RelativeLayout)findViewById(R.id.rlparent);
		//		rl.setOnClickListener(new OnClickListener() {			
		//			@Override
		//			public void onClick(View v) {
		//				// TODO Auto-generated method stub
		//				System.out.println("���ؼ������c���ͼ�ϵ�����؅���");
		//				
		//			}
		//		});


		Button btn_change=(Button)findViewById(R.id.ShowBtn);

		btn_change.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Button btn_top=(Button)findViewById(R.id.toTop);
				Button btn_bot=(Button)findViewById(R.id.toBottom);
				Button btn_left=(Button)findViewById(R.id.toLeft);
				Button btn_right=(Button)findViewById(R.id.toRight);
				Button toTop_20=(Button)findViewById(R.id.toTop20);
				Button toBottom_20=(Button)findViewById(R.id.toBottom20);
				Button toLeft_20=(Button)findViewById(R.id.toLeft20);
				Button toRight_20=(Button)findViewById(R.id.toRight20);
				btn_top.setVisibility(View.VISIBLE);
				btn_bot.setVisibility(View.VISIBLE);
				btn_left.setVisibility(View.VISIBLE);
				btn_right.setVisibility(View.VISIBLE);
				toTop_20.setVisibility(View.VISIBLE);
				toBottom_20.setVisibility(View.VISIBLE);
				toLeft_20.setVisibility(View.VISIBLE);
				toRight_20.setVisibility(View.VISIBLE);

			}
		});
		Button btn_top=(Button)findViewById(R.id.toTop);
		Button btn_bot=(Button)findViewById(R.id.toBottom);
		Button btn_left=(Button)findViewById(R.id.toLeft);
		Button btn_right=(Button)findViewById(R.id.toRight);
		Button toTop_20=(Button)findViewById(R.id.toTop20);
		Button toBottom_20=(Button)findViewById(R.id.toBottom20);
		Button toLeft_20=(Button)findViewById(R.id.toLeft20);
		Button toRight_20=(Button)findViewById(R.id.toRight20);


		toRight_20.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PtoLeft+=20;
				composerLayout btn=(composerLayout)findViewById(R.id.test);
				RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) btn.getLayoutParams();
				linearParams.setMargins(PtoLeft,PtoTop,50,10);
				btn.setLayoutParams(linearParams); 
				changed=true;

			}
		});

		toLeft_20.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PtoLeft-=20;
				composerLayout btn=(composerLayout)findViewById(R.id.test);
				RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) btn.getLayoutParams();
				linearParams.setMargins(PtoLeft,PtoTop,50,10);
				btn.setLayoutParams(linearParams); 
				changed=true;

			}
		});


		toBottom_20.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PtoTop+=20;
				composerLayout btn=(composerLayout)findViewById(R.id.test);
				RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) btn.getLayoutParams();
				linearParams.setMargins(PtoLeft,PtoTop,50,10);
				btn.setLayoutParams(linearParams); 
				changed=true;

			}
		});

		toTop_20.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PtoTop-=20;
				composerLayout btn=(composerLayout)findViewById(R.id.test);
				RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) btn.getLayoutParams();
				linearParams.setMargins(PtoLeft,PtoTop,50,10);
				btn.setLayoutParams(linearParams); 
				changed=true;

			}
		});




		btn_top.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PtoTop--;
				composerLayout btn=(composerLayout)findViewById(R.id.test);
				RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) btn.getLayoutParams();
				linearParams.setMargins(PtoLeft,PtoTop,50,10);
				btn.setLayoutParams(linearParams); 
				changed=true;

			}
		});

		btn_bot.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PtoTop++;
				composerLayout btn=(composerLayout)findViewById(R.id.test);
				RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) btn.getLayoutParams();
				linearParams.setMargins(PtoLeft,PtoTop,50,10);
				btn.setLayoutParams(linearParams); 
				changed=true;

			}
		});

		btn_left.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PtoLeft--;
				composerLayout btn=(composerLayout)findViewById(R.id.test);
				RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) btn.getLayoutParams();
				linearParams.setMargins(PtoLeft,PtoTop,50,10);
				btn.setLayoutParams(linearParams); 
				changed=true;

			}
		});

		btn_right.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PtoLeft++;
				composerLayout btn=(composerLayout)findViewById(R.id.test);
				RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) btn.getLayoutParams();
				linearParams.setMargins(PtoLeft,PtoTop,50,10);
				btn.setLayoutParams(linearParams); 
				changed=true;

			}
		});




		Context ctx = MainActivity.this;       
		SharedPreferences sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);

		int toTop=sp.getInt("toTop", 15532);
		int toLeft=sp.getInt("toLeft", 15532);

		if(toTop==15532)
		{
			toTop=50;
			Editor editor = sp.edit();
			editor.putInt("toTop",toTop);
			editor.commit();


		}

		if(toLeft==15532)
		{		
			toLeft=50;
			Editor editor = sp.edit();
			editor.putInt("toLeft",toLeft);
			editor.commit();

		}
		PtoTop=toTop;
		PtoLeft=toLeft;
		composerLayout btn=(composerLayout)findViewById(R.id.test);
		RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) btn.getLayoutParams();
		linearParams.setMargins(toLeft,toTop,50,10);
		btn.setLayoutParams(linearParams); 
		Log.i("ro","toLeft: "+toLeft);
		Log.i("ro","toTop: "+toTop);
		s.onClick(btn);

	}

	public void sendMessage(View a)
	{

		System.out.println("exit");


		if(changed)
		{
			Context ctx = MainActivity.this;  
			SharedPreferences sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putInt("toTop",PtoTop);
			editor.putInt("toLeft",PtoLeft);
			editor.commit();

		}
		if(clayout.destroy())
		{
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {			
				public void run() {
					MainActivity.this.finish();		
					//android.os.Process.killProcess(android.os.Process.myPid());
					//	System.exit(0);
				}
			}, 530);
		}
		else {
			//System.exit(0);
			MainActivity.this.finish();
		}



	}




	public static  boolean takeScreenShot(View view ,String path){
		boolean isSucc=false;
		/**
		 *  ����Ҫ��ȡ����cache��Ҫͨ��setDrawingCacheEnable������cache������
		 *  Ȼ���ٵ���getDrawingCache�����Ϳ��Ի��view��cacheͼƬ�ˡ�
		 *  buildDrawingCache�������Բ��õ��ã���Ϊ����getDrawingCache����ʱ��
		 *  ����cacheû�н�����ϵͳ���Զ�����buildDrawingCache��������cache��
		 *  ����Ҫ����cache, ����Ҫ����destoryDrawingCache�����Ѿɵ�cache���٣�
		 *  ���ܽ����µġ�
		 */
		view.setDrawingCacheEnabled(true);//������ȡ����
		view.buildDrawingCache();
		Bitmap bitmap=view.getDrawingCache();//�õ�View��cache
		Canvas canvas=new Canvas(bitmap);
		int w=bitmap.getWidth();
		int h=bitmap.getHeight();

		Paint paint=new Paint();
		paint.setColor(Color.YELLOW);
		SimpleDateFormat simple=new SimpleDateFormat("yyyyMMddhhmmss");
		String time=simple.format(new Date());

		//canvas.drawText(time, w-w/2, h-h/10, paint);
		canvas.save();
		canvas.restore();
		FileOutputStream fos=null;
		try{
			File sddir=new File(path);
			if(!sddir.exists()){
				sddir.mkdir();
			}
			File file=new File(path+time + ".jpg");
			fos=new FileOutputStream(file);
			if(fos!=null){
				bitmap.compress(Bitmap.CompressFormat.JPEG,100, fos);
				fos.close();
				isSucc=true;
			}
		}catch(Exception e){

			e.printStackTrace();
		}
		return isSucc;
	}


	void open_wifi()
	{
		Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
		MainActivity.this.finish();


	}

	void volume_set(boolean change)
	{


		Context ctx = MainActivity.this;  
		AudioManager aManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		int volume=aManager.getStreamVolume(AudioManager.STREAM_RING);
		boolean isMute = ((volume ==0)&&(aManager.getRingerMode()==AudioManager.RINGER_MODE_SILENT) );
		boolean isNextMute ;
		if(change)
			isNextMute=!isMute;
		else 
			isNextMute=isMute;
		SharedPreferences sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);


		if( isNextMute )
		{
			if(change&&volume!=0)
			{
				Editor editor = sp.edit();
				editor.putInt("volume",volume);
				editor.commit();
			}
			if(change)
			{
				//				Vibrator vibrator=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
				//				vibrator.cancel();
				aManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);


				//aManager.setStreamVolume(AudioManager.STREAM_RING, 0, AudioManager.FLAG_ALLOW_RINGER_MODES|AudioManager.FLAG_SHOW_UI);
				aManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);

			}
			if(change||((!change)&&(aManager.getRingerMode()==AudioManager.RINGER_MODE_SILENT)))
			{
				clayout.reload_picture(1,R.drawable.composer_music_shut);
				clayout.findViewById(101).setAlpha((float) 0.8);
			}

			else {
				clayout.reload_picture(1,R.drawable.composer_music);
				clayout.findViewById(101).setAlpha((float) 1);
			}
		}
		else
		{
			int volume_pre= sp.getInt("volume", 998);
			volume_img=R.drawable.composer_music_shut;
			if(change)
				aManager.setStreamVolume(AudioManager.STREAM_RING,volume_pre==998?3:volume_pre, AudioManager.FLAG_SHOW_UI|AudioManager.FLAG_PLAY_SOUND);
			clayout.reload_picture(1,R.drawable.composer_music);
			clayout.findViewById(101).setAlpha((float) 1);
		}

	}




	//private ReceiveBroadCast receiveBroadCast;  //�㲥ʵ��
	RegisteLinster s=new RegisteLinster();
	ReceiveBroadCast ss;
	public class RegisteLinster implements OnClickListener
	{
		@Override
		public void onClick(View view)
		{
			// ע��㲥����
			ss = new ReceiveBroadCast();
			IntentFilter filter = new IntentFilter();
			filter.addAction("android.media.VOLUME_CHANGED_ACTION");    //ֻ�г�����ͬ��action�Ľ����߲��ܽ��մ˹㲥
			registerReceiver(ss, filter);
		}
	}

	public class ReceiveBroadCast extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent)
		{
			//�õ��㲥�еõ������ݣ�����ʾ����
			//  String message = intent.getStringExtra("data");
			// Log.i("miao3","onReceive");
			volume_set(false);
		}

	}
	public void lighting_set()
	{
		if(camera2.getIsOn())
		{
			if(camera2.turnOff())
				clayout.reload_picture(2,R.drawable.composer_lighting_shut);
			clayout.findViewById(102).setAlpha((float) 0.8);
		}	
		else {
			if(camera2.turnOn())
				clayout.reload_picture(2,R.drawable.composer_lighting);
			clayout.findViewById(102).setAlpha((float) 1);
		}
	}

	@SuppressWarnings("deprecation")
	private String getCalendarUriBase(Context con) {
		String calendarUriBase = null;
		Uri calendars = Uri.parse("content://calendar/calendars");
		Cursor managedCursor = null;
		try {
			managedCursor = managedQuery(calendars, null, null, null, null);
		} catch (Exception e) {
			// eat
		}

		if (managedCursor != null) {
			calendarUriBase = "content://calendar/";
		} else {
			calendars = Uri.parse("content://com.android.calendar/calendars");
			try {
				managedCursor = managedQuery(calendars, null, null, null, null);
			} catch (Exception e) {
				// statement to print the stacktrace
			}

			if (managedCursor != null) {
				calendarUriBase = "content://com.android.calendar/";
			}

		}

		return calendarUriBase;
	}


	void blue_set()
	{

		Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
		MainActivity.this.finish();




	}
	void blue_toggle()
	{

		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		int blue_state=mBluetoothAdapter.getState();
		if(blue_state==BluetoothAdapter.STATE_OFF||blue_state==BluetoothAdapter.STATE_TURNING_OFF)
		{
			mBluetoothAdapter.enable();

		}
		else {
			mBluetoothAdapter.disable();

		}
		blue_state=mBluetoothAdapter.getState();
		if(blue_state==BluetoothAdapter.STATE_OFF||blue_state==BluetoothAdapter.STATE_TURNING_OFF)
		{
			blue_img=R.drawable.blue_shut;
			clayout.findViewById(103).setAlpha((float) 0.8);
		}
		else {
			blue_img=R.drawable.blue;
			clayout.findViewById(103).setAlpha((float) 1);
		}
		clayout.reload_picture(3,blue_img);
		mVibrator01 = ( Vibrator ) getApplication().getSystemService(Service.VIBRATOR_SERVICE);  
		mVibrator01.vibrate( new long[]{10,50},-1);  
	}

	void liuliang_set()
	{
		boolean isEnabled=false;
		ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(  
				Context.CONNECTIVITY_SERVICE);  
		//		NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);  
		//		if (networkInfo.isConnectedOrConnecting()) {  
		//			liuliang_img=R.drawable.composer_liuliang_shut; 
		//			isEnabled=true;
		//		} else {              
		//			liuliang_img=R.drawable.composer_liuliang;  
		//			isEnabled=false;
		//		}  

		Class<?> cmClass = connectivityManager.getClass();  
		Class<?>[] argClasses = new Class[1];  
		argClasses[0] = boolean.class;  

		// ����ConnectivityManager��hide�ķ���setMobileDataEnabled�����Կ����͹ر�GP
		Method method = null;





		try {
			method = cmClass.getMethod("getMobileDataEnabled");
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if(method.invoke(connectivityManager).toString().equals(new String("true")))
			{
				liuliang_img=R.drawable.composer_liuliang_shut; 
				clayout.findViewById(104).setAlpha((float) 0.8);
				isEnabled=true;
			}
			else {
				liuliang_img=R.drawable.composer_liuliang;  
				clayout.findViewById(104).setAlpha((float) 1);
				isEnabled=false;
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}






		try {
			method = cmClass.getMethod("setMobileDataEnabled", argClasses);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  

		try {
			method.invoke(connectivityManager, !isEnabled);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  

		clayout.reload_picture(4,liuliang_img);


	}
	int getRadius()
	{


		Context ctx = MainActivity.this;
		SharedPreferences sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);

		int radius=sp.getInt("radius", 9981);

		if(radius==9981)
		{
			radius=200;
		}
		return radius;
	}
	//
	//	public void toggleGprs(boolean isEnable) throws Exception {  
	//		connManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);  
	//		Class<?> cmClass = connManager.getClass();  
	//		Class<?>[] argClasses = new Class[1];  
	//		argClasses[0] = boolean.class;  
	//
	//		// ����ConnectivityManager��hide�ķ���setMobileDataEnabled�����Կ����͹ر�GPRS����  
	//		Method method = cmClass.getMethod("setMobileDataEnabled", argClasses);  
	//		method.invoke(connManager, isEnable);  
	//	} 
	Vibrator mVibrator01;
	void wifi_toggle()
	{
		WifiManager wifiManager = (WifiManager)(this.getSystemService(Context.WIFI_SERVICE));

		if(wifiManager != null){

			if(wifiManager.getWifiState()==WifiManager.WIFI_STATE_ENABLED||(wifiManager.getWifiState()==WifiManager.WIFI_STATE_ENABLING))
			{
				wifi_img=R.drawable.composer_wifi;
				wifiManager.setWifiEnabled(false);
				clayout.reload_picture(0,R.drawable.composer_wifi_shut);
				clayout.findViewById(100).setAlpha((float) 0.8);
				mVibrator01 = ( Vibrator ) getApplication().getSystemService(Service.VIBRATOR_SERVICE);  
				mVibrator01.vibrate( new long[]{10,50},-1);  
			}
			else {

				wifi_img=R.drawable.composer_wifi_shut;
				wifiManager.setWifiEnabled(true);
				clayout.reload_picture(0,R.drawable.composer_wifi);
				clayout.findViewById(100).setAlpha((float) 1);
				mVibrator01 = ( Vibrator ) getApplication().getSystemService(Service.VIBRATOR_SERVICE);  
				mVibrator01.vibrate( new long[]{10,50},-1);  

			}

		}
	}

}
