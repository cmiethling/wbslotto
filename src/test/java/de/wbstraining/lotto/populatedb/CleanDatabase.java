package de.wbstraining.lotto.populatedb;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.sql.DataSource;

/**
 *
 * @author gz1
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class CleanDatabase implements CleanDatabaseLocal {

	@Resource(lookup = "java:jboss/datasources/lotto_jndi")
	private DataSource ds;
	
	
	@Override
	public void cleanDatabase(String schema) {
		ResultSet rs;
		String tableName;
		Statement statementFKCheckOff;
		Statement statementFKCheckOn;
		Statement statement;
		String sql;
		try (Connection connection = ds.getConnection()) {
			statementFKCheckOff = connection.createStatement();
			statementFKCheckOff.execute("SET FOREIGN_KEY_CHECKS = 0;");
			DatabaseMetaData mt = connection.getMetaData();
			rs = mt.getTables(schema, null, null, null);
			while (rs.next()) {
				tableName = rs.getString("TABLE_NAME");
				if (!rs.getString("TABLE_TYPE").equals("VIEW")) {
					statement = connection.createStatement();
					sql = "TRUNCATE " + schema + "." + tableName;
					try {
						statement.execute(sql);
					} catch (SQLException e) {
						throw new RuntimeException(e);
					}
				}
			}
			statementFKCheckOn = connection.createStatement();
			statementFKCheckOn.execute("SET FOREIGN_KEY_CHECKS = 1;");

		} catch (SQLException sqle) {
			throw new RuntimeException(sqle);
		}
	}
}
