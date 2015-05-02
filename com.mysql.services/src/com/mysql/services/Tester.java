package com.mysql.services;

public class Tester {

	public static void main(String[] args) {
	
		System.out.println(new MainController().getTags());
		
		
		
		/*StandardResult finalResult = new StandardResult();
		List <VenueObject> results = new ArrayList<VenueObject>();
		
		VenueObject venue1 = new VenueObject();
		venue1.setName("name1");
		
		VenueObject venue2 = new VenueObject();
		venue2.setName("name2");
		
		results.add(venue1);
		results.add(venue2);
		
		
		finalResult.setResults(results);
		
		
		Gson gson = new Gson();
		String json = gson.toJson(finalResult);
		
		System.out.println(json);
		*/
		
	}

}
