import java.io.File
import java.net.URI

class Input(private val name: String) {
    private fun uri(): URI = Input::class.java.classLoader.getResource(name)!!.toURI()
    fun readLines(): List<String> = File(uri()).readLines()
}