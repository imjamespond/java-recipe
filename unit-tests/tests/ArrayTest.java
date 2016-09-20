package test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.collections.buffer.CircularFifoBuffer;

public class ArrayTest {

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
//		ArrayDeque<Integer> arrDeque = new ArrayDeque<Integer>();
//		
//		for(int i=0;i<20;i++){
//			arrDeque.addLast(i);
//		}
//		for(int i=0;i<20;i++){
//			arrDeque.pop();
//		}
//		for(int i=0;i<20;i++){
//			arrDeque.addLast(i);
//		}
		//implement circular loop
		int elementsLength = 32;//must be power of 2
		int tail = 20;
		for(int i=0;i<35;i++){
			tail = (tail + 1) & (elementsLength - 1);//& 11111
			System.out.println(tail);
		}
		
		//删除链接
		Deque<Integer> deq = new LinkedList<Integer>();
		deq.add(1);
		deq.add(2);
		deq.add(3);
		deq.add(4);
		deq.add(5);
		deq.add(6);
		System.out.println(deq.toString());
		Iterator<Integer> it = deq.iterator();
		while(it.hasNext()){
			int i = it.next();
			if(i == 4)
				it.remove();
		}
		System.out.println(deq.toString());
		
/*		
		LinkedBlockingQueue<Integer> blist = new LinkedBlockingQueue<Integer>(4);
		blist.put(1);
		blist.put(11);
		blist.put(111);
		blist.put(1111);
		blist.put(11111);//block here
		System.out.print(blist);*/
		
// CircularFifoBuffer is a first in first out buffer with a fixed size that replaces its oldest element if full.
// The removal order of a CircularFifoBuffer is based on the insertion order; elements are removed in the same order in which they were added. The iteration order is the same as the removal order.
// The add(Object), BoundedFifoBuffer.remove() and BoundedFifoBuffer.get() operations all perform in constant time. All other operations perform in linear time or worse.
// Note that this implementation is not synchronized. The following can be used to provide synchronized access to your CircularFifoBuffer:
//    Buffer fifo = BufferUtils.synchronizedBuffer(new CircularFifoBuffer());
// This buffer prevents null objects from being added. 		
		CircularFifoBuffer cfb = new CircularFifoBuffer(4);		
		cfb.add(1);
		cfb.add(11);
		cfb.add(111);
		cfb.add(1111);
		cfb.add(11111);//block here
		System.out.print(cfb);
		
		
		

		List<Integer> list = new ArrayList<Integer>(4);
			
		System.out.println(String.format("list size:%d", list.size()));
		
		
		Map<String, Test> testMap = new HashMap<String, Test>();
		
		long now = System.currentTimeMillis();
		for(int i = 0; i<1000000;i++){
			//testMap.put(String.valueOf(i), new Test(i));
		}
		System.out.println(System.currentTimeMillis()-now);
		now = System.currentTimeMillis();
		for(Entry<String,Test> entry:testMap.entrySet()){
			Test test = entry.getValue();
			test.check();
		}
		System.out.println(System.currentTimeMillis()-now);
		
		Set<ChessApplicationVO> appMap = new TreeSet<ChessApplicationVO>();
		ChessApplicationVO vo1 = new ChessApplicationVO();
		vo1.rose = 10;
		vo1.userId = 1;
		ChessApplicationVO vo2 = new ChessApplicationVO();
		vo2.rose = 50;
		vo2.userId = 2;
		ChessApplicationVO vo3 = new ChessApplicationVO();
		vo3.rose = 20;
		vo3.userId = 3;
		ChessApplicationVO vo4 = new ChessApplicationVO();
		vo4.rose = 20;
		vo4.userId = 4;
		
		appMap.add(vo1);
		appMap.add(vo2);
		appMap.add(vo3);
		appMap.add(vo4);
		for(ChessApplicationVO entry:appMap){
			System.out.println("userId:"+entry.userId);
		}
		

	    Map<Long,ChessApplicationVO> map = new HashMap<Long,ChessApplicationVO>();
	    map.put(vo1.userId, vo1);
	    map.put(vo2.userId, vo2);
	    map.put(vo3.userId, vo3);
	    map.put(vo4.userId, vo4);
	    System.out.println(map);
	    // prints "{A=3, B=2, C=1}"
	    System.out.println(entriesSortedByValues(map));
	    // prints "[C=1, B=2, A=3]"
	    for(ChessApplicationVO vo:mapByValues(map)){
	    	System.out.println(vo.rose);
	    }
	}
	
	static <K,V extends Comparable<? super V>>
	SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
	    SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
	        new Comparator<Map.Entry<K,V>>() {
	            @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
	                int res = e1.getValue().compareTo(e2.getValue());
	                return res != 0 ? res : 1;
	            }
	        }
	    );
	    sortedEntries.addAll(map.entrySet());
	    return sortedEntries;
	} 
	
	static List<ChessApplicationVO> mapByValues(Map<Long,ChessApplicationVO> map) {
	     List<ChessApplicationVO> list = new LinkedList<ChessApplicationVO>();
	     list.addAll(map.values());
	     Collections.sort(list, new Comparator<ChessApplicationVO>() {
			@Override
			public int compare(ChessApplicationVO o1, ChessApplicationVO o2) {
				if(o1.rose - o2.rose>0){
					return 1;
				}
				return -1;
			}
	     });     
		return list;
	} 
}
class Test{
	long deadline;
	int i;
	
	public Test(int i) {
		this.i = i;
		deadline = System.currentTimeMillis() + 100;
	}

	public void check(){
		long now = System.currentTimeMillis();
		if(deadline>now){
			return;
		}else{
			//System.out.println();
		}
	}
}

class ChessApplicationVO implements Comparable<ChessApplicationVO>{

	//@Desc("id")
	public long userId;
	//@Desc("玩家")
	public String userName;
	//@Desc("申请下棋时间")
	public long appTime;
	//@Desc("玫瑰")
	public int rose;
	@Override
	public int compareTo(ChessApplicationVO o) {
		if(rose > o.rose){
			return -1;
		}
		return 1;
	}
}