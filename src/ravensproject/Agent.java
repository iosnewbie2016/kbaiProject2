package ravensproject;

import java.util.ArrayList;
import java.util.Arrays;
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
		// Algorithm
		// 1. construct matrix
		// 2. findDifference of all the answer choices
		// - returns a relationship
		// 3. see which difference works.
		System.out
				.println("/*************************solver started***************************************/");
		System.out.println("problem Name: " + problem.getName());
		int panels = problem.getProblemType().equals("2x2") ? 3 : 8;
		int matrixSize = panels == 3 ? 2 : 3;
		RavensFigure[][] matrix = new RavensFigure[matrixSize][matrixSize];
		int choices = panels == 3 ? 6 : 8;
		boolean[] validChoices = new boolean[choices];
		int[] scores = new int[choices];
		Arrays.fill(scores, -1);
		HashMap<String, RavensFigure> figures = problem.getFigures();

		char panelSelection = 'A';
		/************************************** semantic network ****************************************/
		// 1. iterate through 0 - panels + 'A' and convert to raven figure
		// 2. iterate through 0 - choices and convert to raven figure

		// initialize matrix
		for (int i = 0; i < panels; i++) {

			matrix[i / matrixSize][i % matrixSize] = figures.get(Character
					.valueOf((char) (panelSelection + i)).toString());
		}
		/************************************** generate and test ****************************************/
		// only generate possible relationships
		/***************** relationships down ********************/

		List<List<Map<String, CHANGE>>> differenceY = null;
		int x = matrixSize - 1;
		List<RavensFigure> listY = new ArrayList<>();
		for (int y = 0; y < matrixSize - 1; y++) {
			listY.add(matrix[x][y]);
		}
		differenceY = generateRelationship(listY);

		/***************** relationships across **********************/

		List<List<Map<String, CHANGE>>> differenceX = null;
		int y = matrixSize - 1;
		List<RavensFigure> listX = new ArrayList<>();
		for (x = 0; x < matrixSize - 1; x++) {
			listX.add(matrix[x][y]);
		}

		differenceX = generateRelationship(listX);
		// TEST
		// TEST transformation
		// for all answers see if there is at least one answer that passes the

		for (int i = 1; i <= choices; i++) {
			// add answer choice to last element
			listY.add(figures.get(i + ""));
			listX.add(figures.get(i + ""));
			differenceY = generateRelationship(listY);
			differenceX = generateRelationship(listX);

			// TEST answer choice
			boolean isPossibleX = testReleationship("x", differenceX, matrix);
			boolean isPossibleY = testReleationship("y", differenceY, matrix);
			validChoices[i - 1] = isPossibleX && isPossibleY;
			scores[i - 1] = calculateScore(differenceX)
					+ calculateScore(differenceY);

			// removing last element
			listY.remove(listY.size() - 1);
			listX.remove(listX.size() - 1);

		}
		int result = -1;
		int resultIndex = -1;
		for (int z = 0; z < scores.length; z++) {
			if (validChoices[z] && scores[z] > result) {
				result = scores[z];
				resultIndex = z;
			}
		}

		return resultIndex;

		// // return top choices
		// // relationship
		//
		// for (String key : figures.keySet()) {
		// System.out
		// .println("######################Figure###############################");
		// System.out.println("Raven Figure: " + key);
		// RavensFigure figure = figures.get(key);
		//
		// System.out.println("figure name: " + figure.getName());
		//
		// // construct a matrix
		// // 3. use generate and test for valid relations
		// // 4. use means-ends analysis to find optimal answer
		// System.out.println("figure visual: " + figure.getVisual());
		//
		// for (String objKey : figure.getObjects().keySet()) {
		// System.out
		// .println("-------------------- objects ----------------------");
		// System.out.println("Raven Obj: " + objKey);
		// RavensObject obj = figure.getObjects().get(objKey);
		// System.out.println("Object: " + obj.getName());
		// for (String attrKey : obj.getAttributes().keySet()) {
		// System.out.println("attribute: " + attrKey);
		// System.out.println("attribute value: "
		// + obj.getAttributes().get(attrKey));
		// }
		//
		// }
		//
		// }

	}

	private boolean testReleationship(String string,
			List<List<Map<String, CHANGE>>> differenceX, RavensFigure[][] matrix) {
		// TODO Auto-generated method stub
		return false;
	}

	// difference of all the frames
	private List<List<Map<String, CHANGE>>> generateRelationship(
			List<RavensFigure> list) {
		List<List<Map<String, CHANGE>>> difference = new ArrayList<>();
		RavensFigure lastFrame = null;
		for (RavensFigure figure : list) {
			if (lastFrame == null) {
				lastFrame = figure;
			}

			difference.add(findDifference(figure, lastFrame));

		}
		return difference;
	}

	/**
	 * return all the difference between the 2 frames
	 * 
	 * @param figure
	 * @param lastFrame
	 * @return
	 */
	private List<Map<String, CHANGE>> findDifference(RavensFigure figure,
			RavensFigure lastFrame) {
		List<Map<String, CHANGE>> frameDiff = new ArrayList<>();

		// an object can be added/ removed or changed.
		// hashMap = change for object
		// List<hashMap>= possible changes for object

		for (String figureKey : figure.getObjects().keySet()) {

			RavensObject figureObject = figure.getObjects().get(figureKey);
			for (String lastFrameKey : lastFrame.getObjects().keySet()) {
				RavensObject lastFrameObject = lastFrame.getObjects().get(
						lastFrameKey);

				// compare objects
				frameDiff.add(findObjDifference(figureObject, lastFrameObject));

			}

		}

		return frameDiff;
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

	private Map<String, CHANGE> findObjDifference(RavensObject figureObject,
			RavensObject lastFrameObject) {
		Map<String, CHANGE> difference = new HashMap<>();
		// this is a set intersect
		String[] figureObjectAttrKey = (String[]) figureObject.getAttributes()
				.keySet().toArray();
		int figureObjAttrLength = figureObjectAttrKey.length;
		String[] lastFrameObjectAttrKey = (String[]) lastFrameObject
				.getAttributes().keySet().toArray();
		List<String> attrFound = new ArrayList<>();
		int lastFrameObjAttrLength = lastFrameObjectAttrKey.length;
		for (int i = 0; i < lastFrameObjAttrLength; i++) {
			for (int j = 0; j < figureObjAttrLength; j++) {
				if (lastFrameObjectAttrKey[i].equals(figureObjectAttrKey[j])) {
					// same attr
					String attrKey = lastFrameObjectAttrKey[i];
					CHANGE attrChange = CHANGE.NO_CHANGE;
					if (!figureObject
							.getAttributes()
							.get(attrKey)
							.equals(lastFrameObject.getAttributes()
									.get(attrKey))) {

						if (attrKey.equals("size")) {
							attrChange = CHANGE.SCALED;
						} else if (attrKey.equals("angle")) {
							attrChange = CHANGE.ROTATED;
						} else if (attrKey.equals("fill")) {
							attrChange = CHANGE.FILL;
						} else {
							attrChange = CHANGE.TRANSLATION;
						}

					}
					attrFound.add(attrKey);
					difference.put(attrKey, attrChange);
				}
			}
		}

		for (String lf_attr : lastFrameObjectAttrKey) {
			if (!attrFound.contains(lf_attr)) {
				difference.put(lf_attr, CHANGE.DELETION);
			}
		}
		for (String f_attr : figureObjectAttrKey) {
			if (!attrFound.contains(f_attr)) {
				difference.put(f_attr, CHANGE.ADDITION);
			}
		}

		// the the same shape
		// for all the objects in figure that wasnt selected, addition
		// for all the object in last frame that wasn't selected, deletion
		return difference;
	}

	private int calculateScore(List<List<Map<String, CHANGE>>> difference) {
		int score = 0;
		for (List<Map<String, CHANGE>> frame : difference) {
			for (Map<String, CHANGE> object : frame) {
				score += calculateScore(object);
			}
		}
		return score;
	}

	private int calculateScore(Map<String, CHANGE> attributes) {
		CHANGE[] changes = (CHANGE[]) attributes.values().toArray();
		int score = 0;
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
		for (CHANGE change : changes) {
			if (change.equals(CHANGE.NO_CHANGE)) {
				score += 5;
			} else if (change.equals(CHANGE.REFLECTED)) {
				score += 4;
			} else if (change.equals(CHANGE.ROTATED)) {
				score += 3;
			} else if (change.equals(CHANGE.SCALED)
					|| change.equals(CHANGE.FILL)) {
				score += 2;
			} else if (change.equals(CHANGE.ADDITION)
					|| change.equals(CHANGE.DELETION)) {
				score += 1;
			}
		}
		return score;
	}

	enum CHANGE {
		NO_CHANGE, REFLECTED, ROTATED, SCALED, FILL, ADDITION, DELETION, TRANSLATION,
	}
}
