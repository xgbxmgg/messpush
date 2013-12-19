package com.android.shop.handler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import com.android.shop.sys.Information;
import com.android.shop.sys.ProtocolParameter;
import com.enveesoft.gz163.domain.GetPush;

public class ClientHandler extends SimpleChannelHandler {
	Logger log = Logger.getLogger(this.getClass().getName());

	// 消息显示文本域
	private JTextArea showText;
	// 在想用户列表显示控件
	private JList<String> list;
	// 在线用户列表
	private static List<String> users = new ArrayList<String>();
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd H:m:s");

	public ClientHandler(JTextArea showText, JList<String> list) {
		this.showText = showText;
		this.list = list;
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		// TODO Auto-generated method stub
		super.channelDisconnected(ctx, e);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		log.info(format.format(new Date()) + " the channel "
				+ ctx.getChannel().getId() + " has encounter exceptio "
				+ e.getCause().getMessage());
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		Information info = (Information) e.getMessage();
		switch (info.getCommand()) {
		case 2:
			flashUser(info, showText);
			break;
		case 4:
			flashMessage(info, showText);
			break;
		default:
			;
		}
	}

	private void flashMessage(Information info, JTextArea showText2) {
		if (info.getResult().equals(ProtocolParameter.RESSUCCESS)) {
			GetPush rec = (GetPush) info.getObject();
			showText.setText(showText.getText() == null ? "" : showText
					.getText()
					+ rec.getType()
					+ " message is:"
					+ rec.getContext()
					+ " at:"
					+ format.format(new Date())
					+ "\n");
			if (showText.getText().length() > 2000) {
				showText.setText("");
			}
		} else {
			System.out.println("发送失败！");
		}
	}

	private void flashUser(Information info, JTextArea showText2) {
		if (info.getResult().equals(ProtocolParameter.RESSUCCESS)) {
			List<String> temps = (List<String>) info.getObject();
			DefaultListModel<String> dlm = new DefaultListModel<String>();
			for (String temp : temps) {
				users.add(temp);
			}
			for (String str : users) {
				dlm.addElement(str);
			}
			list.setModel(dlm);
		} else if (info.getResult().equals(ProtocolParameter.RESMESSDEL)) {
			List<String> temps = (List<String>) info.getObject();
			DefaultListModel<String> dlm = new DefaultListModel<String>();
			for (String temp : temps) {
				users.remove(temp);
			}
			for (String str : users) {
				dlm.addElement(str);
			}
			list.setModel(dlm);
		} else {

		}
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
	}

}
