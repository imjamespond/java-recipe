package com;

import java.io.*;
import java.util.ArrayList;


public class FileUtils {
	public static byte[] readByteDataFromFile(String filename) 
					throws IOException {
		byte[] buffer = new byte[1024*500];

		FileInputStream in = new FileInputStream(filename);

		int totallen = 0;
		int len;
		int offset = 0;
		while((len = in.read(buffer, offset, 1024)) != -1){
			totallen += len;
			offset += 1024;
		}

		byte[] data = new byte[totallen];
		for(int i=0; i<totallen; i++){
			data[i] = buffer[i];
		}
		
		in.close();

		return data;
	}			
	
	public static void writeByteDataToFile(String filename, byte[] imageData) 
												throws IOException {
	    BufferedOutputStream out = null;
	    FileOutputStream fout = null;
	    try{
	        fout = new FileOutputStream(filename);
	        out = new BufferedOutputStream(fout);
								
	        if(imageData == null){
	            System.out.println("ERROR: empty Feature data");
	            return;
	        }
	        
		    out.write(imageData);
		    
		    out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            safeClose(out);
            safeClose(fout);            
        }						
	}		
	
	
	public static void writeStringToFile(String filename, String str)
											 throws IOException {
		BufferedWriter writer = new BufferedWriter (
								new FileWriter(filename));
		if(writer == null){
			System.out.println("ERROR: empty Feature data");
			return;
		}	
		
		writer.write(str);
		
		writer.flush();
		
		writer.close();			
	}		


	@SuppressWarnings("unused")
	public static void appendStringToFile(String filename, String str)
											 throws IOException {
		BufferedWriter writer = new BufferedWriter (
								new FileWriter(filename, true));
		if(writer == null){
			System.out.println("ERROR: empty Feature data");
			return;
		}	
		
		writer.write(str);
		
		writer.flush();
		
		writer.close();			
	}		
	
	public static String readStringFromFile(String filename) throws IOException{
		return readStringFromFile(filename, null);
	}

	@SuppressWarnings("unused")
	public static String readStringFromFile(String filename, String encoding)
			throws IOException {
		BufferedReader reader = null;
		
        // MIG-CHANGE: in = new BufferedReader(new FileReader(new File(filename)));
		if(encoding == null || "".equals(encoding)){
			reader = new BufferedReader(new FileReader(filename));
		}else{			
			try {
				reader = new BufferedReader(new InputStreamReader(	
					new FileInputStream(filename), encoding));
			} catch (UnsupportedEncodingException e) {
				// Use default if the given encoding can't be found
				reader = new BufferedReader(new FileReader(filename));
			}
		}

		if (reader == null) {
			System.out.println("ERROR: invalid reader");
			return "";
		}

		StringBuffer sb = new StringBuffer();
		String line = "";
		boolean first = true;
		while (true) {
			line = reader.readLine();
			if (line == null) {
				break;
			}
			
			if(first == true){
				first = false;
			}else{
				sb.append("\n");
			}
			sb.append(line);			
		}

		reader.close();

		return sb.toString();
	}	
	
	// Method required the following Base64 Library
	/* 
	import org.apache.commons.codec.binary.Base64;
	public static String getBase64DataFromFile(String filename) 
												throws IOException {
		// Step 1: Read the binary data of the specified file
		File f = new File(filename);			
		FileInputStream stm = new FileInputStream(f);
							
		int len = (int) f.length(); 
		byte[] data = new byte[len];
			
		stm.read(data, 0, len);			
				
		stm.close();
				
		// Step 2: Binary byte => String
		byte[] encodedByte = Base64.encodeBase64(data);
		
		return new String(encodedByte);		
	} 
	
	public static boolean writeBase64DataToFile(String filename, String data) 
												throws IOException {
		if(filename == null || data == null){
			return false;													
		}
		// Step 1: Read the binary data of the specified file
		File f = new File(filename);			
		FileOutputStream stm = new FileOutputStream(f);
		
		// Check null
		if(stm == null){
			System.out.println("Null FileOutputStream");
		}		
		// Step 0: data String => byte;
		byte[] dataByte = data.getBytes();

		if(dataByte == null){
			System.out.println("Null data");
		}
											
		// Step 1: String => byte		
		byte[] outdata = Base64.decodeBase64(dataByte);

		
		// Step 2: write to file;
		stm.write(outdata);				
				
		stm.close();		
				
		return true;		
	} */
	
	
	public static void copyFile(String srcFile, String dstFile)
												throws IOException {
		byte[] buffer = new byte[4096];
		// You can change the size of this if you want.
		
		if((new File(dstFile)).isDirectory()){
			String dstPath = dstFile;
			dstFile = dstPath + "/" + (new File(srcFile)).getName();
		}

		FileInputStream in = new FileInputStream(srcFile);
		FileOutputStream out = new FileOutputStream(dstFile);
		int len;
		while ((len = in.read(buffer)) != -1) {
			out.write(buffer, 0, len);
		}
		out.close();
		in.close();
	}

