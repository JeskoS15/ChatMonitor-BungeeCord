package de.jesko.chatmonitor;

import java.io.IOException;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class cmdCM extends Command{

	public cmdCM(String string, main main) {
		super(string, "chatmonitor.command.cm");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		String error = "§c/cm add BlackList <Word>\n/cm add Support <KeyWord> <Support-Message>\n/cm ChatLog <Player> <show>\n§eVerwende für §bFarbCodes §e bitte §f'$'§f.";
		ProxiedPlayer pp = (ProxiedPlayer) sender;
		
		
			if(args.length < 3){
				pp.sendMessage(error);
				return;
			}
			
			if(args[0].equalsIgnoreCase("add")){
				
				//########################################
				if(args[1].equalsIgnoreCase("BlackList")){
					if(args.length == 3){
						String s = main.Logyml.getString("BlackList");
						if(!s.contains(args[2])){
							
							main.Logyml.set("BlackList", s+" "+args[2].toLowerCase());
							main.blacklist.add(args[2].toLowerCase());
							pp.sendMessage("§a[§bCM§a] §e"+args[2]+" §awurde aus die BlackList gestetzt!");
							
							try {
								ConfigurationProvider.getProvider(YamlConfiguration.class).save(main.Logyml, main.file);
							} catch (IOException e) {
								e.printStackTrace();
							}
							
						}else{pp.sendMessage("§a[§bCM§a] §e"+args[2]+" §aist bereits auf der BlackList!");}	
					}else{pp.sendMessage(error);}
				//########################################
				}else if(args[1].equalsIgnoreCase("Support")){
					if(args.length >= 4){
						
						String SupportName = args[2].toLowerCase();
						String msg = "";
						
						if(main.supportlist.contains(SupportName)){
							pp.sendMessage("§a[§bCM§a] §e"+args[2]+" §aist bereits auf der SupportList!");
							return;
						}
						for(int i = 3; i!=args.length;i++){
							msg += args[i]+" ";
						}
						
						msg = msg.replace("$", "§");
						
						main.Logyml.set("SupportList.Supports", main.Logyml.getString("SupportList.Supports")+" "+SupportName);
						main.Logyml.set("SupportList."+SupportName+"M", msg);
						main.supportlist.add(SupportName);
						try {
							ConfigurationProvider.getProvider(YamlConfiguration.class).save(main.Logyml, main.file);
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						pp.sendMessage("§a[§bCM§a] §e"+args[2]+" §awurde als Support hinzugefügt mit: §9"+msg);
					}else{pp.sendMessage(error);}
				}else{pp.sendMessage(error);}
				
			}else if(args[0].equalsIgnoreCase("ChatLog") && args[2].equalsIgnoreCase("show")){
				
				if(!main.ChatLog){
					pp.sendMessage("§a[§bCM§a] §aChatLog ist deaktiviert!");
					return;
				}
				
				if(main.Logyml.getString("ChatLog."+args[1]) == ""){
					pp.sendMessage("§a[§bCM§a] §e"+args[1]+" §aist nie auffällig geworden.");
					return;
				}
				
				String PlayerLog = main.Logyml.getString("ChatLog."+args[1]);
				PlayerLog = PlayerLog.replace("#-#", "\n§c -> §d");
				
				pp.sendMessage("§a[§bCM§a] §a ChatLog von §e"+args[1]+"\n§d"+PlayerLog);
				
			}else{pp.sendMessage(error);}
			
			
			
		
		return;
			
		
	}

}
