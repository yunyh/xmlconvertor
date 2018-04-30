package operator

abstract class MyOperator<T : Any> {
    abstract fun initialize(doing: T)
}