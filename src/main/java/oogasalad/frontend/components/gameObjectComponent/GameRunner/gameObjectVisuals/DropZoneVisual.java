package oogasalad.frontend.components.gameObjectComponent.GameRunner.gameObjectVisuals;

import javafx.scene.Node;

public class DropZoneVisual extends AbstractSelectableVisual {
    private Node unselectedVisual;
    private Node selectedVisual;
    public DropZoneVisual(Node dropZoneImage,Node selectedDropZoneImage, double width, double height, double x, double y,String id) {
        super(id);
        unselectedVisual = dropZoneImage;
        selectedVisual = selectedDropZoneImage;
        initHBox(width,height,x,y);
        this.getChildren().add(unselectedVisual);
    }
    private void initHBox(double width, double height, double x, double y){
        this.setPrefHeight(height);
        this.setPrefWidth(width);
        this.setMaxHeight(height);
        this.setMaxWidth(width);
        this.setLayoutX(x);
        this.setLayoutY(y);
    }

    @Override
    public void showClickable() {
        switchImages(unselectedVisual,selectedVisual);
    }

    @Override
    public void showUnclickable() {
        switchImages(selectedVisual,unselectedVisual);
    }

    @Override
    public void updateClickableVisual(Node newVisual) {
        switchImages(unselectedVisual,newVisual);
    }

    @Override
    public void updateUnClickableVisual(Node newVisual) {
        switchImages(selectedVisual,newVisual);
    }
}
