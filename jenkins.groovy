import groovy.json.JsonOutput

def mapEnvironmentToAWS = [
    "InstancesId's": [
        ec2InstanceIds: ['i-someEc2Id-1','i-someEc2Id-2']
        ]    
    "Jira": [
        ec2InstanceIds: ['i-someEc2Id-1','i-someEc2Id-2'], 
        ],
    "BitBucket": [
        ec2InstanceIds: ['i-someEc2Id-1','i-someEc2Id-2']
        ]
    "Keyclock": [
        ec2InstanceIds: ['i-someEc2Id-1','i-someEc2Id-2']
        ]
    "Confluence": [
        ec2InstanceIds: ['i-someEc2Id-1','i-someEc2Id-2']
        ]
    "Jenkins": [
        ec2InstanceIds: ['i-someEc2Id-1']
        ]
    "Nfs": [
        ec2InstanceIds: ['i-someEc2Id-1']
        ]
    "Sonarqube": [
        ec2InstanceIds: ['i-someEc2Id-1']
        ]
    ],

def getMessage(data, action){
    def message = data
    message.put('action',action)
    return JsonOutput.toJson(message)
}

pipeline {
   agent any        
    parameters {
        string(
            name: 'AWS_ENVIRONMENT', 
            defaultvalue: ['InstancesId', 'Jira','BitBucket','Jenkins','Keyclock','Confluence','Sonarqube','Nfs'], 
            description: 'AWS target environment'
            )
        
        choice(
            name: 'ACTION', 
            choices: ['Start', 'Stop'], 
            description: 'Action to perform'
            )
    }
   stages {

      stage('Start_Stop_Instances') {
         steps {
            sh '''#!/bin/sh
            if [ "${params.ACTION}" = "Start" ]
            then
                aws ec2 start-instances --instance-ids ${params.AWS_ENVIRONMENT}
                echo Instance $InstanceID Started
                echo ""
            elif [ "${params.ACTION}" = "Stop" ]
            then
                aws ec2 stop-instances --instance-ids ${params.AWS_ENVIRONMENT}
                echo Instance $InstanceID Stopped
                echo ""
            fi  '''     
         }
      }
   }
}