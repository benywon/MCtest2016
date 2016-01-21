package kotlin

import MCTest.process
import stats.MultiOrSingle
import java.util.concurrent.TimeUnit

/**
 * Created by benywon on 12/24 0024.
 */
fun main(args: Array<String>) {
    val t0 = System.nanoTime()
    val process = process(160, "train")
    val multiOrSingle = MultiOrSingle(process.MCstructure)
    val t1 = System.nanoTime()
    val millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0)
    println(multiOrSingle.Single)
    println("Hello, world!")
    val i = 3
    for (i in 1..10) {
        println(i) }
}
