package com.android.shop.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import com.android.shop.codec.InfoDecoder;
import com.android.shop.codec.InfoEncoder;
import com.android.shop.handler.ClientHandler;
import com.android.shop.sys.Command;
import com.android.shop.sys.Information;
import com.android.shop.sys.MessageInfo;
import com.android.shop.sys.ProtocolParameter;
import com.enveesoft.gz163.domain.User;

public class Client extends JFrame {
	private static final long serialVersionUID = 1L;
	private Channel channel;
	private User user;
	JTextArea showText = this.getJTextArea("消息显示", 0, 0, 300, 160);
	JList list = new JList();
	JScrollPane jsPane = new JScrollPane();
	JTextArea sendText = this.getJTextArea("消息发送", 20, 260, 300, 50);
	JLabel idLabel = new JLabel("编号：");
	JTextField idText = new JTextField();
	JLabel nameLabel = new JLabel("账号");
	JTextField nameText = new JTextField();
	JLabel passLabel = new JLabel("密码：");
	JPasswordField passText = new JPasswordField();
	JButton login = this.getJButton("登陆", 85, 320, 70, 50);
	JButton sendMessage = this.getJButton("发送", 85, 320, 70, 50);
	JButton cancle = this.getJButton("取消", 160, 320, 70, 50);

