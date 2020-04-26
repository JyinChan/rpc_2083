package begin.service.imp;

import begin.annotation.RpcService;
import begin.service.HelloService;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
@Scope("singleton")
@Lazy
@Service("begin.service.HelloService")
@RpcService(value=HelloService.class)
public class HelloServiceImpl implements HelloService {

    static boolean first = true;

    @Override
    public String hello(String name) {
        if (first) {
            first = false;
            try {
                Thread.sleep(30_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return "hello:" + name;
    }
}
