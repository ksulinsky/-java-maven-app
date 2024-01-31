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
                    customScript.build_app()
                }
            }
        }

        stage('Build Docker Image') {
            environment {
                // customScript.buildDockerImage()
                AWS_ACCESS_KEY_ID = credentials('AWS_ACCESS_KEY_ID')
                AWS_SECRET_ACCESS_KEY = credentials('AWS_SECRET_ACCESS_KEY')
            }
            steps {
                script {
                    echo "Deploying kubernetes image..."
                    sh "kubectl create deployment nginx-deployment --image=nginx"
                }
            }
        }
    }
}
