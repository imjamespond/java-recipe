package com.metasoft.flying.net.annotation;

/**
 * @author james
 *
 */
public enum HandlerType{
	RPC,//rpc,有返回值
	FORWARD;//转发,无返回值,若需要返回请保存serial,
}