package com.android.shop.handler;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.timeout.IdleState;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;

import com.android.shop.sys.Information;
import com.android.shop.sys.ProtocolParameter;
import com.enveesoft.gz163.domain.GetPush;

public class ServerIdleStateAware extends IdleStateAwareChannelHandler {
	Logger log = Logger.getLogger(this.getClass());

	@Override
	public void channelIdle(ChannelHandlerContext ctx, IdleStateEvent e)
			throws Exception {
		Information infoT = new Information(ProtocolParameter.COMMESSRES,
				ProtocolParameter.RESSUCCESS, new GetPush(9999, 9, "empty",
						"0", 9999));
		if (e.getState() == IdleState.WRITER_IDLE) {
			e.getChannel().write(infoT);

		} /*
		 * else if (e.getState() == IdleState.READER_IDLE) {
		 * e.getChannel().write(infoT); } else if (e.getState() ==
		 * IdleState.ALL_IDLE) { }
		 */
	}

}
