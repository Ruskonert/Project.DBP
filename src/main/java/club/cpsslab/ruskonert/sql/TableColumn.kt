package club.cpsslab.ruskonert.sql

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
/**
 * TableColumn 클래스는 [AsyncTableElement]에 의해서 동기화 작업이 수행될 때, 동기화하고자 하는 필드를
 * 지정할 수 있습니다. [ref]은 필드의 이름이 클래스에서 정의한 필드와 이름이 다를때, 개별적으로 설정해줄 수
 * 있습니다.
 *
 * @param ref 참조하고자 하는 필드
 * @param sizeOf 참조하는 필드 사이즈
 * @author ruskonert
 */
annotation class TableColumn(val ref : String = "", val sizeOf : Int = -1)