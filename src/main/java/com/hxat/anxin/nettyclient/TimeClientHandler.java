package com.hxat.anxin.nettyclient;

import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeClientHandler extends ChannelInboundHandlerAdapter {

	private static final Logger logger = Logger.getLogger(TimeClientHandler.class.getName());
	
	private final ByteBuf firstMessage;
	private ChannelHandlerContext ctx;
	
	public TimeClientHandler(){
		byte[] req ="客户端请求建立连接：".getBytes();
		firstMessage = Unpooled.buffer(req.length);
		firstMessage.writeBytes(req);
	}
	
	public void channelActive(ChannelHandlerContext ctx){
		this.ctx = ctx;
		ctx.writeAndFlush(firstMessage);
	}
	
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException{
		ByteBuf buf = (ByteBuf) msg;
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		String body = new String(req, "UTF-8");
		System.out.println("Now is :"+ body);
 	}
	
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
		logger.warning("Unexpected exception from downstream :" + cause.getMessage());
		ctx.close();
	}
	
	public boolean sendMsg(String msg) {
		byte[] req =msg.getBytes();
		ByteBuf reqBuf = Unpooled.buffer(req.length);
		reqBuf.writeBytes(req);
        System.out.println("client:" + msg);
        ctx.channel().writeAndFlush(reqBuf);
        return msg.equalsIgnoreCase("q")? false : true;
    }
}
