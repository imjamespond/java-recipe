package com.metasoft.flying.node.net;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.metasoft.flying.service.common.SpringService;

@Component
public class RpcPipelineFactory implements ChannelPipelineFactory {

    //private LengthFieldPrepender frameEncoder = new LengthFieldPrepender(4);
	@Autowired
	private SpringService springService;

    public RpcPipelineFactory() {
    }

    @Override
    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline p = Channels.pipeline();
        //p.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(16 * 1024 * 1024, 0, 4, 0, 4));
        //p.addLast("rpcDecoder", rpcDecoder);
        p.addLast("decoder", new RpcDecoder());

        //p.addLast("frameEncoder", frameEncoder);
        //p.addLast("rpcEncoder", rpcEncoder);
        p.addLast("encoder", new RpcEncoder());

        p.addLast("handler", SpringService.getBean(RpcMessageHandler.class));
        return p;
    }

}
