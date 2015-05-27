/*
 * Copyright 2000-2014 Vaadin Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.tests.components.grid.basicfeatures.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.vaadin.tests.components.grid.basicfeatures.GridBasicFeatures;
import com.vaadin.tests.components.grid.basicfeatures.GridBasicFeaturesTest;

public abstract class GridEditorTest extends GridBasicFeaturesTest {

    protected static final String[] EDIT_ITEM_5 = new String[] { "Component",
            "Editor", "Edit item 5" };
    protected static final String[] EDIT_ITEM_100 = new String[] { "Component",
            "Editor", "Edit item 100" };
    protected static final String[] TOGGLE_EDIT_ENABLED = new String[] {
            "Component", "Editor", "Enabled" };

    @Before
    public void setUp() {
        setDebug(true);
        openTestURL();
        selectMenuPath(TOGGLE_EDIT_ENABLED);
    }

    @Test
    public void testProgrammaticOpeningClosing() {
        selectMenuPath(EDIT_ITEM_5);
        assertEditorOpen();

        selectMenuPath("Component", "Editor", "Cancel edit");
        assertEditorClosed();
    }

    @Test
    public void testProgrammaticOpeningWhenDisabled() {
        selectMenuPath(TOGGLE_EDIT_ENABLED);
        selectMenuPath(EDIT_ITEM_5);
        assertEditorClosed();
        boolean thrown = logContainsText("Exception occured, java.lang.IllegalStateException");
        assertTrue("IllegalStateException thrown", thrown);
    }

    @Test
    public void testDisablingWhileOpen() {
        selectMenuPath(EDIT_ITEM_5);
        selectMenuPath(TOGGLE_EDIT_ENABLED);
        assertEditorOpen();
        boolean thrown = logContainsText("Exception occured, java.lang.IllegalStateException");
        assertTrue("IllegalStateException thrown", thrown);
    }

    @Test
    public void testProgrammaticOpeningWithScroll() {
        selectMenuPath(EDIT_ITEM_100);
        assertEditorOpen();
    }

    @Test
    public void testKeyboardOpeningClosing() {

        getGridElement().getCell(4, 0).click();
        assertEditorClosed();

        new Actions(getDriver()).sendKeys(Keys.ENTER).perform();
        assertEditorOpen();

        new Actions(getDriver()).sendKeys(Keys.ESCAPE).perform();
        assertEditorClosed();

        // Disable Editor
        selectMenuPath(TOGGLE_EDIT_ENABLED);
        getGridElement().getCell(5, 0).click();
        new Actions(getDriver()).sendKeys(Keys.ENTER).perform();
        assertEditorClosed();
    }

    @Test
    public void testComponentBinding() {
        selectMenuPath(EDIT_ITEM_100);

        List<WebElement> widgets = getEditorWidgets();
        assertEquals("Number of widgets", GridBasicFeatures.EDITABLE_COLUMNS,
                widgets.size());

        assertEquals("(100, 0)", widgets.get(0).getAttribute("value"));
        assertEquals("(100, 1)", widgets.get(1).getAttribute("value"));
        assertEquals("(100, 2)", widgets.get(2).getAttribute("value"));
        assertEquals("<b>100</b>", widgets.get(8).getAttribute("value"));
    }

    protected void assertEditorOpen() {
        assertNotNull("Editor is supposed to be open", getEditor());
        assertEquals("Unexpected number of widgets",
                GridBasicFeatures.EDITABLE_COLUMNS, getEditorWidgets().size());
    }

    protected void assertEditorClosed() {
        assertNull("Editor is supposed to be closed", getEditor());
    }

    protected List<WebElement> getEditorWidgets() {
        assertNotNull(getEditor());
        return getEditor().findElements(By.className("v-textfield"));

    }

    @Test
    public void testUneditableColumn() {
        selectMenuPath(EDIT_ITEM_5);
        assertEditorOpen();

        assertFalse("Uneditable column should not have an editor widget",
                getGridElement().getEditor().isEditable(3));
    }

    protected WebElement getSaveButton() {
        return getDriver().findElement(By.className("v-grid-editor-save"));
    }

    protected WebElement getCancelButton() {
        return getDriver().findElement(By.className("v-grid-editor-cancel"));
    }
}
