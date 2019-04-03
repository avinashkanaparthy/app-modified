import jetbrains.buildServer.configs.kotlin.v2018_2.*
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.PowerShellStep
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.dotnetBuild
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.dotnetPublish
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.dotnetRestore
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.powerShell
import jetbrains.buildServer.configs.kotlin.v2018_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2018_2.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2018.2"

project {

    vcsRoot(HttpsGithubComJasonsturgesMysqlDotnetCoreGitRefsHeadsMaster)

    buildType(Build)

    params {
        param("env.Testparam", "DEV")
    }
}

object Build : BuildType({
    name = "Build"

    artifactRules = """MySqlDotnetCore\ToBeDeployed => MySqlDotnetCore\ToBeDeployed"""

    vcs {
        root(HttpsGithubComJasonsturgesMysqlDotnetCoreGitRefsHeadsMaster)
    }

    steps {
        dotnetRestore {
            projects = "MySqlDotnetCore.sln"
        }
        dotnetBuild {
            projects = "MySqlDotnetCore.sln"
        }
        dotnetPublish {
            projects = "MySqlDotnetCore/MySqlDotnetCore.csproj"
            outputDir = "ToBeDeployed"
            param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
        }
        powerShell {
            name = "powershell command step"
            scriptMode = script {
                content = "Write-Host %env.Testparam%"
            }
            scriptExecMode = PowerShellStep.ExecutionMode.STDIN
            param("org.jfrog.artifactory.selectedDeployableServer.publishBuildInfo", "true")
            param("org.jfrog.artifactory.selectedDeployableServer.downloadSpecSource", "Job configuration")
            param("org.jfrog.artifactory.selectedDeployableServer.useSpecs", "true")
            param("org.jfrog.artifactory.selectedDeployableServer.buildDependencies", "Requires Artifactory Pro.")
            param("org.jfrog.artifactory.selectedDeployableServer.uploadSpecSource", "Job configuration")
            param("org.jfrog.artifactory.selectedDeployableServer.envVarsExcludePatterns", "*password*,*secret*")
        }
    }

    triggers {
        vcs {
        }
    }
})

object HttpsGithubComJasonsturgesMysqlDotnetCoreGitRefsHeadsMaster : GitVcsRoot({
    name = "https://github.com/jasonsturges/mysql-dotnet-core.git#refs/heads/master"
    url = "https://github.com/jasonsturges/mysql-dotnet-core.git"
})
