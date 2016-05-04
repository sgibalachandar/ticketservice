package com.walmart.ticket.model;
import java.util.ArrayList;
import java.util.List;
public enum Venue {
	DEFAULT(-1,"Unknown"),
	ORCHESTRA(1,"Orchestra"),
	MAIN(2,"Main"),
	BALCONY_1(3,"Balcony 1"),
	BALCONY_2(4,"Balcony 2");
	private int levelId;

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	private String levelName;

	public int getLevelId() {
		return levelId;
	}

	public void setLevelId(int levelId) {
		this.levelId = levelId;
	}

	Venue(int levelId,String levelName){
		this.levelId = levelId;
		this.levelName = levelName;
	}
	public static Venue getVenue(int levelId){
		Venue defaultVenue = DEFAULT;
		for(Venue venue : Venue.values()){
			if(venue.getLevelId() == levelId){
				return venue;
			}
		}
		return defaultVenue;

	}
	public static Venue[] getVenues(int min,int max){
		List<Venue> venues = new ArrayList<Venue>();
		for(int i = min;i <= max;i++){
			venues.add(getVenue(i));
		}
		Venue[] venueArray = new Venue[venues.size()];
		venueArray = venues.toArray(venueArray);
		return venueArray;
	}

}
