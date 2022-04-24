public class Piece {
    private final char color;
    private final char type;
    private Position p;

    Piece(char colorIn, char typeIn) {
        String types = "prnbqk";
        colorIn = Character.toLowerCase(colorIn);
        typeIn = Character.toLowerCase(typeIn);
        if ((colorIn != 'w' && colorIn != 'b') || types.indexOf(typeIn) < 0) throw new IllegalArgumentException();
        color = colorIn;
        type = typeIn;
    }

    public char getColor() {
        return color;
    }

    public char getType() {
        return type;
    }

    public String toString() {
        return Character.toString(color) + type;
    }

    public void setPosition(Position p) {
        this.p = p;
    }

    public Position getPosition() {
        return p;
    }

    public boolean equals(Object o) {
        if (o.getClass() != this.getClass()) return false;
        Piece p = (Piece) o;
        return (p.getColor() == this.getColor() && this.getType() == this.getType());
    }
}
