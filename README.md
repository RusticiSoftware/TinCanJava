## TinCanJava Project Website

### API Doc Generation

To build the javadoc checkout to the release tag, edit the pom.xml to uncomment the delombok phase, and then issue:

    mvn javadoc:javadoc

The generated javadoc will be available in `target/site/apidocs/`.
