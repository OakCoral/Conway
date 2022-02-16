import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ConwayController {

    @FXML
    private Canvas canvas;
    
	private boolean start = true;
	private static int[][] currentColor = new int[10][10];
	private static int[][] nextColor = new int[10][10];
	
	/* adds up living neighbors in the array, and returns livingNeighbors */
	public static int checkValidNeighbors(int i, int j) {
    	int livingNeighbors = 0;
    	
    	/* gets data from neighbors */
    	for(int k = i - 1; k < i+2; k++) {
    		if(k >= 0 && k <=9) {
    			for(int l = j - 1; l < j + 2; l++) {
    				if(l >= 0 && l <= 9) {
    					/* not to count himself as a living neighbor */
    					if((currentColor[k][l] == 1) && !((k == i) && (l == j)))
               				livingNeighbors += 1;		
    				}
       			}
    		}
   		}
		
    	return livingNeighbors;
    }
	                                                    
	/* decides by the state of the next color by it's current color and the neighbors color */
    public static void updateNextColor() {
    	int livingNeighbors = 0;
    	// calculate next colors 
		for(int i = 0; i < 10; i++) {
			livingNeighbors = 0;
			for(int j = 0; j < 10; j++) {
				livingNeighbors = checkValidNeighbors(i, j);
				
				// if dead and surrounded by 3 neighbors -> live next generation
				if((currentColor[i][j] == 0) && (livingNeighbors == 3))
    				nextColor[i][j] = 1;
				
    			// if alive 
    			if(currentColor[i][j] == 1) {
    				// if too little or too many neighbors -> death next generation 
    				if((livingNeighbors <= 1) || (livingNeighbors >= 4))
    					nextColor[i][j] = 0;
    				// if 2 or 3 neighbors -> live next generation 
    				if(livingNeighbors == 3 || livingNeighbors == 2)
    					nextColor[i][j] = 1;
    			}
			}
		}    
		
		/* puts the next colors into correntColor for the next coloring */
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				currentColor[i][j] = nextColor[i][j];
			}
		}
		
    }
    
    @FXML
    void NextGenerationButtonPressed(ActionEvent event) {
   
    	GraphicsContext gc = canvas.getGraphicsContext2D();
    	
    	gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    	
    	int livingNeighbors = 0, k = 0, l = 0;
    	
    	/* if it's the first button push, initialize currentColor to random values */ 
    	if(start == true) {
    		start = false;
    		/* initializes currentColor to random 1s and 0s */
    		for(int i = 0; i < 10; i++) {
    			int j = 0;
    			for(j = 0; j < 10; j++) 
        			currentColor[i][j] = (int)Math.round(Math.random());
    		}
    		/* draws the rectangles according to currentColor data */
    		for(int i = 0; i < canvas.getWidth(); i += canvas.getWidth()/10, k++) {
    			l = 0;
        		for(int j = 0; j < canvas.getHeight(); j += canvas.getWidth()/10, l++) {
        			    				
    				if(currentColor[k][l] == 1) {
        				gc.setFill(Color.BLACK);	
        			} else {
        				gc.setFill(Color.WHITE);
        			}
    				
    				gc.fillRect(i, j, canvas.getWidth()/10, canvas.getWidth()/10);
        			gc.strokeRect(i, j, canvas.getWidth()/10, canvas.getWidth()/10);
        		}
        	}
    		
    		updateNextColor();		
    	}
    	/* if the currentColor is already initialized */ 
    	else {
    		
    		for(int i = 0; i < canvas.getWidth(); i += canvas.getWidth()/10, k++) {
    			l = 0;
        		for(int j = 0; j < canvas.getHeight(); j += canvas.getWidth()/10, l++) {
        			    				
    				if(currentColor[k][l] == 1) {
        				gc.setFill(Color.BLACK);	
        			} else {
        				gc.setFill(Color.WHITE);
        			}
    				
    				gc.fillRect(i, j, canvas.getWidth()/10, canvas.getWidth()/10);
        			gc.strokeRect(i, j, canvas.getWidth()/10, canvas.getWidth()/10);
        		}
        	}
    		
    		updateNextColor();		
    	}
    }

}
