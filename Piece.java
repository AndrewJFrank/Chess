import javafx.scene.image.ImageView;
public class Piece
{
    ImageView image;
    private String color;
    private String type;
    private boolean hasMoved = false;
    public Piece(String color, String type)
    {
        this.color = color;
        this.type = type;
    }
    public String getColor()
    {
        return color;
    }
    public ImageView getImage()
    {
        return image;
    }
    public String getType()
    {
        return type;
    }
    public boolean hasMoved()
    {
        return hasMoved;
    }
    public void setHasMoved(boolean hasMoved)
    {
        this.hasMoved = hasMoved;
    }
}