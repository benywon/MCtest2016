package stats

import MCTest.process
import java.util.concurrent.TimeUnit

/**
 * Created by benywon on 12/24 0024.
 */
/**
 * We declare a package-level function main which returns Unit and takes
 * an Array of strings as a parameter. Note that semicolons are optional.
 */

fun main(args: Array<String>) {
    val t0 = System.nanoTime()
    val process = process(160, "train")
    val multiOrSingle = MultiOrSingle(process.MCstructure)
    val t1 = System.nanoTime()
    val millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0)
    println(multiOrSingle.Single)
    print(millis)
    print("ms")
    println("Hello, world!")
}

