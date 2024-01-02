#!/usr/bin/env groovy
pipeline {
    agent any
    triggers {
        pollSCM('* * * * *') // Poll every minute
	#comment
    }

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
            steps {
                script {
                    customScript.buildDockerImage()
                }
            }
        }
                stage('Commit_version_changes') {
                    steps {
                        script {
                            customScript.commitVersion()
                        }
                    }
                }
    }
}
