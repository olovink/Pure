# Pure
Software for Minecraft Bedrock Edition 1.1.x forked Nukkit.

[![](https://jitpack.io/v/olovink/Pure.svg)](https://jitpack.io/#olovink/Pure)
Java 8 installation
-------------
- `sudo apt-get update && sudo apt-get install default-jdk`
- `sudo apt-get install openjdk-8-jdk`
- `update-alternatives --config java`, choose /usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java
- Check java version: `java -version`

Building
-------------
- `./gradlew build --no-daemon`
- Jar file (Pure.jar) is located in build/libs

Running
-------------
- `java -Xmx1G -jar Rooti.jar`
