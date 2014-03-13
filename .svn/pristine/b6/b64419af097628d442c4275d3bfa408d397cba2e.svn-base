package com.ninexiu.client;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import android.os.Handler;

import com.ninexiu.sixninexiu.ChatRoomActivity;

public class ChatClientHandler extends SimpleChannelHandler {
	
	private Handler mHandler;
	
	public ChatClientHandler(Handler mHandler) {
		this.mHandler = mHandler;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		Channel channel = e.getChannel();
		if(channel!=null)
			channel.close();
		//Log.e("ChatClientHandler","error connect:"+e.getCause().toString());
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		Object obj = e.getMessage();
		//Log.e("test","messageReceived = "+obj);
		if(obj != null){
			String content = (String) obj;
			mHandler.sendMessage(mHandler.obtainMessage(ChatRoomActivity.NORMAL_MESSAGE, content));
		}
	}
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)throws Exception {
		mHandler.sendMessage(mHandler.obtainMessage(ChatRoomActivity.SERVER_CONNECTED, null));
	}
}
