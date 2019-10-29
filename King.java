import javafx.scene.image.ImageView;

public class King extends Piece
{
    private ImageView image;
    public King(String color)
    {
        super(color, "king");
        if(color.equals("white"))
        {
            image = new ImageView("WhiteKing.png");
        }
        else
        {
            image = new ImageView("BlackKing.png");
        }
        image.setFitHeight(Main.pieceHeight);
        image.setFitWidth(Main.pieceHeight);
        image.setSmooth(true);
    }
    public ImageView getImage()
    {
        return image;
    }
}