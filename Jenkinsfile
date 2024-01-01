@Library('groovy_project:1.0') _
pipeline {
    agent any
    tools {
        maven 'Maven'
    }

    environment {
        DOCKER_CREDENTIALS = credentials('docker-credentials')
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
                    build_app()
                }
            }
        }

        stage('Build docker image') {
            steps {
                script {
                    buildDockerImage()
                }
            }
        }
    }
}
