package com.assistant.authenticator;

public class User {
	long user_id;
	String username;
	
	public User(String uname){
		username=uname;
	}
	
	public void setID(long id){
		user_id=id;
	}
	
	public long getID(){
		return user_id;
	}
	
	public String getUserName(){
		return username;
	}

}
