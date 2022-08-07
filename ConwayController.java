import java.util.LinkedList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ConwayController {

        @FXML
	private Canvas canvas; 
        GraphicsContext graphicsContext;
	private boolean firstButtonPress = true;
	private final int cellMatrixSize = 10;
	private int[][] currentCells = new int[cellMatrixSize][cellMatrixSize];
	private int[][] nextGenerationCells = new int[cellMatrixSize][cellMatrixSize];
	
	public boolean isAlive(int cell) {
		if(cell == 1)
			return true;
		return false;
	}
	
	public boolean cellIndiciesAreInBounds(int neighborRow, int neighborColumn) {
		if(neighborRow > 9 || neighborColumn > 9 || neighborRow < 0 || neighborColumn < 0)
			return false;
		return true;
	}
	
	public boolean neighbouringCellIsAlive(int neighborRow, int neighborColumn) {
		return cellIndiciesAreInBounds(neighborRow, neighborColumn) && isAlive(currentCells[neighborRow][neighborColumn]);
	}
	
	public  int verifyLivingNeighbors(int cellRow, int cellColumn) {
    		int livingNeighbouringCells = 0;
    		for(int neighborRow = cellRow - 1; neighborRow <= cellRow + 1; neighborRow++) {
    			for(int neighborColumn = cellColumn - 1; neighborColumn <= cellColumn + 1; neighborColumn++) {
    				if(neighbouringCellIsAlive(neighborRow, neighborColumn) && !(cellRow == neighborRow && cellColumn == neighborColumn)) { // do something here.
    					livingNeighbouringCells++;
    				}
    			}
    		}
    		return livingNeighbouringCells;
        }
	                      
	public boolean cellHasExactlyThreeLivingNeighbors(int livingNeighbouringCells){
		if(livingNeighbouringCells == 3)
			return true;
		return false;
	}
	
	public void setCellToLiveNextGeneration(int cellRow, int cellColumn) {
		nextGenerationCells[cellRow][cellColumn] = 1;
	}
	
	public void setCellToDieNextGeneration(int cellRow, int cellColumn) {
		nextGenerationCells[cellRow][cellColumn] = 0;
	}
	
	public boolean tooLittleLivingNeighbors(int livingNeighbouringCells) {
		if(livingNeighbouringCells <= 1)
			return true;
		return false;
	}
	
	public boolean tooManyLivingNeighbors(int livingNeighbouringCells) {
		if(livingNeighbouringCells >= 4)
			return true;
		return false;
	}
	
	public boolean cellHasTwoOrThreeLivingNeighbors(int livingNeighbouringCells) {
		if(livingNeighbouringCells == 3 || livingNeighbouringCells == 2)
			return true;
		return false;
	}
	
	public void updateCurrentCellsToNextGeneration() {
		for(int cellRow = 0; cellRow < cellMatrixSize; cellRow++) {
			for(int cellColumn = 0; cellColumn < cellMatrixSize; cellColumn++)
				currentCells[cellRow][cellColumn] = nextGenerationCells[cellRow][cellColumn];
		}
	}
	
	public void updateNextGenerationCells() {
    		int currentCell, livingNeighbouringCells;
    		
    		for(int currentCellsRow = 0; currentCellsRow < cellMatrixSize; currentCellsRow++) {
    			for(int currentCellsColumn = 0; currentCellsColumn < cellMatrixSize; currentCellsColumn++) {
    				currentCell = currentCells[currentCellsRow][currentCellsColumn];
    				livingNeighbouringCells = verifyLivingNeighbors(currentCellsRow, currentCellsColumn);
				
				if(!isAlive(currentCell) && cellHasExactlyThreeLivingNeighbors(livingNeighbouringCells))
					setCellToLiveNextGeneration(currentCellsRow,currentCellsColumn);
					
    				if(isAlive(currentCell)) {
    					if(tooLittleLivingNeighbors(livingNeighbouringCells) || tooManyLivingNeighbors(livingNeighbouringCells))
    						setCellToDieNextGeneration(currentCellsRow, currentCellsColumn);
    					if(cellHasTwoOrThreeLivingNeighbors(livingNeighbouringCells))
    						setCellToLiveNextGeneration(currentCellsRow, currentCellsColumn);
    				}
			}
		}    
	    
    		updateCurrentCellsToNextGeneration();	
	}	
    
	public void initializeLivingAndDeadCellsRandomly() {
    		for(int cellRow = 0; cellRow < cellMatrixSize; cellRow++) {
				for(int cellColumn = 0; cellColumn < cellMatrixSize; cellColumn++) 
					currentCells[cellRow][cellColumn] = (int)Math.round(Math.random());
			}
    	} 
    
   	public void strokeAndFillCell(int cellXCoordinate, int cellYCoordinate, int cellSize) {
    		graphicsContext.strokeRect(cellXCoordinate, cellYCoordinate, cellSize, cellSize);
    		graphicsContext.fillRect(cellXCoordinate, cellYCoordinate, cellSize, cellSize);
    	}
    
    	public void colorCellBlack(int cellXCoordinate, int cellYCoordinate, int cellSize) {
    		graphicsContext.setFill(Color.BLACK);
    		strokeAndFillCell(cellXCoordinate, cellYCoordinate,cellSize);
    	}
    
    	public void colorCellWhite(int cellXCoordinate, int cellYCoordinate, int cellSize) {
    		graphicsContext.setFill(Color.WHITE);
    		strokeAndFillCell(cellXCoordinate, cellYCoordinate,cellSize);
    	}
    
    	public void repaint(int cellSize) {
    		for(int cellXCoordinate = 0, cellRow = 0; cellXCoordinate < canvas.getWidth(); cellXCoordinate += cellSize, cellRow++) {
    			for(int cellYCoordinate = 0, cellColumn = 0; cellYCoordinate < canvas.getHeight(); cellYCoordinate += cellSize, cellColumn++) {        			    
				if(isAlive(currentCells[cellRow][cellColumn])) 
					colorCellBlack(cellXCoordinate, cellYCoordinate, cellSize);	
    				else 
					colorCellWhite(cellXCoordinate, cellYCoordinate, cellSize);		
    			}
    		}
		updateNextGenerationCells();		
    	}
    
    	@FXML
    	void NextGenerationButtonPressed(ActionEvent event) {
   	
    		graphicsContext = canvas.getGraphicsContext2D();	
    		int cellSize = (int)canvas.getHeight()/cellMatrixSize;
    	
    		if(firstButtonPress == true) {
    			firstButtonPress = false;
    			initializeLivingAndDeadCellsRandomly();
    		}
    		
    		repaint(cellSize);
    		
    	}
	
}
