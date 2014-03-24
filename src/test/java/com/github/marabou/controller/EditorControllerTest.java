package com.github.marabou.controller;

import com.github.marabou.db.HSQLDBController;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class EditorControllerTest {

    @Test
    public void saveSelectedFilesInvokesServiceMethod() throws Exception {

        // given
        EditorController controller = new EditorController();
        controller.hsqldbController = mock(HSQLDBController.class);

        // when
        controller.saveSelectedFiles();

        // then
        verify(controller.hsqldbController).saveSelectedFiles();
    }
}
