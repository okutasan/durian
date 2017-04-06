package co.mailtarget.durian.formatter

import co.mailtarget.durian.DurianException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

/**
 *
 * @author masasdani
 * @since 4/6/17
 */
object TemplateLoader {

    /**
     * @param inputStream
     * @return
     */
    fun read(inputStream: InputStream): String {
        var br: BufferedReader? = null
        try {
            br = BufferedReader(InputStreamReader(inputStream))
            val sb = StringBuilder()
            var line: String? = br.readLine()

            while (line != null) {
                sb.append(line)
                sb.append('\n')
                line = br.readLine()
            }
            return sb.toString()
        } catch (e: IOException) {
            throw DurianException()
        } finally {
            if (br != null) {
                try {
                    br.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

}
