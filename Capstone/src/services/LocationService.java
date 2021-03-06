package services;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import domain.Location;

public class LocationService implements Service<Location>{
	
	Connection connection;
	
	public LocationService() {
		super();
	}
	public LocationService(Connection connection) {
		super();
		this.connection = connection;
	}
	public boolean add(Location location){
		try{
			String locationId = location.getLocationId();
			String street = location.getStreet();
			String city = location.getCity();
			String state = location.getState();
			String country = location.getCountry();
			String zip = location.getZip();
                        String userId= location.getUserId();
                        Double taxRate=location.getTaxRate();
			
			CallableStatement oCSF = connection.prepareCall("{call sp_ins_locations(?,?,?,?,?,?,?,?)}");
			oCSF.setString(1, locationId);
                        oCSF.setString(2,userId);
                        oCSF.setDouble(3,taxRate);
			oCSF.setString(4, street);
			oCSF.setString(5, city);
			oCSF.setString(6, country);
			oCSF.setString(7, state);
			oCSF.setString(8, zip);
			oCSF.execute();
			oCSF.close();
			return true;
		}catch(SQLException e){
			System.out.println(e.getMessage());
			return false;
		}	
	}
	public void deleteById(String id){
		try{
			Statement locationsSt = connection.createStatement();
			locationsSt.executeQuery("Delete from locations where location_id = "+id);
		}catch(SQLException e){
			System.out.println(e.getMessage());
		}
	}
	public ArrayList<Location> getAll(){

		ArrayList<Location> locations = new ArrayList<Location>();
		
		try{
			Statement locationsSt = connection.createStatement();
			ResultSet locationsRs = locationsSt.executeQuery("Select * from Locations");
			
			while(locationsRs.next()){
				Location location = new Location(
						locationsRs.getString(1),
						locationsRs.getString(2),
						locationsRs.getInt(3),
						locationsRs.getString(4),
						locationsRs.getString(5),
						locationsRs.getString(6),
                                                locationsRs.getString(7),
                                                locationsRs.getString(8)
						); 
				locations.add(location);
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return locations;
	}
	public Location getById(String id){
		Location location = null;
		
		try{
			Statement locationsSt = connection.createStatement();
			ResultSet locationsRs = locationsSt.executeQuery("Select * from Locations where USER_ID = '" + id + "'");
			
			locationsRs.next();
			location = new Location(
					locationsRs.getString(1),
					locationsRs.getString(2),
					locationsRs.getInt(3),
					locationsRs.getString(4),
					locationsRs.getString(5),
					locationsRs.getString(7),
                                        locationsRs.getString(6),
					locationsRs.getString(8)
					); 
                       
		}catch(Exception e){
			System.out.println(e.getMessage());
		}	
		
		return location;
	}
        	public Location getByLocId(String id){
		Location location = null;
		
		try{
			Statement locationsSt = connection.createStatement();
			ResultSet locationsRs = locationsSt.executeQuery("Select * from Locations where Location_ID = '" + id + "'");
			
			locationsRs.next();
			location = new Location(
					locationsRs.getString(1),
					locationsRs.getString(2),
					locationsRs.getInt(3),
					locationsRs.getString(4),
					locationsRs.getString(5),
					locationsRs.getString(7),
                                        locationsRs.getString(6),
					locationsRs.getString(8)
					); 
                       
		}catch(Exception e){
			System.out.println(e.getMessage());
		}	
		
		return location;
	}
	public void update(Location location){
		try{
			String locationId = location.getLocationId();
                        String userid=location.getUserId();
                        double taxrate = location.getTaxRate();
			String street = location.getStreet();
			String city = location.getCity();
			String state = location.getState();
			String country = location.getCountry();
			String zip = location.getZip();
                        			
			CallableStatement oCSF = connection.prepareCall("{call sp_update_location(?,?,?,?,?,?,?,?)}");
			oCSF.setString(1, locationId);
                        oCSF.setString(2,userid);
                        oCSF.setDouble(3,taxrate);
			oCSF.setString(4, street);
			oCSF.setString(5, city);
			oCSF.setString(6, country);
			oCSF.setString(7, state);
			oCSF.setString(8, zip);
                   
                        oCSF.execute();
		}catch(SQLException e){
			System.out.println(e.getMessage());
		}	
	}
	
	public ArrayList<Location> getUserLocations(String userId){

		ArrayList<Location> locations = new ArrayList<Location>();
		
		try{
			Statement locationsSt = connection.createStatement();
			ResultSet locationsRs = locationsSt.executeQuery("Select * from Locations where user_id = '" + userId + "'");
			
			while(locationsRs.next()){
				Location location = new Location(
                                        locationsRs.getString(1),
					locationsRs.getString(2),
					locationsRs.getInt(3),
					locationsRs.getString(4),
					locationsRs.getString(5),
					locationsRs.getString(6),
                                        locationsRs.getString(7),
					locationsRs.getString(8)
						); 
				locations.add(location);
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return locations;
	}
}