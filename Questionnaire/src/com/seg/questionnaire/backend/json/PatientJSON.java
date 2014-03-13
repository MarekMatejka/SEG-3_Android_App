package com.seg.questionnaire.backend.json;

public class PatientJSON 
{
	private String NHS;
	
	private String first_name;
	
	private String middle_name;
	
	private String surname;
	
	private String dateOfBirth;
	
	private String postcode;
	
	private String disability;
	
	public String getNHS() {
		return NHS;
	}

	public String getFirst_name() {
		return first_name;
	}

	public String getMiddle_name() {
		return middle_name;
	}

	public String getSurname() {
		return surname;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public String getPostcode() {
		return postcode;
	}

	public String getDisability() {
		return disability;
	}
	
	public String getName(){
		if (middle_name.equals(""))
			return first_name+" "+surname;
		return first_name+" "+middle_name+" "+surname;
	}
}
