package it.unipi.lsmsdb.stocksim.client.entities;

/**
 * Location generic data structure
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public  class Location  {
    protected String city;
    protected String state;
    protected String country;
    protected String address;
    protected String phone;

    protected Location(String city, String state, String country, String address, String phone) {
        this.city = city;
        this.state = state;
        this.country = country;
        this.address = address;
        this.phone = phone;
    }

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
