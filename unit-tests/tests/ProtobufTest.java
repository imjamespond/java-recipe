package test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.GeneratedMessage.GeneratedExtension;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.james.model.annotation.HandlerAnnotation;
import com.james.vo.VOProtos;
import com.james.vo.VOProtos.ChatReq;
import com.james.vo.VOProtos.GeneralRequest;
import com.james.vo.VOProtos.GeneralRequest.Builder;

public class ProtobufTest {
	
	public static final Map<String, Handler> handlerMethodMap = new HashMap<String, Handler>();
		
	static{
		Handler.registRequest(ChatReq.class, ChatReq.chatReqExtNum);
	}

	public static void main(String[] args) throws InvalidProtocolBufferException {
		//some registry
		ExtensionRegistry registry = ExtensionRegistry.newInstance();
		//registry.add(ChatReq.chatReqExtNum);
		VOProtos.registerAllExtensions(registry);
		
		Method[] methods = ProtobufTest.class.getMethods();
		for(Method method:methods){
			HandlerAnnotation keyAnno = method.getAnnotation(HandlerAnnotation.class);
			if(null != keyAnno){
				System.out.println(keyAnno.cmd()+keyAnno.req());
				GeneratedExtension<GeneralRequest,?> extension = Handler.findRequest(keyAnno.req());
				Handler handler = new Handler(extension,method);
				handlerMethodMap.put(keyAnno.cmd(), handler);
			}
		}
		

		
		//request
		ChatReq.Builder chatReq = ChatReq.newBuilder();
		chatReq.setId(0);
		chatReq.setMsg("foobar-_-#");
		GeneralRequest.Builder req = GeneralRequest.newBuilder();
		req.setCmd("chat.send");
		req.setSerial(0);
		req.setExtension(ChatReq.chatReqExtNum, chatReq.build());

		System.out.println(Builder.getDescriptor().getFullName());

		Message msg = req.build();
		String typeName = msg.getDescriptorForType().getFullName();

		System.out.println(msg.getClass().getName() + " type:" + typeName + " size:" + msg.getSerializedSize());

		byte[] bytes = msg.toByteArray();

		FileDescriptor file = VOProtos.getDescriptor();
		List<Descriptor> list = file.getMessageTypes();
		for (Descriptor dscr : list) {
			System.out.println(dscr.getFullName());
		}
//		  Map<Descriptor, Message> myMap = new ...;
//		  myMap.put(FooMessage.getDescriptor(), FooMessage.getDefaultInstance());
		// get dscr by typeName
		Descriptor dscr = file.findMessageTypeByName("GeneralRequest");// The unqualified type name=>"Query";
		//GeneralRequest protoMsg = GeneralRequest.getDefaultInstance().parseFrom(bytes);
		GeneralRequest protoMsg = GeneralRequest.parseFrom(bytes,registry);


		System.out.println(protoMsg.toString());
		System.out.println(protoMsg.getCmd());
		Handler handler = handlerMethodMap.get(protoMsg.getCmd());
		ChatReq req1 = (ChatReq) protoMsg.getExtension(handler.extension);
		try {
			handler.method.invoke(null, protoMsg.getExtension(handler.extension));
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//ChatReq protoMsgChatReq = protoMsg.getExtension(handler.extension);
		//ProtobufTest.sendMsg(protoMsgChatReq);
		System.out.println("end");
	}

	@HandlerAnnotation(cmd = "chat.send", req = ChatReq.class)
	public static void sendMsg(ChatReq req){

		if(req instanceof ChatReq){
			System.out.println("##############"+req.getMsg()+"##############");
		}		
	}
	
	

}
	
	class Handler{
		public static final Map<Class<?>, GeneratedExtension<GeneralRequest,?>> reqMap = new HashMap<Class<?>,  GeneratedExtension<GeneralRequest,?>>();
		public static void registRequest(Class<?> clazz, GeneratedExtension<GeneralRequest,?> extension){
			reqMap.put(clazz, extension);
		}
		public static GeneratedExtension<GeneralRequest,?> findRequest(Class<?> clazz){
			return reqMap.get(clazz);
		}

		public GeneratedMessage.GeneratedExtension<GeneralRequest, ?> extension;
		public final Method method;
		public Handler(Method method) {
			this.method = method;
		}
		public Handler(GeneratedMessage.GeneratedExtension<GeneralRequest, ?> extension, Method method) {
			super();
			this.extension = extension;
			this.method = method;
		}
	}