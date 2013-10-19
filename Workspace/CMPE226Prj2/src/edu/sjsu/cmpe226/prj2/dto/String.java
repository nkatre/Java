package edu.sjsu.cmpe226.prj2.dto;

import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * Word object with its related words
 */
public class String 
{	
	/**
	 * Named node to define the parts of speech of the word
	 * 
	 * Noun–is a word used to name a person, place, thing, or idea. A noun can be a proper noun or a common noun.
	 * Pronoun–is a word that replaces a person, place, thing, or idea. Pronouns can act as subjects or objects, and some can show possession.
	 * Adjective–is a word used to describe, or modify, a noun or a pronoun. An adjective describes “what kind,” “which one,” “how many,” or “how much.”
	 * Verb–is a word that shows action or that indicates a condition or a state of being.
	 * Adverb–is a word used to describe, or modify, a verb, an adjective, or another adverb. An adverb describes how, when, where, or to what extent the verb performs.
	 * Preposition–is a word used to show a relationship between a noun or a pronoun and some other word in the sentence. Prepositions often show direction, location, or time.
	 * Conjunction–is a word that connects other words or groups of words to each other. There are three types of conjunctions: coordinating, subordinating, and correlative.
	 * Interjection–is a word used to express emotion that has no grammatical relationship to other words in the sentence. Interjections should be used sparingly and usually only belong in narrative dialogue
	 */
	public enum PartsOfSpeech {		
		Noun, Pronoun, Adjective, Verb, Adverb, Preposition, Conjunction, Interjection, None
	}
	
	private String term;
	private PartsOfSpeech partsOfSpeech;
	private String meaning;
	private Set<String> synonyms;
	private Set<String> antonyms;
	private Set<String> homonyms;
	private Set<String> meronyms;
	private Set<String> holonyms;
	private Set<String> hypernyms;
	private Set<String> hyponyms;
	private Set<String> otherRelated;
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public PartsOfSpeech getPartsOfSpeech() {
		return partsOfSpeech;
	}
	public void setPartsOfSpeech(PartsOfSpeech partsOfSpeech) {
		this.partsOfSpeech = partsOfSpeech;
	}
	public String getMeaning() {
		return meaning;
	}
	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}
	public Set<String> getSynonyms() {
		return synonyms;
	}
	public void setSynonyms(Set<String> synonyms) {
		this.synonyms = synonyms;
	}
	public Set<String> getAntonyms() {
		return antonyms;
	}
	public void setAntonyms(Set<String> antonyms) {
		this.antonyms = antonyms;
	}
	public Set<String> getHomonyms() {
		return homonyms;
	}
	public void setHomonyms(Set<String> homonyms) {
		this.homonyms = homonyms;
	}
	public Set<String> getMeronyms() {
		return meronyms;
	}
	public void setMeronyms(Set<String> meronyms) {
		this.meronyms = meronyms;
	}
	public Set<String> getHolonyms() {
		return holonyms;
	}
	public void setHolonyms(Set<String> holonyms) {
		this.holonyms = holonyms;
	}
	public Set<String> getHypernyms() {
		return hypernyms;
	}
	public void setHypernyms(Set<String> hypernyms) {
		this.hypernyms = hypernyms;
	}
	public Set<String> getHyponyms() {
		return hyponyms;
	}
	public void setHyponyms(Set<String> hyponyms) {
		this.hyponyms = hyponyms;
	}
	public Set<String> getOtherRelated() {
		return otherRelated;
	}
	public void setOtherRelated(Set<String> otherRelated) {
		this.otherRelated = otherRelated;
	}
	@Override
	public String toString() {
		return "WordData [term=" + term + ", partsOfSpeech=" + partsOfSpeech
				+ ", meaning=" + meaning + ", synonyms=" + synonyms
				+ ", antonyms=" + antonyms + ", homonyms=" + homonyms
				+ ", meronyms=" + meronyms + ", holonyms=" + holonyms
				+ ", hypernyms=" + hypernyms + ", hyponyms=" + hyponyms
				+ ", otherRelated=" + otherRelated + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((antonyms == null) ? 0 : antonyms.hashCode());
		result = prime * result
				+ ((holonyms == null) ? 0 : holonyms.hashCode());
		result = prime * result
				+ ((homonyms == null) ? 0 : homonyms.hashCode());
		result = prime * result
				+ ((hypernyms == null) ? 0 : hypernyms.hashCode());
		result = prime * result
				+ ((hyponyms == null) ? 0 : hyponyms.hashCode());
		result = prime * result + ((meaning == null) ? 0 : meaning.hashCode());
		result = prime * result
				+ ((meronyms == null) ? 0 : meronyms.hashCode());
		result = prime * result
				+ ((otherRelated == null) ? 0 : otherRelated.hashCode());
		result = prime * result
				+ ((partsOfSpeech == null) ? 0 : partsOfSpeech.hashCode());
		result = prime * result
				+ ((synonyms == null) ? 0 : synonyms.hashCode());
		result = prime * result + ((term == null) ? 0 : term.hashCode());
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
		String other = (String) obj;
		if ((antonyms == null) || (antonyms.isEmpty())) {
			if ((other.antonyms != null) && (!other.antonyms.isEmpty()))
				return false;
		} else if (!antonyms.equals(other.antonyms))
			return false;
		if ((holonyms == null) || holonyms.isEmpty()) {
			if ((other.holonyms != null) && (! other.holonyms.isEmpty()))
				return false;
		} else if (!holonyms.equals(other.holonyms))
			return false;
		if ((homonyms == null) || homonyms.isEmpty()){
			if ((other.homonyms != null) && (!other.homonyms.isEmpty()))
				return false;
		} else if (!homonyms.equals(other.homonyms))
			return false;
		if ((hypernyms == null)|| hypernyms.isEmpty()) {
			if ((other.hypernyms != null) && (!other.hypernyms.isEmpty()))
				return false;
		} else if (!hypernyms.equals(other.hypernyms))
			return false;
		if ((hyponyms == null) || hyponyms.isEmpty()) {
			if ((other.hyponyms != null) && (!other.hyponyms.isEmpty()))
				return false;
		} else if (!hyponyms.equals(other.hyponyms))
			return false;
		if ((meaning == null) || (term.isEmpty())) {
			if ((other.meaning != null) && (!other.meaning.isEmpty()))
				return false;
		} else if (!meaning.equals(other.meaning))
			return false;
		if ((meronyms == null) || meronyms.isEmpty()) {
			if ((other.meronyms != null) && (!other.meronyms.isEmpty()))
				return false;
		} else if (!meronyms.equals(other.meronyms))
			return false;
		if ((otherRelated == null) || otherRelated.isEmpty()) {
			if ((other.otherRelated != null) && (!other.otherRelated.isEmpty()))
				return false;
		} else if (!otherRelated.equals(other.otherRelated))
			return false;
		if ((partsOfSpeech == null) || (partsOfSpeech == PartsOfSpeech.None))
		{
			if ((other.partsOfSpeech != null) && ( other.partsOfSpeech != PartsOfSpeech.None))
				return false;
		} else if (partsOfSpeech != other.partsOfSpeech)
			return false;
		if ((synonyms == null)|| synonyms.isEmpty()) {
			if ((other.synonyms != null) && (!other.synonyms.isEmpty()))
				return false;
		} else if (!synonyms.equals(other.synonyms))
			return false;
		if ((term == null) || (term.isEmpty())) {
			if ((other.term != null) && (!other.term.isEmpty()))
				return false;
		} else if (!term.equals(other.term))
			return false;
		return true;
	}
}
