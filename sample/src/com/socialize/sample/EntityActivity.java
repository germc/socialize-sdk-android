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

import com.socialize.Socialize;

public class EntityActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entity);
		
		Socialize.init(this);
		
//		final EditText txtKey = (EditText) findViewById(R.id.txtKey);
//		final EditText txtName = (EditText) findViewById(R.id.txtName);
//		final TextView txtEntityCreateResult = (TextView) findViewById(R.id.txtEntityCreateResult);
		
//		final Button btnEntityCreate = (Button) findViewById(R.id.btnEntityCreate);
//		final Button btnEntityGet = (Button) findViewById(R.id.btnEntityGet);
		
		
//		if(Socialize.getSocialize().isAuthenticated()) {
			
//			btnEntityCreate.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					
//					txtEntityCreateResult.setText("");
//					btnEntityCreate.setEnabled(false);
//					
//					String key = txtKey.getText().toString();
//					String name = txtName.getText().toString();
//					
//					Socialize.getSocialize().createEntity(key, name, new EntityCreateListener() {
//						
//						@Override
//						public void onError(SocializeException error) {
//							txtEntityCreateResult.setText("FAIL: " + ErrorHandler.handleApiError(EntityActivity.this, error));
//							btnEntityCreate.setEnabled(true);
//						}
//						
//						@Override
//						public void onCreate(Entity entity) {
//							btnEntityCreate.setEnabled(true);
//							txtEntityCreateResult.setText("SUCCESS");
//							populateEntityData(entity);
//						}
//					});
//				}
//			});
			
			
//			btnEntityGet.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					txtEntityCreateResult.setText("");
//					btnEntityGet.setEnabled(false);
//					String key = txtKey.getText().toString();
//					
//					Socialize.getSocialize().getEntity(key, new EntityGetListener() {
//						
//						@Override
//						public void onError(SocializeException error) {
//							txtEntityCreateResult.setText("FAIL: " + ErrorHandler.handleApiError(EntityActivity.this, error));
//							btnEntityGet.setEnabled(true);
//						}
//						
//						@Override
//						public void onGet(Entity entity) {
//					
//							btnEntityGet.setEnabled(true);
//							
//							if(entity != null) {
//								txtEntityCreateResult.setText("SUCCESS");
//								populateEntityData(entity);
//							}
//							else {
//								txtEntityCreateResult.setText("NOT FOUND");
//							}
//						}
//					});
//				}
//			});
//		}
//		else {
//			// Not authorized, you would normally do a re-auth here
//			txtEntityCreateResult.setText("AUTH FAIL");
//		}
	}
	
//	private void populateEntityData(Entity entity) {
//		final TextView txtEntityIdCreated = (TextView) findViewById(R.id.txtEntityIdCreated);
//		final TextView txtEntityKeyCreated = (TextView) findViewById(R.id.txtEntityKeyCreated);
//		final TextView txtEntityNameCreated = (TextView) findViewById(R.id.txtEntityNameCreated);
//		final TextView txtEntityShares = (TextView) findViewById(R.id.txtEntityShares);
//		final TextView txtEntityLikes = (TextView) findViewById(R.id.txtEntityLikes);
//		final TextView txtEntityViews = (TextView) findViewById(R.id.txtEntityViews);
//		final TextView txtEntityComments = (TextView) findViewById(R.id.txtEntityComments);
//		
//		
//		if(entity.getId() != null) {
//			txtEntityIdCreated.setText(entity.getId().toString());
//		}
//		
//		if(entity.getName() != null) {
//			txtEntityNameCreated.setText(entity.getName());
//		}
//		
//		if(entity.getKey() != null) {
//			txtEntityKeyCreated.setText(entity.getKey());
//		}
//		
//		if(entity.getShares() != null) {
//			txtEntityShares.setText(entity.getShares().toString());
//		}
//		
//		if(entity.getLikes() != null) {
//			txtEntityLikes.setText(entity.getLikes().toString());
//		}
//		
//		if(entity.getViews() != null) {
//			txtEntityViews.setText(entity.getViews().toString());
//		}
//		
//		if(entity.getComments() != null) {
//			txtEntityComments.setText(entity.getComments().toString());
//		}
//	}

	@Override
	protected void onDestroy() {
		Socialize.destroy(this);
		super.onDestroy();
	}
}
