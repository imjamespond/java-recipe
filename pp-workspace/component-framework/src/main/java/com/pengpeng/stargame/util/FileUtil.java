package com.pengpeng.stargame.util;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件工具类.
 *
 * @author chenhonghong@gmail.com
 * @since 2006
 */
public final class FileUtil {
// ------------------------------ 属性 ------------------------------

    public final static String DEFAULT_IMAGE_ACCEPT_TYPE = "jpg,jpeg,gif";

    /**
     * File's path info store key,please refer to
     * FileUtil.spliteFileInfo(java.io.File)
     */
    public static final String FILE_INFO_PATH = "path";

    /**
     *  分隔符 "/"
     */
    public static final String SEPARATOR = "/";

    /**
     * 分隔符 "."
     */
    public static final String PATTERN = ".";

    /**
     * 换行符 "\r\n"
     */
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    /**
     * File's name info store key,please refer to
     * FileUtil.spliteFileInfo(java.io.File)
     */
    public static final String FILE_INFO_NAME = "name";

    /**
     * File's type info store key,please refer to
     * FileUtil.spliteFileInfo(java.io.File)
     */
    public static final String FILE_INFO_TYPE = "type";

    public static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    private static Logger logger = Logger.getLogger(FileUtil.class);

    public final static String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890-_.:/\\";

    public final static String IMGFILES = "GIF;JPG;JPEG;PNG;BMP";

// -------------------------- 静态方法 --------------------------

    /**
     * Split file's information into 3 part:path,file's name,and the file's type
     *
     * @param aFile A file object
     * @return A map with one or more file information. They are store into
     *         constants starts with FILE_INFO_*; Usage: File file = new
     *         File("D:/test/testExample.txt"); Map map =
     *         FileUtil.splitFileInfo(file); map.get(FileUtil.FILE_INFO_PATH) is
     *         "D:\\test"; map.get(FileUtil.FILE_INFO_NAME) is "testExample";
     *         map.get(FileUtil.FILE_INFO_TYPE) is ".txt";
     *         <p/>
     *         If the file object is directory only or without file type attribute, the
     *         return will missing the missing information
     */
    public static final Map splitFileInfo(File aFile) {
        Map fileMap = new HashMap();
        if (aFile.getParentFile() != null) {
            String filePath = aFile.getParentFile().getPath();
            fileMap.put(FILE_INFO_PATH, filePath + File.separator);
        }
        String fileName = aFile.getName();
        int tailIdx = fileName.lastIndexOf('.');
        if (tailIdx > -1) {
            String fileType = fileName.substring(tailIdx);
            fileMap.put(FILE_INFO_TYPE, fileType);
            fileName = fileName.substring(0, tailIdx);
        }
        fileMap.put(FILE_INFO_NAME, fileName);
        return fileMap;
    }

    /**
     * 取得文件的扩展名，扩展名以小写返回，如果没有扩展名的话，返回空字符串.
     *
     * @param fileName 文件名字
     * @return 比如： .jpg
     */
    public static String parseExtendName(String fileName) {
        Map fileInfo = FileUtil.splitFileInfo(fileName);
        Object obj = fileInfo.get(FileUtil.FILE_INFO_TYPE);
        if(obj == null){
            return "";
        }
        return ((String) obj).toLowerCase();
    }

    /**
     * Split file's information into 3 part:path,file's name,and the file's type
     *
     * @param aFileStr A file format string
     * @return A map with one or more file information. They are store into
     *         constants starts with FILE_INFO_*; Usage: File file = new
     *         File("D:/test/testExample.txt"); Map map =
     *         FileUtil.splitFileInfo(file); map.get(FileUtil.FILE_INFO_PATH) is
     *         "D:\\test"; map.get(FileUtil.FILE_INFO_NAME) is "testExample";
     *         map.get(FileUtil.FILE_INFO_TYPE) is ".txt";
     *         <p/>
     *         If the file object is directory only or without file type attribute, the
     *         return will missing the missing information
     */
    public static final Map splitFileInfo(String aFileStr) {
        File file = new File(aFileStr);
        return splitFileInfo(file);
    }

