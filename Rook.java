import javafx.scene.image.ImageView;

public class Rook extends Piece
{
    private ImageView image;
    public Rook(String color)
    {
        super(color, "rook");
        if(color.equals("white"))
        {
            image = new ImageView("WhiteRook.png");
        }
        else
        {
            image = new ImageView("BlackRook.png");
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