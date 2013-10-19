package org.neo4j.examples.service;

import java.util.Set;

/**
 * Word object with its related words
 */
public class EntityData 
{	

	private String name;
	
	private Set<String> otherRelated;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<String> getOtherRelated() {
		return otherRelated;
	}
	public void setOtherRelated(Set<String> otherRelated) {
		this.otherRelated = otherRelated;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((otherRelated == null) ? 0 : otherRelated.hashCode());
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
		EntityData other = (EntityData) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (otherRelated == null) {
			if (other.otherRelated != null)
				return false;
		} else if (!otherRelated.equals(other.otherRelated))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "EntityData [name=" + name + ", otherRelated=" + otherRelated
				+ "]";
	}
	


}
