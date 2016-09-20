/**
 * Xlands-Project
 */
package com.chitu.chess;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleScriptContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

/**
 * 监测classpath/scripts下的td_script.js脚本变动,执行 标准与错误输出到output.txt
 * 
 * @author ivan
 * 
 */
@Service
public class ScriptModifyDetector {

	private final static Log log = LogFactory.getLog(ScriptModifyDetector.class);

	public static long DETECT_INTERVAL = 2 * DateUtils.MILLIS_PER_SECOND;

	private ScriptEngine engine = new ScriptEngineManager().getEngineByExtension("js");
	private String scriptFile = "td_script.js";
	private String outputFile = "output.txt";
	private File scriptPath;
	private long lastModified = System.currentTimeMillis();
	private volatile boolean running = true;

	@PostConstruct
	public void init() {
		URL url = this.getClass().getResource("/");
		scriptPath = new File(url.getFile() + "/scripts");
		if (!scriptPath.exists()) {
			scriptPath.mkdirs();
		}

		Thread detectThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (running) {

					// 检测执行
					runScript();

					// 休眠2秒
					try {
						Thread.sleep(DETECT_INTERVAL);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		detectThread.setName("Thread_" + this.getClass().getName());
		detectThread.start();
		log.info(detectThread.getName() + " is started!");
	}

	protected void runScript() {
		File file = new File(scriptPath, scriptFile);
		// 如果脚本文件不存在,返回
		if (!file.exists() || file.length() == 0)
			return;
		// 如果是执行过的脚本,返回
		if (file.lastModified() <= lastModified)
			return;

		File output = new File(scriptPath, outputFile);
		if (output.exists()) {
			output.delete();
		}

		PrintWriter outputWriter = null;
		try {
			output.createNewFile();
			outputWriter = new PrintWriter(output);
			ScriptContext context = new SimpleScriptContext();
			// 得到脚本内输出
			context.setErrorWriter(outputWriter);
			context.setWriter(outputWriter);
			String content = FileUtils.readFileToString(file);
			engine.eval(content, context);
		} catch (Exception e) {
			if (outputWriter != null)
				e.printStackTrace(outputWriter);
			else
				e.printStackTrace();
		}
		// 放在这个地方,避免循环执行出错的脚本
		lastModified = file.lastModified();

		// 关闭writer
		if (outputWriter != null) {
			outputWriter.close();
		}
	}

	@PreDestroy
	public void destroy() {
		running = false;
	}

	public static void main(String[] args) {
		ScriptModifyDetector detector = new ScriptModifyDetector();
		detector.init();
		// detector.destroy();
	}
}
