pipeline {
  agent {
    docker {
      image 'maven:3-alpine'
      args '-v $HOME/tools:$HOME/tools \
            -v $HOME/.m2/:$HOME/.m2 \
            -v /etc/passwd:/etc/passwd:ro \
            -e MAVEN_CONFIG=$HOME/.m2 \
           '
    }

  }
  stages {
    stage('Build') {
      steps {
        sh 'ls -a'
        echo 'Build Durian'
        sh 'mvn clean install -DskipTest -Dgpg.skip'
      }
    }

  }
  post {
        always {
            archiveArtifacts artifacts: '$HOME/.m2/', fingerprint: true
        }
    }
}