package com.example.mynet;

import com.alibaba.fastjson.annotation.JSONField;

public class Response{

	@JSONField(name="createdAt")
	private int createdAt;

	@JSONField(name="session")
	private Session session;

	@JSONField(name="truncated")
	private boolean truncated;

	@JSONField(name="error")
	private int error;

	@JSONField(name="statusCode")
	private int statusCode;

	@JSONField(name="token")
	private String token;

	public int getCreatedAt(){
		return createdAt;
	}

	public Session getSession(){
		return session;
	}

	public boolean isTruncated(){
		return truncated;
	}

	public int getError(){
		return error;
	}

	public int getStatusCode(){
		return statusCode;
	}

	public String getToken(){
		return token;
	}
}