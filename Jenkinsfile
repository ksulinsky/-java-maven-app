pipeline {
    agent any
    tools {
        maven 'Maven'
    }

    stages {
        stage('Checkout') {
            steps {
                // This stage checks out the code from your version control system
                checkout scm
            }
        }

        stage('Build') {
            when {
                expression {
                    return env.BRANCH_NAME == 'master'
                }
            }

            steps {
                echo "Building..."
            }
        }

        stage('Test') {
            when {
                expression {
                    return env.BRANCH_NAME == 'master'
                }
            }

            steps {
                echo "Testing..."
            }
        }
    }
}
