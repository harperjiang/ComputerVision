package edu.clarkson.cs.cv;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ConnectedComponent {

	private BufferedImage input;

	private List<List<Run>> runs;

	private Run[][] map;

	public ConnectedComponent(BufferedImage input) {
		this.input = input;
		process();
	}

	protected void process() {
		runs = new ArrayList<List<Run>>();
		map = new Run[input.getWidth()][input.getHeight()];

		// Horizontal Scan
		Run currentRun;
		for (int j = 0; j < input.getHeight(); j++) {
			runs.add(new ArrayList<Run>());
			int color = input.getRGB(0, j);
			Label label = adjacentLabel(0, j, color);
			currentRun = new Run(j, 0, color, null == label ? generateLabel()
					: label);
			map[0][j] = currentRun;
			for (int i = 0; i < input.getWidth(); i++) {
				color = input.getRGB(i, j);
				if (color == currentRun.color) {
					currentRun.end++;
				} else {
					label = adjacentLabel(i, j, color);
					runs.get(j).add(currentRun);
					currentRun = new Run(j, i, color,
							null == label ? generateLabel() : label);
				}
				map[i][j] = currentRun;
			}
			runs.get(j).add(currentRun);
		}

		// Vertical Merge
		for (int i = 0; i < input.getWidth(); i++) {
			for (int j = 0; j < input.getHeight() - 1; j++) {
				if (map[i][j].color == map[i][j + 1].color) {
					merge(map[i][j], map[i][j + 1]);
				}
			}
		}
	}

	protected Label adjacentLabel(int x, int y, int color) {
		Run run = getRun(y, x);
		if (run.color == color)
			return run.label;
		return null;
	}

	protected Run getRun(int row, int column) {
		return map[column][row];
	}

	protected void merge(Run a, Run b) {
		b.label.value = a.label.value;
	}

	static class Run {
		int row;
		int start;
		int end;
		int color;
		Label label;

		public Run(int row, int start, int color, Label label) {
			this.row = row;
			this.start = start;
			this.end = start;
			this.color = color;
			this.label = label;
		}
	}

	static class Label {
		String value;

		public Label(String value) {
			super();
			this.value = value;
		}

		public boolean equals(Object o) {
			if (o instanceof Label) {
				return this.value.equals(((Label) o).value);
			}
			return super.equals(o);
		}

		public int hashCode() {
			return value.hashCode();
		}
	}

	static final Label generateLabel() {
		return new Label(UUID.randomUUID().toString());
	}
}
