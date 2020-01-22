package com.example.tracker.client.view;

import com.example.tracker.client.presenter.EditUserPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

public class EditUserView extends Composite implements EditUserPresenter.Display {
    interface EditUserViewUiBinder extends UiBinder<HTMLPanel, EditUserView> {
    }

    @UiField
    FlexTable table;
    @UiField
    Button saveButton;

    Label login;
    TextBox name;
    TextBox surname;
    TextBox email;
    PasswordTextBox password;
    Label role;
    Label regDate;

    private static EditUserViewUiBinder ourUiBinder = GWT.create(EditUserViewUiBinder.class);

    public EditUserView() {
        initWidget(ourUiBinder.createAndBindUi(this));
        login = new Label();
        name = new TextBox();
        surname = new TextBox();
        email = new TextBox();
        password = new PasswordTextBox();
        role = new Label();
        regDate = new Label();
        initTable();
    }

    private void initTable() {
        table.setText(0, 0, "Login");
        table.setText(1, 0, "Name");
        table.setText(2, 0, "Surname");
        table.setText(3, 0, "Email");
        table.setText(4, 0, "Password");
        table.setText(5, 0, "Role");
        table.setText(6, 0, "Registration Date");

        table.setWidget(0, 1, login);
        table.setWidget(1, 1, name);
        table.setWidget(2, 1, surname);
        table.setWidget(3, 1, email);
        table.setWidget(4, 1, password);
        table.setWidget(5, 1, role);
        table.setWidget(6, 1, regDate);
    }

    @Override
    public HasClickHandlers getSaveButton() {
        return saveButton;
    }

    @Override
    public Label getLogin() {
        return login;
    }

    @Override
    public HasValue<String> getName() {
        return name;
    }

    @Override
    public HasValue<String> getSurname() {
        return surname;
    }

    @Override
    public HasValue<String> getEmail() {
        return email;
    }

    @Override
    public HasValue<String> getPassword() {
        return password;
    }

    @Override
    public Label getRole() {
        return role;
    }

    @Override
    public Label getRegDate() {
        return regDate;
    }
}