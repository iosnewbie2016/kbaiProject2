package ravensproject;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;

public class Agent {
	public Agent() {
	}

	public int Solve(RavensProblem problem) throws IOException {
		HashMap<String, RavensFigure> Ravefigures = problem.getFigures();
		return problem.getProblemType().equals("2x2") ? Solver("A", "B", "C",
				Ravefigures, 6) : Solver("G", "H", "H", Ravefigures, 8);
	}

	private int Solver(String fig1, String fig2, String fig3,
			HashMap<String, RavensFigure> Ravefigures, int choiceNum)
			throws IOException {
		double ratio1 = blackRatio(Ravefigures.get(fig1));
		double ratio2 = blackRatio(Ravefigures.get(fig2));
		double ratio3 = blackRatio(Ravefigures.get(fig3));
		double baseRatio = ratio1 - ratio2;
		double closest = Double.MAX_VALUE;
		int closestAns = -1;
		for (int i = 1; i <= choiceNum; i++) {
			double ratio = ratio3
					- blackRatio(Ravefigures.get(Integer.toString(i)));
			if (Math.abs(ratio - baseRatio) < closest) {
				closest = Math.abs(ratio - baseRatio);
				closestAns = i;
			}
		}
		return closestAns;
	}

	private double blackRatio(RavensFigure thisFigure) throws IOException {
		BufferedImage figureImage = ImageIO.read(new File(thisFigure
				.getVisual()));
		int black = 0, total = figureImage.getWidth() * figureImage.getHeight();
		for (int i = 0; i < figureImage.getWidth(); i++) {
			for (int j = 0; j < figureImage.getHeight(); j++) {
				black += figureImage.getRGB(i, j) != -1 ? 1 : 0;
			}
		}
		return black / (1.0 * total);
	}
}
