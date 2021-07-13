package lijuce.rpc.server.register;

import lijuce.rpc.annotation.InjectService;
import lijuce.rpc.annotation.Service;
import lijuce.rpc.client.ClientProxyFactory;
import lijuce.rpc.server.RpcServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;

/**
 * RPC处理者，支持服务启动暴露、自动注入Service。
 * 注意，这里是自动，自动，自动
 *
 * DefaultRpcProcessor实现了ApplicationListener，
 * 并监听了ContextRefreshedEvent事件，其效果就是在Spring启动完毕过后会收到一个事件通知，
 * 基于这个机制，就可以在这里开启服务，以及注入服务。
 */
public class DefaultRpcProcessor implements ApplicationListener<ContextRefreshedEvent> {

    // 由于需要支持自动注入Service，因此以下利用@Resource注解注入需要的对象
    @Resource
    private ClientProxyFactory clientProxyFactory;

    @Resource
    private ServiceRegister serviceRegister;

    @Resource
    private RpcServer rpcServer;

    // 监听事件
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 获取事件的上下文信息，准备注入
        if (Objects.isNull(event.getApplicationContext().getParent())){
            ApplicationContext context = event.getApplicationContext();
            // 开启服务
            startServer(context);
            // 注入service
            injectService(context);
        }
    }

    /**
     * 开启服务 方法
     * @param context
     */
    private void startServer(ApplicationContext context){
        // 获取带有Service注解的beans
        System.out.println("获取带有Service注解的beans");
        Map<String, Object> beans = context.getBeansWithAnnotation(Service.class);
        System.out.println("beans大小：" + beans.size());
        if (beans.size() != 0){
            boolean startServerFlag = true;
            for (Object obj: beans.values()){
                try {
                    Class<?> aClass = obj.getClass();
                    Class<?>[] interfaces = aClass.getInterfaces();  // 服务 对应的抽象接口
                    ServiceObject so;
                    if (interfaces.length != 1) {
                        Service service = aClass.getAnnotation(Service.class);
                        String value = service.value();
                        if (value.equals("")) {
                            startServerFlag = false;
                            throw new UnsupportedOperationException("The exposed interface is not specific with '" + obj.getClass().getName() + "'");
                        }
                        so = new ServiceObject(value, Class.forName(value), obj);
                    } else {
                        Class<?> superClass = interfaces[0];
                        so = new ServiceObject(superClass.getName(), superClass, obj);
                    }
                    System.out.println("ServiceObject: " + so);
                    serviceRegister.register(so);
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
            if (startServerFlag)
                rpcServer.start();
        }
    }

    private void injectService(ApplicationContext context){
        String[] names = context.getBeanDefinitionNames();
        for (String name: names){
            Class<?> aClass = context.getType(name);
            if (Objects.isNull(aClass))
                continue;

            Field[] fields = aClass.getDeclaredFields();
            for(Field field: fields){
                InjectService injectService = field.getAnnotation(InjectService.class);
                if (Objects.isNull(injectService))
                    continue;
                Class<?> fieldClass = field.getType();
                Object object = context.getBean(name);
                field.setAccessible(true);
                try {
                    field.set(object, clientProxyFactory.getProxy(fieldClass));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
