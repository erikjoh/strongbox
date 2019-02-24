
pipeline {
    agent {
        node {
            label 'master'
            customWorkspace workspace().getUniqueWorkspacePath()
        }
    }
    options {
        timeout(time: 2, unit: 'HOURS')
        disableConcurrentBuilds()
    }
    stages {
        stage('Building')
        {
            steps {
                sh "mvn -U clean install -T2C -Dintegration.tests -Dprepare.revision -Dmaven.test.failure.ignore=true -Pcoverage"
            }
        }
    }
    post {
        always {
            // (fallback) record test results even if withMaven should have done that already.
            junit '**/target/*-reports/*.xml'
        }
        cleanup {
            script {
                workspace().clean()
            }
        }
    }
}
