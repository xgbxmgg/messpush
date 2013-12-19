package com.android.shop.codec;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelDownstreamHandler;

import com.android.shop.sys.Information;

public class InfoEncoder extends SimpleChannelDownstreamHandler {

	@Override
	public void writeRequested(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		Information info = (Information) e.getMessage();
		ByteBuffer headBuffer = ByteBuffer.allocate(12);
		headBuffer.putInt(info.getCommand());
		headBuffer.putInt(info.getResult());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(out);
		oos.writeObject(info.getObject());
		oos.flush();
		oos.close();
		ChannelBuffer dataBuffer = ChannelBuffers.dynamicBuffer();
		dataBuffer.writeBytes(out.toByteArray());
		int length = dataBuffer.readableBytes();
		headBuffer.putInt(length);
		headBuffer.flip();
		ChannelBuffer totalBuffer = ChannelBuffers.dynamicBuffer();
		totalBuffer.writeBytes(headBuffer);
		totalBuffer.writeBytes(dataBuffer);
		Channels.write(ctx, e.getFuture(), totalBuffer);
	}
}
