package top.backrunner.leaf.system.entity;
import top.backrunner.leaf.core.entity.CoreEntityInfo;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "leaf_user")
public class UserInfo extends CoreEntityInfo implements Serializable {
    // 登录的用户名
    @Column(nullable = false)
    private String username;
    // 登录的密码
    @Column(nullable = false)
    private String password;
    // 加密用盐
    @Column(nullable = false)
    private String salt;
    // 邮箱
    private String email;
    // 电话
    private String phone;
    // 是否启用
    @Column(nullable = false)
    private boolean isEnabled;
    // 角色
    private Long roleId;
    // 邀请人id
    private Long inviter;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getInviter() {
        return inviter;
    }

    public void setInviter(Long inviter) {
        this.inviter = inviter;
    }
}
