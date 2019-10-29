import javafx.scene.image.ImageView;

public class Pawn extends Piece
{
    private ImageView image;
    public Pawn(String color)
    {
        super(color, "pawn");
        if(color.equals("white"))
        {
            image = new ImageView("WhitePawn.png");
        }
        else
        {
            image = new ImageView("BlackPawn.png");
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