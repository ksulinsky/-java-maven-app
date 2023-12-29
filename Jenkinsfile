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
                echo 'Testing...'
            }
        }

stage('Deploy') {
    steps {
        script {
            def userInput = input(
                id: 'deployInput',
                message: 'Select environment to deploy:',
                parameters: [choice(choices: ['dev', 'test', 'prod'], description: 'Select the environment', name: 'ENV')]
            )
            
            def selectedEnvironment = userInput.ENV
            echo "Deploying in environment ${selectedEnvironment}"

            if (selectedEnvironment == 'prod') {
                // Additional deployment steps for the production environment
                echo "Executing production deployment steps..."
            }

            withCredentials([usernamePassword(credentialsId: 'docker-credentials', usernameVariable: 'USERNAME', passwordVariable: 'PWD')]) {
                echo "Deploying with version ${VERSION}"
                // Example: sh "deploy-script.sh -v ${VERSION} -u ${USERNAME} -p ${PWD}"
            }
        }
    }
}
}
}
