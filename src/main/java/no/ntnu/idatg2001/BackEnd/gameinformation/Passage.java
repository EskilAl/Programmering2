package no.ntnu.idatg2001.BackEnd.gameinformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Passage {
  private String title;
  private String content;
  private List<Link> links;

  public Passage(String title, String content) {
    this.title = title;
    this.content = content;
    this.links = new ArrayList<>();
  }

  public String getTitle() {
    return title;
  }

  public String getContent() {
    return content;
  }

  public boolean addLink(Link link) throws IllegalArgumentException{
    boolean linkAdded = true;
    try {
      if(link == null) {
        throw new IllegalArgumentException();
      } else {
        getLinks().add(link);
      }
    } catch (Exception exception) {
      linkAdded = false;
    }
    return linkAdded;
  }

  public List<Link> getLinks() {
    return links;
  }

  public boolean hasLinks() {
    return (!this.links.isEmpty());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Passage passage = (Passage) o;
    return title.equals(passage.title) && content.equals(passage.content) && links.equals(
        passage.links);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, content, links);
  }

  @Override
  public String toString() {
    return "Passage{"
        + "title='" + title + '\''
        + ", content='" + content + '\''
        + ", links=" + links
        + '}';
  }
}
