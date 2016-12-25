package com.hxat.anxin.nettyclient;

import java.io.IOException;
import java.util.Scanner;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Hello world!
 *
 */
public class App implements Runnable
{
	private int port=8123;
	private String host = "127.0.0.1";
	
	private final TimeClientHandler handler = new TimeClientHandler();
	public void connect()throws Exception{
		EventLoopGroup group = new NioEventLoopGroup();
		try{
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
			              .option(ChannelOption.TCP_NODELAY, true)
			              .handler(new ChannelInitializer<SocketChannel>() {
			            	  @Override
			            	  public void initChannel(SocketChannel ch)
			            	     throws Exception {
			            		  ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
			            		  ch.pipeline().addLast(new StringDecoder());
			            		  ch.pipeline().addLast(handler);
			            	  }
						});
			ChannelFuture f = b.connect(host, port).sync();
			f.channel().closeFuture().sync();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			group.shutdownGracefully();
		}
	}

	public void run(){
		try {
			connect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	 /**启动客户端控制台*/
    private void runServerCMD() throws IOException {
       
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        String ss= null ;
        
        do{
            ss =   scanner.nextLine();
        }
        while (handler.sendMsg(ss));
    }
	
    public void start() throws IOException{
        new Thread(this).start();
        runServerCMD();
    }
    
    public static void main( String[] args ) throws Exception {
      new App().start();
    }
}
