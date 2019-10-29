import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

class MyListCell extends ListCell<String> {
    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty)
            setGraphic(null);
        else {
            ImageView imageView = new ImageView(item + ".png");
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            setGraphic(new HBox(imageView, new Text(item.substring(5))));
        }
        setText("");
    }
}