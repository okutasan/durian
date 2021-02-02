pipeline {
  agent {
    docker {
      image 'maven:3-alpine'
      args '-u root'
    }

  }
  stages {
    stage('Build') {
      steps {
        sh 'ls -a'
        sh 'ls /root/.m2'
        echo 'Build Durian'
        sh 'mvn clean install -DskipTest -Dgpg.skip'
      }
    }

  }
  post {
        always {
            archiveArtifacts artifacts: '/root/.m2/repository/co/mailtarget/durian/**/*.jar', fingerprint: true
            archiveArtifacts artifacts: '/root/.m2/repository/co/mailtarget/durian/**/*.pom', fingerprint: true
        }
    }
}