package com.james.freemark;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

import com.AnnotationUtils;
import com.FileUtils;
import com.FreeMarkerUtils;
import com.james.freemark.elements.GenBean;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class MetaDBGenTool {
	private static Logger logger = LogManager.getLogger(MetaDBGenTool.class);

	private static final String PATH = "D:/Project/java/flying-server/src/main/java/com/metasoft/flying/model";
	private static final String PACKAGE = "com.metasoft.flying.model.";
//	private static final String PATH = "C:/javaProject/chat-room/src/main/java/chatroom/module";
//	private static final String PACKAGE = "chatroom.module.";
//	private static final String PACKAGE = "test.";
//	private static final String PATH = "C:\\javaProject\\jdbc-template\\src\\main\\java\\test\\";
	
	private static final String CLASS_PREFIX = "Persist";
	private static final String CLASS_TARGET_SUFFIX = "enceService";	
	private static final String CFG_ROOT = "gen";
	private static final String FREE_MARK_TEMPLATE = "persist/meta_soft_db.ftl";
	private static final String EXPORT_PATH = "gen/export/db/";
	private static final String AUTO_CODE_START = "/* auto generated code */";
	private static final String AUTO_CODE_END = "/* end generated */";

	private static Configuration freemarker_cfg = null;

	private static List<GenBean> beans = new ArrayList<GenBean>();

	public static void main(String[] args) throws Exception {

		MetaDBGenTool.getFreeMarkerCFG(CFG_ROOT);

		Map<String, Object> map = collectBeans();

		MetaDBGenTool.beans.clear();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (entry.getKey().indexOf(PACKAGE) >= 0) {
				MetaDBGenTool.reflectObj(entry.getValue());
			}
		}
		MetaDBGenTool.genFiles();
	}

	//收集路径下所有类名 并 创建bean
	public static Map<String, Object> collectBeans() {
		Map<String, Object> allObjects = new HashMap<String, Object>();

		Set<String> allClsNames = FreeMarkerUtils.getClassName(PATH, PACKAGE);

		for (String s : allClsNames) {
			System.out.println(s);
			//类名以CLASS_PREFIX开始
			if (s.indexOf(CLASS_PREFIX) == -1)
				continue;

			Class<?> clz = null;
			try {
				clz = Class.forName(s);
				allObjects.put(s, clz.newInstance());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return allObjects;
	}

	//生成所有文件
	public static void genFiles() {
		for (GenBean bean : beans) {
			HashMap<String, GenBean> prop = new HashMap<String, GenBean>();
			prop.put("vo", bean);
			String exportPath = EXPORT_PATH
					+ bean.getPkg().replace(".", "/");// 生成路径

			String templateFilePath = CFG_ROOT;// 模版路径
			genFile(FREE_MARK_TEMPLATE, prop, exportPath, bean.getCls(),templateFilePath);
		}

	}

	/**
	 * 生成静态文件.
	 * 
	 * @param templateFileName
	 *            模板文件名,相对htmlskin路径,例如"/tpxw/view.ftl"
	 * @param propMap
	 *            用于处理模板的属性Object映射
	 * @param filePath
	 *            要生成的静态文件的路径,相对设置中的根路径,例如 "/tpxw/1/2005/4/"
	 * @param className
	 *            
	 * @param templateFilePath
	 *            模板路径
	 * @return boolean true代表生成文件成功
	 */
	public static void genFile(String templateFileName, Map<String, GenBean> propMap,
			String filePath, String className, String templateFilePath) {
		// templateFileName, prop, asFilePath, asFileName, templateFilePath
		try {

			// 如果根路径存在,则递归创建子目录
			FileUtils.createDir(filePath,true);
			
			//free marker 处理
			StringWriter strWriter = new StringWriter();
			Writer out = strWriter;
			//new BufferedWriter(new OutputStreamWriter(new FileOutputStream(afile)));
			Template t = getFreeMarkerCFG(templateFilePath).getTemplate(templateFileName);
			t.process(propMap, out);
			out.flush();
			//out.close();
			
			//替换标签
	
			StringBuffer sb = new StringBuffer();
			sb.append(strWriter.getBuffer().toString());
			
			String outputfile = filePath + "/" + className + CLASS_TARGET_SUFFIX + ".java";
			FileUtils.writeStringToFile(outputfile, sb.toString());
		} catch (TemplateException e) {
			System.out.print(e.getMessage());
		} catch (IOException e) {
			System.out.print(e.getMessage());
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
	}

	//将对象包名,类名,全名反射出来
	public static void reflectObj(Object obj) {

		GenBean bean = new GenBean();
		//包名
		String pkg = obj.getClass().getPackage().getName();
		bean.setPkg(pkg);
		//类名
		String name = obj.getClass().getSimpleName();
		bean.setCls(name);
		String clsName = "";
		logger.info(String.format("pkg(%s) name(%s) clsName(%s)", pkg, name,
				clsName));
		//父类名
		if (obj.getClass().getSuperclass() != null
				&& !obj.getClass().getSuperclass().getSimpleName()
						.equals("Object")) {
			bean.setfName(obj.getClass().getSuperclass().getSimpleName());
		}
		//类注释
		if (AnnotationUtils.getAnno(obj.getClass(),Table.class)!=null){
			String table = AnnotationUtils.getAnno(obj.getClass(),Table.class).value();
			bean.setTable(table);
        }
		
		//全部字段
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field f : fields) {
            String fieldAnno = "";
            String idAnno = null;
            if (f.getAnnotation(Id.class)!=null){
                idAnno = f.getAnnotation(Id.class).value();
                if(null != idAnno){
                	bean.setIdSeq(idAnno);
                }
            }
            if (f.getAnnotation(Column.class)!=null){
                fieldAnno = f.getAnnotation(Column.class).value();
            }
            else{
            	//identify column
            	continue;
            }
			if (f.getType().isArray()) {
				bean.addMethod("Array", fieldAnno, fieldAnno);
			} else {
				bean.addMethod(f.getType().getName(), fieldAnno, fieldAnno);
			}
		}
		beans.add(bean);
	}

	/**
	 * 
	 * 获取freemarker的配置. freemarker本身支持classpath,目录和从ServletContext获取.
	 * 
	 * @param templateFilePath
	 *            获取模板路径
	 * @return Configuration 返回freemaker的配置属性
	 * @throws Exception
	 */
	private static Configuration getFreeMarkerCFG(String templateFilePath)
			throws Exception {
		if (null == freemarker_cfg) {

			try {
				freemarker_cfg = new Configuration();
				freemarker_cfg.setDirectoryForTemplateLoading(new File(
						templateFilePath));
			} catch (Exception ex) {
				logger.info(ex.getMessage());
			}
		}
		return freemarker_cfg;
	}


}
