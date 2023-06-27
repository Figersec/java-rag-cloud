package test.kailin;

import com.alibaba.fastjson.JSON;
import com.kailin.request.UserIdReq;
import com.kailin.response.UserInfoRes;
import com.kailinjt.middleware.kp.common.api.entity.KpResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author chengpuhui
 * @Date 2022/1/17
 */
public class UserTest extends BaseTest {

    @Autowired
    private UserController userController;

    @Test
    public void testGetUserInfoById(){
        UserIdReq req = new UserIdReq();
        req.setUserId(1L);
        KpResponse<UserInfoRes> res = userController.getUserInfoById(req);
        System.out.println("执行结果：" + JSON.toJSONString(res));
    }
}
