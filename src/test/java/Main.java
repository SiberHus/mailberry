import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import org.apache.commons.lang.math.RandomUtils;


public class Main {

	public static void subList(){
		int[] ids = new int[]{3,5,6,1,7,9,16,29,38};
		System.out.println(ids.length);
		double n = 20;
		int loop = (int)Math.ceil(ids.length/n);
		System.out.println(loop);
		for(int i=0;i<ids.length;i+=n){
			final List<Integer> values = new ArrayList<Integer>();
			for(int j=0;j<n;j++){
				if(i+j<ids.length) values.add(ids[i+j]);
			}
			System.out.println(values);
		}
	}
	
	public static void highLoadOfTimerTask() throws Exception{
		final AtomicInteger counter = new AtomicInteger();
		for(int i=0;i<20000;i++){
			TimerTask task = new TimerTask(){
				@Override
				public void run() {
					counter.incrementAndGet();
//					System.out.println("Hello "+Thread.currentThread().getId());
				}
			};
			Timer timer = new Timer(true);
			timer.schedule(task, new Date(new Date().getTime()+200));
		}
		System.out.println("FINISH");
		Thread.sleep(5*1000);//10 seconds
		System.gc();
		for(int i=0;i<20000;i++){
			TimerTask task = new TimerTask(){
				@Override
				public void run() {
					counter.incrementAndGet();
//					System.out.println("Hello "+Thread.currentThread().getId());
				}
			};
			Timer timer = new Timer(true);
			timer.schedule(task, new Date(new Date().getTime()+200));
		}
		Thread.sleep(5*1000);//10 seconds
		System.out.println("HELLO = "+counter.intValue());
	}
	
	public static void main(String[] args)throws Exception {
		Set<Integer> s = new HashSet<Integer>();
		for(int i=0;i<1000000;i++){
			s.add(RandomUtils.nextInt(100000000));
		}
		System.out.println(s.size());
	}
	
}
