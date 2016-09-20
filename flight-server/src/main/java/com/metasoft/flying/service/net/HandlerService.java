package com.metasoft.flying.service.net;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.metasoft.flying.controller.GeneralController;
import com.metasoft.flying.model.constant.ErrorCodes;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.net.BaseConnection;
import com.metasoft.flying.net.annotation.HandlerAnno;
import com.metasoft.flying.net.annotation.HandlerType;
import com.metasoft.flying.service.common.LocalizationService;
import com.metasoft.flying.vo.general.GeneralRequest;
import com.metasoft.flying.vo.general.GeneralResponse;

@Service
public class HandlerService {
	private static final Logger logger = LoggerFactory.getLogger(HandlerService.class);
	
	@Autowired
	private LocalizationService localService;
	@Autowired
	private Set<GeneralController> controllers;

	private final Map<String, Handler> handlerMap = new HashMap<String, Handler>();
	
	private AtomicInteger errorSerial = new AtomicInteger(0);

	//@PostConstruct
	public void init() {

		logger.trace("registering Controllers...");
		for (GeneralController controller : controllers) {
			registerHandlerMethods(controller);
		}
		if(logger.isDebugEnabled()){
			print();
		}
	}

	public final void handle(GeneralRequest request, BaseConnection conn) {
		String cmd = request.getCmd();
		Object obj = null;
		Handler handler = (Handler) this.handlerMap.get(cmd);
		
		try {
			if (handler == null) {
				throw new GeneralException(ErrorCodes.HANDLE_NOT_FOUND,"HANDLE_NOT_FOUND");
			}
			
			if(handler.type == HandlerType.RPC){			
				obj = handler.method.invoke(handler.controller, request);			
				GeneralResponse response = null;
				if(null == obj){
					response = GeneralResponse.newOK();
				}else{
					response = GeneralResponse.newObject(obj);
				}
				response.setSerial(request.getSerial());			
				doResponse(response, conn);
			}else if(handler.type == HandlerType.FORWARD){
				handler.method.invoke(handler.controller, request);
			}

		} catch (Exception e) {
			//e.printStackTrace();
			try {
				GeneralResponse response = processException(request, e);
				doResponse(response, conn);
			} catch (Throwable e1) {
				e1.printStackTrace();
			}
		}
	}

	private void doResponse(GeneralResponse response, BaseConnection conn) {
		conn.deliver(response);
	}

	protected GeneralResponse processException(GeneralRequest request, Exception ex) {
		Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
		GeneralResponse vo = null;
		if ((cause instanceof GeneralException)){
			GeneralException ge = (GeneralException) cause;
			vo =  GeneralResponse.newError(localService.getLocalString(ge.getMessage()), ge.getErrorCode());
		}else {
			vo =  GeneralResponse.newError(localService.getLocalString("UNKNOWN_REQUEST_ERROR"), 0);
			StringWriter errors = new StringWriter();
			ex.printStackTrace(new PrintWriter(errors));
			logger.error("exception, serial:{} ,trace:\n{}", errorSerial.incrementAndGet(), errors.toString()); 
		}
		vo.setSerial(request.getSerial());
		logger.info("error request:{}, cause:{}", request , cause.getMessage());
		return vo;
	}

	private void registerHandlerMethods(GeneralController controller) {
		logger.debug("regist handler methods in " + controller.getClass());

		Method[] methods = controller.getClass().getMethods();
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			String cmd = checkHandler(method);
			if (cmd != null) {
				Handler handler = new Handler(controller, method);
				handlerMap.put(cmd, handler);
			}
		}
	}

	private String checkHandler(Method method) {
		HandlerAnno anno = method.getAnnotation(HandlerAnno.class);
		if (null == anno) {
			return null;
		}
		String cmd = anno.cmd();
		if (null == cmd) {
			return null;
		}
		String name = anno.name();
		if (null == name) {
			name = method.getName();
		}
		Class<?> vo = method.getReturnType();
		if (null == vo) {
			return null;
		}
		HandlerType type = anno.type();
		if (null == type) {
			return null;
		}
		Object handler = handlerMap.get(cmd);
		if (handler != null) {
			throw new IllegalStateException("Cannot map handler [" + method.getName() + "] to URL path [" + cmd
					+ "]: There is already handler [" + handler + "] mapped.");
		}

		if (method.getModifiers() != 1)
			return null;
		//Class<?> returnType = method.getReturnType();
		//if ((Void.TYPE.equals(returnType)) || (GeneralResponse.class.isAssignableFrom(returnType))) {
			return cmd;
		//}
		//return null;
	}
	
	class Handler{
		public final GeneralController controller;
		public final Method method;
		public final HandlerType type;
		public Handler(GeneralController controller, Method method) {
			super();
			this.controller = controller;
			this.method = method;
			HandlerAnno anno = method.getAnnotation(HandlerAnno.class);
			this.type = anno.type();
		}
	}
	
	private void print(){
		BufferedWriter writer;
		try {
			writer = new BufferedWriter (new FileWriter("./handlers"));
		
			Map<String, Handler> orderedMap = new TreeMap<String, Handler>();
			for(Entry<String, Handler> entry:handlerMap.entrySet()){
				orderedMap.put(entry.getKey(), entry.getValue());
			}
			for(Entry<String, Handler> entry:orderedMap.entrySet()){
				Handler handler = entry.getValue();
				Method method = handler.method;
				HandlerAnno anno = method.getAnnotation(HandlerAnno.class);
				Class<?> vo = method.getReturnType();
				HandlerType type = anno.type();
				writer.write(String.format("Cmd:[%-20s], Name:%s, Param:%s, VO:%s, [method:%s type:%s]\n", 
						anno.cmd(), anno.name(), Arrays.toString(method.getParameterTypes()), vo.getName(), method.getName(), type));
			}
			
			writer.flush();
			writer.close();	
			
		} catch (IOException e) {
			System.out.println("ERROR: empty Feature data");
			return;
		}
	}
}
