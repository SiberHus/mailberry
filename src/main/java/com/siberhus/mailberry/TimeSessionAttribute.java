package com.siberhus.mailberry;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

public class TimeSessionAttribute extends TimerTask implements HttpSessionBindingListener{
	
	private HttpSession session;
	private String attributeName;
	private Object object;
	private int period;
	private Timer timer = new Timer(true);
	
	public TimeSessionAttribute(Object object, int period){
		this.object = object;
		this.period = period;
		timer.schedule(this, period, period);
	}
	
	public Object getObject(){
		//reset timer
		timer.cancel();
		timer = new Timer(true);
		timer.schedule(this, 0, period);
		return object;
	}
	
	@Override
	public void run() {
		if(session!=null){
			session.removeAttribute(attributeName);
		}
	}
	
	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		this.session = event.getSession();
		this.attributeName = event.getName();
		Object attr = session.getAttribute(attributeName);
		if(attr!=null && attr instanceof TimeSessionAttribute){
			((TimeSessionAttribute)attr).timer.cancel();
		}
	}
	
	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		timer.cancel();
		timer = null;
		session = null;
		attributeName = null;
		object = null;
	}
	
	public static void main(String[] args) throws Exception{
		Object a1 = new Object();
		TimeSessionAttribute t1 = new TimeSessionAttribute(a1, 3000);
		System.out.println(">>>>");
		TimeSessionAttribute t2 = new TimeSessionAttribute(a1, 3000);
		t2.getObject();
		Thread.sleep(4000);
	}
}
