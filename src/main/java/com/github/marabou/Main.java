package com.github.marabou;

import com.github.marabou.audio.AudioFileFilter;
import com.github.marabou.controller.MainMenuController;
import com.github.marabou.controller.TableController;
import com.github.marabou.model.Model;
import com.github.marabou.view.*;
import com.github.marabou.helper.*;
import com.github.marabou.properties.ApplicationProperties;
import com.github.marabou.properties.PropertiesHelper;
import com.github.marabou.properties.PropertiesLoader;
import com.github.marabou.properties.UserProperties;
import com.github.marabou.service.AudioFileService;
import com.google.common.eventbus.EventBus;

public class Main {

	public static void main(String[] args) {

        PathHelper pathHelper = new PathHelper();
        PropertiesLoader propertiesLoader = new PropertiesLoader(pathHelper);
        PropertiesHelper propertiesHelper = new PropertiesHelper(pathHelper, propertiesLoader);
        UserProperties userProperties = propertiesHelper.getUserProperties();
        ApplicationProperties applicationProperties = propertiesHelper.getApplicationProperties();
        setupMainWindow(applicationProperties, userProperties);
	}

    private static void setupMainWindow(ApplicationProperties applicationProperties, UserProperties userProperties) {
        new BaseGuiClass();
        ImageLoader imageLoader = new ImageLoader();
        AboutWindow aboutWindow = new AboutWindow(applicationProperties.getVersion());

        AudioFileFilter audioFileFilter = new AudioFileFilter();
        AudioFileService audioFileService = new AudioFileService(audioFileFilter);
        EventBus bus = new EventBus();
        Model model = new Model(bus);
        MainMenuController mainMenuController = new MainMenuController(bus, model, audioFileFilter, userProperties, audioFileService, aboutWindow);
        MainMenu mainMenu = new MainMenu(mainMenuController);
        mainMenu.init();

        MainWindow mainWindow = new MainWindow(bus, mainMenu, imageLoader, userProperties);
        TableShell tableShell = new TableShell(mainWindow.getTableComposite());
        new TableController(tableShell, bus);

        mainWindow.init();
    }
}
