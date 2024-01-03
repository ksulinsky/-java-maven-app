#!/usr/bin/env groovy

import javax.print.attribute.standard.JobOriginatingUserName

def build_app() {
    echo 'Building...'
    sh "mvn package"
}

def buildDockerImage() {
    // Bind the Docker credentials
    def mavenVersion = sh(script: 'mvn help:evaluate -Dexpression=project.version -q -DforceStdout', returnStdout: true).trim()
    def dockerImageTag = "ksulinsky/repository:jma-${mavenVersion}"
    echo "Building docker image with tag: ${dockerImageTag}"
    env.dockerImageTag = dockerImageTag

    withCredentials([usernamePassword(credentialsId: 'docker-credentials', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
        echo 'Building docker image and pushing it...'
        sh "docker build -t ${dockerImageTag} ."
        sh "docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD"
        sh "docker push ${dockerImageTag}"
    }
}

def commitVersion() {
    withCredentials([usernamePassword(credentialsId: '5b637be4-a5d9-4402-abfc-cf9d8d6b41c3', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
        echo "Setting Git remote URL..."
        sh "git remote set-url origin https://${USERNAME}:${PASSWORD}@github.com/ksulinsky/-java-maven-app.git"
        sh "git checkout jenkins_commit"
        //login as jenkins user
        sh 'git config --global user.email "jenkins@example.com"'
        sh 'git config --global user.name "jenkins"'

        // Pull changes and handle conflicts
        try {
            sh 'git pull origin jenkins_commit -X theirs'
        } catch (Exception ex) {
            echo "Failed to pull changes: ${ex.message}"
            // Handle the failure (e.g., exit the script or take appropriate actions)
        }

        sh 'git status'

        // Check if there are changes in the working directory
        def hasChanges = sh(script: 'git diff --exit-code', returnStatus: true) == 1

        if (hasChanges) {
            sh 'git add .'
            sh 'git status' // Check again after adding changes

            // Commit with author information
            sh 'git commit -m "version update" --author="jenkins <jenkins@example.com>"'

            // Push to the branch
            sh 'git push origin jenkins_commit'
        } else {
            echo 'No changes to commit.'
        }
    }
}

def deployApplication() {
    sshagent(['aws']) {
        // Convert env.dockerImageTag to lowercase
        def lowercaseImageTag = env.dockerImageTag.toLowerCase()
        ssh user: 'ec2-user', host: '3.70.229.201', command: 'ls -la'

         //ssh command with lowercase env.dockerImageTag
        def remoteCommand = "docker run -p 8080:8080 -d ${lowercaseImageTag}"
        ssh user: 'ec2-user', host: '3.70.229.201', command: remoteCommand
    }
}


return this
