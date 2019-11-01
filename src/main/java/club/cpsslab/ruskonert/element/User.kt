package club.cpsslab.ruskonert.element

import club.cpsslab.ruskonert.sql.AsyncTableElement
import club.cpsslab.ruskonert.sql.SQLConnectionProvider
import club.cpsslab.ruskonert.sql.TableColumn
import java.sql.SQLException

@Suppress("LiftReturnOrAssignment")
open class User : AsyncTableElement
{
    companion object {
        private val defaultSQLProvider = SQLConnectionProvider("dbp", defaultConnect = true)

        @JvmStatic
        fun getUser(username : String) : User?
        {
            val user : User
            try {
                user = User(username)
                return user
            } catch(e : SQLException) {
                return null
            }
        }
    }

    private constructor(reference_name: String) : super("user","username", reference_name, User.defaultSQLProvider)
    @TableColumn
    private var uniqueId : Int = -1
    fun isAssigned() : Boolean = this.uniqueId != -1

    @TableColumn
    private lateinit var username : String
    fun getUsername() : String = this.username

    @TableColumn(ref = "password")
    private var hashPassword : String? = null
    fun isNeedSetPassword() : Boolean = this.hashPassword == null || this.hashPassword == ""
    fun getHashPassword() : String = this.hashPassword!!

    init {
        if(this.sqlConnectionProvider.getConnection() == null || this.sqlConnectionProvider.getStatement() == null) {
            throw SQLException("Unfortunately not connected with the SQL database")
        }
    }
}