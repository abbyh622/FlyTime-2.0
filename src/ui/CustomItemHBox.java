package ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import main.Experiment;
import main.ExperimentItem;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;


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
        this.editButton = GlyphsDude.createIconButton(FontAwesomeIcon.EDIT);
        this.deleteButton = GlyphsDude.createIconButton(FontAwesomeIcon.TRASH);
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



    // delete these probably
    public CustomItemHBox(Label text) {
        super(5);
        this.text = text;
        this.editButton = GlyphsDude.createIconButton(FontAwesomeIcon.EDIT);
        this.deleteButton = GlyphsDude.createIconButton(FontAwesomeIcon.TRASH);
    }
    public CustomItemHBox(String text) {
        super(5);
        this.text = new Label(text);
        this.editButton = GlyphsDude.createIconButton(FontAwesomeIcon.EDIT);
        this.deleteButton = GlyphsDude.createIconButton(FontAwesomeIcon.TRASH);
    }

}
