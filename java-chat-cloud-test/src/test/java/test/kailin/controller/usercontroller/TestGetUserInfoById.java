package test.kailin.controller.usercontroller;

import com.kailin.ChatApplication;
import com.kailin.controller.UserController;
import com.kailin.framework.TestCase;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.annotations.Test;

import java.util.Map;

@SpringBootTest(classes = ChatApplication.class)
public class TestGetUserInfoById extends TestCase<UserController>{

    @Override
    public void beforeTest(Map<String, Object> context) {

    }

    /**
    *moke录制标签,加入了之后,会自动录制dubbo调用情况,之后可以改为replay模式会重放
    *@Mode(TestMethodMode.REC)
    */
    @Test(dataProvider = "excel")
    @Override
    public void assertCheck(Map<String, Object> context, Object result) {

    }


}
