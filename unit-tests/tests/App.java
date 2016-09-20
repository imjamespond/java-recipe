package test;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	System.out.println( Long.MAX_VALUE );
    	
    	long now = System.currentTimeMillis();
    	System.out.println( now );
    	System.out.println( now % 20000l );
    	System.out.println( Long.toHexString(now));    
        
        AtomicInteger aint = new AtomicInteger();
        System.out.println( String.format("%s%d%03d", '2', now, 1));
        
    	int k = 2147483647;	
    	int i = 1;
    	for(int j=0;j<32;++j){
    		i |= 1<<j;
    		System.out.printf("toBinaryString:%-32s, bit:%d, value:%d\n",Integer.toBinaryString(i), i, k++);
    	}
    	
    	
    	Calendar date = Calendar.getInstance();
    	System.out.println(String.format("week:%d, %d", date.get(Calendar.WEEK_OF_YEAR), date.getWeeksInWeekYear()));
    }
    
    private static final Map<Integer, String> myMap;
    static {
        Map<Integer, String> aMap = new HashMap<Integer, String>();
        aMap.put(1, "one");
        aMap.put(2, "two");
        myMap = Collections.unmodifiableMap(aMap);
    }
}
