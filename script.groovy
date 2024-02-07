def build_app() {
    echo 'Building...'
    sh "mvn package"
}

def buildDockerImage() {
    // Bind the Docker credentials
    def mavenVersion = sh(script: 'mvn help:evaluate -Dexpression=project.version -q -DforceStdout', returnStdout: true).trim()
    dockerImageTag = "ksulinsky/repository:jma-${mavenVersion}"
    echo "Building docker image with tag: ${dockerImageTag}"

    withCredentials([usernamePassword(credentialsId: 'docker-credentials', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
        echo 'Building docker image and pushing it...'
        sh "docker build -t ${dockerImageTag} ."
        sh "docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD"
        sh "docker push ${dockerImageTag} "
    }
}



return this
