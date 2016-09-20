package com.pengpeng.stargame.managed;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 负责加载和读取节点配置信息.
 * <p>为了方便开发和生产部署，按以下优先级读取配置信息：</p>
 * <ol>
 *     <li>读取命令行参数，比如：server.port=9999</li>
 *     <li>读取命令行指定的配置文件，比如 -Dserver.properties=/opt/server.conf</li>
 *     <li>最后才是读取类路径中的 server.properties</li>
 * </ol>
 *
 * @author ChenHonghong@gmail.com
 * @since 13-4-16 上午10:11
 */
public class NodeConfig {

    private static Logger logger = Logger.getLogger(NodeConfig.class);

    private static Map config = new HashMap();

    private static NodeInfo nodeInfo = new NodeInfo();

    public void init() {

        //初始化服务器实例配置
        logger.info("----------------------------------");
        logger.info("Initializing Server Config");
        logger.info("----------------------------------");

        //首次，从类路径中读取配置文件
        Properties classConf = loadFromClasspath();
        merge(classConf);

        //将 server.id 设置为本机 IP
        try {
            InetAddress inet = InetAddress.getLocalHost();
            final String ip = inet.getHostAddress();
            merge("server.host",ip);

            String serverId = NodeConfig.getType() + "-" + ((ip.replace(".","-"))) + "-" + NodeConfig.getPort();
            merge("server.id",serverId);
        } catch (UnknownHostException e) {
            logger.error("无法获得本机IP");
        }

        //次之，从命令中指定的配置文件中读取
        String conf = System.getProperty("server.properties");
        if (StringUtils.isNotBlank(conf)) {
            try {
                Properties fsConf = loadFromFile();
                merge(fsConf);
            } catch (Exception e) {
                //ingore this exception
            }
        }

        //最后，从命令行中读取配置信息
        merge("server.host",System.getProperty("server.host"));
        merge("server.port",System.getProperty("server.port"));
        merge("server.tcp.port",System.getProperty("server.tcp.port"));
        merge("server.type",System.getProperty("server.type"));
        merge("server.id",System.getProperty("server.id"));
        merge("manage.url",System.getProperty("manage.url"));
        //x.server.id 测试，优先 sg-service 脚本中指定的服务器ID
        merge("server.id",(String)config.get("x.server.id"));

        nodeInfo.setBuildVersion(NodeConfig.getBuildVersion());
        nodeInfo.setBuildTime(NodeConfig.getBuildTime());
        nodeInfo.setHost(NodeConfig.getHost());
        nodeInfo.setId(NodeConfig.getId());
        nodeInfo.setType(NodeConfig.getType());
        nodeInfo.setPort(NodeConfig.getPort());
        nodeInfo.setTcpPort(NodeConfig.getTcpPort());

        logger.info("Server Node Config:" + config);
    }



    public static NodeInfo getInfo(){
        return nodeInfo;
    }

    private static Properties loadFromFile() throws Exception {
        String confFile = null;
        Properties properties = new Properties();
        confFile = System.getProperty("server.properties");
        properties.load(new FileInputStream(confFile));
        return properties;
    }

    private static Properties loadFromClasspath() {
        Properties param = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream("server.properties");
        try {
            param.load(stream);
            stream.close();
        } catch (Exception e) {
            param = null;
        }

        return param;
    }

    public static String getManageUrl() {
        return (String)config.get("manage.url");
    }

    public static String getHost() {
        return (String)config.get("server.host");
    }

    public static int getPort() {
        return new Integer((String)config.get("server.port"));
    }

    public static int getTcpPort() {
        final String s = (String) config.get("server.tcp.port");
        if(StringUtils.isBlank(s)){
            return 0;
        }
        return new Integer(s);
    }

    public static String getType() {
        return (String)config.get("server.type");
    }

    public static String getId() {
        return (String)config.get("server.id");
    }

    public static String getBuildVersion(){
        return (String) config.get("build.version");
    }

    public static String getBuildTime(){
        return (String) config.get("build.time");
    }

    private static void merge(Properties properties){
        if(properties == null){
            return;
        }

        HashMap temp = new HashMap();
        temp.putAll(properties);

        Set keys = temp.keySet();
        Iterator iterator = keys.iterator();

        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            String value = (String)temp.get(key);
            if(StringUtils.isNotBlank(value)){
                config.remove(key);
                config.put(key,value);
            }
        }
    }

    private static void merge(String key, String value){
        if(StringUtils.isNotBlank(value)){
            config.remove(key);
            config.put(key,value);
        }
    }

    public static void main(String[] args) {
        new NodeConfig().init();

        System.out.println(getHost());
    }

}
