pipeline {
    agent any
    tools {
        maven 'Maven'
    }

    environment {
        DOCKER_CREDENTIALS = credentials('docker-credentials')
        DOCKER_USERNAME = DOCKER_CREDENTIALS_USR
        DOCKER_PASSWORD = DOCKER_CREDENTIALS_PSW
    }

    stages {
        stage('Checkout') {
            steps {
                // This stage checks out the code from your version control system
                checkout scm
            }
        }

        stage('Initialize') {
            steps {
                script {
                    // Load the script.groovy file globally
                    customScript = load 'script.groovy'
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    customScript.build_app()
                }
            }
        }

        stage('Build docker image') {
            steps {
                script {
                    customScript.buildDockerImage()
                }
            }
        }
    }
}
