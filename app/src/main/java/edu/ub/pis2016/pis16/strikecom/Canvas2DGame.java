package edu.ub.pis2016.pis16.strikecom;


import edu.ub.pis2016.pis16.strikecom.engine.android.AndroidGame;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.screens.CanvasSpriteScreen;

public class Canvas2DGame extends AndroidGame {
	@Override
	public Screen getStartScreen() {
		return new CanvasSpriteScreen(this);
	}
}