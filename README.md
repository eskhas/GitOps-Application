[infra-repo]: https://github.com/eskhas/infra-repo


# GitOps Application

#### I followed this tutorial to complete my application:
* https://github.com/marcel-dempers/docker-development-youtube-series/tree/master/kubernetes/fluxcd

My application uses 2 repositories at the same time, 
 * One is for the source code of a web app which is built with spring boot,
 * The other one is for the yaml files which are Infrastructure as Code (IaC). You can see the other repository from [here][infra-repo] 

### The system architecture looks like this
```
                                                        
                                                                            
        developer    +-----------+     +----------+  docker  +-------------+   
                     |    REPO   |     | CI       |   push   |IMAGE        |   
         ----------> |   (code)  |---> | PIPELINE | -------> |REGISTRY     |   
                     +-----------+     +----------+          +-------------+   
                                                                  ^                
                                                                  |sync            
                                                                  |                
                                       +----------+  commit   +----------+      
                                       |INFRA     | <-------- |  INFRA   |      
                                       |REPO(yaml)|           |  (k8s)   |      
                                       +----------+           +----------+      
```

