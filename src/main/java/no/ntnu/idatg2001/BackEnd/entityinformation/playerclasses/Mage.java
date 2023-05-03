package no.ntnu.idatg2001.BackEnd.entityinformation.playerclasses;

import no.ntnu.idatg2001.BackEnd.entityinformation.Unit;

public class Mage extends Unit {
  public Mage(String entityName) {

    super(100, 100, entityName, 0, 100);
    super.addToInventory("Staff");
    super.addToInventory("Mana Potion");
    super.addToInventory("Mage Robe");
    super.setIntelligence(10);
    super.setDexterity(5);
    super.setStrength(2);
    super.setLuck(4);
  }



}
