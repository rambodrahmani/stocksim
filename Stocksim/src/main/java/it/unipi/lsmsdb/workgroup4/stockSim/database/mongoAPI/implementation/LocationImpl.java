package it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI.implementation;


import it.unipi.lsmsdb.workgroup4.stockSim.database.mongoAPI.entities.Location;

class LocationImpl extends Location {

    protected LocationImpl(String city, String state, String country, String address, String phone) {
        this.city=city;
        this.state=state;
        this.country=country;
        this.address=address;
        this.phone=phone;
    }
}
