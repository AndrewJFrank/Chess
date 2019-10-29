import javafx.scene.image.ImageView;

public class Knight extends Piece
{
    private ImageView image;
    public Knight(String color)
    {
        super(color, "knight");
        if(color.equals("white"))
        {
            image = new ImageView("WhiteKnight.png");
        }
        else
        {
            image = new ImageView("BlackKnight.png");
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