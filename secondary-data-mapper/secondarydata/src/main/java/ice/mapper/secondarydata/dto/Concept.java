package ice.mapper.secondarydata.dto;

import java.io.Serializable;

public class Concept<T> implements Serializable {
  
  private static final long serialVersionUID = 1L;

  private String id;

  private String vocabulary;

  private String name;

  private T value;

  private String date;

  public Concept() {

  }

  public Concept(String id, String vocabulary, String name, T value, String date){
    super();
    this.id = id;
    this.vocabulary = vocabulary;
    this.name = name;
    this.value = value;
    this.date = date;
  }

  public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVocabulary() {
		return vocabulary;
	}

	public void setVocabulary(String vocabulary) {
		this.vocabulary = vocabulary;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}


}