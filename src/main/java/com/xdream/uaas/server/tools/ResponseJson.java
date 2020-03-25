package com.xdream.uaas.server.tools;

import java.util.List;

import com.xdream.JsonUtils;

public class ResponseJson {
     
	private  List<ResponseCard> cards;
	
	private String time_used;
	
	private String request_id;


	public List<ResponseCard> getCards() {
		return cards;
	}



	public void setCards(List<ResponseCard> cards) {
		this.cards = cards;
	}



	public String getTime_used() {
		return time_used;
	}

	public void setTime_used(String time_used) {
		this.time_used = time_used;
	}

	public String getRequest_id() {
		return request_id;
	}

	public void setRequest_id(String request_id) {
		this.request_id = request_id;
	}
	
	
}
