//https://commons.wikimedia.org/wiki/Template:SVG_chess_pieces
import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

public class Main extends Application
{
    private static int blockHeight = 120; //120
    public static int pieceHeight = 120; //120
    private static int animationTime = 2000; //200
    private static boolean forceLegalMoves = true; //true
    private static char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static Hashtable<String, TileProperty> tileArray = new Hashtable();
    private static Rectangle[][] blockArray = new Rectangle[8][8];
    private static ArrayList<String> possibleMoves;
    private static boolean move = true;
    private static boolean upsideDown = false;
    private static boolean canCastle = false;
    private static boolean whiteKingDead = false;
    private static boolean blackKingDead = false;
    private static RotateTransition rtBoard = new RotateTransition();
    private static TranslateTransition trPiece = new TranslateTransition();
    private static String loc;
    public static void main(String[] args)
    {
        launch(args);
    }
    public void start(Stage window)
    {
        Group board = new Group();
        for(int x = 0; x < 8; x++)
        {
            for(int y = 0; y < 8; y++)
            {
                Rectangle r;
                if(x % 2 == 0)
                {
                    if(y % 2 == 0)
                    {
                        r = new Rectangle(blockHeight, blockHeight, Color.WHITE);
                    }
                    else
                    {
                        r = new Rectangle(blockHeight, blockHeight, Color.GRAY);
                    }
                }
                else
                {
                    if(y % 2 == 0)
                    {
                        r = new Rectangle(blockHeight, blockHeight, Color.GRAY);
                    }
                    else
                    {
                        r = new Rectangle(blockHeight, blockHeight, Color.WHITE);
                    }
                }
                board.getChildren().add(r);
                r.setX(x * blockHeight);
                r.setY(y * blockHeight);
                blockArray[x][y] = r;

                //Putting pieces on board

                tileArray.put(alphabet[x] + Integer.toString(8 - y), new TileProperty(x, y, null));
                if(y == 1)
                {
                    addPiece(board, alphabet[x] + Integer.toString(8 - y), new Pawn("black"), false);
                }
                else if(y == 6)
                {
                    addPiece(board, alphabet[x] + Integer.toString(8 - y), new Pawn("white"), false);
                }
                else if(y == 0)
                {
                    if(x == 0 || x == 7)
                    {
                        addPiece(board, alphabet[x] + Integer.toString(8 - y), new Rook("black"), false);
                    }
                    if(x == 1 || x == 6)
                    {
                        addPiece(board, alphabet[x] + Integer.toString(8 - y), new Knight("black"), false);
                    }
                    if(x == 2 || x == 5)
                    {
                        addPiece(board, alphabet[x] + Integer.toString(8 - y), new Bishop("black"), false);
                    }
                    if(x == 3)
                    {
                        addPiece(board, alphabet[x] + Integer.toString(8 - y), new Queen("black"), false);
                    }
                    if(x == 4)
                    {
                        addPiece(board, alphabet[x] + Integer.toString(8 - y), new King("black"), false);
                    }
                }
                else if(y == 7)
                {
                    if(x == 0 || x == 7)
                    {
                        addPiece(board, alphabet[x] + Integer.toString(8 - y), new Rook("white"), false);
                    }
                    if(x == 1 || x == 6)
                    {
                        addPiece(board, alphabet[x] + Integer.toString(8 - y), new Knight("white"), false);
                    }
                    if(x == 2 || x == 5)
                    {
                        addPiece(board, alphabet[x] + Integer.toString(8 - y), new Bishop("white"), false);
                    }
                    if(x == 3)
                    {
                        addPiece(board, alphabet[x] + Integer.toString(8 - y), new Queen("white"), false);
                    }
                    if(x == 4)
                    {
                        addPiece(board, alphabet[x] + Integer.toString(8 - y), new King("white"), false);
                    }
                }
                else
                {

                }
            }
        }
        board.setOnMouseClicked(e ->
        {
            move = !move;
            System.out.println("(" + e.getX() + ", " + e.getY() + ")");
            for(int x = 0; x < 8; x++)
            {
                for(int y = 0; y < 8; y++)
                {
                    if(e.getX() >= blockArray[x][y].getX() && e.getX() < blockArray[x][y].getX() + blockHeight && e.getY() >= blockArray[x][y].getY() && e.getY() < blockArray[x][y].getY() + blockHeight && !whiteKingDead && !blackKingDead)
                    {
                        if(!move)
                        {
                            loc = alphabet[x] + Integer.toString(8 - y);
                            if(tileArray.get(loc).hasPiece() && ((!upsideDown && tileArray.get(loc).getPiece().getColor().equals("white")) || (upsideDown && tileArray.get(loc).getPiece().getColor().equals("black"))) && !rtBoard.getStatus().equals(Animation.Status.RUNNING) && !trPiece.getStatus().equals(Animation.Status.RUNNING))
                            {
                                blockArray[x][y].setStroke(Color.GREEN);
                                blockArray[x][y].setStrokeType(StrokeType.INSIDE);
                                blockArray[x][y].setStrokeWidth(blockHeight / 12);
                                createLegalMoves(x, y);
                            }
                            else
                            {
                                move = !move;
                            }
                        }
                        else
                        {
                            boolean canMove = false;
                            if(!forceLegalMoves)
                            {
                                canMove = true;
                            }
                            for(int z = 0; z < possibleMoves.size(); z++)
                            {
                                if(possibleMoves.get(z).equals(alphabet[x] + Integer.toString(8 - y)))
                                {
                                    canMove = true;
                                }
                            }
                            if(tileArray.get(alphabet[x] + Integer.toString(8 - y)).hasPiece())
                            {
                                if(loc.equals(alphabet[x] + Integer.toString(8 - y)))
                                {
                                    blockArray[tileArray.get(loc).getPosition().getX()][tileArray.get(loc).getPosition().getY()].setStroke(null);
                                    for(int z = 0; z < possibleMoves.size(); z++)
                                    {
                                        blockArray[tileArray.get(possibleMoves.get(z)).getPosition().getX()][tileArray.get(possibleMoves.get(z)).getPosition().getY()].setStroke(null);
                                    }
                                }
                                else if(tileArray.get(loc).getPiece().getColor().equals(tileArray.get(alphabet[x] + Integer.toString(8 - y)).getPiece().getColor()))
                                {
                                    blockArray[tileArray.get(loc).getPosition().getX()][tileArray.get(loc).getPosition().getY()].setStroke(null);
                                    for(int z = 0; z < possibleMoves.size(); z++)
                                    {
                                        blockArray[tileArray.get(possibleMoves.get(z)).getPosition().getX()][tileArray.get(possibleMoves.get(z)).getPosition().getY()].setStroke(null);
                                        System.out.println("yes");
                                    }
                                    blockArray[x][y].setStroke(Color.GREEN);
                                    blockArray[x][y].setStrokeType(StrokeType.INSIDE);
                                    blockArray[x][y].setStrokeWidth(blockHeight / 12);
                                    loc = alphabet[x] + Integer.toString(8 - y);
                                    createLegalMoves(x, y);
                                    move = !move;
                                }
                                else if(canMove == true)
                                {
                                    if(tileArray.get(alphabet[x] + Integer.toString(8 - y)).getPiece().getType().equals("king"))
                                    {
                                        if(tileArray.get(alphabet[x] + Integer.toString(8 - y)).getPiece().getColor().equals("white"))
                                        {
                                            whiteKingDead = true;
                                        }
                                        else if(tileArray.get(alphabet[x] + Integer.toString(8 - y)).getPiece().getColor().equals("black"))
                                        {
                                            blackKingDead = true;
                                        }
                                    }
                                    blockArray[tileArray.get(loc).getPosition().getX()][tileArray.get(loc).getPosition().getY()].setStroke(null);
                                    for(int z = 0; z < possibleMoves.size(); z++)
                                    {
                                        blockArray[tileArray.get(possibleMoves.get(z)).getPosition().getX()][tileArray.get(possibleMoves.get(z)).getPosition().getY()].setStroke(null);
                                    }
                                    if(!whiteKingDead && !blackKingDead)
                                    {
                                        movePiece(board, loc, alphabet[x] + Integer.toString(8 - y), true);
                                    }
                                    else
                                    {
                                        movePiece(board, loc, alphabet[x] + Integer.toString(8 - y), false);
                                    }
                                    if(!upsideDown)
                                    {
                                        window.setTitle("Chess - Andrew Frank - Black's Turn");
                                    }
                                    else
                                    {
                                        window.setTitle("Chess - Andrew Frank - White's Turn");
                                    }
                                }
                                else
                                {
                                    move = !move;
                                }
                                System.out.println("Can Move: " + canMove);
                            }
                            else if(canMove == true)
                            {
                                blockArray[tileArray.get(loc).getPosition().getX()][tileArray.get(loc).getPosition().getY()].setStroke(null);
                                for(int z = 0; z < possibleMoves.size(); z++)
                                {
                                    blockArray[tileArray.get(possibleMoves.get(z)).getPosition().getX()][tileArray.get(possibleMoves.get(z)).getPosition().getY()].setStroke(null);
                                }
                                movePiece(board, loc, alphabet[x] + Integer.toString(8 - y), true);
                                if(!upsideDown)
                                {
                                    window.setTitle("Chess - Andrew Frank - Black's Turn");
                                }
                                else
                                {
                                    window.setTitle("Chess - Andrew Frank - White's Turn");
                                }
                            }
                            else
                            {
                                move = !move;
                            }
                        }
                        tileArray.get(alphabet[x] + Integer.toString(8 - y)).printInfo();
                    }
                }
            }
        });
        Scene scene = new Scene(board);
        window.setScene(scene);
        window.setTitle("Chess - Andrew Frank - White's Turn");
        window.show();
    }
    public static void addPiece(Group board, String location, Piece piece, boolean rotate)
    {
        tileArray.get(location).setPiece(piece);
        board.getChildren().add(piece.getImage());
        piece.getImage().setX(tileArray.get(location).getPosition().getX() * blockHeight);
        piece.getImage().setY(tileArray.get(location).getPosition().getY() * blockHeight);
        if(rotate)
        {
            System.out.println("PIECE SHOULD BE ROTATED");
            piece.getImage().setRotate(piece.getImage().getRotate() + 180);
            rotate(board);
        }
    }
    public static void movePiece(Group board, String old, String current, boolean rotate)
    {
        if(old.equals(current))
        {

        }
        else if(tileArray.get(old).hasPiece())
        {
            boolean promotePawn = false;
            if(canCastle && tileArray.get(old).getPiece().getType().equals("king"))
            {
                if(current.equals("c1"))
                {
                    movePiece(board, "a1", "d1", false);
                }
                if(current.equals("g1"))
                {
                    movePiece(board, "h1", "f1", false);
                }
                if(current.equals("c8"))
                {
                    movePiece(board, "a8", "d8", false);
                }
                if(current.equals("g8"))
                {
                    movePiece(board, "h8", "f8", false);
                }
            }
            trPiece = new TranslateTransition();
            trPiece.setDuration(Duration.millis(animationTime / 4));
            trPiece.setNode(tileArray.get(old).getPiece().getImage());
            trPiece.setByX(tileArray.get(current).getPosition().getX() * blockHeight - tileArray.get(old).getPosition().getX() * blockHeight);
            trPiece.setByY(tileArray.get(current).getPosition().getY() * blockHeight - tileArray.get(old).getPosition().getY() * blockHeight);
            trPiece.setCycleCount(1);
            trPiece.setAutoReverse(false);
            trPiece.play();
            if(tileArray.get(current).hasPiece())
            {
                board.getChildren().remove(tileArray.get(current).getPiece().getImage());
            }
            tileArray.get(current).setPiece(tileArray.get(old).getPiece());
            board.getChildren().remove(tileArray.get(old).getPiece().getImage());
            board.getChildren().add(tileArray.get(current).getPiece().getImage());
            tileArray.get(old).setPiece(null);
            tileArray.get(current).getPiece().setHasMoved(true);
            if(tileArray.get(current).getPiece().getType().equals("pawn"))
            {
                if(tileArray.get(current).getPiece().getColor().equals("white") && tileArray.get(current).getPosition().getY() == 0 && !whiteKingDead && !blackKingDead)
                {
                    promotePawn = true;
                    PromotePawnWindow.display(board, "white", current, tileArray);
                }
                else if(tileArray.get(current).getPiece().getColor().equals("black") && tileArray.get(current).getPosition().getY() == 7 && !whiteKingDead && !blackKingDead)
                {
                    promotePawn = true;
                    PromotePawnWindow.display(board, "black", current, tileArray);
                }
            }
            if(whiteKingDead)
            {
                Text win = new Text("Black Wins!");
                win.setFont(new Font(180));
                win.setX(25);
                win.setY(blockHeight * 4 + blockHeight / 1.5);
                win.setFill(Color.BLACK);
                win.setStyle("-fx-stroke: white; -fx-stroke-width: 5;");
                win.setRotate(win.getRotate() + 180);
                board.getChildren().add(win);
                RotateTransition rtWin = new RotateTransition();
                rtWin.setDuration(Duration.millis(animationTime));
                rtWin.setNode(win);
                rtWin.setByAngle(360);
                rtWin.setCycleCount(Animation.INDEFINITE);
                rtWin.setAutoReverse(false);
                rtWin.setInterpolator(Interpolator.LINEAR);
                rtWin.play();
            }
            else if(blackKingDead)
            {
                Text win = new Text("White Wins!");
                win.setFont(new Font(180));
                win.setX(5);
                win.setY(blockHeight * 4 + blockHeight / 2);
                win.setFill(Color.WHITE);
                win.setStyle("-fx-stroke: black;  -fx-stroke-width: 5;");
                board.getChildren().add(win);
                RotateTransition rtWin = new RotateTransition();
                rtWin.setDuration(Duration.millis(animationTime));
                rtWin.setNode(win);
                rtWin.setByAngle(360);
                rtWin.setCycleCount(Animation.INDEFINITE);
                rtWin.setAutoReverse(false);
                rtWin.setInterpolator(Interpolator.LINEAR);
                rtWin.play();
            }
            if(rotate && !promotePawn)
            {
                trPiece.setOnFinished(e -> rotate(board));
            }
        }
        else
        {

        }
    }
    public static void rotate(Group board)
    {
        upsideDown = !upsideDown;
        rtBoard = new RotateTransition();
        rtBoard.setDuration(Duration.millis(animationTime));
        rtBoard.setNode(board);
        rtBoard.setByAngle(180);
        rtBoard.setCycleCount(1);
        rtBoard.setAutoReverse(false);
        rtBoard.play();
        for(int x = 0; x < 8; x++)
        {
            for(int y = 0; y < 8; y++)
            {
                if(tileArray.get(alphabet[x] + Integer.toString(8 - y)).hasPiece())
                {
                    RotateTransition rtPiece = new RotateTransition();
                    rtPiece.setDuration(Duration.millis(animationTime));
                    rtPiece.setNode(tileArray.get(alphabet[x] + Integer.toString(8 - y)).getPiece().getImage());
                    rtPiece.setByAngle(-180);
                    rtPiece.setCycleCount(1);
                    rtPiece.setAutoReverse(false);
                    rtPiece.play();
                }
            }
        }
    }
    public static void createLegalMoves(int x, int y)
    {
        possibleMoves = new ArrayList<>();
        if(tileArray.get(loc).getPiece().getType().equals("pawn"))
        {
            if(tileArray.get(loc).getPiece().getColor().equals("white"))
            {
                if(tileArray.get(loc).getPosition().getY() == 6)
                {
                    for(int z = 0; z < 2; z++)
                    {
                        if(8 - y + 1 + z > 8 || 8 - y + 1 + z < 1 || tileArray.get(alphabet[x] + Integer.toString(8 - y + 1 + z)).hasPiece())
                        {
                            z = 2;
                        }
                        else
                        {
                            possibleMoves.add(alphabet[x] + Integer.toString(8 - y + 1 + z));
                        }
                    }
                }
                else if(8 - y + 1 > 8 || 8 - y + 1 < 1 || tileArray.get(alphabet[x] + Integer.toString(8 - y + 1)).hasPiece())
                {

                }
                else
                {
                    possibleMoves.add(alphabet[x] + Integer.toString(8 - y + 1));
                }

                if(8 - y + 1 > 8 || 8 - y + 1 < 1 || x + 1 > 7 || x + 1 < 0)
                {

                }
                else if(tileArray.get(alphabet[x + 1] + Integer.toString(8 - y + 1)).hasPiece())
                {
                    if(!tileArray.get(alphabet[x + 1] + Integer.toString(8 - y + 1)).getPiece().getColor().equals(tileArray.get(loc).getPiece().getColor()))
                    {
                        possibleMoves.add(alphabet[x + 1] + Integer.toString(8 - y + 1));
                    }
                }

                if(8 - y + 1 > 8 || 8 - y + 1 < 1 || x - 1 > 7 || x - 1 < 0)
                {

                }
                else if(tileArray.get(alphabet[x - 1] + Integer.toString(8 - y + 1)).hasPiece())
                {
                    if(!tileArray.get(alphabet[x - 1] + Integer.toString(8 - y + 1)).getPiece().getColor().equals(tileArray.get(loc).getPiece().getColor()))
                    {
                        possibleMoves.add(alphabet[x - 1] + Integer.toString(8 - y + 1));
                    }
                }
            }
            if(tileArray.get(loc).getPiece().getColor().equals("black"))
            {
                if(tileArray.get(loc).getPosition().getY() == 1)
                {
                    for(int z = 0; z < 2; z++)
                    {
                        if(8 - y - 1 - z > 8 || 8 - y - 1 - z < 1 || tileArray.get(alphabet[x] + Integer.toString(8 - y - 1 - z)).hasPiece())
                        {
                            z = 2;
                        }
                        else
                        {
                            possibleMoves.add(alphabet[x] + Integer.toString(8 - y - 1 - z));
                        }
                    }
                }
                else if(8 - y - 1 > 8 || 8 - y - 1 < 1 || tileArray.get(alphabet[x] + Integer.toString(8 - y - 1)).hasPiece())
                {

                }
                else
                {
                    possibleMoves.add(alphabet[x] + Integer.toString(8 - y - 1));
                }

                if(8 - y - 1 > 8 || 8 - y - 1 < 1 || x + 1 > 7 || x + 1 < 0)
                {

                }
                else if(tileArray.get(alphabet[x + 1] + Integer.toString(8 - y - 1)).hasPiece())
                {
                    if(!tileArray.get(alphabet[x + 1] + Integer.toString(8 - y - 1)).getPiece().getColor().equals(tileArray.get(loc).getPiece().getColor()))
                    {
                        possibleMoves.add(alphabet[x + 1] + Integer.toString(8 - y - 1));
                    }
                }

                if(8 - y - 1 > 8 || 8 - y - 1 < 1 || x - 1 > 7 || x - 1 < 0)
                {

                }
                else if(tileArray.get(alphabet[x - 1] + Integer.toString(8 - y - 1)).hasPiece())
                {
                    if(!tileArray.get(alphabet[x - 1] + Integer.toString(8 - y - 1)).getPiece().getColor().equals(tileArray.get(loc).getPiece().getColor()))
                    {
                        possibleMoves.add(alphabet[x - 1] + Integer.toString(8 - y - 1));
                    }
                }
            }
        }
        else if(tileArray.get(loc).getPiece().getType().equals("rook"))
        {
            boolean up = true;
            boolean down = true;
            boolean left = true;
            boolean right = true;
            for(int z = 0; z < 8; z++)
            {
                if(8 - y + 1 + z > 8 || 8 - y + 1 + z < 1)
                {
                    up = false;
                }
                else if(tileArray.get(alphabet[x] + Integer.toString(8 - y + 1 + z)).hasPiece() && up)
                {
                    up = false;
                    if(!tileArray.get(alphabet[x] + Integer.toString(8 - y + 1 + z)).getPiece().getColor().equals(tileArray.get(loc).getPiece().getColor()))
                    {
                        possibleMoves.add(alphabet[x] + Integer.toString(8 - y + 1 + z));
                    }
                }
                else if(up)
                {
                    possibleMoves.add(alphabet[x] + Integer.toString(8 - y + 1 + z));
                }

                if(8 - y - 1 - z > 8 || 8 - y - 1 - z < 1)
                {
                    down = false;
                }
                else if(tileArray.get(alphabet[x] + Integer.toString(8 - y - 1 - z)).hasPiece() && down)
                {
                    down = false;
                    if(!tileArray.get(alphabet[x] + Integer.toString(8 - y - 1 - z)).getPiece().getColor().equals(tileArray.get(loc).getPiece().getColor()))
                    {
                        possibleMoves.add(alphabet[x] + Integer.toString(8 - y - 1 - z));
                    }
                }
                else if(down)
                {
                    possibleMoves.add(alphabet[x] + Integer.toString(8 - y - 1 - z));
                }

                System.out.println(alphabet[x + 1 + z] + Integer.toString(8 - y));
                if(x + 1 + z > 7 || x + 1 + z < 0)
                {
                    right = false;
                }
                else if(tileArray.get(alphabet[x + 1 + z] + Integer.toString(8 - y)).hasPiece() && right)
                {
                    right = false;
                    if(!tileArray.get(alphabet[x + 1 + z] + Integer.toString(8 - y)).getPiece().getColor().equals(tileArray.get(loc).getPiece().getColor()))
                    {
                        possibleMoves.add(alphabet[x + 1 + z] + Integer.toString(8 - y));
                    }
                }
                else if(right)
                {
                    possibleMoves.add(alphabet[x + 1 + z] + Integer.toString(8 - y));
                }

                if(x - 1 - z > 7 || x - 1 - z < 0)
                {
                    left = false;
                }
                else if(tileArray.get(alphabet[x - 1 - z] + Integer.toString(8 - y)).hasPiece() && left)
                {
                    left = false;
                    if(!tileArray.get(alphabet[x - 1 - z] + Integer.toString(8 - y)).getPiece().getColor().equals(tileArray.get(loc).getPiece().getColor()))
                    {
                        possibleMoves.add(alphabet[x - 1 - z] + Integer.toString(8 - y));
                    }
                }
                else if(left)
                {
                    possibleMoves.add(alphabet[x - 1 - z] + Integer.toString(8 - y));
                }
            }
        }
        else if(tileArray.get(loc).getPiece().getType().equals("knight"))
        {
            if(8 - y + 2 > 8 || 8 - y + 2 < 1 || x + 1 > 7 || x + 1 < 0)
            {

            }
            else if(tileArray.get(alphabet[x + 1] + Integer.toString(8 - y + 2)).hasPiece())
            {
                if(!tileArray.get(alphabet[x + 1] + Integer.toString(8 - y + 2)).getPiece().getColor().equals(tileArray.get(loc).getPiece().getColor()))
                {
                    possibleMoves.add(alphabet[x + 1] + Integer.toString(8 - y + 2));
                }
            }
            else
            {
                possibleMoves.add(alphabet[x + 1] + Integer.toString(8 - y + 2));
            }

            if(8 - y + 2 > 8 || 8 - y + 2 < 1 || x - 1 > 7 || x - 1 < 0)
            {

            }
            else if(tileArray.get(alphabet[x - 1] + Integer.toString(8 - y + 2)).hasPiece())
            {
                if(!tileArray.get(alphabet[x - 1] + Integer.toString(8 - y + 2)).getPiece().getColor().equals(tileArray.get(loc).getPiece().getColor()))
                {
                    possibleMoves.add(alphabet[x - 1] + Integer.toString(8 - y + 2));
                }
            }
            else
            {
                possibleMoves.add(alphabet[x - 1] + Integer.toString(8 - y + 2));
            }

            if(8 - y - 2 > 8 || 8 - y - 2 < 1 || x + 1 > 7 || x + 1 < 0)
            {

            }
            else if(tileArray.get(alphabet[x + 1] + Integer.toString(8 - y - 2)).hasPiece())
            {
                if(!tileArray.get(alphabet[x + 1] + Integer.toString(8 - y - 2)).getPiece().getColor().equals(tileArray.get(loc).getPiece().getColor()))
                {
                    possibleMoves.add(alphabet[x + 1] + Integer.toString(8 - y - 2));
                }
            }
            else
            {
                possibleMoves.add(alphabet[x + 1] + Integer.toString(8 - y - 2));
            }

            if(8 - y - 2 > 8 || 8 - y - 2 < 1 || x - 1 > 7 || x - 1 < 0)
            {

            }
            else if(tileArray.get(alphabet[x - 1] + Integer.toString(8 - y - 2)).hasPiece())
            {
                if(!tileArray.get(alphabet[x - 1] + Integer.toString(8 - y - 2)).getPiece().getColor().equals(tileArray.get(loc).getPiece().getColor()))
                {
                    possibleMoves.add(alphabet[x - 1] + Integer.toString(8 - y - 2));
                }
            }
            else
            {
                possibleMoves.add(alphabet[x - 1] + Integer.toString(8 - y - 2));
            }

            if(8 - y + 1 > 8 || 8 - y + 1 < 1 || x + 2 > 7 || x + 2 < 0)
            {

            }
            else if(tileArray.get(alphabet[x + 2] + Integer.toString(8 - y + 1)).hasPiece())
            {
                if(!tileArray.get(alphabet[x + 2] + Integer.toString(8 - y + 1)).getPiece().getColor().equals(tileArray.get(loc).getPiece().getColor()))
                {
                    possibleMoves.add(alphabet[x + 2] + Integer.toString(8 - y + 1));
                }
            }
            else
            {
                possibleMoves.add(alphabet[x + 2] + Integer.toString(8 - y + 1));
            }

            if(8 - y - 1 > 8 || 8 - y - 1 < 1 || x + 2 > 7 || x + 2 < 0)
            {

            }
            else if(tileArray.get(alphabet[x + 2] + Integer.toString(8 - y - 1)).hasPiece())
            {
                if(!tileArray.get(alphabet[x + 2] + Integer.toString(8 - y - 1)).getPiece().getColor().equals(tileArray.get(loc).getPiece().getColor()))
                {
                    possibleMoves.add(alphabet[x + 2] + Integer.toString(8 - y - 1));
                }
            }
            else
            {
                possibleMoves.add(alphabet[x + 2] + Integer.toString(8 - y - 1));
            }

            if(8 - y + 1 > 8 || 8 - y + 1 < 1 || x - 2 > 7 || x - 2 < 0)
            {

            }
            else if(tileArray.get(alphabet[x - 2] + Integer.toString(8 - y + 1)).hasPiece())
            {
                if(!tileArray.get(alphabet[x - 2] + Integer.toString(8 - y + 1)).getPiece().getColor().equals(tileArray.get(loc).getPiece().getColor()))
                {
                    possibleMoves.add(alphabet[x - 2] + Integer.toString(8 - y + 1));
                }
            }
            else
            {
                possibleMoves.add(alphabet[x - 2] + Integer.toString(8 - y + 1));
            }

            if(8 - y - 1 > 8 || 8 - y - 1 < 1 || x - 2 > 7 || x - 2 < 0)
            {

            }
            else if(tileArray.get(alphabet[x - 2] + Integer.toString(8 - y - 1)).hasPiece())
            {
                if(!tileArray.get(alphabet[x - 2] + Integer.toString(8 - y - 1)).getPiece().getColor().equals(tileArray.get(loc).getPiece().getColor()))
                {
                    possibleMoves.add(alphabet[x - 2] + Integer.toString(8 - y - 1));
                }
            }
            else
            {
                possibleMoves.add(alphabet[x - 2] + Integer.toString(8 - y - 1));
            }
        }
        else if(tileArray.get(loc).getPiece().getType().equals("bishop"))
        {
            boolean upRight = true;
            boolean upLeft = true;
            boolean downRight = true;
            boolean downLeft = true;
            for(int z = 0; z < 8; z++)
            {
                if(8 - y + 1 + z > 8 || 8 - y + 1 + z < 1 || x + 1 + z > 7 || x + 1 + z < 0)
                {
                    upRight = false;
                }
                else if(tileArray.get(alphabet[x + 1 + z] + Integer.toString(8 - y + 1 + z)).hasPiece() && upRight)
                {
                    upRight = false;
                    if(!tileArray.get(alphabet[x + 1 + z] + Integer.toString(8 - y + 1 + z)).getPiece().getColor().equals(tileArray.get(loc).getPiece().getColor()))
                    {
                        possibleMoves.add(alphabet[x + 1 + z] + Integer.toString(8 - y + 1 + z));
                    }
                }
                else if(upRight)
                {
                    possibleMoves.add(alphabet[x + 1 + z] + Integer.toString(8 - y + 1 + z));
                }

                if(8 - y + 1 + z > 8 || 8 - y + 1 + z < 1 || x - 1 - z > 7 || x - 1 - z < 0)
                {
                    upLeft = false;
                }
                else if(tileArray.get(alphabet[x - 1 - z] + Integer.toString(8 - y + 1 + z)).hasPiece() && upLeft)
                {
                    upLeft = false;
                    if(!tileArray.get(alphabet[x - 1 - z] + Integer.toString(8 - y + 1 + z)).getPiece().getColor().equals(tileArray.get(loc).getPiece().getColor()))
                    {
                        possibleMoves.add(alphabet[x - 1 - z] + Integer.toString(8 - y + 1 + z));
                    }
                }
                else if(upLeft)
                {
                    possibleMoves.add(alphabet[x - 1 - z] + Integer.toString(8 - y + 1 + z));
                }

                if(8 - y - 1 - z > 8 || 8 - y - 1 - z < 1 || x + 1 + z > 7 || x + 1 + z < 0)
                {
                    downRight = false;
                }
                else if(tileArray.get(alphabet[x + 1 + z] + Integer.toString(8 - y - 1 - z)).hasPiece() && downRight)
                {
                    downRight = false;
                    if(!tileArray.get(alphabet[x + 1 + z] + Integer.toString(8 - y - 1 - z)).getPiece().getColor().equals(tileArray.get(loc).getPiece().getColor()))
                    {
                        possibleMoves.add(alphabet[x + 1 + z] + Integer.toString(8 - y - 1 - z));
                    }
                }
                else if(downRight)
                {
                    possibleMoves.add(alphabet[x + 1 + z] + Integer.toString(8 - y - 1 - z));
                }

                if(8 - y - 1 - z > 8 || 8 - y - 1 - z < 1 || x - 1 - z > 7 || x - 1 - z < 0)
                {
                    downLeft = false;
                }
                else if(tileArray.get(alphabet[x - 1 - z] + Integer.toString(8 - y - 1 - z)).hasPiece() && downLeft)
                {
                    downLeft = false;
                    if(!tileArray.get(alphabet[x - 1 - z] + Integer.toString(8 - y - 1 - z)).getPiece().getColor().equals(tileArray.get(loc).getPiece().getColor()))
                    {
                        possibleMoves.add(alphabet[x - 1 - z] + Integer.toString(8 - y - 1 - z));
                    }
                }
                else if(downLeft)
                {
                    possibleMoves.add(alphabet[x - 1 - z] + Integer.toString(8 - y - 1 - z));
                }
            }
        }
        else if(tileArray.get(loc).getPiece().getType().equals("queen"))
        {
            boolean up = true;
            boolean down = true;
            boolean left = true;
            boolean right = true;
            boolean upRight = true;
            boolean upLeft = true;
            boolean downRight = true;
            boolean downLeft = true;
            for(int z = 0; z < 8; z++)
            {
                if(8 - y + 1 + z > 8 || 8 - y + 1 + z < 1)
                {
                    up = false;
                }
                else if(tileArray.get(alphabet[x] + Integer.toString(8 - y + 1 + z)).hasPiece() && up)
                {
                    up = false;
                    if(!tileArray.get(alphabet[x] + Integer.toString(8 - y + 1 + z)).getPiece().getColor().equals(tileArray.get(loc).getPiece().getColor()))
                    {
                        possibleMoves.add(alphabet[x] + Integer.toString(8 - y + 1 + z));
                    }
                }
                else if(up)
                {
                    possibleMoves.add(alphabet[x] + Integer.toString(8 - y + 1 + z));
                }

                if(8 - y - 1 - z > 8 || 8 - y - 1 - z < 1)
                {
                    down = false;
                }
                else if(tileArray.get(alphabet[x] + Integer.toString(8 - y - 1 - z)).hasPiece() && down)
                {
                    down = false;
                    if(!tileArray.get(alphabet[x] + Integer.toString(8 - y - 1 - z)).getPiece().getColor().equals(tileArray.get(loc).getPiece().getColor()))
                    {
                        possibleMoves.add(alphabet[x] + Integer.toString(8 - y - 1 - z));
                    }
                }
                else if(down)
                {
                    possibleMoves.add(alphabet[x] + Integer.toString(8 - y - 1 - z));
                }

                System.out.println(alphabet[x + 1 + z] + Integer.toString(8 - y));
                if(x + 1 + z > 7 || x + 1 + z < 0)
                {
                    right = false;
                }
                else if(tileArray.get(alphabet[x + 1 + z] + Integer.toString(8 - y)).hasPiece() && right)
                {
                    right = false;
                    if(!tileArray.get(alphabet[x + 1 + z] + Integer.toString(8 - y)).getPiece().getColor().equals(tileArray.get(loc).getPiece().getColor()))
                    {
                        possibleMoves.add(alphabet[x + 1 + z] + Integer.toString(8 - y));
                    }
                }
                else if(right)
                {
                    possibleMoves.add(alphabet[x + 1 + z] + Integer.toString(8 - y));
                }

                if(x - 1 - z > 7 || x - 1 - z < 0)
                {
                    left = false;
                }
                else if(tileArray.get(alphabet[x - 1 - z] + Integer.toString(8 - y)).hasPiece() && left)
                {
                    left = false;
                    if(!tileArray.get(alphabet[x - 1 - z] + Integer.toString(8 - y)).getPiece().getColor().equals(tileArray.get(loc).getPiece().getColor()))
                    {
                        possibleMoves.add(alphabet[x - 1 - z] + Integer.toString(8 - y));
                    }
                }
                else if(left)
                {
                    possibleMoves.add(alphabet[x - 1 - z] + Integer.toString(8 - y));
                }

                if(8 - y + 1 + z > 8 || 8 - y + 1 + z < 1 || x + 1 + z > 7 || x + 1 + z < 0)
                {
                    upRight = false;
                }
                else if(tileArray.get(alphabet[x + 1 + z] + Integer.toString(8 - y + 1 + z)).hasPiece() && upRight)
                {
                    upRight = false;
                    if(!tileArray.get(alphabet[x + 1 + z] + Integer.toString(8 - y + 1 + z)).getPiece().getColor().equals(tileArray.get(loc).getPiece().getColor()))
                    {
                        possibleMoves.add(alphabet[x + 1 + z] + Integer.toString(8 - y + 1 + z));
                    }
                }
                else if(upRight)
                {
                    possibleMoves.add(alphabet[x + 1 + z] + Integer.toString(8 - y + 1 + z));
                }

                if(8 - y + 1 + z > 8 || 8 - y + 1 + z < 1 || x - 1 - z > 7 || x - 1 - z < 0)
                {
                    upLeft = false;
                }
                else if(tileArray.get(alphabet[x - 1 - z] + Integer.toString(8 - y + 1 + z)).hasPiece() && upLeft)
                {
                    upLeft = false;
                    if(!tileArray.get(alphabet[x - 1 - z] + Integer.toString(8 - y + 1 + z)).getPiece().getColor().equals(tileArray.get(loc).getPiece().getColor()))
                    {
                        possibleMoves.add(alphabet[x - 1 - z] + Integer.toString(8 - y + 1 + z));
                    }
                }
                else if(upLeft)
                {
                    possibleMoves.add(alphabet[x - 1 - z] + Integer.toString(8 - y + 1 + z));
                }

                if(8 - y - 1 - z > 8 || 8 - y - 1 - z < 1 || x + 1 + z > 7 || x + 1 + z < 0)
                {
                    downRight = false;
                }
                else if(tileArray.get(alphabet[x + 1 + z] + Integer.toString(8 - y - 1 - z)).hasPiece() && downRight)
                {
                    downRight = false;
                    if(!tileArray.get(alphabet[x + 1 + z] + Integer.toString(8 - y - 1 - z)).getPiece().getColor().equals(tileArray.get(loc).getPiece().getColor()))
                    {
                        possibleMoves.add(alphabet[x + 1 + z] + Integer.toString(8 - y - 1 - z));
                    }
                }
                else if(downRight)
                {
                    possibleMoves.add(alphabet[x + 1 + z] + Integer.toString(8 - y - 1 - z));
                }

                if(8 - y - 1 - z > 8 || 8 - y - 1 - z < 1 || x - 1 - z > 7 || x - 1 - z < 0)
                {
                    downLeft = false;
                }
                else if(tileArray.get(alphabet[x - 1 - z] + Integer.toString(8 - y - 1 - z)).hasPiece() && downLeft)
                {
                    downLeft = false;
                    if(!tileArray.get(alphabet[x - 1 - z] + Integer.toString(8 - y - 1 - z)).getPiece().getColor().equals(tileArray.get(loc).getPiece().getColor()))
                    {
                        possibleMoves.add(alphabet[x - 1 - z] + Integer.toString(8 - y - 1 - z));
                    }
                }
                else if(downLeft)
                {
                    possibleMoves.add(alphabet[x - 1 - z] + Integer.toString(8 - y - 1 - z));
                }
            }
        }
        else if(tileArray.get(loc).getPiece().getType().equals("king"))
        {
            canCastle = false;
            if(8 - y + 1 > 8 || 8 - y + 1 < 1)
            {

            }
            else if(tileArray.get(alphabet[x] + Integer.toString(8 - y + 1)).hasPiece())
            {
                if(!tileArray.get(alphabet[x] + Integer.toString(8 - y + 1)).getPiece().getColor().equals(tileArray.get(loc).getPiece().getColor()))
                {
                    possibleMoves.add(alphabet[x] + Integer.toString(8 - y + 1));
                }
            }
            else
            {
                possibleMoves.add(alphabet[x] + Integer.toString(8 - y + 1));
            }

            if(8 - y - 1 > 8 || 8 - y - 1 < 1)
            {

            }
            else if(tileArray.get(alphabet[x] + Integer.toString(8 - y - 1)).hasPiece())
            {
                if(!tileArray.get(alphabet[x] + Integer.toString(8 - y - 1)).getPiece().getColor().equals(tileArray.get(loc).getPiece().getColor()))
                {
                    possibleMoves.add(alphabet[x] + Integer.toString(8 - y - 1));
                }
            }
            else
            {
                possibleMoves.add(alphabet[x] + Integer.toString(8 - y - 1));
            }

            if(x + 1 > 7 || x + 1 < 0)
            {

            }
            else if(tileArray.get(alphabet[x + 1] + Integer.toString(8 - y)).hasPiece())
            {
                if(!tileArray.get(alphabet[x + 1] + Integer.toString(8 - y)).getPiece().getColor().equals(tileArray.get(loc).getPiece().getColor()))
                {
                    possibleMoves.add(alphabet[x + 1] + Integer.toString(8 - y));
                }
            }
            else
            {
                possibleMoves.add(alphabet[x + 1] + Integer.toString(8 - y));
            }

            if(x - 1 > 7 || x - 1 < 0)
            {

            }
            else if(tileArray.get(alphabet[x - 1] + Integer.toString(8 - y)).hasPiece())
            {
                if(!tileArray.get(alphabet[x - 1] + Integer.toString(8 - y)).getPiece().getColor().equals(tileArray.get(loc).getPiece().getColor()))
                {
                    possibleMoves.add(alphabet[x - 1] + Integer.toString(8 - y));
                }
            }
            else
            {
                possibleMoves.add(alphabet[x - 1] + Integer.toString(8 - y));
            }

            if(8 - y + 1 > 8 || 8 - y + 1 < 1 || x + 1 > 7 || x + 1 < 0)
            {

            }
            else if(tileArray.get(alphabet[x + 1] + Integer.toString(8 - y + 1)).hasPiece())
            {
                if(!tileArray.get(alphabet[x + 1] + Integer.toString(8 - y + 1)).getPiece().getColor().equals(tileArray.get(loc).getPiece().getColor()))
                {
                    possibleMoves.add(alphabet[x + 1] + Integer.toString(8 - y + 1));
                }
            }
            else
            {
                possibleMoves.add(alphabet[x + 1] + Integer.toString(8 - y + 1));
            }

            if(8 - y + 1 > 8 || 8 - y + 1 < 1 || x - 1 > 7 || x - 1 < 0)
            {

            }
            else if(tileArray.get(alphabet[x - 1] + Integer.toString(8 - y + 1)).hasPiece())
            {
                if(!tileArray.get(alphabet[x - 1] + Integer.toString(8 - y + 1)).getPiece().getColor().equals(tileArray.get(loc).getPiece().getColor()))
                {
                    possibleMoves.add(alphabet[x - 1] + Integer.toString(8 - y + 1));
                }
            }
            else
            {
                possibleMoves.add(alphabet[x - 1] + Integer.toString(8 - y + 1));
            }

            if(8 - y - 1 > 8 || 8 - y - 1 < 1 || x + 1 > 7 || x + 1 < 0)
            {

            }
            else if(tileArray.get(alphabet[x + 1] + Integer.toString(8 - y - 1)).hasPiece())
            {
                if(!tileArray.get(alphabet[x + 1] + Integer.toString(8 - y - 1)).getPiece().getColor().equals(tileArray.get(loc).getPiece().getColor()))
                {
                    possibleMoves.add(alphabet[x + 1] + Integer.toString(8 - y - 1));
                }
            }
            else
            {
                possibleMoves.add(alphabet[x + 1] + Integer.toString(8 - y - 1));
            }

            if(8 - y - 1 > 8 || 8 - y - 1 < 1 || x - 1 > 7 || x - 1 < 0)
            {

            }
            else if(tileArray.get(alphabet[x - 1] + Integer.toString(8 - y - 1)).hasPiece())
            {
                if(!tileArray.get(alphabet[x - 1] + Integer.toString(8 - y - 1)).getPiece().getColor().equals(tileArray.get(loc).getPiece().getColor()))
                {
                    possibleMoves.add(alphabet[x - 1] + Integer.toString(8 - y - 1));
                }
            }
            else
            {
                possibleMoves.add(alphabet[x - 1] + Integer.toString(8 - y - 1));
            }

            if(!tileArray.get(alphabet[x] + Integer.toString(8 - y)).getPiece().hasMoved() && tileArray.get("a" + Integer.toString(8 - y)).hasPiece() && !tileArray.get("a" + Integer.toString(8 - y)).getPiece().hasMoved())
            {
                boolean piecesInRow = false;
                for(int z = 1; z < 4; z++)
                {
                    if(tileArray.get(alphabet[z] + Integer.toString(8 - y)).hasPiece())
                    {
                        System.out.println(alphabet[z] + Integer.toString(8 - y));
                        piecesInRow = true;
                    }
                }
                if(!piecesInRow)
                {
                    possibleMoves.add("c" + Integer.toString(8 - y));
                    canCastle = true;
                }
                else
                {

                }
                System.out.println("piecesInRow: " + piecesInRow);
            }
            else
            {

            }
            if(!tileArray.get(alphabet[x] + Integer.toString(8 - y)).getPiece().hasMoved() && tileArray.get("h" + Integer.toString(8 - y)).hasPiece() && !tileArray.get("h" + Integer.toString(8 - y)).getPiece().hasMoved())
            {
                boolean piecesInRow = false;
                for(int z = 5; z < 7; z++)
                {
                    if(tileArray.get(alphabet[z] + Integer.toString(8 - y)).hasPiece())
                    {
                        System.out.println(alphabet[z] + Integer.toString(8 - y));
                        piecesInRow = true;
                    }
                }
                if(!piecesInRow)
                {
                    possibleMoves.add("g" + Integer.toString(8 - y));
                    canCastle = true;
                }
                else
                {

                }
                System.out.println("piecesInRow: " + piecesInRow);
            }
            else
            {

            }
            System.out.println(canCastle);
        }
        for(int z = 0; z < possibleMoves.size(); z++)
        {
            blockArray[tileArray.get(possibleMoves.get(z)).getPosition().getX()][tileArray.get(possibleMoves.get(z)).getPosition().getY()].setStroke(Color.LIGHTGREEN);
            blockArray[tileArray.get(possibleMoves.get(z)).getPosition().getX()][tileArray.get(possibleMoves.get(z)).getPosition().getY()].setStrokeType(StrokeType.INSIDE);
            blockArray[tileArray.get(possibleMoves.get(z)).getPosition().getX()][tileArray.get(possibleMoves.get(z)).getPosition().getY()].setStrokeWidth(blockHeight / 12);
        }
    }
}