

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class FreeMarkerUtils {
	
    public static Set<String> getClassName(String strPath,String packageName) { 
    	Set<String> filelist = new HashSet<String>(); 
    	
        File dir = new File(strPath); 
        File[] files = dir.listFiles(); 
        
        if (files == null) 
            return null; 
        for (int i = 0; i < files.length; i++) { 
            if (files[i].isDirectory()) { 
                //refreshFileList(files[i].getAbsolutePath()); 
            } else { 
                //String strFileName = files[i].getAbsolutePath().toLowerCase();
                String strFileName = files[i].getName();
                String[] strs = strFileName.split("\\.");
                filelist.add(packageName + strs[0]);                    
            } 
        }
        
        return filelist;
    }
}
