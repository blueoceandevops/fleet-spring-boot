package com.fleet.socket.entity;

import java.io.Serializable;

public class Msg implements Serializable {

	private static final long serialVersionUID = 1L;

	private long timestamp;

	private String msg;

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Msg(long timestamp, String msg) {
		this.timestamp = timestamp;
		this.msg = msg;
	}

	public Msg() {
	}
}
