package top.backrunner.leaf.system.service;

import top.backrunner.leaf.system.entity.InviteCodeInfo;

import java.util.List;

public interface InviteCodeService {
    public List<InviteCodeInfo> getList(int page, int pageSize);
    public long getCount();
    public void generate(int count);
    public void generate(String prefix,int count);
    public boolean addNew(String code);
    public boolean delete(long id);
    public boolean codeExist(String code);
}
