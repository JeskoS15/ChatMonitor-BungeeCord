package de.jesko.chatmonitor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

public class main extends Plugin implements Listener{
	
	public static List<String> blacklist = new ArrayList<String>();
	public static List<String> supportlist = new ArrayList<String>();
	public static Configuration Logyml;
	public static Configuration config;
	public static File file = new File("./plugins/ChatMonitor/Log.yml");
	public static File file_config = new File("./plugins/ChatMonitor/config.yml");
	public static boolean ChatLog;
	
	
	@Override
	public void onEnable(){
		loadConfig();
		registerCMD();
		loadBlackList();
		loadSupportList();
		
		getProxy().getPluginManager().registerListener(this, this);
	}
	
	private void registerCMD() {
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new cmdCM("cm", this));
	}

	private void loadConfig(){
		try {
			
			if(!getDataFolder().exists()){
				getDataFolder().mkdir();
			}
			
			File file = new File(getDataFolder().getPath(), "config.yml");
			
			if(!file.exists()){
				file.createNewFile();
				
				config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
				
				config.set("Config.Enable", true);
				config.set("Config.blacklist_warn", "§cWir möchten hier solche Wörter nicht!");
				config.set("Config.chat_log", false);
				config.set("Config.send_blocked_message", true);
				config.set("Config.allow_htttp-adress", true);
				config.set("Config.http_warn", "§cWir möchten hier keine Werbung!");
				config.set("Config.write_http_inChatLog", true);
				config.set("Config.Server_Adress", "###");
				
				ConfigurationProvider.getProvider(YamlConfiguration.class).save(main.config, main.file_config);
				
			}else{
				config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ChatLog = config.getBoolean("Config.chat_log");
	}
	
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onChat(ChatEvent e){
		
		if(config.getBoolean("Config.Enable")){
			ProxiedPlayer p = (ProxiedPlayer) e.getSender();
			
			String msg = ChatMonitor.checkBlackList(e);
			
			if(!(msg == e.getMessage())){
				p.sendMessage("§c" + config.getString("Config.blacklist_warn"));
				
				if(!config.getBoolean("Config.send_blocked_message")){
					e.setCancelled(true);
					return;
				}
			}else{
				ChatMonitor.checkSupport(msg, p);
			}
			
			if(config.getBoolean("Config.allow_htttp-adress") == false){
				if(ChatMonitor.checkHTTPAdress(msg, p)){
					e.setCancelled(true);
					return;
				}
			}
			
			e.setMessage(msg);
		}
	}
	
	private void loadSupportList(){
		Scanner sc = new Scanner(Logyml.getString("SupportList.Supports"));
		
		while(sc.hasNext()){
			supportlist.add(sc.next().toLowerCase());
		}
		sc.close();
	}
	
	private void loadBlackList(){
		try{
			if(!file.exists()){
				file.createNewFile();
				Logyml = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
				Logyml.set("BlackList", "hurensohn");
				Logyml.set("SupportList.Supports", " ");
				ConfigurationProvider.getProvider(YamlConfiguration.class).save(main.Logyml, main.file);
				
			}else{
				Logyml = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
			}
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		Scanner sc = new Scanner(Logyml.getString("BlackList"));
		while(sc.hasNext()){
			blacklist.add(sc.next().toLowerCase());
		}
		sc.close();
		
		
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(main.Logyml, main.file);
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(main.config, main.file_config);
		}catch (IOException e){}
	}
}
