package ntnu.no.idatg2001.entityinformation.playerclasses;

import ntnu.no.idatg2001.entityinformation.Entity;
import ntnu.no.idatg2001.entityinformation.PlayerClass;

public class Ranger extends Entity {

  public Ranger(String entityName) {
    super(100, 100, entityName, 0, 50);
    this.setEntityHealth(150);
    super.addToInventory("Bow");
    super.addToInventory("HealthPotion");
    super.addToInventory("Trap");
    super.addToInventory("Boots");
    this.setPlayerClass(PlayerClass.RANGER);
    super.setIntelligence(4);
    super.setDexterity(10);
    super.setStrength(3);
    super.setLuck(5);
  }
}