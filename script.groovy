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
        echo "SSH Agent is ready"
        sh 'ssh-add -L'

        def ec2Host = '3.70.229.201'
        def ec2User = 'ec2-user'

        // Execute 'echo' command on EC2 instance
        def echoCommand = "echo 'Executing ls -la'"
        def echoResult = sshReturnStatus(executable: 'ssh', host: ec2Host, user: ec2User, command: echoCommand)

        if (echoResult == 0) {
            echo "Command 'echo' executed successfully on EC2 instance"
        } else {
            error "Error executing 'echo' command on EC2 instance. Exit code: ${echoResult}"
        }

        // Execute 'ls -la' command on EC2 instance
        def lsCommand = 'ls -la'
        def lsResult = sshReturnStatus(executable: 'ssh', host: ec2Host, user: ec2User, command: lsCommand)

        if (lsResult == 0) {
            echo "Command 'ls -la' executed successfully on EC2 instance"
        } else {
            error "Error executing 'ls -la' command on EC2 instance. Exit code: ${lsResult}"
        }
    }
}

// Function to execute SSH command and return the exit status
def sshReturnStatus(Map options) {
    return sh(script: """
        ${options['executable']} \
        -o StrictHostKeyChecking=no \
        -i ${options['keyfile']} \
        ${options['user']}@${options['host']} \
        ${options['command']}
    """, returnStatus: true)
}




return this