    /**
     * Copy a file to a specific path
     *
     * @param source A java.io.File object
     * @param path   The path where the new copy will be
     * @throws java.io.IOException
     */
    public static final File copyToPath(File source, String path)
            throws IOException {
        if (path == null || path.equals("")) {
            throw new IllegalArgumentException(
                    "The path argument should not be null");
        }
        File dest = new File(path);
        if (!dest.exists())
            dest.mkdirs();
        String newFileName = dest.getAbsolutePath() + File.separator
                + source.getName();
        dest = new File(newFileName);
        return copyFile(dest, source);
    }

    /**
     * Copy file into a new one
     *
     * @param dest   Destination file with path,can be absolute or canonical
     * @param source Source file with path,can be absolute or canonical
     * @throws java.io.IOException If the source file not found, or can not be read, or if
     *                     destnation file cannot be created or overloaded, then
     *                     exception will be thrown
     */
    public static final File copyFile(File dest, File source) throws IOException {
        if (source == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (dest == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (!source.exists()) {
            throw new FileNotFoundException("Source '" + source + "' does not exist");
        }
        if (source.isDirectory()) {
            throw new IOException("Source '" + source
                    + "' exists but is a directory");
        }
        if (source.getCanonicalPath().equals(dest.getCanonicalPath())) {
            throw new IOException("Source '" + source + "' and destination '"
                    + dest + "' are the same");
        }
        if (source.getParentFile() != null
                && dest.getParentFile().exists() == false) {
            if (dest.getParentFile().mkdirs() == false) {
                throw new IOException("Destination '" + dest
                        + "' directory cannot be created");
            }
        }
        if (dest.exists() && dest.canWrite() == false) {
            throw new IOException("Destination '" + dest
                    + "' exists but is read-only");
        }

        InputStream is = null;
        OutputStream os = null;

        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            copyStream(is, os);
        } catch (IOException e) {
            logger.error("error while copy file", e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (Exception e) {
                    logger.error("error occurs while close input stream." + e);
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    logger.error("error occurs while close input stream." + e);
                }
            }
        }
        if (source.length() != dest.length()) {
            throw new IOException("Failed to copy full contents from '"
                    + source + "' to '" + dest + "'");
        }
        return dest;
    }

    /**
     * Copy a file to a path
     *
     * @param source A file with path,can be absolute or canonical
     * @param path   The path where the new copy will be
     * @throws java.io.IOException
     */
    public static final File copyToPath(String source, String path)
            throws IOException {
        File sourceFile = new File(source);
        return copyToPath(sourceFile, path);
    }

    /**
     * Format a file path according to the operation system.
     *
     * @param aFilePath A file path(can include the file itself,i.e. C:\\test.jpg)
     * @return A system readable path string
     * @throws java.io.IOException
     */
    public static final String toURL(String aFilePath) {
        File file = new File(aFilePath);
        if (!file.exists()) {
            file = new File(file.getAbsolutePath());
        }
        try {
            return file.toURL().toExternalForm();
        } catch (MalformedURLException e) {
            // the string is not a formattable one, return it directly
            return file.getAbsolutePath();
        }
    }

    /**
     * Shot cut of <code>toFile(java.lang.String,boolean)</code>,with a
     * default <strong>createNewOne</strong> value <strong>false</strong>
     *
     * @param aStr String A file with path
     * @return
     * @throws java.io.IOException
     */
    public static final File toFile(String aStr) throws IOException {
        return toFile(aStr, false);
    }

    /**
     * <h3>Change a string to be a file type</h3>
     * <p/>
     * Change a path string to be a file type.If the path is end with \ or /<br>
     * ,this will return a directory type File. else a file type File is return.
     * </p>
     *
     * @param aStr
     * @param createNewOne boolean If true is set, the file/or directory will be created<br>
     *                     If it not exists
     * @return
     * @throws java.io.IOException
     */
    public static final File toFile(String aStr, boolean createNewOne)
            throws IOException {
        if (StringUtils.isEmpty(aStr)) {
            logger.warn("The input file path is null");
            return null;
        }
        File file = new File(aStr); // isDirectory() == true if
        // hasParent()!=null
        if (aStr.indexOf('\\') != aStr.length() - 1
                || aStr.indexOf('/') != aStr.length() - 1) {
            // the string is not ending with file seporator \ or /,it must be a
            // file with its path. We must make the isFile == true
            if (file.getParent() != null) {
                String fileName = file.getName();
                file = new File(file.getParent(), fileName);
            }
        }
        if (!file.exists() && createNewOne) {
            file.createNewFile();
        }
        return file;
    }

    /**
     * 将字节流输出到文件.
     *
     * @param inputStream 源字节流
     * @param dest        目标文件
     * @throws Exception 客户端最好是捕捉异常
     */
    public static void writeFile(InputStream inputStream, File dest) throws Exception {
        if (inputStream == null) {
            throw new NullPointerException("源字数流不存在");
        }
        if (dest == null) {
            throw new NullPointerException("目标文件对象不存在");
        }
        if (!dest.exists()) {
            dest.createNewFile();
        }
        OutputStream fis = null;
        try {
            fis = new FileOutputStream(dest);
            FileUtil.copyStream(inputStream, fis);
        } catch (IOException e) {
            logger.error(e);
        } finally {
            inputStream.close();
            if (fis != null) {
                fis.close();
            }
        }
    }

    /**
     * <h3> Utilities method help us to read and write things from on
     * <code>java.io.InputStream</code><br>
     * to a <code>java.io.OutputStream</code> </h3>
     * <p/>
     * In this method we can read and write with streams under a specific speed
     * </p>
     *
     * @param is  InputStream An InputStream source
     * @param os  OutputStream An OutputStream source
     * @throws java.io.IOException
     */
    public static final int copyStream(InputStream is, OutputStream os)
            throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int count = 0, n = 0;
        while (-1 != (n = is.read(buffer))) {
            os.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    /**
     * Count image files in folder, if imageTypes is null, it'll scan JPEG and
     * GIF type
     *
     * @param path
     * @return
     */
    public static final int countImageInFolder(String path, String imageTypes) {
        if (StringUtils.isEmpty(imageTypes))
            imageTypes = DEFAULT_IMAGE_ACCEPT_TYPE;
        return countFileInFolder(path, imageTypes);
    }

    /**
     * Count files of specific type
     *
     * @param path      A specific path to scan file
     * @param fileTypes File types to be counted in, can use "," to join muliple types
     * @return Return files' count of the accept types
     */
    public static final int countFileInFolder(String path, String fileTypes) {
        File filePath = new File(path);
        if (!filePath.exists()) {
            throw new IllegalArgumentException(
                    "Invalid input, the path is not exists.");
        }
        int count = 0;
        FilenameFilter filter = new DefaultFilenameFilter(fileTypes);
        if (filePath.isFile())
            // get the directory of the input
            filePath = filePath.getParentFile();
        count = filePath.listFiles(filter).length;
        return count;
    }

    /**
     * Replace all "\" with "/" to make a path like a relative one
     *
     * @param aPath A relative path
     * @return
     */
    public static String toRelativePath(String aPath) {
        return aPath.replace('\\', '/');
    }

    /**
     * Copy a file to a specific path
     *
     * @param dest   Destination's path,can be absolute or canonical
     * @param source Source file object with path,can be absolute or canonical
     * @throws java.io.IOException
     */
    public static final File copyFile(String dest, File source)
            throws IOException {
        File destFile = new File(dest);
        return copyFile(destFile, source);
    }

    /**
     * Copy a file to a specific path
     *
     * @param dest   Destination's path,can be absolute or canonical
     * @param source Source's path,can be absolute or canonical
     * @throws java.io.IOException
     */
    public static final File copyFile(String dest, String source)
            throws IOException {
        File sourceFile = new File(source);
        return copyFile(dest, sourceFile);
    }

    /**
     * 读取文件成集合。
     * 每一行为 1 个元素，为了避免集合内存溢出，只读取前 10000 行。
     *
     * @param file 文件
     * @return 集合
     */
    public static List<String> readFileAsList(String file) {
        FileInputStream is = null;
        BufferedInputStream inputStream = null;
        InputStreamReader isr = null;
        BufferedReader reader = null;
        List<String> lines = new ArrayList<String>();
        int lineCount = 0;
        int limit = 10000;
        try {
            is = new FileInputStream(file);
            inputStream = new BufferedInputStream(is);
            isr = new InputStreamReader(inputStream);
            reader = new BufferedReader(isr, 1024);
            String line;

            while ((line = reader.readLine()) != null) {
                lines.add(line);
                lineCount++;
                if (lineCount >= limit) {
                    logger.info("文件过大，已达到上限，避免内存溢出，只返回 " + limit + " 行");
                    break;
                }
            }
        } catch (IOException e) {
            logger.info(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.info("关闭日志文件流出错", e);
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    logger.info("关闭日志文件流出错", e);
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.info("关闭日志文件流出错", e);
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.info("关闭日志文件流出错", e);
                }
            }
        }
        return lines;
    }

	/**
	 * 读取文件成集合。
	 * 每一行为 1 个元素，为了避免集合内存溢出，只读取前 10000 行。
	 *
	 * @param is FileInputStream
	 * @return 集合
	 */
	public static List<String> readFileAsList(InputStream is) {
		BufferedInputStream inputStream = null;
		InputStreamReader isr = null;
		BufferedReader reader = null;

		List<String> lines = new ArrayList<String>();
		int lineCount = 0;
		int limit = 10000;
		try {
			inputStream = new BufferedInputStream(is);
			isr = new InputStreamReader(inputStream);
			reader = new BufferedReader(isr, 1024);
			String line;

			while ((line = reader.readLine()) != null) {
				lines.add(line);
				lineCount++;
				if (lineCount >= limit) {
					logger.info("文件过大，已达到上限，避免内存溢出，只返回 " + limit + " 行");
					break;
				}
			}
		} catch (IOException e) {
			logger.info(e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					logger.info("关闭日志文件流出错", e);
				}
			}
			if (isr != null) {
				try {
					isr.close();
				} catch (IOException e) {
					logger.info("关闭日志文件流出错", e);
				}
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.info("关闭日志文件流出错", e);
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.info("关闭日志文件流出错", e);
				}
			}
		}
		return lines;
	}

    /**
     * 读取文件夹下所有文件包括子文件夹
     *
     * @param filePathAndName
     * @return list
     * @throws Exception
     */
    public static List readFile(String filePathAndName) throws Exception {
        List fileNameList = new ArrayList();
        File file = new File(filePathAndName);
        //logger.info("filePathAndName:"+filePathAndName);
        try {
            if (!file.isDirectory()) {
                fileNameList.add(file.getName());
            } else if (file.isDirectory()) {
                String[] filelist = file.list();
                int len = filelist.length;
                for (int i = 0; i < len; i++) {
                    File readfile = new File(filePathAndName + filelist[i]);
                    //logger.info("for循环 dir:"+filePathAndName + filelist[i]+" - "+"readfile.isDirectory() : "+readfile.isDirectory());
                    if (readfile.isFile()) {
                        //logger.info("file.getName() : "+readfile.getName());
                        fileNameList.add(readfile.getName());
                    } else if (readfile.isDirectory()) {
                        readFile(filePathAndName + filelist[i] + File.separator);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("读取文件夹下所有文件 FileNotFoundException.", e);
        }
        return fileNameList;
    }

    /**
     * 获取文件扩展名称
     *
     * @param s 文件名称
     * @return
     */
    public static String getFileExt(String s) {
        String s1 = new String();
        int i = 0;
        int j = 0;
        if (s == null) {
            return null;
        }
        i = s.lastIndexOf('.') + 1;
        j = s.length();
        s1 = s.substring(i, j);
        if (s.lastIndexOf('.')  >  0) {
            return s1;
        } else {
            return "";
        }
    }

    /**
     * 获取文件名称
     *
     * @param pathName 文件路径
     * @return
     */
    public static String getFileName(String pathName) {
        String fileName;
        if (pathName.lastIndexOf('/') > pathName.lastIndexOf('\\')) {
            fileName = pathName.substring(pathName.lastIndexOf('/') + 1);
        } else {
            fileName = pathName.substring(pathName.lastIndexOf('\\') + 1);
        }
        fileName = fileName.substring(0, fileName.lastIndexOf('.'))
                + fileName.substring(fileName.lastIndexOf('.')).toLowerCase();
        return fileName;
    }

    /**
     * 取得文件名（不包含扩展名）
     * @param pathName 文件路径
     * @return 不包含扩展名的文件名
     */
    public static String getFileNameWithoutExt(String pathName){
        String filename = getFileName(pathName);
        return filename.substring(0,filename.lastIndexOf("."));
    }

    /**
     *  获取文件名称
     *
      * @param url  文件url路径
     * @return
     */
    public static String getUrlFileName(String url) {
        String[] names = url.split("/");
        String fileName = null;
        if (names != null) {
            fileName = names[names.length - 1];
        } else {
            fileName = url;
        }
        fileName = fileName.substring(0, fileName.lastIndexOf('.'))
                + fileName.substring(fileName.lastIndexOf('.')).toLowerCase();
        return fileName;
    }

    /**
     * 生成新的文件名称
     *
     * @param fileName 旧文件名称
     * @return
     */
    public static String getNewFileName(String fileName) {
        String newFileName = null;
        File file;
        int i = 0;

        if (fileName == null) {
            return "";
        }

        while (true) {
            i++;
            newFileName = fileName.substring(0, fileName.lastIndexOf('.'))
                    + "_" + i + fileName.substring(fileName.lastIndexOf('.'));
            file = new File(newFileName);
            if (!file.exists()) {
                break;
            }
        }
        return newFileName;
    }

    /**
     * 指定文件是否在设定的文件格式范围内
     *
     * @param fileExt
     * @return
     */
    public static boolean isImgFile(String fileExt) {
        boolean isImage = false;
        String[] exts = IMGFILES.split(";");

        for (int i = 0; i < exts.length; i++) {
            if (fileExt.equalsIgnoreCase(exts[i])) {
                isImage = true;
                break;
            }
        }
        return isImage;
    }

    /**
     *  生成随机数字字符串
     *
     * @return
     */
    public static String randomFile() {
        int rnd = (int) (Math.random() * (double) (0 - 1000)) + 1000;

        return String.valueOf(System.currentTimeMillis() + rnd);
    }

    /**
     *  删除目录及目录下面的所有子目录
     *
     * @param delDir
     */
    public static void delDirectory(String delDir) {
        if (delDir != null) {
            File file = new File(delDir);

            File afile[] = file.listFiles();
            if (afile != null) {
                for (int i = 0; i < afile.length; i++) {
                    if (afile[i].isDirectory()) {
                        delDirectory(afile[i].toString());
                    }
                    afile[i].delete();
                }
            }
            file.delete();
        }
    }

    /**
     *  复制文件
     *
     * @param iFile
     * @param oFile
     * @throws
     */
    public static void copy(String iFile, String oFile) throws Exception {
        File _inFile = null;
        File _outFile = null;
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;

        try {
            // 先创建ID_outFile文件，之后转换它
            String _tempPath = oFile.substring(0, oFile.lastIndexOf("/"));
            File _file = new File(_tempPath);
            if (!_file.exists()) {
                _file.mkdirs();
            }

            _inFile = new File(iFile);
            _outFile = new File(oFile);

            fileInputStream = new FileInputStream(_inFile);
            fileOutputStream = new FileOutputStream(_outFile);

            byte[] inOutb = new byte[fileInputStream.available()];
            fileInputStream.read(inOutb);
            fileOutputStream.write(inOutb);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            try {
                fileInputStream.close();
                fileOutputStream.close();
            } catch (Exception ee) {
                // ignore
            }
        }
    }

    /**
     *  判断指定字符串是否有中文字符
     *
     * @param text
     * @return
     */
    public static boolean haveChineseNspace(String text) {
        if (text == null || "".equals(text)) {
            return false;
        }

        if (text.trim().indexOf(" ") > 0) {
            return true;
        }

        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) > 255) {
                return true;
            }
        }
        return false;
    }

    /**
     * 读取文件夹下所有文件包括子文件夹
     * @param filePathAndName
     * @return list
     * @throws Exception
     */
    public static List readFileAll(String filePathAndName) throws Exception{
        if(filePathAndName == null || filePathAndName.length()==0){
            throw new NullPointerException("filePathAndName == null");
        }

        List fileNameList = new ArrayList();
        File file = new File(filePathAndName);
        //logger.info("filePathAndName:"+filePathAndName);
        try {
            if(!file.isDirectory()){
                fileNameList.add(file.getName());
            }else if(file.isDirectory()){
                String[] filelist = file.list();
                int len = filelist.length;
                for(int i=0;i<len;i++){
                    File readfile = new File(filePathAndName + filelist[i]);
                    //logger.info("for循环 dir:"+filePathAndName + filelist[i]+" - "+"readfile.isDirectory() : "+readfile.isDirectory());
                    if(readfile.isFile()){
                        //logger.info("file.getName() : "+readfile.getName());
                        fileNameList.add(readfile.getName());
                    }else if(readfile.isDirectory()){
                        readFile(filePathAndName + filelist[i]+File.separator);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("读取文件夹下所有文件包括子文件夹 FileNotFoundException.", e);
        }
        return fileNameList;
    }

    /**
     * 创建文件夹
     * @param folderPath
     * @return true : 创建成功
     */
    public static boolean createFolder(String folderPath){
        if(folderPath ==null || folderPath.length()==0){
            throw new NullPointerException("folderPath == null");
        }
        File myFilePath = new File(folderPath);
        boolean flag = myFilePath.exists();
        if(flag){
            return true;
        }
        if(!flag){
            myFilePath.mkdirs();
            return true;
        }
        return false;
    }

    /**
     * 判断文件是否存在
     * @param filePath
     * @return true : 文件存在
     */
    public static boolean fileIsExist(String filePath){
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * 判断文件是否存在
     * @param file
     * @return true : 文件存在
     */
    public static boolean fileIsExist(File file){
        return file.exists();
    }

    /**
     * 带编码的创建文件
     * @param file - 文件
     * @param fileContent - 内容
     * @param encoding - 编码
     */
//    public static void createFile(File file, String fileContent, String encoding){
//        BufferedWriter br = null;
//        try{
//            if(!file.exists()){
//                file.createNewFile();
//            }else{
//                file.delete();
//                file.createNewFile();
//            }
//
//            FileOutputStream fis=new FileOutputStream(file);
//            OutputStreamWriter isr=new OutputStreamWriter(fis,encoding);
//            br = new BufferedWriter(isr);
//            br.write(fileContent);
//        }catch(Exception e){
//            logger.error("创建文件操作出错",e);
//        }finally {
//            if(br !=null){
//                try {
//                    br.flush();
//                    br.close();
//                } catch (IOException e) {
//                }
//            }
//        }
//    }

    /**
     * 读取文件
     * <pre>
     * 以字符为单位读取文件，常用于读文本，数字等类型的文件
     * </pre>
     * @param fileName 文件名
     * @param encoding 编码
     */
    public static String readFileByChars(String fileName,String encoding) {
        StringBuilder str = new StringBuilder();
        String st = "";
        InputStreamReader isr = null;
        BufferedReader br = null;
        try{
            FileInputStream fs = new FileInputStream(fileName);

            if(encoding.equals("")){
                isr = new InputStreamReader(fs);
            }else{
                isr = new InputStreamReader(fs, encoding);
            }

            br = new BufferedReader(isr);

            try{
                String data = "";
                while((data = br.readLine()) != null){
                    str.append(data);
                }
            }catch(Exception e){
                logger.error("",e);
            }
            st = str.toString();
        }catch(IOException es){
            st = "";
            logger.error("",es);
        }finally {
            try {
                br.close();
            } catch (IOException e) {
            }
        }
        return st;
    }

    public static void writeAppendFile(String fileName,String fileContent) {
        RandomAccessFile randomFile = null;
        try {
            randomFile = new RandomAccessFile(fileName, "rw");
            long fileLength = randomFile.length();
            randomFile.seek(fileLength);
            randomFile.write(fileContent.getBytes());
        } catch (Exception e) {
            logger.error("",e);
        }finally {
            if(randomFile != null){
                try {
                    randomFile.close();
                } catch (IOException e) {
                }
            }
        }
    }

// -------------------------- 内部类 --------------------------

    /**
     * A helper class for user to filter files by name
     *
     * @author jgnan
     */
    public static class DefaultFilenameFilter implements FilenameFilter {

        // accept file type list, if null, all files are accept
        private String acceptTypes;

        public DefaultFilenameFilter() {
        }

        public DefaultFilenameFilter(String acceptTypes) {
            this.acceptTypes = acceptTypes;
        }

        /**
         * Indicates whether the current handling file is acceptable, The result
         * will be as one of the following case: If the dir is a file,and the
         * acceptTypes is empty, true will return
         */
        public boolean accept(File dir, String name) {
            // if accept types are null, accept all
            boolean accept = StringUtils.isEmpty(acceptTypes);
            if (!accept) {
                File tmp = new File(dir.getPath() + File.separator + name);
                accept = tmp.isFile();
                if (accept) {
                    // start to filter file
                    int dotIdx = name.lastIndexOf('.');
                    if (dotIdx > -1) {
                        String fileType = name.substring(dotIdx + 1);
                        accept = acceptTypes.indexOf(fileType) > -1;
                    }
                }
            }
            return accept;
        }
    }
}
