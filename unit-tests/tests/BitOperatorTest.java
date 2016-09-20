package test;

public class BitOperatorTest {

	public static void main(String[] args) {		
    	int k = 2147483647;	
    	int i = 0;
    	for(int j=0;j<32;++j){
    		i |= 1<<j;
    		System.out.printf("toBinaryString:%-32s, bit:%d, value:%d\n",Integer.toBinaryString(i), i, k++);
    	}
	}

}
