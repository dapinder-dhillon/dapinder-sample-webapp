# Practice Application for trying out AWS

This is sample small application created using Spring MVC (and not Spring boot). Its having two `endpoints` i.e.:
1. `/home`
2. `/health` - health endpoint

The application should have the capacity to be automcatically depployable. It shall have good level of automation in place.

## AMI (Way to deploy artifact)
The project would be baking an AMI (Amazon Machine Image) with the help of [Packer](https://www.packer.io/intro/getting-started/build-image.html).
To understand an AMI, think of a snapshot of your entire system e.g. Think running an OS (Windows, Mac etc) and over the time have installed many apps or softwares. You can create an AMI of your 
entire system so that in case system crashes, you just boot-up new system using the image and with all the softwares installed. Your system can be on same state from the time you burned the image.

Amazon Machine Image is similar and one can create a Virtual Machine (EC2) using the AMI. There are many AMI already existing on AWS - some of them have been made public by Amazon itself and some by contributors.

Similar to OOP concept of `Inheritance`, AMIs can be extended too and referred as Source AMIs. e.g. in the below example of baking an AMI, we are extending `buntu-xenial-16.04-amd64-server` image baked by owner `099720109477 - Canonical Group Limited`

Below is the configuration file used to define what image we want built and how is called a template in Packer terminology. The format of a template is simple JSON
is the packer template JSON.

```{
     "builders": [{
       "type": "amazon-ebs",
       "region": "eu-west-1",
       "source_ami_filter": {
         "filters": {
           "virtualization-type": "hvm",
           "name": "ubuntu/images/*ubuntu-xenial-16.04-amd64-server-*",
           "root-device-type": "ebs"
         },
         "owners": ["099720109477"],
         "most_recent": true
       },
       "instance_type": "t2.micro",
       "ssh_username": "ubuntu",
       "ami_name": "packer-example-dapinder {{timestamp}}"
     }],
     "provisioners": [
       {
         "type": "file",
         "source": "target/dapinder-sample-webapp.war",
         "destination": "/tmp/dapinder-sample-webapp.war"
       },
       {
         "type": "shell",
         "inline": [
           "sleep 30",
           "sudo apt-get update",
           "sudo apt-get -y install openjdk-8-jdk",
           "sudo groupadd tomcat",
           "sudo useradd -s /bin/false -g tomcat -d /opt/tomcat tomcat",
           "cd /tmp",
           "sudo curl -O http://mirrors.ibiblio.org/apache/tomcat/tomcat-8/v8.5.46/bin/apache-tomcat-8.5.46.tar.gz",
           "sudo mkdir /opt/tomcat",
           "sudo tar xzvf apache-tomcat-8*tar.gz -C /opt/tomcat --strip-components=1",
           "cd /opt/tomcat",
           "sudo chgrp -R tomcat /opt/tomcat",
           "sudo chmod -R g+r conf",
           "sudo chmod g+x conf",
           "sudo chown -R tomcat webapps/ work/ temp/ logs/",
           "sudo cp /tmp/dapinder-sample-webapp.war webapps/"
         ]
       }
     ]
   }
```

1. Here I am trying to bake an AMI with Ubuntu 16.04 OS as a source AMI which is being filtered  on virtualization-type, route-device-type, owners etc.
2. Using an EC2 instance of t2.micro.
3. The real utility of Packer comes from being able to install and configure software into the images as well. This stage is also known as the provision step. Provisioners are configured as part of the template. 
4. We'll use the built-in shell provisioner that comes with Packer to install Java8, Tomcat8 and copy our artificat WAR to tomcat/webapps..
