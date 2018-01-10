# cover

分布式锁, 单机锁简单实现

方法上@BanReentry 默认key为clz + methodName，支持参数前使用@Param增加key唯一性
```java
    @BanReentry
    public TestDTO query(@Param int appId) {
        ...
    }
```

* spring xml中开启扫描和代理

```xml
    <!-- scan package -->
    <context:component-scan base-package="com.cover"/>
    <aop:aspectj-autoproxy proxy-target-class="true"/>
```

* 使用自己实现的重入处理器
```xml
    <!-- use config 1/2...-->
    <bean id="reentryAspect" class="com.cover.core.ReentryAspect">
        <!-- use default return types-->
        <property name="returnTypes" ref="returnTypes"/>
        <!-- use reentry handler -->
        <property name="reentryHandler" ref="redisReetryHandler"/>
    </bean>
```

# 重入处理器
* 自定义实现
```java
  实现ReentryHandler接口中isReentry方法即可
```
* 重入处理器配置1:redis分布式锁
```xml
    <!--1. redis config -->
    <bean id="jedisPool" class="redis.clients.jedis.JedisPool">
        <constructor-arg name="poolConfig" ref="poolConfig"/>
        <constructor-arg name="host" value="192.168.180.10"/>
        <constructor-arg name="port" value="6379"/>
    </bean>

    <bean id="poolConfig" class="org.apache.commons.pool2.impl.GenericObjectPoolConfig">
        <property name="testOnBorrow" value="true"/>
    </bean>

    <bean id="redisReetryHandler" class="com.cover.core.RedisReetryHandler">
        <property name="jedisPool" ref="jedisPool"/>
    </bean>
```

* 重入处理器配置2:单机锁
```xml
    <!--2. local memory config -->
    <bean id="standardReentryHandler" class="com.cover.core.StandardReentryHandler"/>
```


# 配置方法重入时返回的默认数据
* 实现InitializerReturnType接口即可
```java
  public class Init implements InitializerReturnType {

      @Override
      public Map<Class, Object> getInstanceOfReturnTypes() {
          Map<Class, Object> map = new HashMap<>();
          //map.put(AA.class, new AA(-1, "error"));
          return map;
      }

  }
```

```xml
    <!-- return types config -->
    <bean id="returnTypes" class="com.cover.core.Init"/>
```



