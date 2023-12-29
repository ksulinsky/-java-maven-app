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
            steps {
                echo 'Building...'
                sh "mvn package"
            }
        }

        


        
    }

}
