public class ViewConsole implements View{
    @Override
    public void display(Cell[][] grid, int size) {
        System.out.println("################## Current State ##################") ;
        for(int i = 0 ; i < size ; i++){
            for(int j = 0 ; j < size ; j++){
                System.out.print(grid[i][j].get_cell_representation() + " ");
            }
            System.out.println();
        }
    }
}
