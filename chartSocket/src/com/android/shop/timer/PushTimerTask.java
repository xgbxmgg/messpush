package com.android.shop.timer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;

import com.android.shop.dao.GetPushDao;
import com.android.shop.dao.SetPushDao;
import com.android.shop.dao.impl.GetPushDaoImpl;
import com.android.shop.dao.impl.ProcedureImpl;
import com.android.shop.dao.impl.SetPushDaoImpl;
import com.android.shop.handler.ServerHandler;
import com.android.shop.sys.Information;
import com.android.shop.sys.ProtocolParameter;
import com.enveesoft.gz163.domain.GetPush;

public class PushTimerTask extends TimerTask {
	Logger log = Logger.getLogger(getClass().getName());
	private ServerHandler handler;
	private GetPushDao getPushDao = new GetPushDaoImpl();
	private SetPushDao setPushDao = new SetPushDaoImpl();
	private ProcedureImpl call = new ProcedureImpl();
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd H:m:s");

	public PushTimerTask(ServerHandler handler) {
		this.handler = handler;
	}

	@Override
	public void run() {
		// log.info(new Date() + " timer is running");
		Map<Channel, String> map = new HashMap<Channel, String>(
				handler.getChannelMap());
		List<String> users = new ArrayList<String>(handler.getUsers());
		log.info(format.format(new Date()) + " user size " + users.size());
		for (String str : users) {
			setPushDao.insertPush(str);
			call.execProcedure();
			// log.info(format.format(new Date()) + " procedure is runed");
			List<GetPush> list = getPushDao.findByIms(str);
			if (map.containsValue(str)) {
				Channel channel = this.getKey(map, str);
				if (channel != null && channel.isConnected()) {
					if (list.size() > 0) {
						log.info(format.format(new Date()) + " push user:"
								+ str + " message size is :" + list.size()
								+ " rows");
						for (GetPush push : list) {
							Information infoT = new Information(
									ProtocolParameter.COMMESSRES,
									ProtocolParameter.RESSUCCESS, push);
							ChannelFuture future = channel.write(infoT);
							if (future.isSuccess()) {
								log.info(format.format(new Date())
										+ " push out message info to user:"
										+ str + " message id:"
										+ push.getPushId()
										+ " message content:"
										+ push.getContext() + " message type:"
										+ push.getType() + " message flag:"
										+ push.getFlag());
							} else {
								log.info(format.format(new Date())
										+ " push out message  to user fail:"
										+ str + " message id:"
										+ " exception is:" + future.getCause()
										+ " message id is:" + push.getPushId()
										+ " message content:"
										+ push.getContext() + " message type:"
										+ push.getType() + " message flag:"
										+ push.getFlag());
							}
						}
						log.info(format.format(new Date())
								+ " push out message info to user:" + str
								+ " is done");
					}
				}
			}
		}
	}

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
}
