package top.backrunner.leaf.system.service;

public interface RecaptchaService {
    public boolean verify(String token);
}
