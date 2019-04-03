import jetbrains.buildServer.configs.kotlin.v2018_1.*
import jetbrains.buildServer.configs.kotlin.v2018_1.buildSteps.powerShell
import jetbrains.buildServer.configs.kotlin.v2018_1.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2018_1.vcs.GitVcsRoot

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

    vcsRoot(GitGitlabComMeghana9876swatGit)

    buildType(Build)
}

object Build : BuildType({
    name = "Build"

    vcs {
        root(GitGitlabComMeghana9876swatGit)
    }

    steps {
        powerShell {
            name = "Testing gitlab webhooks"
            scriptMode = script {
                content = """Write-Host "This is a test and its Success.""""
            }
            param("org.jfrog.artifactory.selectedDeployableServer.downloadSpecSource", "Job configuration")
            param("org.jfrog.artifactory.selectedDeployableServer.useSpecs", "false")
            param("org.jfrog.artifactory.selectedDeployableServer.uploadSpecSource", "Job configuration")
        }
    }

    triggers {
        vcs {
            branchFilter = ""
            perCheckinTriggering = true
            enableQueueOptimization = false
        }
    }
})

object GitGitlabComMeghana9876swatGit : GitVcsRoot({
    name = "git@gitlab.com:meghana9876/swat.git"
    url = "git@gitlab.com:meghana9876/swat.git"
    branch = "refs/heads/Testbranch"
    authMethod = customPrivateKey {
        customKeyPath = """C:\Users\Yerriswamy.Konanki\avinash\id_key"""
    }
})
