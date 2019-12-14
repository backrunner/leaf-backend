package top.backrunner.leaf.system.service.impl;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.springframework.stereotype.Service;
import top.backrunner.leaf.system.dao.InviteCodeDao;
import top.backrunner.leaf.system.entity.InviteCodeInfo;
import top.backrunner.leaf.system.service.InviteCodeService;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class InviteCodeServiceImpl implements InviteCodeService {

    @Resource
    private InviteCodeDao inviteCodeDao;

    @Override
    public void generate(String prefix, int count) {
        for (int i=0;i<count;i++){
            InviteCodeInfo info = new InviteCodeInfo();
            info.setCreateTime(new Date());
            info.setUsed(false);
            info.setCode(prefix + new Md5Hash(new SecureRandomNumberGenerator().nextBytes().toHex() + System.currentTimeMillis()).toHex());
            inviteCodeDao.add(info);
        }
    }

    @Override
    public boolean addNew(String code) {
        InviteCodeInfo info = new InviteCodeInfo();
        info.setCreateTime(new Date());
        info.setUsed(false);
        info.setCode(code);
        return inviteCodeDao.add(info);
    }

    @Override
    public boolean delete(long id) {
        return inviteCodeDao.removeByHql("DELETE FROM InviteCodeInfo WHERE id = "+id);
    }

    @Override
    public boolean codeExist(String code) {
        return inviteCodeDao.checkExists(code);
    }

    @Override
    public List<InviteCodeInfo> getList(int page, int pageSize) {
        return inviteCodeDao.showPage("FROM InviteCodeInfo", page, pageSize);
    }

    @Override
    public long getCount() {
        return inviteCodeDao.countByHql("select count(*) from InviteCodeInfo");
    }

    @Override
    public void generate(int count) {
        for (int i=0;i<count;i++){
            InviteCodeInfo info = new InviteCodeInfo();
            info.setCreateTime(new Date());
            info.setUsed(false);
            info.setCode(new Md5Hash(new SecureRandomNumberGenerator().nextBytes().toHex() + System.currentTimeMillis()).toHex());
            inviteCodeDao.add(info);
        }
    }
}
