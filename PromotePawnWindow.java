import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import java.util.Hashtable;

public class PromotePawnWindow
{
    public static void display(Group board, String color, String location, Hashtable<String, TileProperty> tileArray)
    {
        Stage window = new Stage();
        window.setTitle("Promote Your Pawn");
        window.initModality(Modality.APPLICATION_MODAL);
        window.setOnCloseRequest(e -> e.consume());
        int pawnSize = 150;
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(30, 25, 30, 25));
        HBox textHolder = new HBox();
        Text promoteText = new Text("Promote Your Pawn");
        promoteText.setFont(Font.font(30));
        promoteText.setTextAlignment(TextAlignment.CENTER);
        textHolder.getChildren().add(promoteText);
        textHolder.setAlignment(Pos.CENTER);
        VBox center = new VBox(40);
        ComboBox<String> choice = new ComboBox<>();
        choice.getItems().addAll(color + "Queen", color + "Bishop", color + "Knight", color + "Rook");
        choice.setValue(color + "Queen");
        choice.setCellFactory(param -> new MyListCell());
        choice.setButtonCell(new MyListCell());
        ImageView arrow = new ImageView("Arrow.png");
        arrow.setRotate(180);
        arrow.setFitWidth(arrow.getImage().getWidth() / 4.7);
        arrow.setFitHeight(arrow.getImage().getHeight() / 4.7);
        center.getChildren().addAll(choice, arrow);
        center.setAlignment(Pos.CENTER);
        HBox imageHolder = new HBox();
        ImageView image;
        if(color.equals("white"))
        {
            image = new ImageView("WhitePawn.png");
        }
        else
        {
            image = new ImageView("BlackPawn.png");
        }
        image.setFitHeight(pawnSize);
        image.setFitWidth(pawnSize);
        imageHolder.getChildren().add(image);
        imageHolder.setAlignment(Pos.BOTTOM_CENTER);
        HBox image2Holder = new HBox();
        ImageView image2;
        if(color.equals("white"))
        {
            image2 = new ImageView("WhiteQueen.png");
        }
        else
        {
            image2 = new ImageView("BlackQueen.png");
        }
        image2.setFitHeight(pawnSize);
        image2.setFitWidth(pawnSize);
        image2Holder.getChildren().add(image2);
        image2Holder.setAlignment(Pos.BOTTOM_CENTER);
        HBox buttonHolder = new HBox();
        Button continuePlaying = new Button("Continue Playing");
        continuePlaying.setStyle("-fx-font-size: 15px;");
        continuePlaying.setAlignment(Pos.CENTER);
        continuePlaying.setPadding(new Insets(10));
        buttonHolder.getChildren().add(continuePlaying);
        buttonHolder.setAlignment(Pos.CENTER);
        layout.setTop(textHolder);
        layout.setMargin(textHolder, new Insets(0, 0, 30, 0));
        layout.setCenter(center);
        layout.setMargin(center, new Insets(0, 0, 40, 0));
        layout.setRight(image2Holder);
        layout.setLeft(imageHolder);
        layout.setBottom(buttonHolder);
        layout.setMargin(buttonHolder, new Insets(30, 0, 0, 0));
        choice.setOnAction(e ->
        {
            System.out.println(choice.getValue());
            ImageView imageView = new ImageView(choice.getValue() + ".png");
            image2.setImage(imageView.getImage());
        });
        continuePlaying.setOnAction(e ->
        {
            Piece piece;
            boolean rotate = false;
            board.getChildren().remove(tileArray.get(location).getPiece().getImage());
            if(choice.getValue().equals(color + "Queen"))
            {
                piece = new Queen(color);
                piece.setHasMoved(true);
                if(color.equals("black"))
                {
                    rotate = true;
                }
                Main.addPiece(board, location, piece, rotate);
            }
            else if(choice.getValue().equals(color + "Bishop"))
            {
                piece = new Bishop(color);
                piece.setHasMoved(true);
                if(color.equals("black"))
                {
                    rotate = true;
                }
                Main.addPiece(board, location, piece, rotate);
            }
            else if(choice.getValue().equals(color + "Knight"))
            {
                piece = new Knight(color);
                piece.setHasMoved(true);
                if(color.equals("black"))
                {
                    rotate = true;
                }
                Main.addPiece(board, location, piece, rotate);
            }
            else if(choice.getValue().equals(color + "Rook"))
            {
                piece = new Rook(color);
                piece.setHasMoved(true);
                if(color.equals("black"))
                {
                    rotate = true;
                }
                Main.addPiece(board, location, piece, rotate);
            }
            if(color.equals("white"))
            {
                Main.rotate(board);
            }
            window.close();
        });
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}