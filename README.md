

<div align=center>
  <img width="320" src="http://www.zhoutao123.com/picture/book_convert/saka.png" alt="图片加载失败"/>
</div>



<div style="height:20px"></div>

您可以通过以下导航来在 语雀 中访问我的读书笔记，涵盖了技术、服务端开发与基础架构、闲谈杂记等多个项目：


《[前端开发杂记](https://www.yuque.com/zhoutao123/front_end)》
《[设计模式](https://www.yuque.com/zhoutao123/design_pattern)》
《[深入理解JVM虚拟机](https://www.yuque.com/zhoutao123/jvm)》
《[Java 并发编程](https://www.yuque.com/zhoutao123/java_concurrent)》
《[Netty入门与实战](https://www.yuque.com/zhoutao123/netty)》
《[高性能MySQL](https://www.yuque.com/zhoutao123/mysql)》

 [最新文档，请访问语雀文档](https://www.yuque.com/zhoutao123)

---


## Saka JavaWeb 事件总线框架

+ Saka是一个简单的基于事件的框架总库,支持一对一发送、一对多发送，以及多对多发送消息机制，
目前项目处于1.0版本，正在处于开发中,稍后更详细的使用日志会同步更新。


## 使用方法

#### 1、添加依赖


#### 2、使用注解标注对象和执行器
**(注意：目前仅支持最多无参接收或者一参接收，暂不支持多个参数接收消息)**

+ 使用```@SakaSService``` 既可以将此对象注入至Spring容器


+ 使用```@SakaSubscribe```即可声明某方法为接收方法

|参数名称|说明|默认值|备注|
|-----|----|-----|-----|
|debug|是否为调试模式|false|调试模式在使用中会打印日志|
|order|设置优先级顺序|5|数字越小，优先级越高,越先被执行|

如下定义了三种基本的接收器

```java
/**
 * 实现一个接收消息
 *
 * <p>使用@SakaService注入到Spring容器
 *
 * <p>使用@SakaSubscribe标注这是一个接收器
 *
 * @author tao
 */
@SakaService
public class BaseService {

  /** 自动注入对象测试 */
  @Autowired SpeakService speakService;

  /** 不接受参数 */
  @SakaSubscribe
  public void sendEmpty() {
    speakService.speak();
    System.out.println("exec sendEmpty ");
  }

  /** 接受一个参数 */
  @SakaSubscribe
  public void sendString(String message) {
    speakService.speak();
    System.out.println("exec  sendString = " + message);
  }
  
  /** 接受一个参数自定义的参数类型 */
  @SakaSubscribe
  public void sendClassObject(Message message) {
    speakService.speak();
    System.out.println("exec  sendClassObject = " + message.getName());
  }  
}

```

#### 2、注入SakaSendClient
Saka在应用启动的时候向Spring的Context中注入ISakaClient,在SpringBoot项目中可使用以下代码自动的注入Bean对象。

```java
@RestController
public class TestController {

  /** 自动注入SakaClient */
  @Autowired
  SakaSendClient sakaSendClient;

}  
```


#### 3、使用Client发送消息
在需要发送消息的地方，注入SakaClient对象后，使用send()或者send(Object)方法发送消息.

```java
@RestController
public class TestController {

  /** 自动注入SakaClient */
  @Autowired SakaSendClient sakaSendClient;

  /**
   * 尝试发送一个字符串消息
   *
   * @param name
   * @return
   * @throws Exception
   */
  @GetMapping("/string/{name}")
  public String sendString(@PathVariable("name") String name) throws Exception {
    sakaSendClient.send(name);
    return name;
  }

  /**
   * 尝试发送自定义类型消息
   *
   * @param message
   * @return
   * @throws Exception
   */
  @GetMapping("/message/{message}")
  public String sendMessage(@PathVariable("message") String message) throws Exception {
    Message messageObject = new Message().setName(message).setAge(12);
    sakaSendClient.send(messageObject);
    return messageObject.getName();
  }

  /**
   * 尝试发送空消息
   *
   * @return
   * @throws Exception
   */
  @GetMapping("/empty")
  public String sendEmpty() throws Exception {
    sakaSendClient.send();
    return "success";
  }
} 

```

#### 4、控制台观察消息发送日志
可以使用注解@SakaSubscribe注解参数debug配置是否输入打印日志，默认是不打印日志。

```text
//Saka注册接收器日志                                                    
 Saka ------> Add a methods execCommand(1) to Saka
 Saka ------> Add a methods sendClassObject(1) to Saka
 Saka ------> Add a methods printMessage(0) to Saka
 Saka ------> Add a methods speak(0) to Saka                
                                                                  
//Saka发送消息  
 Saka ------> Send data to testMethodes1 successfully             
 Saka ------>  Saka has successfully sent 1 times data.           
 Saka ------>  Saka has successfully sent 1 times data.           
 Saka ------>  Saka has successfully sent 1 times data.           
```
## 开启优先级顺序执行
在通过注解```@SakaSubscribe```设置Subscribe的同同时，您可以为其设置优先级order，如果不设置默认值为OrderConstance.ORDER_NORMALE(5)，数字越小，优先级越高,越先被执行。如下所示

```java
  @SakaSubscribe(debug = true,order = OrderConstance.ORDER_NORMALE)
  public void printMessage() {}

  /** 接受参数一个参数 */
  @SakaSubscribe(debug = true,order = OrderConstance.ORDER_HIGHT)
  public void execCommand(String message) {}

  /** 接受一个参数自定义的参数类型 */
  @SakaSubscribe(order = OrderConstance.ORDER_LOWWER)
  public void sendClassObject(Message message) {}
```
同时,考虑到性能，顺序执行Subscribe是默认关闭状态的，您可以通过application配置文件配置启用此功能

+ properties文件

```properties
saka.sequence-execute=true
```
+ yaml文件

```yaml
saka:
  sequence-execute: true
```

## 设置监听器
在一些情况下您可以设置listener来实现Subscribe的执行状况监听,您只需要在Spring容器中注入监听对象即可，此Bean要求继承HandleSubscribeListener接口。

比如:

```java
  @Bean
  public HandleSubscribeListener handleSubscribeListener(){
    return new HandleSubscribeListener() {
      //TODO 返回执行结果
      @Override
      public void onSuccess(MetaMethod metaMethod, Object resultObject) {
        log.info("Subscribe监听器接收到执行了方法 = {} ",metaMethod.getMethod().getName());
      }

      @Override
      public boolean onError(Throwable t) {
        log.error("Subscribe监听器接收执行错误的通知,错误信息 = {}",t);
        return true;
      }
    };
  }

```
+ 成功执行一个Subscribe的时候将会执行```public void onSuccess(MetaMethod, Object )```方法，其中MetaMethod表示当前执行的对象,resultObject表示执行的结果对象
+ 执行Subscribe中出现了异常信息的时候，将执行```public boolean onError(Throwable)``` 您可以返回一个布尔类型的数据表示是否继续执行剩余的Subscribe，其中Throwable表示出现的异常信息


## 设置统一异常处理
Saka 提供了统一异常处理的机制，当消费者执行消息出现异常，则会执行异常处理，Saka 内置了一个默认的处理器，但是仅仅打印出系统的错误日志而已，如果不满足业务需要，可以通过继承
`com.zhoutao123.framework.saka.advice.ExceptionAdvice`

```java
/** 统一异常处理接口 */
public interface ExceptionAdvice {

  /**
   * 异常处理
   *
   * @param method 执行的方法名称
   * @param e 发生的异常信息
   */
  void handleException(Method method, RuntimeException e);
}
```


接口并注入Bean 到IOC容器即可。如

```java
@Bean
public class DefaultExceptionAdvice implements ExceptionAdvice {

  @Override
  public void handleException(Method method, RuntimeException e) {
    log.error("Saka执行事件{}出现错误", method.getName());
    log.error("Saka 订阅事件执行出现异常", e);
  }
}

```

## 关闭Saka

在使用的过程中,可以通过配置动态的关闭Saka,重新启动应用，可以看到以下提示则说明关闭成功。
```text
Saka ------> Don't open the Saka, please check the configuration information
```


#### properties文件配置

```properties
saka.enable= false
```
#### yaml文件配置

```yaml
saka:
    enable: false
```
