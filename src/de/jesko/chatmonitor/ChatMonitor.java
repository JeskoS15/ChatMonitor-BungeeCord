package de.jesko.chatmonitor;

import java.io.IOException;
import java.util.Scanner;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class ChatMonitor {
	
	public static String checkBlackList(ChatEvent e){
		String msg = e.getMessage().toLowerCase();
		String msgOrgi = e.getMessage();
		int counter = 0;

		msg = msg.replace(".", " ");
		msg = msg.replace("!", " ");
		msg = msg.replace("?", " ");
		msg = msg.replace(",", " ");
		msg = msg.replace(":", " ");
		msg = msg.replace(";", " ");
		
		Scanner scan1 = new Scanner(msg);
		
		while(scan1.hasNext()){
			if(main.blacklist.contains(scan1.next())){
				System.out.println("wort ist in der blacklist");
				Scanner scan2 = new Scanner(msgOrgi);
				for(int i = 0; i<counter;i++){scan2.next();}
				msgOrgi = msgOrgi.replace(scan2.next(), "*****");
				scan2.close();
				
				if(main.ChatLog){
					String PlayerLog = " ";
					
					if(!(main.Logyml.getString("ChatLog."+((CommandSender) e.getSender()).getName()) == null)){
						PlayerLog = main.Logyml.getString("ChatLog."+((CommandSender) e.getSender()).getName());
					}
					
					PlayerLog += "#-# "+e.getMessage();
					
					main.Logyml.set("ChatLog."+((CommandSender) e.getSender()).getName(), PlayerLog);
					try {ConfigurationProvider.getProvider(YamlConfiguration.class).save(main.Logyml, main.file);}catch (IOException r){}
				}
			}
			counter++;
		}
		
		scan1.close();
		return msgOrgi;
	}
	
	
	@SuppressWarnings("deprecation")
	public static void checkSupport(String msg, ProxiedPlayer p){
		
		msg = msg.replace(".", " ");
		msg = msg.replace("!", " ");
		msg = msg.replace("?", " ");
		msg = msg.replace(",", " ");
		msg = msg.replace(":", " ");
		msg = msg.replace(";", " ");
		
		Scanner sc = new Scanner(msg);
		
		while(sc.hasNext()){
			String s = sc.next().toLowerCase();
			if(main.supportlist.contains(s)){
				p.sendMessage(main.Logyml.getString("SupportList."+s+"M"));
			}
		}	
		sc.close();
	}
	
}
