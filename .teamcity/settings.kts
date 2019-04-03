package AppTest.buildTypes

import jetbrains.buildServer.configs.kotlin.v2018_2.*
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.dotnetBuild
import jetbrains.buildServer.configs.kotlin.v2018_2.triggers.vcs

version = "2018.2"


project {

    val sequence = sequence {
        build(AppTest_Build) {
            produces("application.dll")
        }
    }
    println(sequence)
}

object AppTest_Build : BuildType({
    id("Build")
    name = "Build"

    vcs {
        root(random)
    }
    steps {
        dotnetBuild {
            name = "Build .net step"
            projects = "MySqlDotnetCore/MySqlDotnetCore.csproj"
            param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
        }
    }
    triggers {
        vcs {
        }
    }
})
