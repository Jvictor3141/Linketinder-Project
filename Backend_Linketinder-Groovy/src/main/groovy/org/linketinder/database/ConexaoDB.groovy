package org.linketinder.database

import java.sql.Connection
import java.sql.DriverManager

class ConexaoDB {

    static Connection conectar() {
        Properties props = new Properties()
        InputStream is = ConexaoDB.class.getResourceAsStream("/db.properties")
        if (is == null) {
            throw new IllegalStateException("db.properties não encontrado em resource.")
        }
        is.withCloseable { props.load(it) }

        return DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password")
        )
    }
}
