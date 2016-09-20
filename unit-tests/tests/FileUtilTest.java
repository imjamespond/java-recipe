package test;

import java.io.File;
import java.net.URL;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

public class FileUtilTest {

	public static void main(String[] args) {

		rename("D:/Project/flex/Library/miniui/","class ", "");
		
        long a = System.currentTimeMillis();
        //refreshFileList("./");
        System.out.println(System.currentTimeMillis() - a);

	}
	
	public static void rename(String path,String target, String replacement){
		try {
			// URL url = getClass().getResource(itemDataAnno.value()).toURI();
			//URL url = FileUtilTest.class.getResource(path);
			//URL url = new Url();
			//if (url != null) {
				//路径
				File dir = new File(path);
				Collection<File> files = FileUtils.listFiles(dir, new String[] { "xml", "gz", "as" }, true);
				for (File file : files) {
					String filename = file.getCanonicalPath();
					String replace = filename.replace(target,replacement);
					System.out.println(replace);
					file.renameTo(new File(replace));
				}

			//}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    public static void refreshFileList(String strPath) { 
        File dir = new File(strPath); 
        File[] files = dir.listFiles(); 
        
        if (files == null) 
            return; 
        for (int i = 0; i < files.length; i++) { 
            if (files[i].isDirectory()) { 
                //refreshFileList(files[i].getAbsolutePath()); 
            } else { 
                //String strFileName = files[i].getAbsolutePath().toLowerCase();
                String strFileName = files[i].getName();
                String[] strs = strFileName.split("\\.");
                System.out.println(strFileName);                
            } 
        } 
    }

}
