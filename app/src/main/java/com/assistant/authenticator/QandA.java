package com.assistant.authenticator;

public class QandA {
	String Username;
	long id;
	long Qno;
	String Q;;
	String A;
	
	public QandA(){}
	
	public QandA(String uname,long no,String q,String a){
		Username=uname;
		Qno=no;
		Q=q;
		A=a;
	}
	
	public void setID(long i){
		id=i;
	}
	
	public void setUserName(String u){
		Username=u;
	}
	
	public void setQno(long no){
		Qno=no;
	}
	
	public void setQ(String q){
		Q=q;
	}
	
	public void setA(String a){
		A=a;
	}
	
	public void setQandA(String q,String a){
		Q=q;
		A=a;
	}
	
	public String getUserName(){
		return Username;
	}
	
	public long getQno(){
		return Qno;
	}
	
	public String getQ(){
		return Q;
	}
	
	public String getA(){
		return A;
	}
	
	public long getID(){
		return id;
	}
	
}
