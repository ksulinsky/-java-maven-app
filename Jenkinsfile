pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    environment {
        DOCKER_CREDENTIALS = credentials('docker-credentials')
        DOCKER_IMAGE_TAG = ''
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
    environment {
        AWS_ACCESS_KEY_ID     = credentials('AWS_ACCESS_KEY_ID')
        AWS_SECRET_ACCESS_KEY = credentials('AWS_SECRET_ACCESS_KEY')
        AWS_REGION            = 'eu-west-3'
        EKS_CLUSTER_NAME      = 'demo-cluster'
    }
    
    steps {
        script {
            // Configure AWS CLI
            sh "aws configure set aws_access_key_id ${AWS_ACCESS_KEY_ID}"
            sh "aws configure set aws_secret_access_key ${AWS_SECRET_ACCESS_KEY}"
            sh "aws configure set region ${AWS_REGION}"
            
            // Authenticate to EKS cluster
            sh "aws eks --region ${AWS_REGION} update-kubeconfig --name ${EKS_CLUSTER_NAME}"

            // Verify kubectl configuration
            sh "kubectl config get-contexts"
            
            // Deploy your application using kubectl
            sh "kubectl create deployment nginx-deployment --image=nginx"
            
            // Optionally, expose the deployment as a service
        }
    }
}
    }
}
