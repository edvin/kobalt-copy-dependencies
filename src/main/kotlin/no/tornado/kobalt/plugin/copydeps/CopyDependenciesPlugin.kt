package no.tornado.kobalt.plugin.copydeps

import com.beust.kobalt.TaskResult
import com.beust.kobalt.api.BasePlugin
import com.beust.kobalt.api.IClasspathDependency
import com.beust.kobalt.api.Project
import com.beust.kobalt.api.ProjectDescription
import com.beust.kobalt.api.annotation.Task
import com.beust.kobalt.internal.JvmCompilerPlugin
import com.beust.kobalt.maven.DependencyManager
import com.beust.kobalt.misc.KFiles
import com.beust.kobalt.misc.log
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CopyDependenciesPlugin @Inject constructor(val dependencyManager: DependencyManager) : BasePlugin() {
    override val name = "copy-dependencies"

    @Task(name = "copy-dependencies", alwaysRunAfter = arrayOf("assemble"), description = "Copy all dependencies to build folder")
    fun copyDependencies(project: Project): TaskResult {
        val deps = collectDeps(project)

        val libsDir = Paths.get(libsDir(project))

        for (dep in deps) {
            val source = Paths.get(dep)
            val target = libsDir.resolve(source.fileName)

            log(2, "Copying $source to $target")

            Files.copy(source, target)
        }

        return TaskResult()
    }

    private fun buildDir(project: Project) = KFiles.makeDir(project.directory, project.buildDirectory)
    private fun libsDir(project: Project) = KFiles.Companion.makeDir(buildDir(project).path, "libs").path

    fun collectDeps(project: Project): List<String> {
        val allDeps = arrayListOf<String>()

        val seen = hashSetOf<String>()
        @Suppress("UNCHECKED_CAST")
        val dependentProjects = project.projectProperties.get(JvmCompilerPlugin.DEPENDENT_PROJECTS)
                as List<ProjectDescription>
        listOf(dependencyManager.calculateDependencies(project, context, dependentProjects,
                project.compileDependencies),
                dependencyManager.calculateDependencies(project, context, dependentProjects,
                        project.compileRuntimeDependencies))
                .forEach { deps: List<IClasspathDependency> ->
                    deps.map {
                        it.jarFile.get()
                    }.forEach { file: File ->
                        if (!seen.contains(file.name)) {
                            seen.add(file.name)
                            allDeps.add(file.path)
                        }
                    }
                }
        return allDeps
    }

}