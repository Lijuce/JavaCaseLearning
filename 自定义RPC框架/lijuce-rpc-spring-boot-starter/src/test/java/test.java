import lijuce.rpc.RpcApplication;
import lijuce.rpc.config.AutoConfiguration;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ClassName test
 * @Description TODO
 * @Author Lijuce_K
 * @Date 2021/7/23 18:05
 * @Version 1.0
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RpcApplication.class)
public class test {
    @Test
    public void getMessageProtocolTest(){
        AutoConfiguration.getMessageProtocol("javaJdk");
//        System.out.println("hello");
    }
}
