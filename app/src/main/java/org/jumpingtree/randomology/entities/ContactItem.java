package org.jumpingtree.randomology.entities;

import java.io.Serializable;

/**
 * Created by Miguel on 26/01/2015.
 */
public class ContactItem implements Serializable{

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String number;
    private String photo;

    public ContactItem(){
        this.id = null;
        this.name = null;
        this.number = null;
        this.photo = null;
    }

    public ContactItem(String id, String name, String number, String photo) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContactItem that = (ContactItem) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (!number.equals(that.number)) return false;
        if (photo != null ? !photo.equals(that.photo) : that.photo != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + number.hashCode();
        result = 31 * result + (photo != null ? photo.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ContactItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
