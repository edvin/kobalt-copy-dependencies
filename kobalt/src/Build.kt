import com.beust.kobalt.*
import com.beust.kobalt.api.License
import com.beust.kobalt.api.Scm
import com.beust.kobalt.plugin.packaging.assemble
import com.beust.kobalt.plugin.kotlin.*
import com.beust.kobalt.plugin.publish.jcenter

val repos = repos()

val p = kotlinProject {
    name = "kobalt-copy-dependencies"
    group = "no.tornado"
    artifactId = name
    version = "0.2"
    description = "Copy the dependencies of your project to a directory"
    url = "https:/github.com/edvin/kobalt-copy-dependencies"
    licenses = listOf(License("Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0"))
    scm = Scm(url = "http://github.com/edvin/kobalt-copy-dependencies",
            connection = "https://github.com/edvin/kobalt-copy-dependencies.git",
            developerConnection = "git@github.com:edvin/kobalt-copy-dependencies.git")

    sourceDirectories {
        path("src/main/kotlin")
        path("src/main/resources")
    }

    dependencies {
        compile("com.beust:kobalt:0.329")
    }

    assemble {
        mavenJars {
        }
    }

    jcenter {
        publish = true
    }
}