	public static void safeClose(OutputStream os){
	    if(os == null){
	        return;
	    }
	    
	    try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public static void safeClose(InputStream is){
	    if(is == null){
	        return;
	    }
	    
	    try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	/**
	 *  
	 */
	public static boolean createDir(String path, boolean makeParents){
	    // Create a directory; all ancestor directories must exist
		if(makeParents){
			return (new File(path)).mkdirs();
		}else{
			return (new File(path)).mkdir();
		}
	}
	
	public static String refineFilename(Object filename){
		return refineFilename((String) filename);
	}
	
	public static String refineFilename(String filename){
		if(filename == null){
			return "";
		}
		
		return filename.replaceAll(" ", "_").toLowerCase();
	}

	/**
	 * @param gameName
	 * @return
	 */
	public static String toDirName(String s) {
		// TODO Auto-generated method stub
		if (s == null)
			return null;
		s = s.toLowerCase();
		char ac[] = new char[s.length()];
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == ' ')
				ac[i] = '_';
			else
				ac[i] = c;
		}

		return new String(ac);
	}

	public static ArrayList<File> getDirFiles(String path, String prefix, String ext){
	    File dir = new File(path);
	    
	    File[] files = dir.listFiles();
	    if(files == null){
	        return new ArrayList<File>();
	    }
	    
	    ArrayList<File> list = new ArrayList<File>(); 
	    for(int i=0; i<files.length; i++){
	        String filename = files[i].getName().toLowerCase();
	        
	        if(prefix != null && filename.startsWith(prefix) == false){
	            continue;
	        }
	        
	        if(ext != null && filename.endsWith("." + ext) == false){
	            continue;
	        }
	        
	        list.add(files[i]);	    
	    }
	    
	    return list;
	}
	
	public static byte[] packZeroByte(byte[] input, int size){
		if(input == null){
			return new byte[size];
		}

		byte[] bytedata = new byte[size];
		
		int i=0;
		for(i=0; i<size && i< input.length; i++){
			bytedata[i] = input[i];
		}
		for(;i<size; i++){
			bytedata[i] = (byte) 0;
		}
		
		return bytedata;
		
	}

	
	public static ArrayList<String[]> readArrayFromFile(String filename, String encoding, 
										boolean skipFirstRow){
		FileInputStream fin = null;
		BufferedReader in = null;
		ArrayList<String[]> datalist = new ArrayList<String[]>();
		
		try {
			fin = new FileInputStream(filename);
			InputStreamReader isr = new InputStreamReader(fin, encoding);
			in = new BufferedReader(isr);
			
			int lineIdx = 0;
			while(true){
				
				String line = in.readLine();
				
				if(line == null){
					break;
				}
			
				
				String[] data = line.split("\t");
				
				if(skipFirstRow && lineIdx == 0){
					lineIdx++;
					continue; 
				}
				datalist.add(data);
				
				lineIdx++;
			}			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return datalist;
	}
		
	public static boolean writeArrayToFile(ArrayList<?> list, 
							String filename, String encoding){ 
        // Saving to file 
        FileOutputStream fout = null;
        OutputStreamWriter osw = null;
        
        try{
	        fout = new FileOutputStream(filename);
	        osw = new OutputStreamWriter(fout, encoding);
	        BufferedWriter bw = new BufferedWriter(osw);
	        
	        for(int i=0; i<list.size(); i++){
	        	String[] data = (String[]) list.get(i);
	        	String line = StringUtils.join(data, "\t");
	        	
	        	bw.write(line + "\n");
	        }
	        
	        bw.close();
	        osw.close();
	        return true;
        }catch(IOException e){
        	e.printStackTrace();
        	return false;
        }finally{
        	safeClose(fout);
        }
	}	
	
	public static boolean writeStringToFile(String filename, 
											String str, String encoding) {
		// Saving to file
		FileOutputStream fout = null;
		OutputStreamWriter osw = null;

		try {
			fout = new FileOutputStream(filename);
			osw = new OutputStreamWriter(fout, encoding);
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write(str);

			bw.close();
			osw.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			safeClose(fout);
		}
	}	
}
