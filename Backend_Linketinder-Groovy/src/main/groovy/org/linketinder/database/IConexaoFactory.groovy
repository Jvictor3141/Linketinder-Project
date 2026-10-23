package org.linketinder.database

import java.sql.Connection

interface IConexaoFactory {
    Connection criarConexao()
}
