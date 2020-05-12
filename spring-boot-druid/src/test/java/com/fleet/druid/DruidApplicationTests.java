package com.fleet.druid;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DruidApplicationTests {

	@Autowired
	DataSource dataSource;

	@Test
	public void contextLoads() throws SQLException {
		Connection connection = dataSource.getConnection();
		PreparedStatement prepareStatement = connection.prepareStatement("select * from user where user_id = 1");
		ResultSet resultSet = prepareStatement.executeQuery();
		while (resultSet.next()) {
			String userName = resultSet.getString("user_name");
			System.out.println(userName);
		}
	}

}
