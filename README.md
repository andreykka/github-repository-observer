# vcs-repository-observer
VCS Repository Observer is an application that at it's base version allows extracting information from VCS(currently GitHub only)  
 
### Documentation: 
* Swagger UI available at path: `/swagger-ui/index.html`
* Swagger specification available at path: `/v3/api-docs`

### Build project:
Run `mvn clean package` to compile and generate all classes and run tests.

### Run project locally:
1. Using generated JAR artifact:
    ```
    java -jar -Dspring.profiles.active=dev target/vcs-repository-observer-*.jar
    ```
2. Using Docker engine:
    ```shell
    # build a docker image
    docker build -t vcs-repository-observer .
    # run image
    docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=dev vcs-repository-observer:latest
    ```
3. Go to `http://localhost:8080/swagger-ui/#` to verify everything set up

### Useful links to be considered for automated deployment:
- **Health** check of the application(with underlying dependencies):`/actuator/health`
- **Liveness probe** - checks whether application up and running **Note:** dependencies(like DB) not considered here: `/actuator/health/liveness`
- **Readiness probe** - checks whether application completely started and ready to receive traffic: `/actuator/health/readiness`


## Deploy to AWS: 
> **Pre requisites:** There should be AWS account with a VPC and 2 availability zones available to run CloudFormation script

1. Go to `aws` folder at root level of a project and find CloudFormation stack template: `ecs-cluster-and-app.yaml`
2. Log in to aws console(or use `awscli`) to create stack. (See docs below)
3. After stack has been created read output property `SwaggerURL`. It will contain a Swagger URL to the application load balancer.

For more details about how to create CloudFormation stack refer to AWS guides:
[CloudFormation Deploy using console](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/cfn-console-create-stack.html)
[CloudFormation Deploy using cli](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-cli-creating-stack.html)