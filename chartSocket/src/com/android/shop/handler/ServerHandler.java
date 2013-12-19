package com.android.shop.handler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import com.android.shop.sys.Information;
import com.android.shop.sys.MessageInfo;
import com.android.shop.sys.ProtocolParameter;

public class ServerHandler extends SimpleChannelHandler {

	Logger log = Logger.getLogger(getClass());

	public Map<Channel, String> getChannelMap() {
		return channelMap;
	}

	public List<String> getUsers() {
		return users;
	}

	private static Map<Channel, String> channelMap = new HashMap<Channel, String>();
	private static List<String> users = new ArrayList<String>();
	private Calendar cal = Calendar.getInstance();
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd H:m:s");

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		log.info(format.format(new Date()) + " the user: "
				+ channelMap.get(e.getChannel()) + " disconnect from server ");
		this.close(e.getChannel());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		log.info(format.format(new Date()) + " the user: "
				+ channelMap.get(e.getChannel()) + " has encounter exception "
				+ e.getCause().getMessage());
		this.close(e.getChannel());
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		/*
		 * int hour = cal.get(Calendar.HOUR); int minute =
		 * cal.get(Calendar.MINUTE); if (hour > 15 && minute > 15) {
		 * log.info("time end out!" + hour + ":" + minute); channelMap.clear();
		 * users.clear(); }
		 */
		Information info = (Information) e.getMessage();
		switch (info.getCommand()) {
		case 1:
			userLogin(ctx, info);
			break;
		case 3:
			sendMessage(ctx, info);
			break;
		default:
			;
		}
	}

	private void sendMessage(ChannelHandlerContext ctx, Information info) {
		MessageInfo mess = (MessageInfo) info.getObject();
		for (String in : mess.getToUsers()) {
			Channel channel = getKey(channelMap, in);
			if (channel != null && !(in == mess.getFromUser())) {
				Information infoT = new Information(
						ProtocolParameter.COMMESSRES,
						ProtocolParameter.RESSUCCESS, mess.getFromUser()
								+ "  say:" + mess.getMessage() + "\n");
				channel.write(infoT);
			} else {
				Information infoT = new Information(
						ProtocolParameter.COMMESSRES,
						ProtocolParameter.RESSENDFAIL, "用户未在线！");
				ctx.getChannel().write(infoT);
			}
		}
	}

	private void userLogin(ChannelHandlerContext ctx, Information info) {
		String user = (String) info.getObject();
		// log.info(" user ims is:"+user);
		if (user != null && !user.equals("")) {
			if (!channelMap.containsValue(user)) {
				log.info(format.format(new Date()) + ":" + user
						+ " login service.");
				channelMap.put(ctx.getChannel(), user);
				users.add(user);
			} else {
				log.info(format.format(new Date()) + ":" + user
						+ " login repeat!");
				if (!channelMap.containsKey(ctx.getChannel())) {
					ctx.getChannel().close();
				}
			}
		} else {
			log.info(format.format(new Date()) + " login with null name");
			ctx.getChannel().close();
			log.info("close null name:" + ctx.getChannel().close().toString());
		}
	}

	// 通过map的value值去获取相对应得key，这里即用户所建立的channel。
	public Channel getKey(Map<Channel, String> map, String value) {
		Channel key = null;
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Entry) it.next();
			if (entry.getValue().equals(value)) {
				key = (Channel) entry.getKey();
			}
		}
		return key;
	}

	private void updateClientUser(Information infoT) {
		for (Channel c : channelMap.keySet()) {
			c.write(infoT);
		}
	}

	private void close(Channel channel) {
		users.remove(channelMap.get(channel));
		channelMap.remove(channel);
		channel.close();
	}
}
