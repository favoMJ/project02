package com.JDBC;

import java.sql.Connection;

import java.sql.DriverManager;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.sql.SQLException;

public class PositionSave {

	String driver = "com.mysql.jdbc.Driver";

	String dbName = "test";

	String passwrod = "654321";

	String userName = "root";

	String url = "jdbc:mysql://localhost:3306/" + dbName;

	String searchSql = "select position from book where bookName=?";

	String saveSql = "insert into book values(?,?)";

	String updateSql = "update book set position=? where bookName=?";

	Connection conn;

	PreparedStatement savePre;

	PreparedStatement searchPre;

	PreparedStatement updatePre;

	public PositionSave() {

		try {

			Class.forName(driver);

			conn = DriverManager.getConnection(url, userName, passwrod);

			savePre = conn.prepareStatement(saveSql);

			searchPre = conn.prepareStatement(searchSql);

			updatePre = conn.prepareStatement(updateSql);

		} catch (Exception e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}

	}

	public void save(String name, int position) {

		try {

			if (search(name) == 0) {

				savePre.setString(1, name);

				savePre.setInt(2, position);

				savePre.execute();

			}

			else {

				updatePre.setInt(1, position);

				updatePre.setString(2, name);

				//System.out.println(updatePre);

				updatePre.execute();

			}

		} catch (SQLException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}

	}

	public int search(String name) {

		try {

			int ps = 0;

			searchPre.setString(1, name);

			ResultSet rs = searchPre.executeQuery();

			while (rs.next()) {

				ps = rs.getInt(1);

			}

			return ps;

		} catch (SQLException e) {

			// TODO Auto-generated catch block

			return -1;

		}

	}

	public static void main(String[] args) {

		new PositionSave().save("教练万岁.txt", 9958);

	}

}