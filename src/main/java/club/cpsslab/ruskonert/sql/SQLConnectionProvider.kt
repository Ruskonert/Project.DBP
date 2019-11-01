package club.cpsslab.ruskonert.sql

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.sql.*

open class SQLConnectionProvider(database_name: String, defaultConnect: Boolean = false)
{
    private val databaseName: String = database_name
    fun getDatabaseName() : String = this.databaseName
    init
    {
        if(defaultConnect) this.connect(SQL_DEFAULT_ADDRESS, SQL_DEFAULT_PORT.toString(), SQL_DEFAULT_ID, SQL_DEFAULT_PASSWORD)
    }

    companion object
    {
        const val JDBC_OLD_DRIVER : String = "com.mysql.jdbc.Driver"
        const val JDBC_DRIVER : String = "com.mysql.cj.jdbc.Driver"
        const val SQL_DEFAULT_ID : String = "root"
        const val SQL_DEFAULT_PASSWORD : String = "12345678"

        const val SQL_DEFAULT_ADDRESS : String = "localhost"
        const val SQL_DEFAULT_PORT : Int = 3306
    }

    private var connection : Connection? = null
    fun getConnection() : Connection? = this.connection

    private var statement : Statement? = null
    fun getStatement() : Statement? = this.statement

    private fun connect(address: String, port: String, db_user: String, db_password: String,
                                reference : SQLConnectionProvider = this) = runBlocking {
        val job = launch {
            try {
                Class.forName(JDBC_DRIVER)
                reference.connection = DriverManager.getConnection("jdbc:mysql://$address:$port/$databaseName?user=$db_user&password=$db_password")
                reference.statement = reference.connection!!.createStatement()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
    }

    fun execute(queryString : String) : ResultSet = this.statement!!.executeQuery(queryString)
    fun execute(queryInd : QueryIndicator) : ResultSet = this.statement!!.executeQuery(queryInd.getQueryString())

    protected fun disconnect() : Boolean {
        this.connection ?: return false
        this.statement  ?: return false
        this.connection!!.close()
        this.statement!!.close()
        return true
    }
}