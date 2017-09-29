package com.metasoft.kafka.util;

public class ProcessUtil {
	public static void Shell(String cmd) throws Exception {
		cmd = cmd == null ? "/bin/sh" : cmd;
		ProcessBuilder processBuilder = new ProcessBuilder(cmd); 
		Process process = processBuilder.start();
		
//		//Output to the stream is piped into the standard input of the process represented by this Process object
//		OutputStream stdin = process.getOutputStream(); // <- Eh?
//		//the standard output of a subprocess can be read using the input stream 
//        InputStream stdout = process.getInputStream();
//
//        BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
//        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));
//        
//        new Thread(new Runnable() {
//            public void run() { 
//               try {
//                  while (true) {
//                	  String line = reader.readLine();
//                	  if(line!=null)
//                		  System.out.println(line);
//                  }
//               } catch (Exception ex) {
//                  ex.printStackTrace(); 
//               }
//
//            }
//         }).start();
//        
//        for(int i=0;i<10;i++){  
//        	writer.write("echo 'bar'");
//        	writer.newLine();
//            writer.flush();
//        }
        
		// writer.write("val textFile = spark.read.textFile(\"README.md\")");
		// writer.newLine();
		// writer.write("textFile.count()");
		// writer.newLine();
		// writer.write("textFile.first() ");
		// writer.newLine();
		// writer.write("textFile.filter(line =>
		// line.contains(\"Spark\")).count()");
		// writer.newLine();
		// writer.flush();
        
        //writer.close();
        
        process.waitFor(); 
	   }

	//new ProcessBuilder("/bin/sh","-c","echo 'foo'>bar");

//	public static void Shell_() throws Exception { 
//		ProcessBuilder pb = new ProcessBuilder("/bin/sh", "-c", "echo $VAR1"); 
//		Map<String, String> env = pb.environment(); 
//		env.put("VAR1", "myValue"); env.remove("OTHERVAR");
//		env.put("VAR2", env.get("VAR1") + "suffix"); 
//		pb.directory(new File("."));
//		File log = new File("log");
//		pb.redirectErrorStream(true);
//		pb.redirectOutput(Redirect.appendTo(log));
//		Process p = pb.start();
//		assert pb.redirectInput() == Redirect.PIPE;
//		assert pb.redirectOutput().file() == log;
//		assert p.getInputStream().read() == -1;
//	 }

	public static void main(String[] args) throws Exception {
		Shell(args[0]);
	}

}
