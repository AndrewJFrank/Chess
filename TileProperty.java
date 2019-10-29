public class TileProperty
{
    private static char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private Position position;
    private Piece piece;
    public TileProperty(int x, int y, Piece piece)
    {
        position = new Position(x, y);
        this.piece = piece;
    }
    public Piece getPiece()
    {
        return piece;
    }
    public void setPiece(Piece piece)
    {
        this.piece = piece;
    }
    public Position getPosition()
    {
        return position;
    }
    public boolean hasPiece()
    {
        if(piece == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    public void printInfo()
    {
        System.out.print("Tile: " + alphabet[position.getX()] + Integer.toString(8 - position.getY()));
        if(!hasPiece())
        {
            System.out.println(", Piece: none");
        }
        else
        {
            System.out.println(", Piece: " + piece.getType() + " - " + piece.getColor());
        }
    }
}