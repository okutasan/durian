pipeline {
  agent {
    docker {
      image 'maven:3-alpine'
      args ' -v $HOME/.m2/:$HOME/.m2'
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
  post {
        always {
            archiveArtifacts artifacts: '.m2/repository/co/mailtarget/durian/**/*.jar', fingerprint: true
            archiveArtifacts artifacts: '.m2/repository/co/mailtarget/durian/**/*.pom', fingerprint: true
        }
    }
}