pipeline {
  agent {
    docker {
      image 'maven:3-alpine'
      args '-v /root/.m2:/root/.m2'
    }

  }
  stages {
    stage('Build') {
      steps {
        sh 'ls -a'
        echo 'Build Durian'
        sh 'mvn clean install -DskipTest -Dgpg.skip'
        archiveArtifacts '/root/.m2'
      }
    }

  }
}