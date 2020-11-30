package com.example.mynet;

import com.alibaba.fastjson.annotation.JSONField;

public class Session{

	@JSONField(name="keepalive_interval")
	private int keepaliveInterval;

	@JSONField(name="token_expires_in")
	private int tokenExpiresIn;

	@JSONField(name="keepalive")
	private boolean keepalive;

	@JSONField(name="context")
	private String context;

	@JSONField(name="started_at")
	private int startedAt;

	@JSONField(name="id")
	private String id;

	@JSONField(name="network_changed")
	private boolean networkChanged;

	public int getKeepaliveInterval(){
		return keepaliveInterval;
	}

	public int getTokenExpiresIn(){
		return tokenExpiresIn;
	}

	public boolean isKeepalive(){
		return keepalive;
	}

	public String getContext(){
		return context;
	}

	public int getStartedAt(){
		return startedAt;
	}

	public String getId(){
		return id;
	}

	public boolean isNetworkChanged(){
		return networkChanged;
	}
}