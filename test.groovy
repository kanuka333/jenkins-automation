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
                    echo 'Publishing SNS message to AWS'
            withAWS(credentials:'AWSCredentialsForEC2') {
                snsPublish(
                    topicArn:'arn:aws:sns:us-west-2:yourAccountId:aws_resource_status_toggle', 
                    subject:"${params.ACTION} ${params.AWS_ENVIRONMENT}", 
                    message: getMessage(mapEnvironmentToAWS[params.AWS_ENVIRONMENT], params.ACTION )
                    )
            }  
         }
      }
   }
}