package com.metasoft.flying.util;

public class MathUtils {
    public static final int fashHash(int a) {         
          a ^= (a << 13);
          a ^= (a >>> 17);        
          a ^= (a << 5);
          return a;   
    }
    public static final long fashHash(long a) {         
        a ^= (a << 23);
        a ^= (a >>> 29);        
        a ^= (a << 11);
        return a;    
    }
}
