This repository shows three techniques for running functional tests of an application deployed to a Liberty server:

 * Using the build script to start and stop the Liberty server
 * Using a JUnit Rule to start and stop the server
 * Using [Arquillian](http://arquillian.org/) to start and stop the server

It is the code that backs up an article explaining the techniques in more detail here:

https://developer.ibm.com/wasdev/docs/writing-functional-tests-liberty

To run the sample you must have Gradle and Liberty (with the servlet-3.1 feature) installed and then do the following:

1. Update functionaltest-application/gradle.properties so the libertyRoot property points to your Liberty installation
2. In the sample.functionaltest directory run:
```bash
$ gradle build
```
