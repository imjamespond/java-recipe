package test;

public class HashTest {

	public static void main(String[] args) {
		for(int i=0;i<1000;i++){
			System.out.println(fashHash(i*100));
		}
	}
    public static final long fashHash(long a) {         
        a ^= (a << 23);
        a ^= (a >>> 29);        
        a ^= (a << 11);
        return a;    
    }
}
