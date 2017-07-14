package edu.fx_01.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.fx_01.ObjectsManipulation.Anchor;
import edu.fx_01.ObjectsManipulation.AnchorList;
import edu.fx_01.ObjectsManipulation.BlockIha;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;

public class Utils {

	private static final int LINK_DIRECT = 0;
	private static final int LINK_CIRCLE = 1;

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

	public static void linkAnchor(Anchor self, Anchor job) {

		if (job != self) {
			
			Anchor master = (job.getParent() instanceof BlockIha) ? job : self;
			Anchor slave = (job.getParent() instanceof BlockIha) ? self : job;
			
			if (self.getParent() instanceof BlockIha ||
			    job.getParent() instanceof BlockIha ) {
				master.setNotMoved(true);
				slave.setNotMoved(true);
			}
			

			master.getLinkAnchor().addAll(slave.getLinkAnchor());
			
			slave.getParent().toBack();
			master.getParent().toFront();
			
			p("master:",master,3);
			p("slave:",slave,3);
			
			if (master.getLinkAnchor().size() <= 2){
				p("A");
				link(master, slave, LINK_DIRECT);
			} else {
				p("B");
				link(master, slave, LINK_CIRCLE);
			}
		}
	}

	private static void link(Anchor master, Anchor slave, int selector) {
		switch (selector) {
		case LINK_DIRECT:
			
			slave.getLinkAnchor().addAll(master.getLinkAnchor());

			slave.centerXProperty().bind(master.centerXProperty());
			slave.centerYProperty().bind(master.centerYProperty());
			
			break;
		
		case LINK_CIRCLE:
			slave.centerXProperty().bind(master.centerXProperty());
			slave.centerYProperty().bind(master.centerYProperty());
			
			for (Anchor item : master.getLinkAnchor()) {

				if (item != master) {
				
					item.getLinkAnchor().addAll(master.getLinkAnchor());
		
					if (master.isNotMoved()) item.setNotMoved(true);
					
					item.getParent().toBack();
				}
			}
			
			break;
		}
	}
	
	public static void p(String msg) {
		System.out.println(msg);
	}

	public static void p(Anchor anr) {
		System.out.println("--------------" + anr.getID() + "--------------");
		for (Anchor item : anr.getLinkAnchor()) {
			System.out.println("| " + item.getParent());
		}
		System.out.println("-------------- End anchor list--------------");
	}

	public static void p(String mark, Anchor anr, int selector) {

		System.out.println("--------------" + mark + " " + anr.getID() + "--------------");
		switch (selector) {
		case 0:
			for (Anchor item : anr.getLinkAnchor()) {
				System.out.println("| " + item.getID().substring(33) + " - " + item.getParent().toString().substring(20));
			}
			break;
		case 1:
			System.out.println("| " + anr.getID().substring(33));
			break;
		case 2:
			System.out.println("| " + anr.getID().substring(33) + " - " + anr.getParent().toString().substring(20));
			break;
		case 3:
			System.out.println("| " + anr.getID().substring(33) + " - " + anr.getParent().toString().substring(20) 
					                + " NotMoved: " + anr.isNotMoved());
			break;
		}
		System.out.println("-------------- End anchor list--------------");
	}

	public static void p(Set<Anchor> anr) {
		System.out.println("-------------- Anchor list --------------");
		for (Anchor item : anr) {
			System.out.println("| " + item.getParent());
		}
		System.out.println("-------------- End anchor list--------------");
	}

	public static void p(String msg, Set<Anchor> anr, int selector) {
		System.out.println("--------------" + msg + " Anchor list --------------");
		for (Anchor item : anr) {
			switch (selector) {
			case 0:
				System.out.println("| " + item.getParent().toString().substring(20));
				break;
			case 1:
				System.out.println("| " + item.getID());

			case 2:
				System.out.println("| " + item.getID() + " - " + item.getParent().toString().substring(20));

			case 3:
				System.out
						.println("| " + item.getID().substring(33) + " - " + item.getParent().toString().substring(20));
			}
		}
		System.out.println("-------------- End anchor list--------------");
	}

	public static void p(boolean parent, Anchor anr) {
		if (parent)
			System.out.println("---Anchor: " + anr.getID() + "--------");
		else
			System.out.println("---Anchor: " + anr.getParent() + "--------");
	}

	public static void s() {
		System.out.println();
	}
}
