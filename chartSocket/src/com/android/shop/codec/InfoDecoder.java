package com.android.shop.codec;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import com.android.shop.sys.Information;

public class InfoDecoder extends FrameDecoder {

	@Override
	protected Object decode(ChannelHandlerContext context, Channel channel,
			ChannelBuffer buffer) throws Exception {
		if (buffer.readableBytes() < 12) {
			return null;
		}
		buffer.markReaderIndex();
		Integer command = buffer.readInt();
		Integer result = buffer.readInt();
		Integer length = buffer.readInt();
		if (buffer.readableBytes() < length) {
			buffer.resetReaderIndex();
			return null;
		}
		ChannelBuffer dataBuffer = ChannelBuffers.buffer(length);
		buffer.readBytes(dataBuffer, length);
		ByteArrayInputStream input = new ByteArrayInputStream(
				dataBuffer.array());
		ObjectInputStream ois = new ObjectInputStream(input);
		Object obj = ois.readObject();
		Information info = new Information(command, result, obj);
		return info;
	}

}
