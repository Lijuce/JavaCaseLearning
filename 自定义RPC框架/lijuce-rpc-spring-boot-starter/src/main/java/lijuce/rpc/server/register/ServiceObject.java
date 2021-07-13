package lijuce.rpc.server.register;

/**
 * 服务持有对象，保存具体的服务信息作为备用
 */
public class ServiceObject {
    private String name;

    private Class<?> aClass;

    private Object obj;

    @Override
    public String toString() {
        return "ServiceObject{" +
                "name='" + name + '\'' +
                ", aClass=" + aClass +
                ", obj=" + obj +
                '}';
    }

    public ServiceObject(String name, Class<?> aClass, Object obj) {
        this.name = name;
        this.aClass = aClass;
        this.obj = obj;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getaClass() {
        return aClass;
    }

    public void setaClass(Class<?> aClass) {
        this.aClass = aClass;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
