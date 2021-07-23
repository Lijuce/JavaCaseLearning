package lijuce.rpc.annotation;


import java.lang.annotation.*;

/**
 * 消息协议专用注解
 * @author Lijuce
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MessageProtocolAno {
    String value() default "";
}
