package com.android.shop.handler;

import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.timeout.IdleStateHandler;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timer;

import com.android.shop.codec.InfoDecoder;
import com.android.shop.codec.InfoEncoder;

public class ServerInitializer implements ChannelPipelineFactory {
	private ServerHandler handler = new ServerHandler();

	private final Timer trigger = new HashedWheelTimer();
	private final ChannelHandler idleOutHandler = new IdleStateHandler(trigger,
			60, 60, 0);
	private ServerIdleStateAware idleStateAware = new ServerIdleStateAware();

	public ServerHandler getHandler() {
		return handler;
	}

	public void setHandler(ServerHandler handler) {
		this.handler = handler;
	}

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		return Channels.pipeline(idleOutHandler, idleStateAware,
				new InfoEncoder(), new InfoDecoder(), handler);
	}

}
