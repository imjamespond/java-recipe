package com.pengpeng.test;

import org.jboss.netty.channel.*;

/**
 * @auther foquan.lin@com.pengpeng.com
 * @since: 13-3-22上午11:37
 */
public class ChannelHandlerContextMock implements ChannelHandlerContext {
    private Object token;
    @Override
    public Channel getChannel() {
        return null;  //TODO:方法需要实现
    }

    @Override
    public ChannelPipeline getPipeline() {
        return null;  //TODO:方法需要实现
    }

    @Override
    public String getName() {
        return null;  //TODO:方法需要实现
    }

    @Override
    public ChannelHandler getHandler() {
        return null;  //TODO:方法需要实现
    }

    @Override
    public boolean canHandleUpstream() {
        return false;  //TODO:方法需要实现
    }

    @Override
    public boolean canHandleDownstream() {
        return false;  //TODO:方法需要实现
    }

    @Override
    public void sendUpstream(ChannelEvent e) {
        //TODO:方法需要实现
    }

    @Override
    public void sendDownstream(ChannelEvent e) {
        //TODO:方法需要实现
    }

    @Override
    public Object getAttachment() {
        return token;
    }

    @Override
    public void setAttachment(Object attachment) {
        this.token = attachment;
    }
}
