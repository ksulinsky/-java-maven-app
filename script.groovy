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
        echo 'Committing version changes...'
        sh 'git add .'
        sh 'git commit -m "version update"'
        sh 'git push origin jenkins_commit'
    }
}

return this
