package presenter;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import model.Model;
import view.View;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

import commands.Command;

/**
 * The Class Presenter.
 */
public class Presenter implements Observer {

	/** The Model. */
	Model m;

	/** The View. */
	View v;

	/** The list of names. */
	LinkedList<String> nlist;
	
	String latestMaze;


	/**
	 * Instantiates a new presenter.
	 *
	 * @param v
	 *            The view to be set
	 * @param m
	 *            The model to be set
	 */
	public Presenter(View v, Model m) {
		this.v = v;
		this.m = m;
		this.nlist = new LinkedList<String>();

		v.setCommands("generate maze", new generateMazeCommand());
		v.setCommands("display maze", new displayMazeCommand());
		v.setCommands("solve maze", new solveMazeCommand());
		v.setCommands("display solution", new displaySolutionCommand());
		v.setCommands("exit", new Command() {

			@Override
			public void doCommand(String cmd) {
				System.out.println("exiting...");
			}

		});
		v.setCommands("list", new Command() {

			@Override
			public void doCommand(String cmd) {
				System.out.println("\nList of commands:");
				System.out
						.println("generate maze    <name> <x> <y> ---- generate maze with the specified name, x rows y columns");
				System.out
						.println("display maze     <name>         ---- display the maze with the specified name");
				System.out
						.println("solve maze       <name>         ---- solve the maze with the specified name");
				System.out
						.println("display solution <name>         ---- display the solution for the maze with the specified name");
				System.out.println("exit");

			}
		});
	}

	/**
	 * Update.
	 *
	 * Gets notices from the observable's which the object is subscribed to, And
	 * executes the relevant steps.
	 *
	 * @param o
	 *            observer to notify
	 * @param obj
	 *            the object being passed by the observable
	 */
	@Override
	public void update(Observable o, Object obj) {
		if (o == m) {
			switch((String)(obj)){
			case "maze":
				latestMaze = nlist.poll();
				Maze maze = m.getMaze(latestMaze);
				if(maze != null)
					v.displayMaze(maze);
				break;
				
			case "solution":
				Solution s = m.getSolution(latestMaze);
				if(s != null)
					v.displaySolution(s);
				break;
				
			case "not connected":
				v.displayError("Could not connect to Server!");
				break;
				
			case "already exists":
				v.displayError("Maze with this name already exists!");
				break;
			case "doesn't exists":
				v.displayError("Maze with this name doesn't exists!");
			}
		}
		else if (o == v) {
			Command cmd;
			String[] s = null;
			if(obj != null){
				s = ((String)(obj)).split(" ");
				cmd = v.getUserCommand(s[0] + " " + s[1]);

			}
			else{
			cmd = v.getUserCommand(null);
			}
			if (cmd != null) {
				String arg = null;
				if(s!=null)
					switch(s[0]){
					case "generate":
						arg = s[2] + " " + s[3] + " " + s[4];
						break;
						
					case "solve":
						arg = "";
						for(int i = 0; i < 5; i++){
							arg += s[i+2];
							if(i == 4)
								continue;
							arg += " ";
						}
						break;
					case "display":
						arg =  latestMaze = s[2] ;
						break;
					}
				else
					arg = v.getUserArg(null);	
				cmd.doCommand(arg);
			}
		}
	}

	// ##################### Commands:

	/**
	 * The Class generateMazeCommand.
	 */
	public class generateMazeCommand implements Command {

		/**
		 * doCommand.
		 * 
		 * A command to generate a new Maze.
		 * 
		 * @param arg
		 *            The arguments to the command, includes the name of the
		 *            maze, and its size (rows*columns).
		 */
		@Override
		public void doCommand(String arg) {
			String[] args = arg.split(" ");
			if (args.length == 3) {
//				if (m.isMazeExist(args[0])) {
//					System.out.println("Maze with this name already exists!");
//					return;
//				}
				nlist.add(args[0]);

				int rows = Integer.parseInt(args[1]);
				int cols = Integer.parseInt(args[2]);

				m.generateMaze(args[0], rows, cols);
			} else
				System.out.println("args error");
		}
	}

	/**
	 * The Class displayMazeCommand.
	 */
	public class displayMazeCommand implements Command {

		/**
		 * doCommand.
		 * 
		 * A command to display a maze.
		 * 
		 * @param mName
		 *            The arguments to the command, the name of the maze to be
		 *            displayed.
		 */
		@Override
		public void doCommand(String mName) {
			Maze maze = m.getMaze(mName);
			if(maze != null)
				v.displayMaze(maze);
		}
	}

	/**
	 * The Class solveMazeCommand.
	 */
	public class solveMazeCommand implements Command {

		/**
		 * doCommand.
		 * 
		 * A command to solve a given maze.
		 * 
		 * @param mName
		 *            The arguments to the command, the name of the maze to
		 *            solve.
		 */
		@Override
		public void doCommand(String mName) {
//			String[] s = mName.split(" ");
//			Maze maze = m.getMaze(s[0]);
//			SearchableMaze sm = new SearchableMaze(maze, false);
//			if(s.length > 1){
//				MazeState sState = new MazeState(s[1] + " " + s[2]);
//				MazeState gState = new MazeState(s[3] + " " + s[4]);
//				sm.setStartState(sState);
//				sm.setgState(gState);
//			}
			m.solveMaze(mName);
		}
	}

	/**
	 * The Class displaySolutionCommand.
	 */
	public class displaySolutionCommand implements Command {

		/**
		 * doCommand.
		 * 
		 * A command to display a solution.
		 * 
		 * @param mName
		 *            The arguments to the command, the name of the maze to
		 *            whose solution to be displayed.
		 */
		@Override
		public void doCommand(String mName) {
			String[] s = mName.split(" ");
			v.displaySolution(m.getSolution(s[0]));
		}
	}

}
