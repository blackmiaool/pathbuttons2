/*
 * Copyright (C) 2012 The Evervolv Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package geniuz.pathbuttons;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class BluetoothWidgetProvider extends AppWidgetProvider {
 
    @Override
    public void onUpdate(Context context,
			 AppWidgetManager appWidgetManager,
			 int[] appWidgetIds){

        super.onUpdate(context, appWidgetManager, appWidgetIds);
        
        
        
        
        
        

		//create remoteViews
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.c_c_button_layout);

		//create intents for setting brightness when buttons are clicked
		Intent setBrightness1 = new Intent(context, MainActivity.class);
		setBrightness1.setAction("com.blackmiaool.forc_c_.start");
		setBrightness1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

		//register on click listeners with buttons		
		remoteViews.setOnClickPendingIntent(R.id.start_btn, PendingIntent.getActivity(context, 1, setBrightness1, PendingIntent.FLAG_UPDATE_CURRENT));		
		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);

    }


    /**
	* this method will receive all Intents that it register fors in
	* the android manifest file.
	*/
    @Override
    public void onReceive(Context context, Intent intent){
    	//Log.i("miao3","onReceive");
    	super.onReceive(context, intent);
    	
 
    }

	/**
	* this method is called when the widget is added to the home
	* screen, and so it contains the initial setup of the widget.
	*/





}
