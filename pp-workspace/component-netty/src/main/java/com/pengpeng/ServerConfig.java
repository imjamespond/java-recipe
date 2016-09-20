package com.pengpeng;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;


@Configuration()
//@ImportResource({"context-stargame.xml","beanRefPlayerDataAccess.xml","beanRefRuleDataAccess.xml"})
@ImportResource({"context-stargame.xml"})
public class ServerConfig {

}
