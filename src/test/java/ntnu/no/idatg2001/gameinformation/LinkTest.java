package ntnu.no.idatg2001.gameinformation;

import static org.junit.jupiter.api.Assertions.*;

import ntnu.no.idatg2001.actions.Action;
import ntnu.no.idatg2001.actions.GoldAction;
import ntnu.no.idatg2001.entityinformation.Entity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class LinkTest {
  Link link;

  @BeforeEach
  void setup() {
    link = new Link("testText", "testReference");
  }

  @AfterEach
  void tearDown() {
    link = null;
  }

  @Nested
  class addActionTest {
    @Test
    void validActionAddedDoesNotThrowException() {
      Action testAction = new GoldAction(10);
      assertTrue(link.addAction(testAction));
      assertTrue(link.getActions().contains(testAction));
    }

    @Test
    void notValidActionAddedThrowsException() {
      assertFalse(link.addAction(null));
      assertTrue(link.getActions().isEmpty());
    }
  }
}