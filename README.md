# awesome-pong

Install

`jdk8-openjdk`, `jre8-openjdk`

```bash
cd src

/usr/lib/jvm/java-8-openjdk/bin/javac \
  -classpath \
  ../libraries/lwjgl-2.9.3/jar/lwjgl.jar \
  ./*/*.java

LD_LIBRARY_PATH=../libraries/lwjgl-2.9.3/native/linux \
  /usr/lib/jvm/java-8-openjdk/bin/java \
  -classpath \
  .:../libraries/lwjgl-2.9.3/jar/lwjgl.jar \
  pongGame.Run
```

