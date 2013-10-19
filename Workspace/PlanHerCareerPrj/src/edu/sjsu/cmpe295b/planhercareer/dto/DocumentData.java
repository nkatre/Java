package edu.sjsu.cmpe295b.planhercareer.dto;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * Document object with its related Documents
 */
public class DocumentData 
{	
	/**
	 * Named node to define the parts of speech of the Document
	 * 
	 * Noun–is a Document used to name a person, place, thing, or idea. A noun can be a proper noun or a common noun.
	 * Pronoun–is a Document that replaces a person, place, thing, or idea. Pronouns can act as subjects or objects, and some can show possession.
	 * Adjective–is a Document used to describe, or modify, a noun or a pronoun. An adjective describes “what kind,” “which one,” “how many,” or “how much.”
	 * Verb–is a Document that shows action or that indicates a condition or a state of being.
	 * Adverb–is a Document used to describe, or modify, a verb, an adjective, or another adverb. An adverb describes how, when, where, or to what extent the verb performs.
	 * Preposition–is a Document used to show a relationship between a noun or a pronoun and some other Document in the sentence. Prepositions often show direction, location, or time.
	 * Conjunction–is a Document that connects other Documents or groups of Documents to each other. There are three types of conjunctions: coordinating, subordinating, and correlative.
	 * Interjection–is a Document used to express emotion that has no grammatical relationship to other Documents in the sentence. Interjections should be used sparingly and usually only belong in narrative dialogue
	 */
	public enum SourceWebsite {		
		LeanIn, CNN, TecCrunch, GlassDoor, None
	}
	
	private String url;
	private SourceWebsite sourceWebsite;
	private Set<String> category;
	private Set<String> keyDocument;
	private Set<String> concept;
	private Set<String> entities;
	private Set<String> sentiments;
	private Set<String> otherRelated;
	public String getUrl() {
		return url;
	}
	public void setUrl(String term) {
		this.url = term;
	}
	public SourceWebsite getSourceWebsite() {
		return sourceWebsite;
	}
	public void setSourceWebsite(SourceWebsite partsOfSpeech) {
		this.sourceWebsite = partsOfSpeech;
	}
	public Set<String> getCategory() {
		return category;
	}
	public void setCategory(Set<String> string) {
		this.category = string;
	}
	public Set<String> getKeyDocument() {
		return keyDocument;
	}
	public void setKeyDocument(Set<String> keyDocument) {
		this.keyDocument = keyDocument;
	}
	public Set<String> getSentiment() {
		return sentiments;
	}
	public void setSentiment(Set<String> sentiments) {
		this.sentiments = sentiments;
	}
	public Set<String> getConcept() {
		return concept;
	}
	public void setConcept(Set<String> concept) {
		this.concept = concept;
	}
	public Set<String> getEntities() {
		return entities;
	}
	public void setEntities(Set<String> entities) {
		this.entities = entities;
	}
	public Set<String> getOtherRelated() {
		return otherRelated;
	}
	public void setOtherRelated(Set<String> otherRelated) {
		this.otherRelated = otherRelated;
	}
	@Override
	public String toString() {
		return "DocumentData [url=" + url + ", sourceWebsite=" + sourceWebsite
				+ ", category=" + category + ", keyDocument=" + keyDocument
				+ ", Concept=" + concept + ", homonyms=" + entities
				+ ", otherRelated=" + otherRelated + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((concept == null) ? 0 : concept.hashCode());
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		
		result = prime * result
				+ ((otherRelated == null) ? 0 : otherRelated.hashCode());
		result = prime * result
				+ ((sourceWebsite == null) ? 0 : sourceWebsite.hashCode());
		result = prime * result
				+ ((keyDocument == null) ? 0 : keyDocument.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocumentData other = (DocumentData) obj;
		if ((concept == null) || (concept.isEmpty())) {
			if ((other.concept != null) && (!other.concept.isEmpty()))
				return false;
		} else if (!concept.equals(other.concept))
			return false;
		if ((category == null) || (url.isEmpty())) {
			if ((other.category != null) && (!other.category.isEmpty()))
				return false;
		} else if (!category.equals(other.category))
			return false;

		if ((otherRelated == null) || otherRelated.isEmpty()) {
			if ((other.otherRelated != null) && (!other.otherRelated.isEmpty()))
				return false;
		} else if (!otherRelated.equals(other.otherRelated))
			return false;
		if ((sourceWebsite == null) || (sourceWebsite == SourceWebsite.None))
		{
			if ((other.sourceWebsite != null) && ( other.sourceWebsite != SourceWebsite.None))
				return false;
		} else if (sourceWebsite != other.sourceWebsite)
			return false;
		if ((keyDocument == null)|| keyDocument.isEmpty()) {
			if ((other.keyDocument != null) && (!other.keyDocument.isEmpty()))
				return false;
		} else if (!keyDocument.equals(other.keyDocument))
			return false;
		if ((url == null) || (url.isEmpty())) {
			if ((other.url != null) && (!other.url.isEmpty()))
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

}
