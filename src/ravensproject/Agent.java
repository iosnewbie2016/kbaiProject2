package ravensproject;

import java.util.HashMap;

// Uncomment these lines to access image processing.
//import java.awt.Image;
//import java.io.File;
//import javax.imageio.ImageIO;

/**
 * Your Agent for solving Raven's Progressive Matrices. You MUST modify this
 * file.
 * 
 * You may also create and submit new files in addition to modifying this file.
 * 
 * Make sure your file retains methods with the signatures: public Agent()
 * public char Solve(RavensProblem problem)
 * 
 * These methods will be necessary for the project's main method to run.
 * 
 */
public class Agent {
	/**
	 * The default constructor for your Agent. Make sure to execute any
	 * processing necessary before your Agent starts solving problems here.
	 * 
	 * Do not add any variables to this signature; they will not be used by
	 * main().
	 * 
	 */
	public Agent() {
		System.out.println("agent created");
	}

	/**
	 * The primary method for solving incoming Raven's Progressive Matrices. For
	 * each problem, your Agent's Solve() method will be called. At the
	 * conclusion of Solve(), your Agent should return a String representing its
	 * answer to the question: "1", "2", "3", "4", "5", or "6". These Strings
	 * are also the Names of the individual RavensFigures, obtained through
	 * RavensFigure.getName().
	 * 
	 * In addition to returning your answer at the end of the method, your Agent
	 * may also call problem.checkAnswer(String givenAnswer). The parameter
	 * passed to checkAnswer should be your Agent's current guess for the
	 * problem; checkAnswer will return the correct answer to the problem. This
	 * allows your Agent to check its answer. Note, however, that after your
	 * agent has called checkAnswer, it will *not* be able to change its answer.
	 * checkAnswer is used to allow your Agent to learn from its incorrect
	 * answers; however, your Agent cannot change the answer to a question it
	 * has already answered.
	 * 
	 * If your Agent calls checkAnswer during execution of Solve, the answer it
	 * returns will be ignored; otherwise, the answer returned at the end of
	 * Solve will be taken as your Agent's answer to this problem.
	 * 
	 * @param problem
	 *            the RavensProblem your agent should solve
	 * @return your Agent's answer to this problem
	 */
	public int Solve(RavensProblem problem) {
		System.out
				.println("/*************************solver started***************************************/");
		System.out.println("problem Name: " + problem.getName());
		int panels = problem.getProblemType().equals("2x2") ? 3 : 8;
		int matrixSize = panels == 3 ? 2 : 3;
		RavensFigure[][] matrix = new RavensFigure[matrixSize][matrixSize];
		int choices = panels == 3 ? 6 : 8;
		HashMap<String, RavensFigure> figures = problem.getFigures();

		char panelSelection = 'A';
		/************************************** semantic network ****************************************/
		// 1. iterate through 0 - panels + 'A' and convert to raven figure
		// 2. iterate through 0 - choices and convert to raven figure

		// initialize matrix
		for (int i = 0; i < panels; i++) {
			matrix[i / matrixSize][i % matrixSize] = figures.get(panelSelection
					+ i);
		}
		/************************************** generate and test ****************************************/
		/* O X X
		 * Y 
		 * Y
		 * 
		 * 
		 * O X
		 * Y
		 */
		//only generate possible relationships
		//generate relationships across
		//generate relationships down
		
		//TEST
		//for all answers see if it passes relationship

		for (String key : figures.keySet()) {
			System.out
					.println("######################Figure###############################");
			System.out.println("Raven Figure: " + key);
			RavensFigure figure = figures.get(key);

			System.out.println("figure name: " + figure.getName());

			// construct a matrix
			// 3. use generate and test for valid relations
			// 4. use means-ends analysis to find optimal answer
			System.out.println("figure visual: " + figure.getVisual());

			for (String objKey : figure.getObjects().keySet()) {
				System.out
						.println("-------------------- objects ----------------------");
				System.out.println("Raven Obj: " + objKey);
				RavensObject obj = figure.getObjects().get(objKey);
				System.out.println("Object: " + obj.getName());
				for (String attrKey : obj.getAttributes().keySet()) {
					System.out.println("attribute: " + attrKey);
					System.out.println("attribute value: "
							+ obj.getAttributes().get(attrKey));
				}

			}

		}
		return -1;
	}
}
