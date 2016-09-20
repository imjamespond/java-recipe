package com.pengpeng.stargame;

import java.util.Locale;

/**
 * 服务器配置信息
 * 
 * @author 林佛权
 * 
 */
public class ServerConfigure {
	private int soltSize =2;//建造队列大小
	private int mapx = 100;
	private int mapy = 100;//地图大小
	private int port = 443;//服务器端口
	private boolean debug;//是否debug模式

	private Locale local = Locale.CHINA;//语言选择
	
	private long timeStep = 1000;//计时器间隔时间
	private boolean refresh =true;//是否刷新
	private long refreshTime = 6*60*1000;//刷新时间
	private long rankStep = 60*60*1000;//排名间隔时间
	
	private boolean log;//记录用户行为
	private String logIp;//logserver 地址
	private int logPort;//logserver 端口
	
	private boolean jmx = true;//是否启动jmx
	private int jmxPort = 81;//jmx端口
	
	private boolean gm = false;//是否启动gm
	private int gmPort = 82;//gm端口

    private String defaultScene;
    private int defaultX;
    private int defaultY;

    public String getDefaultScene() {
        return defaultScene;
    }

    public void setDefaultScene(String defaultScene) {
        this.defaultScene = defaultScene;
    }

    public int getDefaultX() {
        return defaultX;
    }

    public void setDefaultX(int defaultX) {
        this.defaultX = defaultX;
    }

    public int getDefaultY() {
        return defaultY;
    }

    public void setDefaultY(int defaultY) {
        this.defaultY = defaultY;
    }

    public boolean isDebug() {
		return debug;
	}
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	public boolean isGm() {
		return gm;
	}
	public void setGm(boolean gm) {
		this.gm = gm;
	}
	public int getGmPort() {
		return gmPort;
	}
	public void setGmPort(int gmPort) {
		this.gmPort = gmPort;
	}
	public boolean isJmx() {
		return jmx;
	}
	public void setJmx(boolean jmx) {
		this.jmx = jmx;
	}
	public int getJmxPort() {
		return jmxPort;
	}
	public void setJmxPort(int jmxPort) {
		this.jmxPort = jmxPort;
	}
	public Locale getLocal() {
		return local;
	}
	public void setLocal(Locale local) {
		this.local = local;
	}
	public boolean isLog() {
		return log;
	}
	public void setLog(boolean log) {
		this.log = log;
	}
	public String getLogIp() {
		return logIp;
	}
	public void setLogIp(String logIp) {
		this.logIp = logIp;
	}
	public int getLogPort() {
		return logPort;
	}
	public void setLogPort(int logPort) {
		this.logPort = logPort;
	}
	public int getMapx() {
		return mapx;
	}
	public void setMapx(int mapx) {
		this.mapx = mapx;
	}
	public int getMapy() {
		return mapy;
	}
	public void setMapy(int maxy) {
		this.mapy = maxy;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public long getRankStep() {
		return rankStep;
	}
	public void setRankStep(long rankStep) {
		this.rankStep = rankStep;
	}
	public boolean isRefresh() {
		return refresh;
	}
	public void setRefresh(boolean refresh) {
		this.refresh = refresh;
	}
	public long getRefreshTime() {
		return refreshTime;
	}
	public void setRefreshTime(long refreshTime) {
		this.refreshTime = refreshTime;
	}
	public int getSoltSize() {
		return soltSize;
	}
	public void setSoltSize(int soltSize) {
		this.soltSize = soltSize;
	}
	public long getTimeStep() {
		return timeStep;
	}
	public void setTimeStep(long timeStep) {
		this.timeStep = timeStep;
	}
	
	public String toString(){
		StringBuffer str = new StringBuffer("[");
		str.append("soltSize=").append(this.soltSize).append(";");
		str.append("mapx=").append(this.mapx).append(";");
		str.append("mapy=").append(this.mapy).append(";");
		str.append("port=").append(this.port).append(";");
		str.append("debug=").append(this.debug).append(";");
		str.append("timeStep=").append(this.timeStep).append(";");
		str.append("refresh=").append(this.refresh).append(";");
		str.append("refreshTime=").append(this.refreshTime).append(";");
		str.append("log=").append(this.log).append(";");
		str.append("logIp=").append(this.logIp).append(";");
		str.append("logPort=").append(this.logPort).append(";");
		str.append("jmx=").append(this.jmx).append(";");
		str.append("jmxPort=").append(this.jmxPort).append(";");
		str.append("gm=").append(this.gm).append(";");
		str.append("gmPort=").append(this.gmPort).append(";");
		return str.toString();
	}
}
