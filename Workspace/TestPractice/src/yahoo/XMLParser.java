package yahoo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/*
 * 
 * You are required to parse the xml file: 
<ledger> <person> <name>Jai</name><location>Bangalore</location> </person> <entries> <entry><day>1</day><credit>50</credit><debit>40</debit></entry> 
É. 
É 
multiple entries were there, and multiple people were there. 
We were required to validate the XML file.Open and Close tags matching. 
We were required to parse, maintain the max balance for each person, 
the longest span of days each person had the max balance, and report queries such as 
who had the overall max balance , his span and location. 
Span must contain the day numbers, not length.
 */

public class XMLParser {

	File _file;
	Scanner _scanner;
	List<Tag> tags;
	
	


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_file == null) ? 0 : _file.hashCode());
		result = prime * result
				+ ((_scanner == null) ? 0 : _scanner.hashCode());
		result = prime * result + ((tags == null) ? 0 : tags.hashCode());
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
		XMLParser other = (XMLParser) obj;
		if (_file == null) {
			if (other._file != null)
				return false;
		} else if (!_file.equals(other._file))
			return false;
		if (_scanner == null) {
			if (other._scanner != null)
				return false;
		} else if (!_scanner.equals(other._scanner))
			return false;
		if (tags == null) {
			if (other.tags != null)
				return false;
		} else if (!tags.equals(other.tags))
			return false;
		return true;
	}

	public class MalformedXMLException extends Exception {

		public MalformedXMLException() {
			super();
		}

		public MalformedXMLException(String arg0, Throwable arg1) {
			super(arg0, arg1);
		}

		public MalformedXMLException(String arg0) {
			super(arg0);
		}

		public MalformedXMLException(Throwable arg0) {
			super(arg0);
		}
	}

	public XMLParser(String fileName) throws FileNotFoundException {
		super();
		this._file = new File(fileName);
		_scanner = new Scanner(this._file);
	}

	public void parse() {

		while(_scanner.hasNextLine()) {
			System.out.println(_scanner.nextLine());
		}

	}

	public class Tag implements Iterable {

		String name;

		boolean closed;

		String value;

		List<Tag> children = new LinkedList<Tag>();

		boolean isValid;

		@Override
		public String toString() {
			return "Tag [name=" + name + ", closed=" + closed + ", value="
					+ value + ", children=" + children + ", isValid=" + isValid
					+ "]";
		}

		public Tag(String name, String value) {
			super();
			this.name = name;
			this.value = value;
		}

		public Tag(String name,  String value, boolean closed) {
			super();
			this.name = name;
			this.value = value;
			this.closed = closed;
		}

		public Tag(String name, boolean closed) {
			super();
			this.name = name;
			this.closed = closed;
		}

		public boolean isParent() {
			if (children.isEmpty())
				return false;
			return true;
		}

		public boolean isValid() {
			if((null != value) && (this.isParent()))
				return false;
			return true;
		}

		public boolean addChild(Tag tag) {

			if (null != tag) {
				children.add(tag);
				return true;
			}

			return false;
		}

		public boolean addChild(Tag tag, int index) {

			if (null != tag) {
				children.add(index, tag);
				return true;
			}

			return false;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public boolean isClosed() {
			return closed;
		}

		public void setClosed(boolean closed) {
			this.closed = closed;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public List<Tag> getChildren() {
			return children;
		}

		public void setChildren(List<Tag> children) {
			this.children = children;
		}

		@Override
		public Iterator<Tag> iterator() {
			Iterator<Tag> iChildren = children.iterator();
	        return iChildren;
		}

	}

	public List<Tag> convertAsTagObject(String line) throws MalformedXMLException {

		if(line.isEmpty() || (null == line) || (line.trim().equals("")))
			return null;

		String key;
		String value = null;
		StringBuilder sbuilder = new StringBuilder(line);

		Tag tag = null;
		List<Tag> tags = new LinkedList<Tag>();

		try{

			int beginIndex = sbuilder.indexOf("<");
			int endIndex = sbuilder.indexOf(">");

			while((beginIndex != -1) && (endIndex != -1)) {
				beginIndex = sbuilder.indexOf("<");
				endIndex = sbuilder.indexOf(">");

				//if(beginIndex > endIndex) {
				//throw new MalformedXMLException("the xml is not properly formed");
				//}

				if((beginIndex != -1) && (endIndex != -1)) {
					key = sbuilder.substring(beginIndex+1, endIndex);			

					sbuilder = new StringBuilder(sbuilder.substring(endIndex+1));

					if(sbuilder.indexOf("<") != -1) {
						value = sbuilder.substring(0, sbuilder.indexOf("<"));

						if((null != tag) && (tag.getName().equals(key.replace("/", "")))) {
							tag.setClosed(true);
						}
						key = key.replace("/", "");
					}

					if((tag != null) && (sbuilder.indexOf("<") !=-1) &&  (sbuilder.indexOf(">") !=-1)
							&& (tag.getName().equals(sbuilder.substring(sbuilder.indexOf("<"), sbuilder.indexOf(">")).replace("/", "")))) {
						tag.setClosed(true);

					}
					
					key = key.replace("/", "");
					
					if (null != tag) {
						if((tag.getName().equals(key)) ) {
							tag.setClosed(true);
						} else {
							
							Tag g = getDescendantWithTheName(tag, key);
							
							if(null != g) {
								g.setClosed(true);
							} else {
								tag.addChild(new Tag(key, value));
							};
						}

					} else {
						tag = new Tag(key, value, false);
					}

					if(tag.isClosed() && !sbuilder.equals("")) {
						List<Tag> list = convertAsTagObject(sbuilder.toString());
						if(null != list)
							tags.addAll(list);
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		boolean canAdd = true;
		for(Tag g: tags) {
			if(g.getName().equals(tag.getName())) {
				canAdd = false;
			}
		}
		if(canAdd) {
			tags.add(tag);
		}

		return tags;
	}
	
	public Tag getDescendantWithTheName(Tag tag, String key) {
		List<Tag> children = tag.getChildren();
		Tag tg = null;
		
		if(null != children) {
			
			for(Tag t : tag.getChildren()) {
				if(t.getName().equals(key)) {
					tg = t;
				} else {
					tg = getDescendantWithTheName(t, key);
				}
			}
		} 
		return tg;
	}

	public static void main(String[] args) throws MalformedXMLException {
		try {
			XMLParser parser = new XMLParser("xmltest.txt");
			String s = "<ledger> <person> <name>Jai</name><location>Bangalore</location> </person> <entries> <entry><day>1</day><credit>50</credit><debit>40</debit></entry><entries>";

			System.out.println(parser.convertAsTagObject(s));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

}
