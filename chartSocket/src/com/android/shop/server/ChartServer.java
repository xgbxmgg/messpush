package com.android.shop.server;

import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.concurrent.Executors;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import com.android.shop.handler.ServerInitializer;
import com.android.shop.timer.PushTimerTask;

public class ChartServer {
	private static ServerInitializer init = new ServerInitializer();

	public static void main(String[] args) {
		ChannelFactory cf = new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());
		ServerBootstrap boot = new ServerBootstrap(cf);
		boot.setPipelineFactory(init);
		boot.setOption("child.tcpNoDelay", true);
		boot.setOption("child.keepAlive", true);
		boot.bind(new InetSocketAddress(8090));
		final Timer time = new Timer();
		time.schedule(new PushTimerTask(init.getHandler()), 0, 80000);
	}
}
