package top.backrunner.leaf;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import top.backrunner.leaf.system.service.UserService;
import top.backrunner.leaf.utils.misc.GeoIPUtils;
import top.backrunner.leaf.utils.misc.QiniuUtils;

import javax.annotation.Resource;

@Component
public class LeafInitializer implements ApplicationRunner {

    @Resource
    UserService userService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 初始化纯真IP数据库
        System.out.println("Init cz88.net Geo IP database...");
        GeoIPUtils.o.init();
        // 初始化 QiniuUtils
        System.out.println("Init qiniu utilities...");
        QiniuUtils.init();
        // 用户角色检查
        if (!userService.roleExists("admin")){
            // 不存在则初始化
            userService.initRole("admin");
        }
        if (!userService.roleExists("user")){
            // 不存在则初始化
            userService.initRole("user");
        }
    }
}
