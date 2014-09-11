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
package com.vaadin.tests.components.grid.basicfeatures;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.vaadin.tests.annotations.TestCategory;
import com.vaadin.tests.tb3.MultiBrowserTest;

@TestCategory("grid")
public abstract class EscalatorBasicClientFeaturesTest extends MultiBrowserTest {
    protected static final String COLUMNS_AND_ROWS = "Columns and Rows";

    protected static final String COLUMNS = "Columns";
    protected static final String ADD_ONE_COLUMN_TO_BEGINNING = "Add one column to beginning";
    protected static final String ADD_ONE_ROW_TO_BEGINNING = "Add one row to beginning";
    protected static final String REMOVE_ONE_COLUMN_FROM_BEGINNING = "Remove one column from beginning";
    protected static final String REMOVE_ONE_ROW_FROM_BEGINNING = "Remove one row from beginning";

    protected static final String HEADER_ROWS = "Header Rows";
    protected static final String BODY_ROWS = "Body Rows";
    protected static final String FOOTER_ROWS = "Footer Rows";

    protected static final String GENERAL = "General";
    protected static final String POPULATE_COLUMN_ROW = "Populate Escalator (columns, then rows)";
    protected static final String POPULATE_ROW_COLUMN = "Populate Escalator (rows, then columns)";
    protected static final String CLEAR_COLUMN_ROW = "Clear (columns, then rows)";
    protected static final String CLEAR_ROW_COLUMN = "Clear (rows, then columns)";

    protected static final String FEATURES = "Features";
    protected static final String COLUMN_SPANNING = "Column spanning";
    protected static final String COLSPAN_NORMAL = "Apply normal colspan";
    protected static final String COLSPAN_NONE = "Apply no colspan";

    @Override
    protected Class<?> getUIClass() {
        return EscalatorBasicClientFeatures.class;
    }

    protected WebElement getEscalator() {
        return getDriver().findElement(By.className("v-escalator"));
    }

    protected WebElement getHeaderRow(int row) {
        return getRow("thead", row);
    }

    protected WebElement getBodyRow(int row) {
        return getRow("tbody", row);
    }

    protected WebElement getFooterRow(int row) {
        return getRow("tfoot", row);
    }

    protected WebElement getHeaderCell(int row, int col) {
        return getCell("thead", row, col);
    }

    protected WebElement getBodyCell(int row, int col) {
        return getCell("tbody", row, col);
    }

    protected WebElement getFooterCell(int row, int col) {
        return getCell("tfoot", row, col);
    }

    private WebElement getCell(String sectionTag, int row, int col) {
        WebElement rowElement = getRow(sectionTag, row);
        if (rowElement != null) {
            try {
                return rowElement.findElement(By.xpath("*[" + (col + 1) + "]"));
            } catch (NoSuchElementException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    private WebElement getRow(String sectionTag, int row) {
        WebElement escalator = getEscalator();
        WebElement tableSection = escalator.findElement(By.tagName(sectionTag));

        try {
            return tableSection.findElement(By.xpath("tr[" + (row + 1) + "]"));
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    protected void selectMenu(String menuCaption) {
        WebElement menuElement = getMenuElement(menuCaption);
        Dimension size = menuElement.getSize();
        new Actions(getDriver()).moveToElement(menuElement, size.width - 10,
                size.height / 2).perform();
    }

    private WebElement getMenuElement(String menuCaption) {
        return getDriver().findElement(
                By.xpath("//td[text() = '" + menuCaption + "']"));
    }

    protected void selectMenuPath(String... menuCaptions) {
        new Actions(getDriver()).moveToElement(getMenuElement(menuCaptions[0]))
                .click().perform();
        for (int i = 1; i < menuCaptions.length - 1; ++i) {
            selectMenu(menuCaptions[i]);
            new Actions(getDriver()).moveByOffset(20, 0).perform();
        }
        new Actions(getDriver())
                .moveToElement(
                        getMenuElement(menuCaptions[menuCaptions.length - 1]))
                .click().perform();
    }

    protected void assertLogContains(String substring) {
        assertTrue("log should've contained, but didn't: " + substring,
                getLogText().contains(substring));
    }

    protected void assertLogDoesNotContain(String substring) {
        assertFalse("log shouldn't have contained, but did: " + substring,
                getLogText().contains(substring));
    }

    private String getLogText() {
        WebElement log = getDriver().findElement(By.cssSelector("#log"));
        return log.getText();
    }

    protected void assertLogContainsInOrder(String... substrings) {
        String log = getLogText();
        int cursor = 0;
        for (String substring : substrings) {
            String remainingLog = log.substring(cursor, log.length());
            int substringIndex = remainingLog.indexOf(substring);
            if (substringIndex == -1) {
                fail("substring \"" + substring
                        + "\" was not found in order from log.");
            }

            cursor += substringIndex + substring.length();
        }
    }

    protected void scrollVerticallyTo(int px) {
        executeScript("arguments[0].scrollTop = " + px, getVeticalScrollbar());
    }

    private WebElement getVeticalScrollbar() {
        return getEscalator().findElement(
                By.className("v-escalator-scroller-vertical"));
    }

    protected Object executeScript(String script, Object... args) {
        return ((JavascriptExecutor) getDriver()).executeScript(script, args);
    }
}
