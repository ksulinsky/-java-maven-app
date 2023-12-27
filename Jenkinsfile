pipeline {
    agent any

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
                // This stage could include commands to run your tests
                echo 'testing...'
            }
        }

        stage('Deploy') {
            steps {
                // This stage could include commands to deploy your application
                sh 'deploying...'
            }
        }
    }

}
