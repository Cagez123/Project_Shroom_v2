package fi.tamk.tiko.shroom.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import fi.tamk.tiko.shroom.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Project Shroom";
        config.height = 800;
        config.width = 480;
		new LwjglApplication(new Main(), config);
	}
}
