package club.cpsslab.ruskonert.sql

import java.sql.ResultSet

/**
 * AsyncTableElement 클래스는 하위 클래스에서 상속시켜 사용하며, 각각의 필드 값들을 테이블의 값과 동기화합니다.
 * 필드 값을 동기화하고 싶다면, 필드에 [TableColumn]을 어노테이트(Annotate)하십시오.
 * 자세한 소스 코드 정보는 [TableColumn] 클래스 정보를 참고하세요.
 *
 * @author ruskonert
 * @param tableName 지속적인 동기 작업을 해줄 테이블
 * @param referenceColumn 테이블을 탐색하여 값을 얻어와 동기화 작업을 할 때, 탐색에 참조하고자 하는 값의 컬럼
 * @param value 탐색을 위해 참조하고 있는 컬럼의 값으로, 해당 값이 일치하는 요소에 대해서 해당 클래스와 동기화함
 * @param provider 동기화 작업을 해줄 수 있는 SQL 프로바인더
 */
abstract class AsyncTableElement(private val tableName: String, referenceColumn: String, value: Any, provider: SQLConnectionProvider? = null) : SQLController()
{
    /**
     * 지속적인 동기 작업을 해줄 테이블 이름을 가져옵니다.
     * @return 테이블 이름을 가져옵니다.
     */
    fun getTableName() : String = this.tableName

    /**
     * 동기화 작업을 수행하는 SQL 서비스 프로바인더의 값입니다.
     */
    protected val sqlConnectionProvider : SQLConnectionProvider = provider ?: SQLConnectionProvider("dbp", defaultConnect = true)

    /**
     * 해당 테이블과 연결되어 있는 SQL 서비스 프로바인더를 가져옵니다.
     * @return SQL 서비스 프로바인더를 가져옵니다.
     */
    fun getConnectionProvider() : SQLConnectionProvider = this.sqlConnectionProvider

    /**
     * 테이블에서 값을 얻어와 클래스 필드에 값을 반영합니다. 해당 메소드는 참조 값이 [String]일 때, 사용하는 메소드입니다.
     *
     * @param reference_column 테이블을 탐색하여 값을 얻어와 동기화 작업을 할 때, 탐색에 참조하고자 하는 값의 컬럼
     * @param value 탐색을 위해 참조하고 있는 컬럼의 값
     * @return 동기화 작업 이후 결과 코드를 반환합니다.
     */
    private fun reflectTableField(reference_column : String, value : String) : TableElementResult {

        val result = this.sqlConnectionProvider.execute("SELECT * FROM $tableName WHERE $reference_column=\"$value\"")
        return this.reflectTableField0(result)
    }

    /**
     * 테이블에서 값을 얻어와 클래스 필드에 값을 반영합니다. 해당 메소드는 참조 값이 [Int]일 때, 사용하는 메소드입니다.
     *
     * @param reference_column 테이블을 탐색하여 값을 얻어와 동기화 작업을 할 때, 탐색에 참조하고자 하는 값의 컬럼
     * @param value 탐색을 위해 참조하고 있는 컬럼의 값
     * @return 동기화 작업 이후 결과 코드를 반환합니다.
     */
    private fun reflectTableField(reference_column : String, value : Int) : TableElementResult {

        val result = this.sqlConnectionProvider.execute("SELECT * FROM $tableName WHERE $reference_column=$value")
        return this.reflectTableField0(result)
    }

    /**
     * 테이블에서 값을 얻어와 클래스 필드에 값을 반영합니다. 해당 함수는 내부 함수로 [reflectTableField] 함수의 결과 값을 통해서
     * 클래스 필드의 값들을 동기화합니다.
     *
     * @param result 동기화에 참조하고자 하는 SQL 요소
     * @return 동기화 작업 이후 결과 코드를 반환합니다.
     */
    private fun reflectTableField0(result : ResultSet) : TableElementResult {
        if(this.getRowCount(result) > 1) return TableElementResult.DUPLICATE_ELEMENT_QUERY
        if(this.getRowCount(result) < 1) return TableElementResult.NO_EXIST_ELEMENT_QUERY

        val fields = this::class.java.declaredFields
        for(field in fields) {
            val typeClazz = field.type
            var ref : String = ""
            if(field.annotations.isEmpty()) continue
            else {
                var isAnnotated = false
                for(ann in field.annotations) {
                    if (ann is TableColumn) {
                        ref = ann.ref
                        isAnnotated = true
                        break
                    }
                }
                if(!isAnnotated) continue
            }

            if(ref == "") ref = field.name

            field.isAccessible = true
            if(typeClazz.isAssignableFrom(Int::class.java)) {
                field.set(this, result.getInt(ref))
            }
            else if (typeClazz.isAssignableFrom(String::class.java)) {
                field.set(this, result.getString(ref))
            }
        }

        return TableElementResult.SUCCESSFUL_QUERY
    }

    /**
     * 동기화 작업에 대한 결과 코드를 담고 있는 클래스읍니다.
     */
    enum class TableElementResult {
        /**
         * 동기화하고자 하는 값의 참조 요소가 2개일 때 반환합니다.
         */
        DUPLICATE_ELEMENT_QUERY,

        /**
         * 동기화하고자 하는 값의 요소가 1개도 없을 때 반환합니다.
         */
        NO_EXIST_ELEMENT_QUERY,

        /**
         * 동기화 작업에 성공하면 반환합니다.
         */
        SUCCESSFUL_QUERY
    }

    init {
        when(value) {
            is String -> this.reflectTableField(referenceColumn, value)
            is Int -> reflectTableField(referenceColumn, value)
        }
    }

    fun getRowCount(resultSet : ResultSet) : Int {
        resultSet.last()
        val count = resultSet.row
        resultSet.first()
        return count
    }
}
