pipelineJob('mars-exploration-rovers-seed') {
    definition {
        cpsScm {
            scm {
                github('jfelipeoliveira90/mars-exploration-rovers.git', ${BRANCH})
            }
            scriptPath('Jenkinsfile')
        }
    }

    wrappers {
        parameters {
            stringParam('BRANCH', 'master')
        }
    }
}