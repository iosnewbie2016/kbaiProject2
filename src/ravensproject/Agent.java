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
		int[] validChoices = new int[choices];
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
		int isPossibleX = testReleationship("x", matrixSize, matrix);
		int isPossibleY = testReleationship("y", matrixSize, matrix);
		System.out.println(" X+Y" + isPossibleX + " " + isPossibleY);
		for (int i = 1; i <= choices; i++) {
			// add answer choice to last element
			listY.add(figures.get(i + ""));
			listX.add(figures.get(i + ""));
			differenceY = generateRelationship(listY);
			differenceX = generateRelationship(listX);
			// TODO[DEBUG] remove
			// if (problem.getName().equals("Basic Problem B-05")) {
			// if (i == 4 || i == -1) {
			// System.out.println("===============================ANSWER" + i);
			//
			// System.out.println("difference in Y ");
			// printRelationship(differenceY);
			// System.out.println("difference in X ");
			// printRelationship(differenceX);
			// }
			// }
			int scoreX = calculateScore(differenceX);
			int scoreY = calculateScore(differenceY);

			// TEST answer choice
			if (isPossibleY - scoreY == 0) {
				validChoices[i - 1]++;
			}
			if (isPossibleX - scoreX == 0) {
				validChoices[i - 1]++;
			}
			// validChoices[i - 1] = isPossibleX && isPossibleY;
			scores[i - 1] = Math.abs(((isPossibleY - scoreY))
					+ (isPossibleX - scoreX));
			System.out.println("scores[" + (i - 1) + "] = " + scoreX + " "
					+ scoreY + "=" + scores[i - 1]);

			// removing last element
			listY.remove(listY.size() - 1);
			listX.remove(listX.size() - 1);

		}
		int result = Integer.MAX_VALUE;
		int resultIndex = -2;
		int index = 0;
		for (int z = 0; z < scores.length; z++) {
			if (scores[z] < result
					|| (scores[z] == result && validChoices[z] > validChoices[index])) {
				result = scores[z];
				resultIndex = z;
				index = z;
			}

		}
		
		int sum =0;
		for(int score:scores){
			sum+=score;
		}
		System.out.println(sum);
		if(sum==0){
			resultIndex=-2;
		}
		System.out.println("The Answer picked is: " + (resultIndex + 1));
		return resultIndex + 1;

	}

	// prints out the relationship
	// for debugging purposes only
	private String printRelationship(List<List<Map<String, CHANGE>>> difference) {
		int frameIndex = 1;
		for (List<Map<String, CHANGE>> frame : difference) {
			int objectIndex = 1;
			System.out.println("frame" + frameIndex
					+ "--------------------------------");
			for (Map<String, CHANGE> object : frame) {
				System.out.println("object" + objectIndex
						+ "--------------------------------");
				for (String attrKey : object.keySet()) {
					System.out.println(attrKey + " : " + object.get(attrKey));
				}
				objectIndex++;
			}
			frameIndex++;
		}
		return null;
	}

	private int testReleationship(String string, int matrixSize,
			RavensFigure[][] matrix) {
		// step 1. generate relationships for all rows and columns except answer
		// row/column
		// step 2. compare relationship with answer relationship
		// step 3. generate a score for the difference in relationship
		// a. no shapes
		int score = 0;
		List<List<Map<String, CHANGE>>> frameDifference = null;
		List<RavensFigure> list = new ArrayList<>();
		if (string.equals("x")) {

			for (int y = 0; y < matrixSize - 1; y++) {

				for (int x = 0; x < matrixSize; x++) {
					list.add(matrix[x][y]);
				}
				frameDifference = generateRelationship(list);
				//TODO REMOVE
				// System.out.println("X");
				// printRelationship(frameDifference);
				
				
				score += calculateScore(frameDifference);
				list.clear();
			}
		} else {
			for (int x = 0; x < matrixSize - 1; x++) {

				for (int y = 0; y < matrixSize; y++) {
					list.add(matrix[x][y]);
				}
				frameDifference = generateRelationship(list);
				//TODO REMOVE
				System.out.println("Y");
				printRelationship(frameDifference);
				score += calculateScore(frameDifference);
				list.clear();
			}
		}

		return score / (matrixSize - 1);
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
		// TODO: fix this
		String[] figureKeys = figure.getObjects().keySet()
				.toArray(new String[0]);

		String[] lastFrameKeys = lastFrame.getObjects().keySet()
				.toArray(new String[0]);
		Arrays.sort(figureKeys);
		Arrays.sort(lastFrameKeys);

		int size = figureKeys.length > lastFrameKeys.length ? lastFrameKeys.length
				: figureKeys.length;

		for (int i = 0; i < size; i++) {
			RavensObject figureObject = figure.getObjects().get(figureKeys[i]);
			RavensObject lastFrameObject = lastFrame.getObjects().get(
					lastFrameKeys[i]);
			frameDiff.add(findObjDifference(figureObject, lastFrameObject));
		}

		if (size < figureKeys.length) {
			for (int j = size; j < figureKeys.length; j++) {
				RavensObject figureObject = figure.getObjects().get(
						figureKeys[j]);
				frameDiff.add(findObjDifference(figureObject, null));
			}
		}
		if (size < lastFrameKeys.length) {
			for (int j = size; j < lastFrameKeys.length; j++) {
				RavensObject lastFrameObject = lastFrame.getObjects().get(
						lastFrameKeys[j]);
				frameDiff.add(findObjDifference(null, lastFrameObject));
			}
		}

		// for (String figureKey : figure.getObjects().keySet()) {
		//
		// RavensObject figureObject = figure.getObjects().get(figureKey);
		// for (String lastFrameKey : lastFrame.getObjects().keySet()) {
		// RavensObject lastFrameObject = lastFrame.getObjects().get(
		// lastFrameKey);
		// // compare objects
		// frameDiff.add(findObjDifference(figureObject, lastFrameObject));
		// }
		// }

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

		if (lastFrameObject == null) {
			for (String key : figureObject.getAttributes().keySet()) {
				if (key.equals("shape")) {
					difference.put(key, CHANGE.ADDITION);
				} else {
					difference.put(key, CHANGE.NO_CHANGE);
				}
			}
			return difference;

		} else if (figureObject == null) {

			for (String key : lastFrameObject.getAttributes().keySet()) {
				if (key.equals("shape")) {
					difference.put(key, CHANGE.DELETION);
				} else {
					difference.put(key, CHANGE.NO_CHANGE);
				}
			}
			return difference;

		}

		// this is a set intersect
		String[] figureObjectAttrKey = (String[]) figureObject.getAttributes()
				.keySet().toArray(new String[0]);
		int figureObjAttrLength = figureObjectAttrKey.length;
		String[] lastFrameObjectAttrKey = (String[]) lastFrameObject
				.getAttributes().keySet().toArray(new String[0]);
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
						if (attrKey.equals("inside")) {
							attrChange = CHANGE.NO_CHANGE;
						}else if (attrKey.equals("size")) {
							attrChange = CHANGE.SCALED;
						} else if (attrKey.equals("angle")) {
							int figureValue = Integer.valueOf(figureObject
									.getAttributes().get(attrKey));
							int lastFrameValue = Integer
									.valueOf(lastFrameObject.getAttributes()
											.get(attrKey));

							int value = figureValue - lastFrameValue;

							if (value > 180) {
								value -= 360;
							}
							if (value == 45) {
								attrChange = CHANGE.ROTATED_45;
							} else if (value == 90) {
								attrChange = CHANGE.ROTATED_90;
							} else if (value == 135) {
								attrChange = CHANGE.ROTATED_135;
							} else if (value == 180) {
								attrChange = CHANGE.ROTATED_180;
							} else if (value == 225) {
								attrChange = CHANGE.ROTATED_225;
							} else if (value == 270) {
								attrChange = CHANGE.ROTATED_270;
							} else if (value == 315) {
								attrChange = CHANGE.ROTATED_315;
							} else if (value == -45) {
								attrChange = CHANGE.ROTATED_NEG_45;
							} else if (value == -90) {
								attrChange = CHANGE.ROTATED_NEG_90;
							} else if (value == -135) {
								attrChange = CHANGE.ROTATED_NEG_135;
							} else if (value == -180) {
								attrChange = CHANGE.ROTATED_NEG_180;
							} else if (value == -225) {
								attrChange = CHANGE.ROTATED_NEG_225;
							} else if (value == -270) {
								attrChange = CHANGE.ROTATED_NEG_270;
							} else if (value == -315) {
								attrChange = CHANGE.ROTATED_NEG_315;
							}

						} else if (attrKey.equals("fill")) {
							if (figureObject.getAttributes().get(attrKey)
									.equals("no")) {
								attrChange = CHANGE.UNFILL;
							} else {
								attrChange = CHANGE.FILL;
							}
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
		CHANGE[] changes = (CHANGE[]) attributes.values()
				.toArray(new CHANGE[0]);
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
				score += 0;
			} else if (change.equals(CHANGE.REFLECTED)) {
				score -= 6;
			} else if (change.equals(CHANGE.ROTATED_45)) {
				score -= 7;
			} else if (change.equals(CHANGE.ROTATED_90)) {
				score -= 8;
			} else if (change.equals(CHANGE.ROTATED_135)) {
				score -= 9;
			} else if (change.equals(CHANGE.ROTATED_180)) {
				score -= 10;
			} else if (change.equals(CHANGE.ROTATED_225)) {
				score -= 11;
			} else if (change.equals(CHANGE.ROTATED_270)) {
				score -= 12;
			} else if (change.equals(CHANGE.ROTATED_315)) {
				score -= 13;
			} else if (change.equals(CHANGE.ROTATED_NEG_45)) {
				score += 6;
			} else if (change.equals(CHANGE.ROTATED_NEG_90)) {
				score += 7;
			} else if (change.equals(CHANGE.ROTATED_NEG_135)) {
				score += 8;
			} else if (change.equals(CHANGE.ROTATED_NEG_180)) {
				score += 9;
			} else if (change.equals(CHANGE.ROTATED_NEG_225)) {
				score += 10;
			} else if (change.equals(CHANGE.ROTATED_NEG_270)) {
				score += 11;
			} else if (change.equals(CHANGE.ROTATED_NEG_315)) {
				score += 12;
			} else if (change.equals(CHANGE.SCALED)
					|| change.equals(CHANGE.FILL)) {
				score -= 5;
			} else if (change.equals(CHANGE.UNFILL)) {
				score += 2;
			} else if (change.equals(CHANGE.ADDITION)
					|| change.equals(CHANGE.DELETION)) {
				if (change.equals(CHANGE.ADDITION)) {
					score -= 15;
				} else {
					score -= 15;
				}
			} else if (change.equals(CHANGE.TRANSLATION)) {
				score +=1;
			}
		}
		return score;
	}

	enum CHANGE {
		NO_CHANGE, REFLECTED, ROTATED_45, ROTATED_90, ROTATED_135, ROTATED_180, ROTATED_225, ROTATED_270, ROTATED_315, ROTATED_NEG_45, ROTATED_NEG_90, ROTATED_NEG_135, ROTATED_NEG_180, ROTATED_NEG_225, ROTATED_NEG_270, ROTATED_NEG_315, SCALED, FILL, UNFILL, ADDITION, DELETION, TRANSLATION,
	}
}
