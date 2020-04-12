module com.mechanic.code {
	exports com.mechanic.code.database;
	exports com.mechanic.code.print;
	exports com.mechanic.code.forms;
	exports com.mechanic.code.main;
	exports com.mechanic.code.temporary;
    requires transitive javafx.graphics;
    requires transitive javafx.controls;
    requires transitive javafx.base;
	requires mysql.connector.java;
	requires java.sql;
	exports com.mechanic.code;
}
