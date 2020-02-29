package net.rccdevelopment.joinme.commands;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.rccdevelopment.joinme.JoinMe;
import net.rccdevelopment.joinme.messages.PictureCharacter;
import net.rccdevelopment.joinme.messages.PictureMessage;
import net.rccdevelopment.joinme.utils.TokensSQL;

public class JoinMe_cmd extends Command{

	public JoinMe plugin;

	public JoinMe_cmd(String name, JoinMe plugin) {
		super(name);
		this.plugin = plugin;
	}
	
	private TokensSQL tokens = new TokensSQL();

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!(sender instanceof ProxiedPlayer)) {
			sender.sendMessage(plugin.prefix + "§7Du musst ein Spieler sein, um diesen Befehl zu verwenden.");
			return;
		}

		ProxiedPlayer p = (ProxiedPlayer) sender;

		String prefix = plugin.prefix;

		ProxyServer.getInstance().getScheduler().runAsync(plugin, new Runnable() {

			@SuppressWarnings({ "deprecation" })
			@Override
			public void run() {
				if (args.length == 0) {
					if (tokens.getTokens(p.getUniqueId().toString()) >= 1) {
						if (!p.getServer().getInfo().getName().contains("Lobby")) {
					
							if (JoinMe.joinme.containsKey(p)) {
								if (!JoinMe.joinme.get(p).equalsIgnoreCase(p.getServer().getInfo().getName())) {
									JoinMe.joinme.remove(p);
								}
							}

							for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
								URL url = null;
								try {
									url = new URL("https://minotar.net/avatar/" + p.getName() + "/8.png");
								} catch (MalformedURLException ex) {
									ex.printStackTrace();
								}
								PictureMessage PictureMessage = null;
								try {
									PictureMessage = new PictureMessage(ImageIO.read(url), 8, PictureCharacter.BLOCK.getChar());
								} catch (IOException ex) {
									ex.printStackTrace();
								}
								int i = 1;
								for (String lines : PictureMessage.getLines()) {
									if (i != 4 && i != 5) {
										players.sendMessage(lines);
									}
									if (i == 4) {
										players.sendMessage(lines + " " + p.getDisplayName() + " §7spielt aktuell §6"
												+ (p.getServer().getInfo().getName().contains("-")
														? p.getServer().getInfo().getName().split("-")[0]
														: p.getServer().getInfo().getName())
												+ "§7.");
									}
									if (i == 5) {
										TextComponent linesString = new TextComponent(lines + " ");
										TextComponent joinString = new TextComponent("§a§nServer betreten");

										joinString.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§8» §e" + p.getServer().getInfo().getName()).create()));

										joinString.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/joinme " + p.getServer().getInfo().getName()));
										
										
										linesString.addExtra(joinString);

										players.sendMessage(linesString);
										tokens.setTokens(p.getUniqueId().toString(), -1);
									}
									i++;
								}
							}
							JoinMe.joinme.put(p, p.getServer().getInfo().getName());
							return;
						} else {
							p.sendMessage(prefix + "§7Du darfst diese Funktion nicht in der Lobby verwenden.");
							return;
						}
					} else {
						p.sendMessage(prefix + "§7Zu wenige Tokens");
						return;
					}
				}
				if (args.length == 1) {
					boolean isValid = false;
					for (String servers : ProxyServer.getInstance().getServers().keySet()) {
						if (args[0].equalsIgnoreCase(servers) && !args[0].equalsIgnoreCase("Lobby")) {
							isValid = true;
						}
					}
					if (!isValid) {
						p.sendMessage(prefix + "§7Dieser Befehl existiert nicht.");
						return;
					}
					isValid = false;
					for (String servers : JoinMe.joinme.values()) {
						if (args[0].equalsIgnoreCase(servers)) {
							p.connect(ProxyServer.getInstance().getServerInfo(servers));
							p.sendMessage(prefix + "§7Du wirst auf den Server §e" + servers + " §7verschoben.");
							isValid = true;
							break;
						}
					}
					if (!isValid) {
						p.sendMessage(prefix + "§7Der Spieler dieses JoinMe's hat diesen Server bereits verlassen.");
					}
					return;
				} else {
					p.sendMessage(prefix + "§7Dieser Befehl existiert nicht.");
					return;
				}
			}
		});
	}

}


