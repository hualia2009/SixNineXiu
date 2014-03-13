package com.ninexiu.client;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import android.os.Handler;
import android.util.Log;

public class ChatClient {
	
	ClientBootstrap bootstrap;
	ChannelFuture channelFuture = null;
	Channel ch;
	
	public ChatClient(){
		
	}
	
	public void start(String host, int port,final Handler mHandler) throws Exception {
		bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool()));
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline channelPipeline = Channels.pipeline();
				channelPipeline.addLast("encode", new StringEncoder());
				channelPipeline.addLast("decode", new StringDecoder());
				channelPipeline.addLast("handler", new ChatClientHandler(mHandler));
				return channelPipeline;
			}
		});
		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setOption("keepAlive", true);
		bootstrap.setOption("reuseAddress", true);
		channelFuture = bootstrap.connect(new InetSocketAddress(host,port));
		if(channelFuture!=null)
			ch = channelFuture.getChannel(); 
	}
	
	public boolean sendmsg(String msg){
		if(ch!=null && ch.isConnected()){
			ch.write(msg);
			return true;
		}
		return false;
	}
	
	public void disconnect() {
		Log.v("ChatClient","ChatClient disconnect...");
		try {
			Channel channel = channelFuture.awaitUninterruptibly().getChannel();
			if(ch!=null){
				ch.disconnect();
				ch.close();
			}
			if (channel != null) {
				channel.close().awaitUninterruptibly();
			}
			if (bootstrap != null) {
				bootstrap.releaseExternalResources();
			}
			if(channelFuture != null)
				channelFuture.getChannel().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
