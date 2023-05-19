package no.ntnu.idatg2001.backend.gameinformation;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import no.ntnu.idatg2001.backend.actions.Action;

@Entity
@Table(name = "story")
public class Story {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "story_id")
  private Map<Link, Passage> passages;
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "opening_passage_id")
  private Passage openingPassage;


  private String title;

  public Story(String title, Passage openingPassage) {
    this.title = title;
    this.openingPassage = openingPassage;
    this.passages = new HashMap<>();
  }

  public Story(Story story) {
    this.title = story.getTitle();
    this.openingPassage = story.getOpeningPassage();
    this.passages = story.getPassages();
  }

  public Story() {}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Story setTitle(String title) {
    this.title = title;
    return this;
  }

  public String getTitle() {
    return title;
  }


  public void setOpeningPassage(Passage openingPassage) {
    this.openingPassage = openingPassage;
  }

  public Passage getOpeningPassage() {
    return openingPassage;
  }

  public boolean addPassage(Passage passage) {
    if (getPassages().containsValue(passage)) {
      return false;
    } else {
      getPassages().put(new Link(passage.getTitle(), passage.getTitle()), passage);
      return true;
    }
  }

  //TODO må sjekke om detter rett.
  /*
  public void removePassage(Link link) {
    for (Entry<Link, Passage> entry : passages.entrySet()) {
      if (entry.getValue().getLinks().size() == 1
      && entry.getValue().getLinks().contains(link)) {
        passages.remove(link);
      }
    }
  }
   */

  public void removePassage(Passage passage) {
    List<Link> linksToRemove = new ArrayList<>();

    for (Entry<Link, Passage> entry : passages.entrySet()) {
      Passage currentPassage = entry.getValue();
      if (currentPassage.equals(passage)) {
        linksToRemove.addAll(currentPassage.getLinks());
      }
    }

    for (Link link : linksToRemove) {
      passages.remove(link);
    }
    // Remove the passage from the passages map
    passages.values().remove(passage);
  }

  //TODO må sjekke om detter rett.
  public List<Link> getBrokenLinks() {
    ArrayList<Link> brokenLinks = new ArrayList<>();
    for (Entry<Link, Passage> entry : passages.entrySet()) {
      if (!entry.getKey().getReference().equals(entry.getValue().getTitle())) {
        brokenLinks.add(entry.getKey());
      }
    }
    System.out.println(brokenLinks);
    return brokenLinks;
  }

  public Passage getPassage(Link link) {
    return getPassages().get(link);
  }

  public Map<Link, Passage> getPassages() {
    return passages;
  }

  /**
   * This method returns a string representation of the Story object.
   * @return a string representation of the Story object.
   */
  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(String.format("Story{"
        + "Title=%s,"
        + "OpeningPassage=%s", title, openingPassage));
    for (Passage passage : passages.values()) {
      stringBuilder.append(String.format("%nPassage{%nTitle=%s", passage.getTitle()));
      for (Link link : passage.getLinks()) {
        stringBuilder.append(String.format("%nLink{%nText=%s%nReference=%s%n",
            link.getText(), link.getReference()));
        for (Action action : link.getActions()) {
          stringBuilder.append(String.format("%nAction{%nText=%s%nType=%s}}%n",
              action.getActionType(), action.getValue()));
        }
      }
    }
    stringBuilder.append("}");
    return stringBuilder.toString();
  }

  /**
   * This method counts the amount of passages in Story.
   * @return the amount of passages in the story as a int.
   */
  public int getTotalAmountOfPassages() {
    List<Passage> passageList = new ArrayList<>();
    for (Passage storyPassage: passages.values()) {
      if (!passageList.contains(storyPassage)) {
        passageList.add(storyPassage);
      }
    }
    if (getOpeningPassage() != null) {
      passageList.add(getOpeningPassage());
    }
    return passageList.size();
  }

  /**
   * This method counts the amount of links in passages in Story.
   * @return the amount of links in passages in the story as an int.
   */
  public int getTotalAmountOfPassagesLinks() {
    List<Link> linkList = new ArrayList<>();

    for (Passage passage : passages.values()) {
      passage.getLinks().forEach(link -> {
        if (!linkList.contains(link)) {
          linkList.add(link);
        }
      });
    }
    return linkList.size();
  }
}