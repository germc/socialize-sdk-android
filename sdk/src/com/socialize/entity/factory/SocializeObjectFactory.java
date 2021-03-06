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
package com.socialize.entity.factory;

import org.json.JSONException;
import org.json.JSONObject;

import com.socialize.entity.SocializeObject;

/**
 * @author Jason Polites
 *
 * @param <T>
 */
public abstract class SocializeObjectFactory<T extends SocializeObject> extends JSONFactory<T> {
	
	@Override
	protected void fromJSON(JSONObject from, T to) throws JSONException {
		if(from.has("id")) {
			to.setId(from.getInt("id"));
		}
		postFromJSON(from, to);
	}

	@Override
	protected void toJSON(T from, JSONObject to) throws JSONException {
		Integer id = from.getId();
		if(id != null) {
			to.put("id", id);
		}
		postToJSON(from, to);
	}
	
	protected abstract void postFromJSON(JSONObject from, T to) throws JSONException;
	
	protected abstract void postToJSON(T from, JSONObject to) throws JSONException;
}
