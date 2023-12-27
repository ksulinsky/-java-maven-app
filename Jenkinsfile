pipeline {
    agent any
    tools {
        maven 'Maven'

    }

parameters {
choice(name: 'VERSION: ', choices: ['1.1.0', '1.2.0', '1.3.0'], description: '')
booleanParam(name: 'ExecuteTests', defaultValue: true, description: '')
}
    environment {
        NEW_VERSION='1.3.0'
        SERVER_CREDENTIALS=credentials('docker-credentials')
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
            echo 'building...'
            }
        }

        stage('Test') {
            steps {
                when {
                    expression {
                        params.ExecuteTests == true
                    }
                }
                // This stage could include commands to run your tests
                echo 'testing...'
            }
        }

        stage('Deploy') {
            steps {
                // This stage could include commands to deploy your application
                echo 'deploying...'
                 withCredentials([usernamePassword(credentialsId: 'docker-credentials', usernameVariable: 'USERNAME', password: 'PWD')]) {
                echo "Deploying with version ${VERSION}"

            }
        }
    }
    post {
    always { 
    
    }
        success {
            
        }
        failure {
            
        }
    }
}
}
