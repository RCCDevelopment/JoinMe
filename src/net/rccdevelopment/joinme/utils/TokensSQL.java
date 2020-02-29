package net.rccdevelopment.joinme.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TokensSQL {
	private SQL sql = new SQL();

	public boolean playerExists(String uuid) {
		try {
			PreparedStatement ps = SQL.connection.prepareStatement("SELECT * FROM Tokens WHERE UUID = ?");
			ps.setString(1, uuid);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString("UUID") != null;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void createPlayer(String uuid) {
		if (!(playerExists(uuid))) {
			try {
				PreparedStatement ps = SQL.connection.prepareStatement("INSERT INTO Tokens(UUID,Tokens) VALUES (?,0)");
				ps.setString(1, uuid);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public int getTokens(String UUID) {

		try {

			PreparedStatement preparedStatement = sql.connection
					.prepareStatement("SELECT Tokens FROM Tokens WHERE UUID = ?");
			preparedStatement.setString(1, UUID);

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return resultSet.getInt("Tokens");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;

	}

	public void setTokens(String UUID, int tokens) {

		tokens += getTokens(UUID);

		try {

			PreparedStatement preparedStatement = sql.connection
					.prepareStatement("UPDATE Tokens SET Tokens = ? WHERE UUID = ?");
			preparedStatement.setInt(1, tokens);
			preparedStatement.setString(2, UUID);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
