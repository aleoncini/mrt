package it.redhat.mrt.model;

public class Key {
    public String name;
    public String value;

    public Key setName(String name){
        this.name = name;
        return this;
    }
    public Key setValue(String value){
        this.value = value;
        return this;
    }
}
