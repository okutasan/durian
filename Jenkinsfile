pipeline {
  agent {
    docker {
      image 'maven:3-alpine'
      args '-v "$HOME"/.m2:/root/.m2'
    }

  }
  stages {
    stage('Build') {
      steps {
        sh 'ls -a'
        echo 'Build Durian'
        sh 'mvn clean install -DskipTest -Dgpg.skip'
        sh 'ls /var/jenkins_home/workspace/durian_ci-jenkins/?/.m2'
      }
    }

  }
}