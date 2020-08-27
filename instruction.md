### cannot access class jdk.internal.misc.Unsafe
Add to vm
```
--add-opens java.base/jdk.internal.misc=ALL-UNNAMED -Dio.netty.tryReflectionSetAccessible=true
```
