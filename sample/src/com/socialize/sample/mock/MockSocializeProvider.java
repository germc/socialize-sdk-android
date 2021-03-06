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
package com.socialize.sample.mock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;

import com.socialize.api.SocializeSession;
import com.socialize.api.SocializeSessionImpl;
import com.socialize.entity.ListResult;
import com.socialize.entity.SocializeObject;
import com.socialize.entity.factory.SocializeObjectFactory;
import com.socialize.error.SocializeException;
import com.socialize.provider.SocializeProvider;

/**
 * Mock provider used to test sample app UI interaction without access the real API.
 * @author jasonpolites
 *
 * @param <T>
 */
public class MockSocializeProvider<T extends SocializeObject> implements SocializeProvider<T> {

	protected SocializeObjectFactory<T> objectFactory;
	
	public MockSocializeProvider() {
		super();
	}

	public MockSocializeProvider(Context context) {
		super();
	}

	@Override
	public SocializeSession authenticate(String endpoint, String key, String secret, String uuid) throws SocializeException {
		return new SocializeSessionImpl();
	}

	@Override
	public ListResult<T> list(SocializeSession session, String endpoint, String key, String[] ids) throws SocializeException {
		return makeMockListResult(ids);
	}

	@Override
	public T get(SocializeSession session, String endpoint, String id) throws SocializeException {
		return makeObject(id);
	}

	@Override
	public ListResult<T> put(SocializeSession session, String endpoint, T object) throws SocializeException {
		return makeListResult(object);
	}

	@Override
	public ListResult<T> post(SocializeSession session, String endpoint, T object) throws SocializeException {
		return makeListResult(object);
	}

	@Override
	public ListResult<T> put(SocializeSession session, String endpoint, Collection<T> objects) throws SocializeException {
		return makeListResult(objects);
	}

	@Override
	public ListResult<T> post(SocializeSession session, String endpoint, Collection<T> object) throws SocializeException {
		return makeListResult(object);
	}

	@Override
	public void delete(SocializeSession session, String endpoint, String id) throws SocializeException {}
	
	protected ListResult<T> makeListResult(Collection<T> objects) {
		List<T> results = new ArrayList<T>(objects);
		
		int id = 0;
		
		for (T t : results) {
			t.setId(id);
			id++;
		}
		
		ListResult<T> result = new ListResult<T>(results);
		return result;
	}
	
	protected ListResult<T> makeListResult(T object) {
		List<T> results = new ArrayList<T>(1);
		object.setId(0);
		results.add(object);
		ListResult<T> result = new ListResult<T>(results);
		return result;
	}
	
	protected ListResult<T> makeMockListResult(String[] ids) {
		ListResult<T> result = new ListResult<T>();
		List<T> list = new ArrayList<T>(ids.length);
		
		for (int i = 0; i < ids.length; i++) {
			list.add(makeObject(ids[i]));
		}
		
		result.setResults(list);
		return result;
	}
	
	protected T makeObject(String id) {
		T object = objectFactory.instantiateObject();
		object.setId(Integer.parseInt(id));
		return object;
	}

	public SocializeObjectFactory<T> getObjectFactory() {
		return objectFactory;
	}

	public void setObjectFactory(SocializeObjectFactory<T> objectFactory) {
		this.objectFactory = objectFactory;
	}
}
