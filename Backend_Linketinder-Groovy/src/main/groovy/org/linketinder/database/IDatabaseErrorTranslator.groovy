package org.linketinder.database

import java.sql.SQLException

interface IDatabaseErrorTranslator {
    RuntimeException translate(SQLException exception, DatabaseOperation operation)
}
