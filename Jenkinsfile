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
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo 'Building...'
                sh "mvn package"
            }
        }

        stage('Build and Push Docker Image') {
            steps {
                script {
                    // Bind the Docker credentials
                    withCredentials([usernamePassword(credentialsId: 'docker-credentials', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                        echo 'Building docker image...'
                        sh "docker build -t maven-java-app:1.0 ."
                        
                        echo 'Logging in to Docker Hub...'
                        sh "docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD"
                        
                        echo 'Pushing the Docker image to the private repository...'
                        sh 'docker push ksulinsky/repository:jma-1.0'
                    }
                }
            }
        }
    }
}
