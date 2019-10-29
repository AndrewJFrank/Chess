import javafx.scene.image.ImageView;

public class Queen extends Piece
{
    private ImageView image;
    public Queen(String color)
    {
        super(color, "queen");
        if(color.equals("white"))
        {
            image = new ImageView("WhiteQueen.png");
        }
        else
        {
            image = new ImageView("BlackQueen.png");
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