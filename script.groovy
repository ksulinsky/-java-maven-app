def build_app() {
    echo 'Building...'
    sh "mvn package"
}

def buildDockerImage() {
    // Bind the Docker credentials
    withCredentials([usernamePassword(credentialsId: 'docker-credentials', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
        echo 'Building docker image and pushing it...'
        sh "docker build -t maven-java-app:1.0 ."
        sh "docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD"
        sh 'docker push ksulinsky/repository:jma-1.0'
    }
}

def incrementVersion() {

        // Increment the major version using the versions-maven-plugin and build-helper-maven-plugin
        sh '''
            mvn build-helper:parse-version versions:set \
                -DnewVersion=${parsedVersion.majorVersion + 1}.${parsedVersion.minorVersion}.${parsedVersion.incrementalVersion} \
                versions:commit
        '''
}

return this
