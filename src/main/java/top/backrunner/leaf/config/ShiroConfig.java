package top.backrunner.leaf.config;

import net.sf.ehcache.CacheManager;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.io.ResourceUtils;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.backrunner.leaf.config.shiro.OptionsRequestFilter;
import top.backrunner.leaf.config.shiro.RetryLimitCredentialsMatcher;
import top.backrunner.leaf.config.shiro.UserRealm;

import javax.servlet.Filter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    @Bean
    public Realm realm(){
        UserRealm realm = new UserRealm();
        realm.setCredentialsMatcher(retryLimitCredentialsMatcher());
        return realm;
    }

    @Bean
    public SimpleCookie rememberMeCookie(){
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        simpleCookie.setMaxAge(604800);
        return simpleCookie;
    }

    @Bean
    public CookieRememberMeManager rememberMeManager(){
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        cookieRememberMeManager.setCipherKey("OHGODTHISISKEY!!".getBytes());
        return cookieRememberMeManager;
    }

    @Bean
    public SessionManager sessionManager(){
        return new DefaultWebSessionManager();
    }

    @Bean
    public EhCacheManager ehCacheManager() {
        CacheManager cm = CacheManager.getCacheManager("es");
        if (cm == null) {
            try {
                cm = CacheManager.create(ResourceUtils.getInputStreamForPath("classpath:shiro/ehcache.xml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManager(cm);
        return cacheManager;
    }

    @Bean
    public CredentialsMatcher retryLimitCredentialsMatcher() {
        RetryLimitCredentialsMatcher retryLimitCredentialsMatcher = new RetryLimitCredentialsMatcher(ehCacheManager());
        retryLimitCredentialsMatcher.setHashAlgorithmName("SHA-256");
        retryLimitCredentialsMatcher.setHashIterations(32);
        return retryLimitCredentialsMatcher;
    }

    @Bean({"securityManager"})
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm());
        securityManager.setCacheManager(ehCacheManager());
        securityManager.setSessionManager(sessionManager());
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    public OptionsRequestFilter optionsRequestFilter(){
        return new OptionsRequestFilter();
    }

    @Bean({"shiroFilter"})
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
        // 配置unauth跳转
        shiroFilter.setLoginUrl("/error/unauth");
        shiroFilter.setUnauthorizedUrl("/error/unauth");
        // 配置不会被拦截的api
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/portal/**", "anon");
        filterChainDefinitionMap.put("/captcha/**", "anon");
        filterChainDefinitionMap.put("/druid/**", "anon");
        filterChainDefinitionMap.put("/stat/**", "anon");
        filterChainDefinitionMap.put("/admin/**", "roles[admin]");
        // 对应的是user拦截器，支持rememberMe
        filterChainDefinitionMap.put("/**", "optionsRequestFilter");
        shiroFilter.setFilterChainDefinitionMap(filterChainDefinitionMap);
        // 自定义过滤器
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("optionsRequestFilter", optionsRequestFilter());
        shiroFilter.setFilters(filterMap);
        return shiroFilter;
    }

    @Bean({"lifecycleBeanPostProcessor"})
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
