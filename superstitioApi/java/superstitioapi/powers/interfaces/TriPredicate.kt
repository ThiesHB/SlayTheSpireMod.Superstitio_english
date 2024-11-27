package superstitioapi.powers.interfaces

fun interface TriPredicate<T, T1, T2>
{
    fun test(t: T, t1: T1, t2: T2): Boolean
}
