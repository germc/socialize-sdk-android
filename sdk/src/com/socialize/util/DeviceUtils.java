/*
 * Copyright (c) 2011 Socialize Inc. 
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy 
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.socialize.util;

import android.Manifest.permission;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import com.socialize.Socialize;
import com.socialize.log.SocializeLogger;


/**
 * @author Jason Polites
 */
public class DeviceUtils {
	
	private SocializeLogger logger;
	private String userAgent;
	
	public String getUDID(Context context) {
		if(hasPermission(context, permission.READ_PHONE_STATE)) {
			TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			return tManager.getDeviceId();
		}
		else {
			// this is fatal
			if(logger != null) {
				logger.error(SocializeLogger.NO_UDID);
			}
			
			return null;
		}
	}
	
	public String getUserAgentString() {
		if(userAgent == null) {
			userAgent = "Android-" + android.os.Build.VERSION.SDK_INT + "/" + android.os.Build.MODEL + " SocializeSDK/v" + Socialize.VERSION;
		}
		return userAgent;
	}
	
	public boolean hasPermission(Context context, String permission) {
		return context.getPackageManager().checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED;
	}

	public SocializeLogger getLogger() {
		return logger;
	}

	public void setLogger(SocializeLogger logger) {
		this.logger = logger;
	}
}
