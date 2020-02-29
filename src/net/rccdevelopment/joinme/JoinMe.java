package net.rccdevelopment.joinme;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.rccdevelopment.joinme.commands.JoinMe_cmd;
import net.rccdevelopment.joinme.commands.Tokens_cmd;
import net.rccdevelopment.joinme.utils.SQL;

public class JoinMe extends Plugin{

	

	private SQL sql = new SQL();
	
	public String prefix = "§6JoinMe | ";
	
	public static HashMap<ProxiedPlayer,String> joinme = new HashMap<ProxiedPlayer, String>();

	@Override
	public void onEnable() {

		sql.connect();
		
		create();
		
		getProxy().getPluginManager().registerCommand(this, new JoinMe_cmd("joinme", this));
		getProxy().getPluginManager().registerCommand(this, new Tokens_cmd("tokens", this));
	}

	@Override
	public void onDisable() {
		sql.close();
	}

	private void create() {

		try {

			PreparedStatement preparedStatement = sql.connection
					.prepareStatement("CREATE TABLE IF NOT EXISTS Tokens(UUID VARCHAR(64),Token INTEGER(64))");
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}


