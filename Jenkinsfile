pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    environment {
        DOCKER_CREDENTIALS = credentials('docker-credentials')
        DOCKER_IMAGE_TAG = ''
        AWS_ACCESS_KEY_ID = credentials('AWS_ACCESS_KEY_ID')
        AWS_SECRET_ACCESS_KEY = credentials('AWS_SECRET_ACCESS_KEY')
        AWS_REGION = 'eu-west-3'
        EKS_CLUSTER_NAME = 'demo-cluster'
        EC2_IP = ''
        EC2_SSH_KEY = credentials('server-ssh-key') 
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

        stage('Deploy to EKS') {
            steps {
                script {
                    // Configure AWS CLI
                    sh "aws configure set aws_access_key_id ${AWS_ACCESS_KEY_ID}"
                    sh "aws configure set aws_secret_access_key ${AWS_SECRET_ACCESS_KEY}"
                    sh "aws configure set region ${AWS_REGION}" 

                    // Configure terraform
                    dir('terraform') {
                        // Execute your script
                        sh 'terraform init'
                        sh 'terraform apply --auto-approve'
                        EC2_IP = sh(script: 'terraform output server_ip', returnStdout: true).trim()

                        // SSH into EC2 instance
                        sshCommand remote: [
                            user: 'ec2-user',
                            host: EC2_IP,
                            keyFile: EC2_SSH_KEY
                        ], command: 'ls -la'
                    }
                }
            }
        }
    }
}
