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
package com.socialize.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.socialize.Socialize;
import com.socialize.entity.Like;
import com.socialize.error.SocializeException;
import com.socialize.listener.like.LikeAddListener;
import com.socialize.listener.like.LikeDeleteListener;
import com.socialize.sample.util.ErrorHandler;
import com.socialize.util.StringUtils;

public class LikeActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.like);
		
		Socialize.init(this);
		
		final EditText txtKey = (EditText) findViewById(R.id.txtKey);
		final TextView txtLikeCreateResult = (TextView) findViewById(R.id.txtLikeCreateResult);
		
		final Button btnLikeCreate = (Button) findViewById(R.id.btnLikeCreate);
		final Button btnLikeDelete = (Button) findViewById(R.id.btnLikeDelete);
		
		btnLikeDelete.setEnabled(false);
		
		final TextView txtLikeIdCreated = (TextView) findViewById(R.id.txtLikeIdCreated);
		final TextView txtLikeDateCreated = (TextView) findViewById(R.id.txtLikeDateCreated);
		
		if(Socialize.getSocialize().isAuthenticated()) {
			
			btnLikeCreate.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					txtLikeCreateResult.setText("");
					btnLikeCreate.setEnabled(false);
					
					String key = txtKey.getText().toString();
					
					if(!StringUtils.isEmpty(key)) {
						Socialize.getSocialize().like(key, new LikeAddListener() {
							
							@Override
							public void onError(SocializeException error) {
								txtLikeCreateResult.setText("FAIL: " + ErrorHandler.handleApiError(LikeActivity.this, error));
								btnLikeCreate.setEnabled(true);
							}
							
							@Override
							public void onCreate(Like entity) {
								btnLikeCreate.setEnabled(true);
								btnLikeDelete.setEnabled(true);
								txtLikeCreateResult.setText("SUCCESS");
								
								if(entity.getId() != null) {
									txtLikeIdCreated.setText(String.valueOf(entity.getId()));
								}
								
								if(entity.getDate() != null) {
									txtLikeDateCreated.setText(String.valueOf(entity.getDate()));
								}
							}
						});
					}
					else {
						txtLikeCreateResult.setText("FAIL: No Key");
						btnLikeCreate.setEnabled(true);
					}
					
				}
			});
			
			btnLikeDelete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					txtLikeCreateResult.setText("");
					
					String id =  txtLikeIdCreated.getText().toString();
					
					if(!StringUtils.isEmpty(id)) {
						Socialize.getSocialize().unlike(Integer.parseInt(id), new LikeDeleteListener() {
							
							@Override
							public void onError(SocializeException error) {
								txtLikeCreateResult.setText("FAIL: " + ErrorHandler.handleApiError(LikeActivity.this, error));
							}
							
							@Override
							public void onDelete() {
								txtLikeCreateResult.setText("SUCCESS");
								txtLikeIdCreated.setText("");
								txtLikeDateCreated.setText("");
							}
						});
					}
					else {
						txtLikeCreateResult.setText("FAIL: No ID");
					}
				}
			});
		}
		else {
			// Not authorized, you would normally do a re-auth here
			txtLikeCreateResult.setText("AUTH FAIL");
		}
	}

	@Override
	protected void onDestroy() {
		Socialize.destroy(this);
		super.onDestroy();
	}
}
