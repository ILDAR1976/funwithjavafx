package edu.fx_01.model;

import java.util.List;

import edu.fx_01.model.ObjectsManipulation.Anchor;
import edu.fx_01.model.ObjectsManipulation.BlockIha;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;

public class Utils {

	public static Node pick(Node node, double sceneX, double sceneY) {
		Point2D p = node.sceneToLocal(sceneX, sceneY, true);
		if (!node.contains(p))
			return null;
		if (node instanceof Parent) {
			Node bestMatchingChild = null;
			List<Node> children = ((Parent) node).getChildrenUnmodifiable();
			for (int i = children.size() - 1; i >= 0; i--) {
				Node child = children.get(i);
				p = child.sceneToLocal(sceneX, sceneY, true);
				if (child.isVisible() && !child.isMouseTransparent() && child.contains(p)) {
					bestMatchingChild = child;
					break;
				}
			}

			if (bestMatchingChild != null) {
				return pick(bestMatchingChild, sceneX, sceneY);
			}
		}

		return node;
	}

	static void linkAnchor(Anchor self, Object point) {
		Anchor jobPoint = (Anchor) point;
		if (jobPoint != self) {
			
			self.linkAnchor.add(jobPoint);
			self.linkAnchor.addAll(jobPoint.linkAnchor);

			Anchor main = null;
			
			for (Anchor item : self.linkAnchor) {
				if (item != self){
					item.linkAnchor.addAll(self.linkAnchor);
				}
				
				if (item.getParent() instanceof BlockIha) { 
					main = item;
				}
			}
			
			if (main != null && !(self.getParent() instanceof BlockIha)) {
				
				self.centerXProperty().bind(main.centerXProperty());
				self.centerYProperty().bind(main.centerYProperty());
				
				self.getParent().toBack();
				main.getParent().toFront();
				
			} else {

				jobPoint.centerXProperty().bind(self.centerXProperty());
				jobPoint.centerYProperty().bind(self.centerYProperty());

				jobPoint.getParent().toBack();
				self.getParent().toFront();

				jobPoint.linkAnchor.addAll(self.linkAnchor);
				jobPoint.linkAnchor.add(self);
			
			}
		}
	}

}
