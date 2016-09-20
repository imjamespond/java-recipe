package test;

public class ModTest {

	public static void main(String[] args) {
		final long MAX = 999999999;
		final long MOD = 126;
		long begin = System.currentTimeMillis();
		for(long i=0;i<MAX; i++){
			long rs = i%MOD;
		}
		long end1 = System.currentTimeMillis();
		System.out.println(end1 - begin);
		for(long i=0;i<MAX; i++){
			long rs = i&(MOD-1);
		}
		long end2 = System.currentTimeMillis();
		System.out.println(end2 - end1);
	}

}
