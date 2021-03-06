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
package com.socialize.test.unit;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.test.mock.MockContext;

import com.google.android.testing.mocking.AndroidMock;
import com.google.android.testing.mocking.UsesMocks;
import com.socialize.api.SocializeRequestFactory;
import com.socialize.api.SocializeSessionFactory;
import com.socialize.api.SocializeSessionPersister;
import com.socialize.api.WritableSession;
import com.socialize.config.SocializeConfig;
import com.socialize.entity.ListResult;
import com.socialize.entity.SocializeObject;
import com.socialize.entity.User;
import com.socialize.entity.factory.SocializeObjectFactory;
import com.socialize.entity.factory.UserFactory;
import com.socialize.error.SocializeApiError;
import com.socialize.net.HttpClientFactory;
import com.socialize.provider.DefaultSocializeProvider;
import com.socialize.test.SocializeActivityTest;
import com.socialize.util.HttpUtils;
import com.socialize.util.IOUtils;
import com.socialize.util.JSONParser;

/**
 * @author Jason Polites
 */
@UsesMocks({
	SocializeObjectFactory.class, 
	UserFactory.class,
	SocializeSessionPersister.class,
	HttpClientFactory.class, 
	SocializeSessionFactory.class,
	WritableSession.class,
	HttpClient.class,
	JSONParser.class,
	JSONObject.class,
	JSONArray.class,
	SocializeRequestFactory.class,
	HttpUriRequest.class,
	HttpResponse.class,
	User.class,
	HttpEntity.class,
	HttpUtils.class,
	IOUtils.class})
public class SocializeProviderTest extends SocializeActivityTest {
	
	SocializeSessionPersister sessionPersister;
	SocializeObjectFactory<SocializeObject> objectFactory;
	SocializeRequestFactory<SocializeObject> requestFactory;
	UserFactory userFactory;
	SocializeSessionFactory sessionFactory;
	HttpClientFactory clientFactory;
	WritableSession session;
	HttpClient client;
	HttpUriRequest request;
	HttpResponse response;
	JSONParser jsonParser;
	JSONObject json;
	User user;
	HttpEntity entity;
	HttpUtils httpUtils;
	IOUtils ioUtils;
	JSONArray jsonArray;
	Context mockContext;
	SocializeConfig config;
	
	final String jsonString = "{foobar}";
	
