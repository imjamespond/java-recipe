package test;

import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

public class BytesTest {

	public static void main(String[] args) {
		
		byte[] bytes = null;
		
		//serialization
		{
			Set<Long> set = new HashSet<Long>(); 
			set.add(123l);
			set.add(999l);
			
			int length = 1 + set.size() * (8);// set长度*(8)
			ByteBuffer buffer = ByteBuffer.allocate(length);
			//version
			byte version = 'a';
			buffer.put(version);
			//put data
			if(version == 'a'){
				for (Long userId : set) {
					buffer.putLong(userId);
				}
			}
			bytes = buffer.array();
		}
		
		//unserialization
		{
			Set<Long> set = new HashSet<Long>(10);
			ByteBuffer buffer = ByteBuffer.wrap(bytes);
			byte version = '\0';
			//read version	
			if(buffer.remaining()>0){
				version = buffer.get();
				System.out.println("buffer.remaining:"+buffer.remaining());
			}
			//read data
			if(version == 'a'){
				while (buffer.remaining() >= 8) {
					long userId = buffer.getLong();
					set.add(userId);
					System.out.println("buffer.remaining:"+buffer.remaining());
				}	
			}//else other version...
			
			System.out.println(set);
		}
	}

}
