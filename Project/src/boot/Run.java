package boot;

import model.MyModel;
import presenter.Preferences;
import presenter.Presenter;
import view.MyView;

public class Run {

	public static void main(String[] args) {
		Preferences pref = new Preferences();
		pref.loadPreferences();
		MyModel m = new MyModel(pref.getSolverAlg(), pref.getGeneratorAlg(), pref.getNumOfThreads());
		MyView v = new MyView();
		Presenter p = new Presenter(v,m);
		m.addObserver(p);
		v.addObserver(p);

		v.start();
		m.stop();
	}

}
