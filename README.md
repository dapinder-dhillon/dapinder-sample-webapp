#Practice Application for trying out AWS

This is sample small application created using Spring MVC (and not Spring boot). Its having two `endpoints` i.e.:
1. `/home`
2. `/health` - health endpoint

The application should have the capacity to be automcatically depployable. It shall have good level of automation in place.

## AMI (Way to deploy artifact)
The project would be baking an AMI (Amazon Machine Image) with the help of [Packer](https://www.packer.io/intro/getting-started/build-image.html).
``packer_template.json`` is the file being used for baking an AMI.