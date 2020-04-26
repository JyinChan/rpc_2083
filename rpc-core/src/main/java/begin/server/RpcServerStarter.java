package begin.server;

import begin.service.HelloService;
import begin.service.ServiceScan;
import begin.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("prototype") //必须多例A才能多例
@SpringBootApplication(/*scanBasePackages = {"begin.service.imp", "begin.server"}*/)
public class RpcServerStarter {

    public static void main(String[] args) throws Exception {
        //SpringApplication.run(RpcServerStarter.class);
        ApplicationContext ctx = new AnnotationConfigApplicationContext(RpcServerStarter.class, ServiceScan.class);
        Log.getLogger().debug("service : " + ServiceBeanFactory.getService(HelloService.class.getName()));
        Log.getLogger().debug("service : " + ServiceBeanFactory.getService(HelloService.class.getName()));

        Log.getLogger().debug("scope test: " + ctx.getBean(RpcServerStarter.class).getA());
        Log.getLogger().debug("scope test: " + ctx.getBean(RpcServerStarter.class).getA());

    }

    @Autowired
    private A a;

    public A getA() {
        return a;
    }
}

@Scope("prototype")
@Component
class A {

}
