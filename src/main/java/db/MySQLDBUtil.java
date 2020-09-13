package db;

public class MySQLDBUtil {
	private static final String INSTANCE = "laiproject1-instance.cy1ocyojnu8d.us-east-2.rds.amazonaws.com";
	private static final String PORT_NUM = "3306";
	public static final String DB_NAME = "laiproject1";
	private static final String USERNAME = "admin";
	private static final String PASSWORD = "zyy721108";
	public static final String URL = "jdbc:mysql://"
			+ INSTANCE + ":" + PORT_NUM + "/" + DB_NAME
			+ "?user=" + USERNAME + "&password=" + PASSWORD
			+ "&autoReconnect=true&serverTimezone=UTC";

}
