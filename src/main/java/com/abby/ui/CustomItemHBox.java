package com.abby.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import com.abby.main.ExperimentItem;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome.FontAwesome;


// HBox class for easily making editable and deletable items for TreeViews etc

public class CustomItemHBox extends HBox implements TreeDisplayable {
    private ExperimentItem experimentItem;
    private Label text;
    private Button editButton;
    private Button deleteButton;

    public CustomItemHBox(ExperimentItem expmnt) {
        super(5);
        this.experimentItem = expmnt;
        this.text = new Label(expmnt.getText());
        this.editButton = new Button();
        editButton.setGraphic(new FontIcon(FontAwesome.EDIT));
        this.deleteButton = new Button();
        deleteButton.setGraphic(new FontIcon(FontAwesome.TRASH));
        // set layout of hbox
        HBox.setHgrow(text, Priority.ALWAYS);  
        text.setMaxWidth(Double.MAX_VALUE);  
        this.getChildren().addAll(text, editButton, deleteButton);
    }

    @Override
    public String getText() {
        return this.text.getText();
    }
    public ExperimentItem getExperimentItem() {
        return this.experimentItem;
    }
    public Button getEditButton() {
        return this.editButton;
    }
    public Button getDeleteButton() {
        return this.deleteButton;
    }
}
