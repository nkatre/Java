package org.neo4j.examples.service;

public class Person {
	
	String id;
	String name;
	String age_range; 
	String bio; 
	String birthday; 
	String cover; 
	String devices; 
	String education; 
	String email; 
	String first_name; 
	String last_name; 
	
	enum gender {
		male, female, NotSpecified
	}
	
	String languages; 
	String link; 
	String location; 
	String middle_name; 
	String quotes; 
	String relationship_status; 
	String religion; 
	String significant_other; 
	String timezone; 
	String username; 
	String updated_time; 
	String work; 
	String address; 
	String favorite_athletes; 
	String favorite_teams; 
	String inspirational_people; 
	String interested_in; 
	String meeting_for; 
	String name_format; 
	String political; 
	String sports; 
	String friends; 
	String family; 
	String events; 
	String books; 
	String apprequests; 
	String albums; 
	String activities; 
	String accounts; 
	String games; 
	String groups; 
	String interests; 
	String likes; 
	String locations; 
	String movies; 
	String music; 
	String posts; 
	String questions; 
	String subscribedto; 
	String subscribers; 
	String television;
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
	public String getAge_range() {
		return age_range;
	}
	public void setAge_range(String age_range) {
		this.age_range = age_range;
	}
	public String getBio() {
		return bio;
	}
	public void setBio(String bio) {
		this.bio = bio;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getDevices() {
		return devices;
	}
	public void setDevices(String devices) {
		this.devices = devices;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getLanguages() {
		return languages;
	}
	public void setLanguages(String languages) {
		this.languages = languages;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getMiddle_name() {
		return middle_name;
	}
	public void setMiddle_name(String middle_name) {
		this.middle_name = middle_name;
	}
	public String getQuotes() {
		return quotes;
	}
	public void setQuotes(String quotes) {
		this.quotes = quotes;
	}
	public String getRelationship_status() {
		return relationship_status;
	}
	public void setRelationship_status(String relationship_status) {
		this.relationship_status = relationship_status;
	}
	public String getReligion() {
		return religion;
	}
	public void setReligion(String religion) {
		this.religion = religion;
	}
	public String getSignificant_other() {
		return significant_other;
	}
	public void setSignificant_other(String significant_other) {
		this.significant_other = significant_other;
	}
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUpdated_time() {
		return updated_time;
	}
	public void setUpdated_time(String updated_time) {
		this.updated_time = updated_time;
	}
	public String getWork() {
		return work;
	}
	public void setWork(String work) {
		this.work = work;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getFavorite_athletes() {
		return favorite_athletes;
	}
	public void setFavorite_athletes(String favorite_athletes) {
		this.favorite_athletes = favorite_athletes;
	}
	public String getFavorite_teams() {
		return favorite_teams;
	}
	public void setFavorite_teams(String favorite_teams) {
		this.favorite_teams = favorite_teams;
	}
	public String getInspirational_people() {
		return inspirational_people;
	}
	public void setInspirational_people(String inspirational_people) {
		this.inspirational_people = inspirational_people;
	}
	public String getInterested_in() {
		return interested_in;
	}
	public void setInterested_in(String interested_in) {
		this.interested_in = interested_in;
	}
	public String getMeeting_for() {
		return meeting_for;
	}
	public void setMeeting_for(String meeting_for) {
		this.meeting_for = meeting_for;
	}
	public String getName_format() {
		return name_format;
	}
	public void setName_format(String name_format) {
		this.name_format = name_format;
	}
	public String getPolitical() {
		return political;
	}
	public void setPolitical(String political) {
		this.political = political;
	}
	public String getSports() {
		return sports;
	}
	public void setSports(String sports) {
		this.sports = sports;
	}
	public String getFriends() {
		return friends;
	}
	public void setFriends(String friends) {
		this.friends = friends;
	}
	public String getFamily() {
		return family;
	}
	public void setFamily(String family) {
		this.family = family;
	}
	public String getEvents() {
		return events;
	}
	public void setEvents(String events) {
		this.events = events;
	}
	public String getBooks() {
		return books;
	}
	public void setBooks(String books) {
		this.books = books;
	}
	public String getApprequests() {
		return apprequests;
	}
	public void setApprequests(String apprequests) {
		this.apprequests = apprequests;
	}
	public String getAlbums() {
		return albums;
	}
	public void setAlbums(String albums) {
		this.albums = albums;
	}
	public String getActivities() {
		return activities;
	}
	public void setActivities(String activities) {
		this.activities = activities;
	}
	public String getAccounts() {
		return accounts;
	}
	public void setAccounts(String accounts) {
		this.accounts = accounts;
	}
	public String getGames() {
		return games;
	}
	public void setGames(String games) {
		this.games = games;
	}
	public String getGroups() {
		return groups;
	}
	public void setGroups(String groups) {
		this.groups = groups;
	}
	public String getInterests() {
		return interests;
	}
	public void setInterests(String interests) {
		this.interests = interests;
	}
	public String getLikes() {
		return likes;
	}
	public void setLikes(String likes) {
		this.likes = likes;
	}
	public String getLocations() {
		return locations;
	}
	public void setLocations(String locations) {
		this.locations = locations;
	}
	public String getMovies() {
		return movies;
	}
	public void setMovies(String movies) {
		this.movies = movies;
	}
	public String getMusic() {
		return music;
	}
	public void setMusic(String music) {
		this.music = music;
	}
	public String getPosts() {
		return posts;
	}
	public void setPosts(String posts) {
		this.posts = posts;
	}
	public String getQuestions() {
		return questions;
	}
	public void setQuestions(String questions) {
		this.questions = questions;
	}
	public String getSubscribedto() {
		return subscribedto;
	}
	public void setSubscribedto(String subscribedto) {
		this.subscribedto = subscribedto;
	}
	public String getSubscribers() {
		return subscribers;
	}
	public void setSubscribers(String subscribers) {
		this.subscribers = subscribers;
	}
	public String getTelevision() {
		return television;
	}
	public void setTelevision(String television) {
		this.television = television;
	}

	

}
