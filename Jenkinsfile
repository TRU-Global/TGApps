pipeline {
    agent any 
    stages {
        stage('Check-out SCM'){
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/SFDC_18_Testcases']], extensions: [], userRemoteConfigs: [[credentialsId: '82da358f-b2a2-4dbb-a5aa-c1b4bebfa5a3', url: 'https://github.com/TRU-Global/TRUGlobalAutomation.git']]])
            }
        }
        stage('Build'){
            steps { 
            sh 'mvn -f */pom.xml clean install'
        }
        }
        stage('surefire report') {
            steps {
                 sh 'mvn surefire-report:report'
            }
            post {
                success {
                    archiveArtifacts artifacts: '**/target/*.jar'
                 archiveArtifacts artifacts: '**/target/site/*.html'

                }
            }
        }

        }
    }