	@SuppressWarnings("unchecked")
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		sessionPersister = AndroidMock.createMock(SocializeSessionPersister.class);
		objectFactory = AndroidMock.createMock(SocializeObjectFactory.class);
		requestFactory = AndroidMock.createMock(SocializeRequestFactory.class);
		userFactory = AndroidMock.createMock(UserFactory.class);
		sessionFactory = AndroidMock.createMock(SocializeSessionFactory.class);
		clientFactory = AndroidMock.createMock(HttpClientFactory.class);
		session = AndroidMock.createMock(WritableSession.class);
		client = AndroidMock.createMock(HttpClient.class);
		request = AndroidMock.createMock(HttpUriRequest.class);
		response = AndroidMock.createMock(HttpResponse.class);
		jsonParser = AndroidMock.createMock(JSONParser.class);
		json = AndroidMock.createMock(JSONObject.class);
		user = AndroidMock.createMock(User.class);
		entity = AndroidMock.createMock(HttpEntity.class);
		httpUtils = AndroidMock.createMock(HttpUtils.class);
		ioUtils = AndroidMock.createMock(IOUtils.class);
		jsonArray = AndroidMock.createMock(JSONArray.class);
		mockContext = new MockContext();
		config = AndroidMock.createMock(SocializeConfig.class);
	}
	
	private DefaultSocializeProvider<SocializeObject> getNewProvider() {
		DefaultSocializeProvider<SocializeObject> provider = new DefaultSocializeProvider<SocializeObject>(mockContext);
		
		provider.setObjectFactory(objectFactory);
		provider.setUserFactory(userFactory);
		provider.setClientFactory(clientFactory);
		provider.setSessionFactory(sessionFactory);
		provider.setRequestFactory(requestFactory);
		provider.setConfig(config);
		provider.setJsonParser(jsonParser);
		provider.setHttpUtils(httpUtils);
		provider.setIoUtils(ioUtils);
		
		return provider;
	}

	public void testAuthenticate() throws Exception {
		
		final String key = "foo";
		final String secret = "bar";
		final String uuid = "uuid";
		final String endpoint = "foobar/";
		final String host = "host";
		final String oauth_token = "oauth_token";
		final String oauth_token_secret = "oauth_token_secret";
		final String url = "https://" + host + "/" + endpoint;
		
		AndroidMock.expect(sessionFactory.create(key, secret)).andReturn(session);
		AndroidMock.expect(clientFactory.getClient()).andReturn(client);
		AndroidMock.expect(client.execute(request)).andReturn(response);
		AndroidMock.expect(response.getEntity()).andReturn(entity);
		AndroidMock.expect(entity.getContent()).andReturn(null);
		AndroidMock.expect(requestFactory.getAuthRequest(session, url, uuid)).andReturn(request);
		AndroidMock.expect(jsonParser.parseObject((InputStream)null)).andReturn(json);
		AndroidMock.expect(json.getJSONObject("user")).andReturn(json);
		AndroidMock.expect(json.getString("oauth_token")).andReturn(oauth_token);
		AndroidMock.expect(json.getString("oauth_token_secret")).andReturn(oauth_token_secret);
		AndroidMock.expect(userFactory.fromJSON(json)).andReturn(user);
		AndroidMock.expect(httpUtils.isHttpError(response)).andReturn(false);
		AndroidMock.expect(sessionPersister.load(mockContext)).andReturn(null); // No persistence for this one
		AndroidMock.expect(session.getHost()).andReturn(host);
		
		// Expect save
		sessionPersister.save(mockContext, session);
		
		session.setConsumerToken(oauth_token);
		session.setConsumerTokenSecret(oauth_token_secret);
		session.setUser(user);
		
		entity.consumeContent();
		
		DefaultSocializeProvider<SocializeObject> provider = getNewProvider();
		
		AndroidMock.replay(user);
		AndroidMock.replay(session);
		AndroidMock.replay(sessionFactory);
		AndroidMock.replay(clientFactory);
		AndroidMock.replay(requestFactory);
		AndroidMock.replay(jsonParser);
		AndroidMock.replay(client);
		AndroidMock.replay(json);
		AndroidMock.replay(userFactory);
		AndroidMock.replay(entity);
		AndroidMock.replay(response);
		AndroidMock.replay(httpUtils);
		AndroidMock.replay(sessionPersister);
		
		provider.setSessionPersister(sessionPersister);
		
		provider.authenticate(endpoint, key, secret, uuid);
		
		AndroidMock.verify(user);
		AndroidMock.verify(session);
		AndroidMock.verify(sessionFactory);
		AndroidMock.verify(clientFactory);
		AndroidMock.verify(requestFactory);
		AndroidMock.verify(jsonParser);
		AndroidMock.verify(client);
		AndroidMock.verify(json);
		AndroidMock.verify(userFactory);
		AndroidMock.verify(entity);
		AndroidMock.verify(response);
		AndroidMock.verify(httpUtils);
		AndroidMock.verify(sessionPersister);
	}
	
	
	public void testLoadCachedCredentialsOnAuthenticate() throws Exception {
		
		final String key = "foo";
		final String secret = "bar";
		final String host = "snafu";
		final String uuid = "uuid";
		final String endpoint = "foobar/";
		
		AndroidMock.expect(sessionPersister.load(mockContext)).andReturn(session); 
		
		AndroidMock.expect(session.getConsumerKey()).andReturn(key);
		AndroidMock.expect(session.getConsumerSecret()).andReturn(secret);
		AndroidMock.expect(session.getHost()).andReturn(host);
		AndroidMock.expect(config.getProperty(SocializeConfig.API_HOST)).andReturn(host);

		AndroidMock.replay(config);
		AndroidMock.replay(session);
		AndroidMock.replay(sessionPersister);
		
		DefaultSocializeProvider<SocializeObject> provider = getNewProvider();
		
		provider.setSessionPersister(sessionPersister);
		
		provider.authenticate(endpoint, key, secret, uuid);
		
		AndroidMock.verify(config);
		AndroidMock.verify(session);
		AndroidMock.verify(sessionPersister);
	}
	
	public void testGet() throws Exception {
		
		final String id = "foo";
		final String endpoint = "foobar/";
		final String host = "host";
		final String url = "http://" + host + "/" + endpoint;
		final SocializeObject object = new SocializeObject();
		
		AndroidMock.expect(clientFactory.getClient()).andReturn(client);
		AndroidMock.expect(requestFactory.getGetRequest(session, url, id)).andReturn(request);
		AndroidMock.expect(client.execute(request)).andReturn(response);
		AndroidMock.expect(response.getEntity()).andReturn(entity);
		AndroidMock.expect(entity.getContent()).andReturn(null);
		AndroidMock.expect(jsonParser.parseObject((InputStream)null)).andReturn(json);
		AndroidMock.expect(objectFactory.fromJSON(json)).andReturn(object);
		AndroidMock.expect(httpUtils.isHttpError(response)).andReturn(false);
		AndroidMock.expect(session.getHost()).andReturn(host);
		
		entity.consumeContent();
		
		AndroidMock.replay(session);
		AndroidMock.replay(sessionFactory);
		AndroidMock.replay(clientFactory);
		AndroidMock.replay(objectFactory);
		AndroidMock.replay(requestFactory);
		AndroidMock.replay(jsonParser);
		AndroidMock.replay(client);
		AndroidMock.replay(json);
		AndroidMock.replay(userFactory);
		AndroidMock.replay(entity);
		AndroidMock.replay(response);
		AndroidMock.replay(httpUtils);
		
		DefaultSocializeProvider<SocializeObject> provider = getNewProvider();
		
		SocializeObject gotten = provider.get(session, endpoint, id);
		
		AndroidMock.verify(session);
		AndroidMock.verify(sessionFactory);
		AndroidMock.verify(clientFactory);
		AndroidMock.verify(requestFactory);
		AndroidMock.verify(jsonParser);
		AndroidMock.verify(client);
		AndroidMock.verify(json);
		AndroidMock.verify(userFactory);
		AndroidMock.verify(entity);
		AndroidMock.verify(response);
		AndroidMock.verify(objectFactory);
		AndroidMock.verify(httpUtils);
		
		assertSame(object,gotten);
	}
	
	public void testDelete() throws Exception {
		
		final String id = "foo";
		final String endpoint = "foobar/";
		final String host = "host";
		final String url = "http://" + host + "/" + endpoint;
		
		AndroidMock.expect(clientFactory.getClient()).andReturn(client);
		AndroidMock.expect(requestFactory.getDeleteRequest(session, url, id)).andReturn(request);
		AndroidMock.expect(client.execute(request)).andReturn(response);
		AndroidMock.expect(httpUtils.isHttpError(response)).andReturn(false);
		AndroidMock.expect(session.getHost()).andReturn(host);
		
		AndroidMock.replay(clientFactory);
		AndroidMock.replay(requestFactory);
		AndroidMock.replay(client);
		AndroidMock.replay(httpUtils);
		AndroidMock.replay(session);
		
		DefaultSocializeProvider<SocializeObject> provider = getNewProvider();
		
		provider.delete(session, endpoint, id);
		
		AndroidMock.verify(clientFactory);
		AndroidMock.verify(requestFactory);
		AndroidMock.verify(client);
		AndroidMock.verify(httpUtils);
		AndroidMock.verify(session);
	}
	
	@UsesMocks (StatusLine.class)
	public void testSessionDeleteOnGetFail() throws Exception {
		final String id = "foo";
		final String endpoint = "foobar/";
		final String host = "host";
		final String url = "http://" + host + "/" + endpoint;
		final int statusCode = 69;
		
		StatusLine status = AndroidMock.createMock(StatusLine.class);
		
		AndroidMock.expect(clientFactory.getClient()).andReturn(client);
		AndroidMock.expect(requestFactory.getGetRequest(session, url, id)).andReturn(request);
		AndroidMock.expect(client.execute(request)).andReturn(response);
		AndroidMock.expect(response.getStatusLine()).andReturn(status);
		AndroidMock.expect(status.getStatusCode()).andReturn(statusCode);
		AndroidMock.expect(httpUtils.isHttpError(response)).andReturn(true); // Fail
		AndroidMock.expect(httpUtils.isAuthError(response)).andReturn(true); // Fail
		AndroidMock.expect(response.getEntity()).andReturn(entity);
		AndroidMock.expect(entity.getContent()).andReturn(null);
		AndroidMock.expect(ioUtils.readSafe(null)).andReturn("TEST ERROR IGNORE ME");
		AndroidMock.expect(session.getHost()).andReturn(host);
		
		sessionPersister.delete(mockContext);
		
		entity.consumeContent();
		
		AndroidMock.replay(session);
		AndroidMock.replay(sessionFactory);
		AndroidMock.replay(clientFactory);
		AndroidMock.replay(objectFactory);
		AndroidMock.replay(requestFactory);
		AndroidMock.replay(jsonParser);
		AndroidMock.replay(client);
		AndroidMock.replay(json);
		AndroidMock.replay(userFactory);
		AndroidMock.replay(entity);
		AndroidMock.replay(response);
		AndroidMock.replay(httpUtils);
		AndroidMock.replay(sessionPersister);
		AndroidMock.replay(status);
		
		DefaultSocializeProvider<SocializeObject> provider = getNewProvider();
		
		provider.setSessionPersister(sessionPersister);
		
		try {
			 provider.get(session, endpoint, id);
			 fail();
		}
		catch (Exception e) {
			assertTrue(e instanceof SocializeApiError);
			assertEquals(69, ((SocializeApiError)e).getResultCode());
		}
		
		AndroidMock.verify(session);
		AndroidMock.verify(sessionFactory);
		AndroidMock.verify(clientFactory);
		AndroidMock.verify(requestFactory);
		AndroidMock.verify(jsonParser);
		AndroidMock.verify(client);
		AndroidMock.verify(json);
		AndroidMock.verify(userFactory);
		AndroidMock.verify(entity);
		AndroidMock.verify(response);
		AndroidMock.verify(objectFactory);
		AndroidMock.verify(httpUtils);
		AndroidMock.verify(sessionPersister);
		AndroidMock.verify(status);
	}
	
	
	@UsesMocks (StatusLine.class)
	public void testSessionDeleteOnDeleteFail() throws Exception {
		final String id = "foo";
		final String endpoint = "foobar/";
		final String host = "host";
		final String url = "http://" + host + "/" + endpoint;
		final int statusCode = 69;
		
		StatusLine status = AndroidMock.createMock(StatusLine.class);
		
		AndroidMock.expect(clientFactory.getClient()).andReturn(client);
		AndroidMock.expect(requestFactory.getDeleteRequest(session, url, id)).andReturn(request);
		AndroidMock.expect(client.execute(request)).andReturn(response);
		AndroidMock.expect(response.getStatusLine()).andReturn(status);
		AndroidMock.expect(status.getStatusCode()).andReturn(statusCode);
		AndroidMock.expect(httpUtils.isHttpError(response)).andReturn(true); // Fail
		AndroidMock.expect(httpUtils.isAuthError(response)).andReturn(true); // Fail
		AndroidMock.expect(response.getEntity()).andReturn(entity);
		AndroidMock.expect(entity.getContent()).andReturn(null);
		AndroidMock.expect(ioUtils.readSafe(null)).andReturn("TEST ERROR IGNORE ME");
		AndroidMock.expect(session.getHost()).andReturn(host);
		
		sessionPersister.delete(mockContext);
		
		entity.consumeContent();
		
		AndroidMock.replay(session);
		AndroidMock.replay(sessionFactory);
		AndroidMock.replay(clientFactory);
		AndroidMock.replay(objectFactory);
		AndroidMock.replay(requestFactory);
		AndroidMock.replay(jsonParser);
		AndroidMock.replay(client);
		AndroidMock.replay(json);
		AndroidMock.replay(userFactory);
		AndroidMock.replay(entity);
		AndroidMock.replay(response);
		AndroidMock.replay(httpUtils);
		AndroidMock.replay(sessionPersister);
		AndroidMock.replay(status);
		
		DefaultSocializeProvider<SocializeObject> provider = getNewProvider();
		
		provider.setSessionPersister(sessionPersister);
		
		try {
			 provider.delete(session, endpoint, id);
			 fail();
		}
		catch (Exception e) {
			assertTrue(e instanceof SocializeApiError);
			assertEquals(69, ((SocializeApiError)e).getResultCode());
		}
		
		AndroidMock.verify(session);
		AndroidMock.verify(sessionFactory);
		AndroidMock.verify(clientFactory);
		AndroidMock.verify(requestFactory);
		AndroidMock.verify(jsonParser);
		AndroidMock.verify(client);
		AndroidMock.verify(json);
		AndroidMock.verify(userFactory);
		AndroidMock.verify(entity);
		AndroidMock.verify(response);
		AndroidMock.verify(objectFactory);
		AndroidMock.verify(httpUtils);
		AndroidMock.verify(sessionPersister);
		AndroidMock.verify(status);
	}
	
	@UsesMocks (StatusLine.class)
	public void testSessionDeleteOnListFail() throws Exception {
		final String key = "foo";
		final String endpoint = "foobar/";
		final String host = "host";
		final String url = "http://" + host + "/" + endpoint;
		final int statusCode = 69;
		
		StatusLine status = AndroidMock.createMock(StatusLine.class);
		
		AndroidMock.expect(clientFactory.getClient()).andReturn(client);
		AndroidMock.expect(requestFactory.getListRequest(session, url, key, null)).andReturn(request);
		AndroidMock.expect(client.execute(request)).andReturn(response);
		AndroidMock.expect(response.getStatusLine()).andReturn(status);
		AndroidMock.expect(status.getStatusCode()).andReturn(statusCode);
		AndroidMock.expect(httpUtils.isHttpError(response)).andReturn(true); // Fail
		AndroidMock.expect(httpUtils.isAuthError(response)).andReturn(true); // Fail
		AndroidMock.expect(response.getEntity()).andReturn(entity);
		AndroidMock.expect(entity.getContent()).andReturn(null);
		AndroidMock.expect(ioUtils.readSafe(null)).andReturn("TEST ERROR IGNORE ME");
		AndroidMock.expect(session.getHost()).andReturn(host);
		
		sessionPersister.delete(mockContext);
		
		entity.consumeContent();
		
		AndroidMock.replay(session);
		AndroidMock.replay(sessionFactory);
		AndroidMock.replay(clientFactory);
		AndroidMock.replay(objectFactory);
		AndroidMock.replay(requestFactory);
		AndroidMock.replay(jsonParser);
		AndroidMock.replay(client);
		AndroidMock.replay(json);
		AndroidMock.replay(userFactory);
		AndroidMock.replay(entity);
		AndroidMock.replay(response);
		AndroidMock.replay(httpUtils);
		AndroidMock.replay(sessionPersister);
		AndroidMock.replay(status);
		
		DefaultSocializeProvider<SocializeObject> provider = getNewProvider();
		
		provider.setSessionPersister(sessionPersister);
		
		try {
			 provider.list(session, endpoint, key, null);
			 fail();
		}
		catch (Exception e) {
			assertTrue(e instanceof SocializeApiError);
			assertEquals(69, ((SocializeApiError)e).getResultCode());
		}
		
		AndroidMock.verify(session);
		AndroidMock.verify(sessionFactory);
		AndroidMock.verify(clientFactory);
		AndroidMock.verify(requestFactory);
		AndroidMock.verify(jsonParser);
		AndroidMock.verify(client);
		AndroidMock.verify(json);
		AndroidMock.verify(userFactory);
		AndroidMock.verify(entity);
		AndroidMock.verify(response);
		AndroidMock.verify(objectFactory);
		AndroidMock.verify(httpUtils);
		AndroidMock.verify(sessionPersister);
		AndroidMock.verify(status);
	}

	public void testList() throws Exception {
		
		final String key = "foo";
		final String[] ids = {"foo", "bar"};
		final String endpoint = "foobar/";
		final String host = "host";
		final String url = "http://" + host + "/" + endpoint;
		final SocializeObject object = new SocializeObject();
		
		
		final int arrayLength = 3;
		
		AndroidMock.expect(clientFactory.getClient()).andReturn(client);
		AndroidMock.expect(requestFactory.getListRequest(session, url, key, ids)).andReturn(request);
		AndroidMock.expect(client.execute(request)).andReturn(response);
		AndroidMock.expect(response.getEntity()).andReturn(entity);
		AndroidMock.expect(entity.getContent()).andReturn(null);
		
		AndroidMock.expect(ioUtils.readSafe(null)).andReturn(jsonString);
		AndroidMock.expect(jsonParser.parseObject(jsonString)).andReturn(json);
		AndroidMock.expect(json.has(DefaultSocializeProvider.JSON_ATTR_ERRORS)).andReturn(false);
		AndroidMock.expect(json.has(DefaultSocializeProvider.JSON_ATTR_ITEMS)).andReturn(true);
		AndroidMock.expect(json.isNull(DefaultSocializeProvider.JSON_ATTR_ITEMS)).andReturn(false);
		AndroidMock.expect(json.getJSONArray(DefaultSocializeProvider.JSON_ATTR_ITEMS)).andReturn(jsonArray);
		
		AndroidMock.expect(jsonArray.length()).andReturn(arrayLength);
		AndroidMock.expect(jsonArray.getJSONObject(0)).andReturn(json);
		AndroidMock.expect(jsonArray.getJSONObject(1)).andReturn(json);
		AndroidMock.expect(jsonArray.getJSONObject(2)).andReturn(json);
		AndroidMock.expect(objectFactory.fromJSON(json)).andReturn(object).times(arrayLength);
		AndroidMock.expect(httpUtils.isHttpError(response)).andReturn(false);
		AndroidMock.expect(session.getHost()).andReturn(host);
		
		entity.consumeContent();
		
		AndroidMock.replay(session);
		AndroidMock.replay(sessionFactory);
		AndroidMock.replay(clientFactory);
		AndroidMock.replay(objectFactory);
		AndroidMock.replay(requestFactory);
		AndroidMock.replay(jsonParser);
		AndroidMock.replay(client);
		AndroidMock.replay(json);
		AndroidMock.replay(jsonArray);
		AndroidMock.replay(userFactory);
		AndroidMock.replay(entity);
		AndroidMock.replay(response);
		AndroidMock.replay(httpUtils);
		AndroidMock.replay(ioUtils);
		
		DefaultSocializeProvider<SocializeObject> provider = getNewProvider();
		
		ListResult<SocializeObject> list = provider.list(session, endpoint, key, ids);
		
		AndroidMock.verify(session);
		AndroidMock.verify(sessionFactory);
		AndroidMock.verify(clientFactory);
		AndroidMock.verify(requestFactory);
		AndroidMock.verify(jsonParser);
		AndroidMock.verify(client);
		AndroidMock.verify(json);
		AndroidMock.verify(jsonArray);
		AndroidMock.verify(userFactory);
		AndroidMock.verify(entity);
		AndroidMock.verify(response);
		AndroidMock.verify(objectFactory);
		AndroidMock.verify(httpUtils);
		AndroidMock.verify(ioUtils);
		
		for (SocializeObject gotten : list.getResults()) {
			assertSame(object,gotten);
		}
	}
	
	
	public void testPost() throws Exception {
		
		final String endpoint = "foobar/";
		final String host = "host";
		final String url = "http://" + host + "/" + endpoint;
		final SocializeObject object = new SocializeObject();
		
		final int arrayLength = 3;
		
		
		AndroidMock.expect(clientFactory.getClient()).andReturn(client);
		AndroidMock.expect(requestFactory.getPostRequest(session, url, object)).andReturn(request);
		AndroidMock.expect(client.execute(request)).andReturn(response);
		AndroidMock.expect(response.getEntity()).andReturn(entity);
		AndroidMock.expect(entity.getContent()).andReturn(null);
		
		AndroidMock.expect(ioUtils.readSafe(null)).andReturn(jsonString);
		AndroidMock.expect(jsonParser.parseObject(jsonString)).andReturn(json);
		AndroidMock.expect(json.has(DefaultSocializeProvider.JSON_ATTR_ERRORS)).andReturn(false);
		AndroidMock.expect(json.has(DefaultSocializeProvider.JSON_ATTR_ITEMS)).andReturn(true);
		AndroidMock.expect(json.isNull(DefaultSocializeProvider.JSON_ATTR_ITEMS)).andReturn(false);
		AndroidMock.expect(json.getJSONArray(DefaultSocializeProvider.JSON_ATTR_ITEMS)).andReturn(jsonArray);
		
		AndroidMock.expect(jsonArray.length()).andReturn(arrayLength);
		AndroidMock.expect(jsonArray.getJSONObject(0)).andReturn(json);
		AndroidMock.expect(jsonArray.getJSONObject(1)).andReturn(json);
		AndroidMock.expect(jsonArray.getJSONObject(2)).andReturn(json);
		AndroidMock.expect(objectFactory.fromJSON(json)).andReturn(object).times(arrayLength);
		AndroidMock.expect(httpUtils.isHttpError(response)).andReturn(false);
		AndroidMock.expect(session.getHost()).andReturn(host);
		
		entity.consumeContent();
		
		AndroidMock.replay(session);
		AndroidMock.replay(sessionFactory);
		AndroidMock.replay(clientFactory);
		AndroidMock.replay(objectFactory);
		AndroidMock.replay(requestFactory);
		AndroidMock.replay(jsonParser);
		AndroidMock.replay(client);
		AndroidMock.replay(json);
		AndroidMock.replay(jsonArray);
		AndroidMock.replay(userFactory);
		AndroidMock.replay(entity);
		AndroidMock.replay(response);
		AndroidMock.replay(httpUtils);
		AndroidMock.replay(ioUtils);
		
		DefaultSocializeProvider<SocializeObject> provider = getNewProvider();
		
		ListResult<SocializeObject> list = provider.post(session, endpoint, object);
		
		AndroidMock.verify(session);
		AndroidMock.verify(sessionFactory);
		AndroidMock.verify(clientFactory);
		AndroidMock.verify(requestFactory);
		AndroidMock.verify(jsonParser);
		AndroidMock.verify(client);
		AndroidMock.verify(json);
		AndroidMock.verify(jsonArray);
		AndroidMock.verify(userFactory);
		AndroidMock.verify(entity);
		AndroidMock.verify(response);
		AndroidMock.verify(objectFactory);
		AndroidMock.verify(httpUtils);
		AndroidMock.verify(ioUtils);
		
		for (SocializeObject gotten : list.getResults()) {
			assertSame(object,gotten);
		}
	}

	public void testPostCollection() throws Exception {
		
		final String endpoint = "foobar/";
		final SocializeObject object0 = new SocializeObject();
		final SocializeObject object1 = new SocializeObject();
		final List<SocializeObject> objects = new ArrayList<SocializeObject>();
		
		final String host = "host";
		final String url = "http://" + host + "/" + endpoint;
		
		objects.add(object0);
		objects.add(object1);
		
		final int arrayLength = objects.size();
		
		AndroidMock.expect(clientFactory.getClient()).andReturn(client);
		AndroidMock.expect(requestFactory.getPostRequest(session, url, objects)).andReturn(request);
		AndroidMock.expect(client.execute(request)).andReturn(response);
		AndroidMock.expect(response.getEntity()).andReturn(entity);
		AndroidMock.expect(entity.getContent()).andReturn(null);

		AndroidMock.expect(ioUtils.readSafe(null)).andReturn(jsonString);
		AndroidMock.expect(jsonParser.parseObject(jsonString)).andReturn(json);
		AndroidMock.expect(json.has(DefaultSocializeProvider.JSON_ATTR_ERRORS)).andReturn(false);
		AndroidMock.expect(json.has(DefaultSocializeProvider.JSON_ATTR_ITEMS)).andReturn(true);
		AndroidMock.expect(json.isNull(DefaultSocializeProvider.JSON_ATTR_ITEMS)).andReturn(false);
		AndroidMock.expect(json.getJSONArray(DefaultSocializeProvider.JSON_ATTR_ITEMS)).andReturn(jsonArray);
		
		AndroidMock.expect(jsonArray.length()).andReturn(arrayLength);
		AndroidMock.expect(jsonArray.getJSONObject(0)).andReturn(json);
		AndroidMock.expect(jsonArray.getJSONObject(1)).andReturn(json);
		AndroidMock.expect(objectFactory.fromJSON(json)).andReturn(object0).once();
		AndroidMock.expect(objectFactory.fromJSON(json)).andReturn(object1).once();
		AndroidMock.expect(httpUtils.isHttpError(response)).andReturn(false);
		AndroidMock.expect(session.getHost()).andReturn(host);
		
		entity.consumeContent();
		
		AndroidMock.replay(session);
		AndroidMock.replay(sessionFactory);
		AndroidMock.replay(clientFactory);
		AndroidMock.replay(objectFactory);
		AndroidMock.replay(requestFactory);
		AndroidMock.replay(jsonParser);
		AndroidMock.replay(client);
		AndroidMock.replay(json);
		AndroidMock.replay(jsonArray);
		AndroidMock.replay(userFactory);
		AndroidMock.replay(entity);
		AndroidMock.replay(response);
		AndroidMock.replay(httpUtils);
		AndroidMock.replay(ioUtils);
		
		DefaultSocializeProvider<SocializeObject> provider = getNewProvider();
		
		ListResult<SocializeObject> list = provider.post(session, endpoint, objects);
		
		assertEquals(objects.size(), list.getResults().size());
		
		for (SocializeObject gotten : list.getResults()) {
			assertTrue(objects.contains(gotten));
		}
		
		AndroidMock.verify(session);
		AndroidMock.verify(sessionFactory);
		AndroidMock.verify(clientFactory);
		AndroidMock.verify(requestFactory);
		AndroidMock.verify(jsonParser);
		AndroidMock.verify(client);
		AndroidMock.verify(json);
		AndroidMock.verify(jsonArray);
		AndroidMock.verify(userFactory);
		AndroidMock.verify(entity);
		AndroidMock.verify(response);
		AndroidMock.verify(objectFactory);
		AndroidMock.verify(httpUtils);
		AndroidMock.verify(ioUtils);
	}
	
	public void testPut() throws Exception {
		
		final String endpoint = "foobar/";
		final String host = "host";
		final String url = "http://" + host + "/" + endpoint;
		final SocializeObject object = new SocializeObject();
		
		final int arrayLength = 3;
		
		AndroidMock.expect(clientFactory.getClient()).andReturn(client);
		AndroidMock.expect(requestFactory.getPutRequest(session, url, object)).andReturn(request);
		AndroidMock.expect(client.execute(request)).andReturn(response);
		AndroidMock.expect(response.getEntity()).andReturn(entity);
		AndroidMock.expect(entity.getContent()).andReturn(null);

		AndroidMock.expect(ioUtils.readSafe(null)).andReturn(jsonString);
		AndroidMock.expect(jsonParser.parseObject(jsonString)).andReturn(json);
		AndroidMock.expect(json.has(DefaultSocializeProvider.JSON_ATTR_ERRORS)).andReturn(false);
		AndroidMock.expect(json.has(DefaultSocializeProvider.JSON_ATTR_ITEMS)).andReturn(true);
		AndroidMock.expect(json.isNull(DefaultSocializeProvider.JSON_ATTR_ITEMS)).andReturn(false);
		AndroidMock.expect(json.getJSONArray(DefaultSocializeProvider.JSON_ATTR_ITEMS)).andReturn(jsonArray);
		
		AndroidMock.expect(jsonArray.length()).andReturn(arrayLength);
		AndroidMock.expect(jsonArray.getJSONObject(0)).andReturn(json);
		AndroidMock.expect(jsonArray.getJSONObject(1)).andReturn(json);
		AndroidMock.expect(jsonArray.getJSONObject(2)).andReturn(json);
		AndroidMock.expect(objectFactory.fromJSON(json)).andReturn(object).times(arrayLength);
		AndroidMock.expect(httpUtils.isHttpError(response)).andReturn(false);
		AndroidMock.expect(session.getHost()).andReturn(host);
		
		entity.consumeContent();
		
		AndroidMock.replay(session);
		AndroidMock.replay(sessionFactory);
		AndroidMock.replay(clientFactory);
		AndroidMock.replay(objectFactory);
		AndroidMock.replay(requestFactory);
		AndroidMock.replay(jsonParser);
		AndroidMock.replay(client);
		AndroidMock.replay(json);
		AndroidMock.replay(jsonArray);
		AndroidMock.replay(userFactory);
		AndroidMock.replay(entity);
		AndroidMock.replay(response);
		AndroidMock.replay(httpUtils);
		AndroidMock.replay(ioUtils);
		
		DefaultSocializeProvider<SocializeObject> provider = getNewProvider();
		
		ListResult<SocializeObject> list = provider.put(session, endpoint, object);
		
		AndroidMock.verify(session);
		AndroidMock.verify(sessionFactory);
		AndroidMock.verify(clientFactory);
		AndroidMock.verify(requestFactory);
		AndroidMock.verify(jsonParser);
		AndroidMock.verify(client);
		AndroidMock.verify(json);
		AndroidMock.verify(jsonArray);
		AndroidMock.verify(userFactory);
		AndroidMock.verify(entity);
		AndroidMock.verify(response);
		AndroidMock.verify(objectFactory);
		AndroidMock.verify(httpUtils);
		AndroidMock.verify(ioUtils);
		
		for (SocializeObject gotten : list.getResults()) {
			assertSame(object,gotten);
		}
	}
	
	public void testPutCollection() throws Exception {
		
		final String endpoint = "foobar/";
		final String host = "host";
		final String url = "http://" + host + "/" + endpoint;
		final SocializeObject object0 = new SocializeObject();
		final SocializeObject object1 = new SocializeObject();
		final List<SocializeObject> objects = new ArrayList<SocializeObject>();
		
		objects.add(object0);
		objects.add(object1);
		
		final int arrayLength = objects.size();
		
		AndroidMock.expect(clientFactory.getClient()).andReturn(client);
		AndroidMock.expect(requestFactory.getPutRequest(session, url, objects)).andReturn(request);
		AndroidMock.expect(client.execute(request)).andReturn(response);
		AndroidMock.expect(response.getEntity()).andReturn(entity);
		AndroidMock.expect(entity.getContent()).andReturn(null);

		AndroidMock.expect(ioUtils.readSafe(null)).andReturn(jsonString);
		AndroidMock.expect(jsonParser.parseObject(jsonString)).andReturn(json);
		AndroidMock.expect(json.has(DefaultSocializeProvider.JSON_ATTR_ERRORS)).andReturn(false);
		AndroidMock.expect(json.has(DefaultSocializeProvider.JSON_ATTR_ITEMS)).andReturn(true);
		AndroidMock.expect(json.isNull(DefaultSocializeProvider.JSON_ATTR_ITEMS)).andReturn(false);
		AndroidMock.expect(json.getJSONArray(DefaultSocializeProvider.JSON_ATTR_ITEMS)).andReturn(jsonArray);
		
		AndroidMock.expect(jsonArray.length()).andReturn(arrayLength);
		AndroidMock.expect(jsonArray.getJSONObject(0)).andReturn(json);
		AndroidMock.expect(jsonArray.getJSONObject(1)).andReturn(json);
		AndroidMock.expect(objectFactory.fromJSON(json)).andReturn(object0).once();
		AndroidMock.expect(objectFactory.fromJSON(json)).andReturn(object1).once();
		AndroidMock.expect(httpUtils.isHttpError(response)).andReturn(false);
		AndroidMock.expect(session.getHost()).andReturn(host);
		
		entity.consumeContent();
		
		AndroidMock.replay(session);
		AndroidMock.replay(sessionFactory);
		AndroidMock.replay(clientFactory);
		AndroidMock.replay(objectFactory);
		AndroidMock.replay(requestFactory);
		AndroidMock.replay(jsonParser);
		AndroidMock.replay(client);
		AndroidMock.replay(json);
		AndroidMock.replay(jsonArray);
		AndroidMock.replay(userFactory);
		AndroidMock.replay(entity);
		AndroidMock.replay(response);
		AndroidMock.replay(httpUtils);
		AndroidMock.replay(ioUtils);
		
		DefaultSocializeProvider<SocializeObject> provider = getNewProvider();
		
		ListResult<SocializeObject> list = provider.put(session, endpoint, objects);
		
		assertEquals(objects.size(), list.getResults().size());
		
		for (SocializeObject gotten : list.getResults()) {
			assertTrue(objects.contains(gotten));
		}
		
		AndroidMock.verify(session);
		AndroidMock.verify(sessionFactory);
		AndroidMock.verify(clientFactory);
		AndroidMock.verify(requestFactory);
		AndroidMock.verify(jsonParser);
		AndroidMock.verify(client);
		AndroidMock.verify(json);
		AndroidMock.verify(jsonArray);
		AndroidMock.verify(userFactory);
		AndroidMock.verify(entity);
		AndroidMock.verify(response);
		AndroidMock.verify(objectFactory);
		AndroidMock.verify(httpUtils);
		AndroidMock.verify(ioUtils);
	}
	
}
