package top.backrunner.leaf.core.dao;

public interface LogDao<T> extends BaseDao<T> {
    public boolean create(T log);
}
