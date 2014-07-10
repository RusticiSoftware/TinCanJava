A Java library for implementing with the Tin Can API.

[![Build Status](https://travis-ci.org/RusticiSoftware/TinCanJava.png)](https://travis-ci.org/RusticiSoftware/TinCanJava)

For hosted API documentation, basic usage instructions, supported version listing, etc. visit the main project website at:

http://rusticisoftware.github.io/TinCanJava/

For more information about the Tin Can API visit:

http://tincanapi.com/

This library uses Maven 3 for project management, building, etc. It outputs Maven artifacts.

Building
--------

Check out the source:

    git clone https://github.com/RusticiSoftware/TinCanJava.git
    cd TinCanJava

With the repo cloned copy the `src/test/resources/lrs.properties.template` file
to `src/test/resources/lrs.properties` and adjust the values to point to a valid LRS. Then
build the project using:

    mvn install

This will download dependencies (that are not already locally available) and build and test the
artifact. The result will be in `target/tincan-0.2.5-SNAPSHOT.jar`. To build a specific version
of the library you will need to checkout to the tag for that version first, such as:

    git checkout tincan-0.2.4

And then do `mvn install`, which will provide `target/tincan-0.2.4.jar`.

Releasing
---------

See http://maven.apache.org/maven-release/maven-release-plugin/index.html.

    mvn release:prepare -DdryRun=true
    mvn release:clean
    mvn release:prepare
    mvn release:perform

At some point in the future we plan to make the maven artifacts available via some publicly
available repository.
