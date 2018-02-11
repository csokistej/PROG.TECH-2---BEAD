package accountmanager.entity;

import java.io.Serializable;

public abstract class PersistentEntity implements Serializable {

    protected Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public abstract Object get(int columnIndex);

    public abstract void set(int columnIndex, Object value);

    
    
}
