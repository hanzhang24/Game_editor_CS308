package oogasalad.frontend.nodeEditor.Nodes.DraggableNodes;

import oogasalad.frontend.nodeEditor.NodeController;

public class EndNestNode extends DraggableAbstractNode {

  public EndNestNode(NodeController nodeController, double x, double y,
      double width, double height, String color) {
    super(nodeController, x, y, 0, width, height, color);
  }

  @Override
  public String getJSONString() {
    return "]";
  }

  @Override
  protected void setContent() {

  }

  
}
