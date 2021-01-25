pipeline {
  agent {
    node {
      label 'master'
    }

  }
  stages {
    stage('Build') {
      steps {
        echo 'Build Durian'
        sh 'mvn clean install -DskipTest -Dgpg.skip'
      }
    }

  }
}