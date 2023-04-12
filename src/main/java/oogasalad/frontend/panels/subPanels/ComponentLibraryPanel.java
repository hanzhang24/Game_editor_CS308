package oogasalad.frontend.panels.subPanels;

import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import oogasalad.frontend.factories.ButtonFactory;
import oogasalad.frontend.panels.AccordionPanel;
import oogasalad.frontend.panels.Panel;

public class ComponentLibraryPanel extends AccordionPanel {
  ButtonFactory buttonFactory = new ButtonFactory();

  /**
   * Constructor for HeaderMenu
   */
  public ComponentLibraryPanel() {
    super();
  }
  /**
   * Creates the menu for the header
   * @return
   */
  public Accordion createAccordion() {
    TitledPane t1 = new TitledPane("Game Objects", new Button("B1"));
    TitledPane t2 = new TitledPane("Players", new Button("B2"));
    TitledPane t3 = new TitledPane("Displayable", new Button("B3"));
    Accordion accordion = new Accordion();
    accordion.getPanes().addAll(t1, t2, t3);
    return accordion;
  }

  @Override
  public Panel makePanel() {
    return null;
  }

  @Override
  public Panel refreshPanel() {
    return null;
  }

  @Override
  public String getTitle() {
    return null;
  }

  @Override
  public void save() {

  }
}
