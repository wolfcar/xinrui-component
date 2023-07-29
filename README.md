# xinrui-component

## 代码解构利器-SWAK

### 背景
> 随着业务的稳步发展，不断有新的业务模式接入。  
> 最初，我们可能在代码中添加一些开关来适配多种不同的实现。但这种方式好比给一个水管打洞，最终会发现到处”漏水“。  
> 原有的系统在演进过程中，面临着诸多挑战。

```
if(A类型) {
    if(A1类型) {
        doSomething1();
    }else if(A2类型) {
        doSomething2();
    }
} else if(B类型) {
    doSomething3();
} else if(C类型) {
    if(C1类型) {
        doSomething4();
    }else if(C2类型) {
        doSomething5();
    }
}
```

### 组件优势
代码解构利器，核心聚焦与上述描述的业务场景，即一个业务动作可能会多种实现的场景。
在分离业务实现的同时，给了业务系统无限的扩展能力，同时大大较低了代码维护的复杂性和人力成本。




### 实现原理
> ImportBeanDefinitionRegistrar、动态代理、策略模式

### Get Started

从开发者角度来看，使用该插件非常简单，下面是全部的集成和使用的步骤，如果是仅仅开发，只需关注3、4两部分。    
集成使用步骤描述如下：
1. 引入maven包
```xml
<dependency>
    <groupId>com.guoke</groupId>
    <artifactId>star-common-swak</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```
2. 在启动类，或者Configuration类上加上扫描注解，即要到哪些目录扫描@SwakInterface注解的扩展点接口

```java
import com.gkht.component.swak.annotation.SwakScan;

@Configuration
@ComponentScan(basePackages = {"com.guoke.star.saas.order"}) 
@SwakScan(basePackages={"com.guoke.star.saas.order"}) // 这里需要定制自己的扫描路径
public class SwakConfig {
    // pass
}
```
3. 在你需要扩展的接口类上添加注解
```java
@SwakInterface   //生成代理类
public interface TestService {
    String sayHello(BaseParam param);
}
```

4. 随便写自己的多个实现
```java
@SwakBiz(bizcode="star", tags="peijia-mode")
public class TestServiceImpl implements TestService {
    @Override
    public String sayHello(String param) {
        return "star say: hello world," + param;
    }
}
```
```java
@SwakBiz(bizcode="guoke", tags="maidi-mode")
public class BTestServiceImpl implements TestService {
    @Override
    public String sayHello(String param) {
        return "guoke say: hello world," + param;
    }
}
```
```java
@SwakBiz   //默认实现，一个标准化流程两个扩展点，一个业务模型，可能一个扩展点走标准化流程，一个走扩展逻辑，所以每个扩展点需有默认实现
public class BTestServiceImpl implements TestService {
    @Override
    public String sayHello(String param) {
        return "normal say: hello world," + param;
    }
}
```

```java
@RestController
public class SwakHelloWorldController {

    @Autowired //扩展点使用与正常注入 没有区别
    private TestService helloWorldSwakBiz;

    @RequestMapping("/hello")
    public String helloWorld(HelloWorldForm form ){
        return helloWorldSwakBiz.helloworld(form.getMessage());
    }
}
```
------------------
that's all




