package begin.server;

import begin.annotation.RpcService;
import begin.util.ClassScanner;
import begin.util.Log;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class ServiceBeanFactory implements ApplicationContextAware {

    private final static ConcurrentHashMap<String, Object> serviceTable = new ConcurrentHashMap<>();

    public static void load(String packageName) throws Exception {
        String ext = ".class";
        String fileName[] = new ClassScanner().scannerPackage(packageName, ext);
        for (String f : fileName) {
            String className = packageName + "." + f.substring(0, f.length() - ext.length());
            Class<?> c = Class.forName(className);
            RpcService rpcServiceAnnotation = c.getAnnotation(RpcService.class);
            if (rpcServiceAnnotation != null) {
                Class<?> interfaceClass = rpcServiceAnnotation.value();
                String interfaceName = interfaceClass.getName();
                Object instance = ServiceProxy.createProxy(c.newInstance());
                Log.getLogger().debug("register service [{}] - [{}]", interfaceName, instance);
                serviceTable.put(interfaceName, instance);
            }
            else {
                Log.getLogger().warn("can not found @RpcService for [{}]", className);
            }
    }
    }

    public static Object getService(String serviceName) {
        return serviceTable.get(serviceName);
    }

    private static ApplicationContext ctx;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }
/*
    public static Object getService(String serviceName) {
        try {
            return ctx.getBean(serviceName);
        } catch (BeansException e) {
            Log.getLogger().error("", e);
        }
        return null;
    }*/
}
