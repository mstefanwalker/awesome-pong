# awesome-pong

Pong project from around 2015

Use `Q` `A` vs `O` `L`

Set `powerThreshold = 3` in `Run.java` for some fun! Use `W` and `I` when the time is right.

## Play

I think I had this set up in an editor like Eclipse or Netbeans way back when.

Here are some Manjaro instructions that I threw together by trial and error on my machine.

`jdb` is a cool drop in replacement for the `java` binary if you are debugging a setup on a different OS.

### **Install**

`jdk8-openjdk`, `jre8-openjdk`

### **Compile**

from src folder

```bash
/usr/lib/jvm/java-8-openjdk/bin/javac \
  -classpath \
  ../libraries/lwjgl-2.9.3/jar/lwjgl.jar \
  ./*/*.java
```

### **Run**

from src folder

```bash
LD_LIBRARY_PATH=../libraries/lwjgl-2.9.3/native/linux \
  /usr/lib/jvm/java-8-openjdk/bin/java \
  -classpath \
  .:../libraries/lwjgl-2.9.3/jar/lwjgl.jar \
  pongGame.Run
```

