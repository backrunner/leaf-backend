package top.backrunner.leaf.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.backrunner.leaf.utils.common.R;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/error")
public class ErrorController {
    @RequestMapping(value = "/unauth")
    @ResponseBody
    public R unauth(HttpServletResponse response){
        response.setStatus(401);
        return R.unauth("无权限访问");
    }
}