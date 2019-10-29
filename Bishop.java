import javafx.scene.image.ImageView;

public class Bishop extends Piece
{
    private ImageView image;
    public Bishop(String color)
    {
        super(color, "bishop");
        if(color.equals("white"))
        {
            image = new ImageView("WhiteBishop.png");
        }
        else
        {
            image = new ImageView("BlackBishop.png");
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