package qdb.qdl.common;
//基于ThreadLocal封装的工具类，用户保持和获取当前登入用户id
public class BaaseContext {
    private static ThreadLocal<Long>threadLocal = new ThreadLocal<>();
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
