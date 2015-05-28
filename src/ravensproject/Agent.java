package ravensproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		/*
		 * O X ? Y ? ? ? ? ?
		 * 
		 * 
		 * O ? ? ?
		 */
		// only generate possible relationships
		/***************** relationships across ********************/
		Map<String,Map<String, CHANGE>> relationshipX;
		Map<String,Map<String, CHANGE>> relationshipY;
		// find difference in attributes in a list of RavensFigures
		for (int x = 0; x < matrixSize - 1; x++) {
			// each column's change needs to be the same.
			List<RavensFigure> list = new ArrayList<>();
			for (int y = 0; y < matrixSize; y++) {
				list.add(matrix[x][y]);
			}
			// find difference
			// relationship for row
			relationshipX = findRelationship(list);

		}

		// generate ways of achieve that difference

		/***************** relationships down **********************/
		// find difference in attributes
		// generate ways of achieve that difference
		for (int y = 0; y < matrixSize - 1; y++) {
			List<RavensFigure> list = new ArrayList<>();
			for (int x = 0; x < matrixSize; x++) {
				list.add(matrix[x][y]);
			}
			// find relationship
			// relationship for column
			relationshipY = findRelationship(list);

		}
		// TEST
		// for all answers see if there is at least one answer that passes the
		// relationship

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

	/**
	 * map <object, <attribute, change>>
	 * @param list
	 * @return
	 */
	private Map<String,Map<String, CHANGE>> findRelationship(List<RavensFigure> list) {
		
		HashMap<String, Map<String, CHANGE>> change = new HashMap<>();
		for (RavensFigure figure : list) {
			for (String objectKey : figure.getObjects().keySet()) {
				HashMap<String, String> attributes = null;
				Map<String, CHANGE> attrChange =null;
				if(change.containsKey(objectKey)){
					attrChange = change.get(objectKey);
				}else{
					
				 attrChange = new HashMap<>();
				}
				HashMap<String, String> temp = figure.getObjects()
						.get(objectKey).getAttributes();
				if (attributes == null) {
					attributes = temp;
					for (String attrKey : temp.keySet()) {
						attrChange.put(attrKey, CHANGE.NO_CHANGE);
					}
				}
				for (String attrKey : temp.keySet()) {
					// key was found
					//TODO above, inside, below, etc.
					if (attributes.containsKey(attrKey)) {
						if (!attributes.get(attrKey).equals(temp.get(attrKey))) {
							if (attrKey.equals("size")) {
								attrChange.put(attrKey, CHANGE.SCALED);
							} else if (attrKey.equals("angle")) {
								attrChange.put(attrKey, CHANGE.ROTATED);
							} else if (attrKey.equals("fill")) {
								attrChange.put(attrKey, CHANGE.FILL);
							} else if (attrKey.equals("shape")) {
								attrChange.put(attrKey, CHANGE.SHAPE);
							} else {
								attrChange.put(attrKey, CHANGE.TRANSLATION);
							}
						}

					} else {
						attributes.put(attrKey, temp.get(attrKey));
						attrChange.put(attrKey, CHANGE.ADDITION);
					}

				}
				// search for deletions
				for (String key : attributes.keySet()) {
					if (!temp.containsKey(key)) {
						attributes.remove(key);
						attrChange.put(key, CHANGE.DELETION);

					}
				}
				change.put(objectKey, attrChange);
			}
		}

		return change;
	}

	//
	// Unchanged
	// 5 points
	// Reflected
	// 4 points
	// Rotated
	// 3 points
	// Scaled
	// 2 points
	// Deleted
	// 1 point
	// Shape unchanged
	// 0 point

	enum CHANGE {
		NO_CHANGE, REFLECTED, ROTATED, SCALED, SHAPE, FILL, ADDITION, DELETION, TRANSLATION,
	}
}
