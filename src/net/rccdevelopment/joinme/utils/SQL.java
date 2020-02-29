package net.rccdevelopment.joinme.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQL {
	public static Connection connection;

	public void close() {
		try {
			if (SQL.connection != null) {
				SQL.connection.close();
			}
		} catch (Exception ex) {
			System.err.println(ex);
		}
	}

	public void connect() {
		try {
			SQL.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/user?autoReconnect=true", "db",
					"pw");
			System.out.println("[Coins] MySQL-Verbindung hergestellt!");
		} catch (SQLException e) {
			System.out.println("[Coins] MySQL-Verbindung fehlgeschlagen!");
			e.printStackTrace();
		}
	}

	public void Update(final String qry) {
		try {
			final Statement stmt = SQL.connection.createStatement();
			stmt.executeUpdate(qry);
			stmt.close();
		} catch (Exception ex) {
			connect();
			System.err.println(ex);
		}
	}

	public ResultSet Query(final String qry) {
		ResultSet rs = null;
		try {
			final Statement stmt = SQL.connection.createStatement();
			rs = stmt.executeQuery(qry);
		} catch (Exception ex) {
			connect();
			System.err.println(ex);
		}
		return rs;
	}
}
