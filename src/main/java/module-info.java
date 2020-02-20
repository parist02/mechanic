module com.mechanic.code {
	exports com.mechanic.code.databaseClasses;
    requires transitive javafx.graphics;
    requires transitive javafx.controls;
	requires mysql.connector.java;
	requires java.sql;
	exports com.mechanic.code;
}
