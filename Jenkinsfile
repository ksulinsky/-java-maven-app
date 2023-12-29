pipeline {
    agent any
    tools {
        maven 'Maven'
    }

    parameters {
        choice(name: 'VERSION', choices: ['1.1.0', '1.2.0', '1.3.0'], description: 'Select the version')
        booleanParam(name: 'ExecuteTests', defaultValue: true, description: 'Run tests')
    }

    environment {
        NEW_VERSION = '1.3.0'
        SERVER_CREDENTIALS = credentials('docker-credentials')
    }

    stages {
        stage('Checkout') {
            steps {
                // This stage checks out the code from your version control system
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo 'Building...'
            }
        }

        stage('Test') {
            when {
                expression {
                    params.ExecuteTests == true
                }
            }
            steps {
                // This stage could include commands to run your tests
                echo 'Testing...'
            }
        }

        stage('Deploy') {
            input {
                message "Select environment to deploy:"
                ok "Done"
                choice(name: 'ENV', choices: ['dev', 'test', 'prod'], description: 'Select the environment')
            }

            steps {
                script {
                    // This stage could include commands to deploy your application
                    echo "Deploying in environment ${ENV}"

                    withCredentials([usernamePassword(credentialsId: 'docker-credentials', usernameVariable: 'USERNAME', passwordVariable: 'PWD')]) {
                        echo "Deploying with version ${VERSION}"
                        // Example: sh "deploy-script.sh -v ${VERSION} -u ${USERNAME} -p ${PWD}"
                    }
                }
            }
        }
    }
}
