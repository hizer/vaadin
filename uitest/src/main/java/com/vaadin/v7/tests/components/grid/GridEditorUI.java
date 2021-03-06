/*
 * Copyright 2000-2016 Vaadin Ltd.
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
package com.vaadin.v7.tests.components.grid;

import com.vaadin.server.VaadinRequest;
import com.vaadin.tests.components.AbstractReindeerTestUI;
import com.vaadin.tests.util.PersonContainer;
import com.vaadin.v7.ui.Grid;
import com.vaadin.v7.ui.PasswordField;
import com.vaadin.v7.ui.TextField;

public class GridEditorUI extends AbstractReindeerTestUI {

    @Override
    protected void setup(VaadinRequest request) {
        PersonContainer container = PersonContainer.createWithTestData();

        addComponent(createGrid(container));
    }

    protected Grid createGrid(PersonContainer container) {
        Grid grid = new Grid(container);

        // Don't use address since there's no converter
        grid.removeColumn("address");

        grid.setEditorEnabled(true);

        grid.getColumn("firstName").setEditorField(new PasswordField());

        TextField lastNameField = (TextField) grid.getColumn("lastName")
                .getEditorField();
        lastNameField.setMaxLength(50);

        grid.getColumn("phoneNumber").getEditorField().setReadOnly(true);

        return grid;
    }

}
