public class Cell {
    private String cellColor ; // Color of the cell occupying this cell, null if empty
    private String destinationColor; // Destination color for this cell, null if not a destination

    public Cell()
    {
        cellColor = "Gray";
        destinationColor = "";
    }
    public Cell(String cellColor , String destinationColor)
    {
        this.cellColor = cellColor ;
        this.destinationColor = destinationColor ;
    }
    public Cell(Cell other)
    {
        this.cellColor = other.cellColor;
        this.destinationColor = other.destinationColor ;

    }
    public String getCellColor() {
        return cellColor;
    }

    public void setCellColor(String cellColor) {
        this.cellColor = cellColor;
    }

    public String getDestinationColor() {
        return destinationColor;
    }

    public void setDestinationColor(String destinationColor) {
        this.destinationColor = destinationColor;
    }
    public boolean arrived_to_destination()
    {
        return cellColor.equals(destinationColor.toLowerCase());
    }
    public String get_cell_representation()
    {
        String color = "";
        if(cellColor.equals("Gray")){
            if(destinationColor.isEmpty()) color = ".";
            else color = destinationColor ;
        }else if(cellColor.equals("Black")){
            color = "#";
        }
        else{
            if(destinationColor.isEmpty()) color = cellColor ;
            else color = cellColor + "(" + destinationColor + ")" ;
        }
        return color ;
    }
}
