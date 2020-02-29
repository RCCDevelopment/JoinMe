package net.rccdevelopment.joinme.commands;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.rccdevelopment.joinme.JoinMe;
import net.rccdevelopment.joinme.utils.TokensSQL;

public class Tokens_cmd extends Command{

	private JoinMe plugin;

	private TokensSQL tokens = new TokensSQL();

	public Tokens_cmd(String name, JoinMe plugin) {
		super(name);
		this.plugin = plugin;

	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {

		if (sender instanceof ProxiedPlayer) {

			ProxiedPlayer player = (ProxiedPlayer) sender;
			if (player.hasPermission("cafe.tokens")) {

				if (args.length == 2) {

					if (args[0].equalsIgnoreCase("get")) {

						if (player.hasPermission("cafe.tokens")) {

							if (BungeeCord.getInstance().getPlayer(args[1]) != null) {

								ProxiedPlayer reciever = BungeeCord.getInstance().getPlayer(args[1]);

								if (!tokens.playerExists(reciever.getUniqueId().toString())) {
									tokens.createPlayer(reciever.getUniqueId().toString());

									player.sendMessage(new TextComponent(
											plugin.prefix + "§7Der Spieler wurde gerade neu registriert"));

								} else {
									player.sendMessage(new TextComponent(plugin.prefix + "§7Der Spieler hat: §6"
											+ tokens.getTokens(reciever.getUniqueId().toString() + " §7Tokens")));
								}

							} else {
								player.sendMessage(
										new TextComponent(plugin.prefix + "§7Der angegebene Spieler ist nicht online"));
							}

						}

					} else if (args.length == 3) {

						if (args[0].equalsIgnoreCase("set")) {

							ProxiedPlayer getter = BungeeCord.getInstance().getPlayer(args[1]);

							if (getter != null) {

								if (!tokens.playerExists(getter.getUniqueId().toString())) {
									tokens.createPlayer(getter.getUniqueId().toString());
									tokens.setTokens(getter.getUniqueId().toString(), Integer.parseInt(args[2]));

									player.sendMessage(new TextComponent(plugin.prefix + "§7Du hast dem Spieler"
											+ getter.getDisplayName() + "§6" + args[2] + "§7Tokens gesendet"));
									getter.sendMessage(
											new TextComponent(plugin.prefix + "§7Der Spieler " + player.getDisplayName()
													+ "§7hat dir §6" + args[2] + " §7Tokens gegeben"));
								} else {
									tokens.setTokens(getter.getUniqueId().toString(), Integer.parseInt(args[2]));

									player.sendMessage(new TextComponent(plugin.prefix + "§7Du hast dem Spieler"
											+ getter.getDisplayName() + "§6" + args[2] + "§7Tokens gesendet"));
									getter.sendMessage(
											new TextComponent(plugin.prefix + "§7Der Spieler " + player.getDisplayName()
													+ "§7hat dir §6" + args[2] + " §7Tokens gegeben"));
								}

							} else {
								player.sendMessage(
										new TextComponent(plugin.prefix + "§7Der angegebene Spieler ist nicht online"));
							}

						}
					}

				}

			} else {
				sender.sendMessage(new TextComponent(plugin.prefix + "Du musst ein Spieler sein"));
			}

		}
	}
}


