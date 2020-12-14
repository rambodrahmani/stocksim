package it.unipi.lsmsdb.stocksim.database.mongodb.entities;

public abstract class Location  {
    protected String city;
    protected String state;
    protected String country;
    protected String address;
    protected String phone;

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }
}
