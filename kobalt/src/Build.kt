import com.beust.kobalt.*
import com.beust.kobalt.plugin.packaging.assemble
import com.beust.kobalt.plugin.kotlin.*

val repos = repos()

val p = kotlinProject {
    name = "kobalt-copy-dependencies"
    group = "no.tornado"
    artifactId = name
    version = "0.1"

    sourceDirectories {
        path("src/main/kotlin")
        path("src/main/resources")
    }

    dependencies {
        compile("com.beust:kobalt:0.335")
    }

    assemble {
        jar {
        }
    }
}
