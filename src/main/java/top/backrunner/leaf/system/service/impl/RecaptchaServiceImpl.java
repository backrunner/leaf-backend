package top.backrunner.leaf.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import top.backrunner.leaf.config.RecaptchaConfig;
import top.backrunner.leaf.system.service.RecaptchaService;
import top.backrunner.leaf.utils.network.HttpResult;
import top.backrunner.leaf.utils.network.HttpUtils;

import javax.annotation.Resource;
import java.util.HashMap;

@Service
public class RecaptchaServiceImpl implements RecaptchaService {
    @Resource
    private RecaptchaConfig recaptchaConfig;

    private final String VERIFY_URL = "https://www.recaptcha.net/recaptcha/api/siteverify";

    @Override
    public boolean verify(String token) {
        // 为空不处理
        if (token == null || "".equals(token)){
            return false;
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("secret", recaptchaConfig.getSecret());
        params.put("response", token);
        HttpResult res = null;
        try {
            res = HttpUtils.doPost(VERIFY_URL, params);
        } catch (Exception e) {
            // post到api出现异常，返回false
            e.printStackTrace();
            return false;
        }
        // http不成功或者回复不正常，返回false
        if (res.getCode() != 200){
            return false;
        }
        if (res.getBody() == null){
            return false;
        }
        JSONObject parsedObject = JSON.parseObject(res.getBody());
        return (boolean)parsedObject.get("success");
    }
}