	public Client() {
		this.setTitle("局域网聊天");
		setVisible(true);
		pack();
		setLocation(350, 200);
		idLabel.setBounds(20, 230, 40, 20);
		idLabel.setVisible(true);
		idText.setBounds(60, 230, 60, 20);
		idText.setVisible(true);
		nameLabel.setBounds(120, 230, 40, 20);
		nameLabel.setVisible(true);
		nameText.setBounds(160, 230, 60, 20);
		nameText.setVisible(true);
		passLabel.setBounds(220, 230, 40, 20);
		passLabel.setVisible(true);
		passText.setBounds(260, 230, 60, 20);
		passText.setVisible(true);

		// getContentPane().add(showText);
		getContentPane().add(sendText);
		getContentPane().add(idLabel);
		getContentPane().add(nameLabel);
		getContentPane().add(passLabel);
		getContentPane().add(idText);
		getContentPane().add(nameText);
		getContentPane().add(passText);
		showText.setLineWrap(true);
		showText.setWrapStyleWord(true);
		sendText.setVisible(false);
		sendMessage.setVisible(false);
		list.setBounds(0, 160, 300, 60);
		list.setBackground(Color.GREEN);
		// list.setVisibleRowCount(3);
		// 20, 32, 300, 160
		jsPane.setBounds(20, 32, 300, 220);
		jsPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jsPane.setViewportView(showText);
		// jsPane.setViewportView(list);
		jsPane.setVisible(false);

		list.setVisible(false);

		getContentPane().add(login);
		getContentPane().add(jsPane);
		getContentPane().add(sendMessage);
		getContentPane().add(cancle);
		login.addActionListener(new LoginAction());
		sendMessage.addActionListener(new SendMessageAction());
		cancle.addActionListener(new StopSendListener());
		getContentPane().setLayout(null);
		getContentPane().setBackground(Color.WHITE);
		setSize(350, 400);
		setResizable(true);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new CloseWindow());
	}

	public JLabel getJlabel(String text, int... location) {
		JLabel label = new JLabel(text);
		label.setFont(new Font(label.getText(), 0, 18));
		label.setBounds(location[0], location[1], location[2], location[3]);
		return label;
	}

	public JTextField getJTextField(String name, int... location) {

		JTextField text = null;
		switch (location[4]) {
		case 1:
			text = new JTextField();
			break;
		default:
			text = new JPasswordField();
			break;
		}
		text.setBounds(location[0], location[1], location[2], location[3]);
		text.setBorder(BorderFactory.createLineBorder(new java.awt.Color(51,
				255, 51)));
		return text;
	}

	public JTextArea getJTextArea(String name, int... location) {
		JTextArea areaText = new JTextArea();
		areaText.setBounds(location[0], location[1], location[2], location[3]);
		areaText.setBorder(BorderFactory.createLineBorder(new java.awt.Color(
				51, 255, 51)));
		return areaText;
	}

	public JButton getJButton(String text, int... location) {
		JButton button = new JButton(text);
		button.setBounds(location[0], location[1], location[2], location[3]);
		return button;
	}

	class CloseWindow extends WindowAdapter {

		@Override
		public void windowClosing(WindowEvent e) {
			int option = JOptionPane.showConfirmDialog(getParent(), "确定", "取消",
					JOptionPane.OK_CANCEL_OPTION);
			if (JOptionPane.OK_OPTION == option) {
				// 关闭连接
				if (channel != null) {
					if (channel.isConnected()) {
						channel.disconnect();
					}
				}
				System.exit(1);
			}
		}
	}

	class LoginAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			ChannelFactory cf = new NioClientSocketChannelFactory(
					Executors.newCachedThreadPool(),
					Executors.newCachedThreadPool());
			ClientBootstrap boot = new ClientBootstrap(cf);
			boot.setPipelineFactory(new ChannelPipelineFactory() {

				@Override
				public ChannelPipeline getPipeline() throws Exception {
					return Channels.pipeline(new InfoEncoder(),
							new InfoDecoder(),
							new ClientHandler(showText, list));
				}
			});
			boot.setOption("tcpNoDelay", true);
			boot.setOption("keepAlive", true);
			// boot.setOption("connectTimeoutMillis", 10000);
			/*
			 * ChannelFuture f = boot.connect(new
			 * InetSocketAddress("220.172.33.53", 8090));
			 * f.awaitUninterruptibly(); if (!f.isSuccess()) {
			 * System.out.println(f.getCause()); } else { channel =
			 * f.getChannel(); }
			 */
			// Wait until the connection is made successfully.
			channel = boot.connect(new InetSocketAddress("localhost", 8090))
					.awaitUninterruptibly().getChannel();
			if (channel.isConnected()) {
				System.out.println("connect ok !");
				user = new User(idText.getText(), nameText.getText(), passText
						.getPassword().toString());
				Information info = new Information(ProtocolParameter.COMLINK,
						ProtocolParameter.RESWORKING, user.getUserId());
				channel.write(info);
				idLabel.setVisible(false);
				idText.setVisible(false);
				nameLabel.setVisible(false);
				nameText.setVisible(false);
				passLabel.setVisible(false);
				passText.setVisible(false);
				login.setVisible(false);
				sendMessage.setVisible(true);
				sendText.setVisible(true);
				list.setVisible(true);
				jsPane.setVisible(true);
				showText.setText("");
			}
		}
	}

	class SendMessageAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (channel != null) {
				showText.setText(showText.getText() == null ? "" : showText
						.getText()
						+ user.getUserId()
						+ "  say:"
						+ sendText.getText() + "\n");
				List<String> toUsers = new ArrayList<String>();
				if (list.getSelectedValues().length != 0) {
					for (Object obj : list.getSelectedValues()) {
						if (!obj.equals(user.getUserId())) {
							toUsers.add((String) obj);
							MessageInfo mess = new MessageInfo(
									user.getUserId(), toUsers, sendText
											.getText().trim());
							// 协议对象，3表示发送消息请求
							Information info = new Information(
									ProtocolParameter.COMSENDMES,
									ProtocolParameter.RESWORKING, mess);
							channel.write(info);
							sendText.setText("");
						} else {
							sendText.setText("请选择你的好友进行聊天！");
							System.out.println("您选择的是自己！");
						}
					}
				} else {
					sendText.setText("请选择一个或多个好友聊天！");
					System.out.println("未选择聊天对象！");
				}
			}
		}
	}

	class StopSendListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			sendText.setText("");
		}
	}

	public static void main(String[] args) {
		Client client = new Client();
	}
}
