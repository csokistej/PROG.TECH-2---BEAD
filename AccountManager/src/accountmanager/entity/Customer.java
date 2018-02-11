package accountmanager.entity;

import java.util.Objects;

/**
 *
 * @author kmozsi
 */
public class Customer extends PersistentEntity {

    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    
    
    public Customer() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return lastName + " " + firstName + " " + address + " " + phone;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Customer other = (Customer) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public Object get(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return id;
            case 1:
                return lastName;
            case 2:
                return firstName;
            case 3:
                return address;
            case 4:
                return phone;
            default:
                return null;
        }
    }

    @Override
    public void set(int columnIndex, Object value) {
        switch (columnIndex) {
            case 0:
                setId((Integer) value);
                break;
            case 1:
                setLastName((String) value);
                break;
            case 2:
                setFirstName((String) value);
                break;
            case 3:
                setAddress((String) value);
                break;
            case 4:
                setPhone((String) value);  
        }
    }

    
    
    public static final String[] COLUMN_NAMES= {"ID", "First Name", "Last Name", "Address", "Phone"};

}
