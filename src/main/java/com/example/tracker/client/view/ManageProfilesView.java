package com.example.tracker.client.view;

import com.example.tracker.client.presenter.ManageProfilesPresenter;
import com.example.tracker.shared.model.User;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.*;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.MultiSelectionModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ManageProfilesView extends Composite implements ManageProfilesPresenter.Display {
    interface ManageViewUiBinder extends UiBinder<HTMLPanel, ManageProfilesView> {
    }

    @UiField
    HTMLPanel tablePanel;
    @UiField
    Button addButton;
    @UiField
    Button editButton;
    @UiField
    Button deleteButton;

    private CellTable<User> userTable;

    private MultiSelectionModel<User> selectionModel;

    private static ManageViewUiBinder ourUiBinder = GWT.create(ManageViewUiBinder.class);

    public ManageProfilesView() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setData(List<User> userList) {
        userTable = new CellTable<>();
        userTable.setVisible(true);
        selectionModel = new MultiSelectionModel<>();
        userTable.setSelectionModel(selectionModel);
        tablePanel.clear();

        CheckboxCell checkboxCell = new CheckboxCell();

        Column<User, Boolean> checkColumn = new Column<User, Boolean>(checkboxCell) {
            @Override
            public Boolean getValue(User user) {
                return selectionModel.isSelected(user);
            }
        };

        CheckboxCell checkAllHeaderCB = new CheckboxCell(true, true);
        Header<Boolean> checkAllHeader = new Header<Boolean>(checkAllHeaderCB) {
            @Override
            public Boolean getValue() {
                return selectionModel.getSelectedSet().size() == userList.size();
            }
        };

        checkAllHeader.setUpdater(value -> {
            for (User user : userList) {
                selectionModel.setSelected(user, value);
            }
        });
        userTable.addColumn(checkColumn, checkAllHeader);

        Column<User, Number> idColumn = new Column<User, Number>(new NumberCell()) {
            @Override
            public Number getValue(User user) {
                return user.getId();
            }
        };
        userTable.addColumn(idColumn, "ID");

        TextColumn<User> typeColumn = new TextColumn<User>() {
            @Override
            public String getValue(User user) {
                return user.getLogin();
            }
        };
        userTable.addColumn(typeColumn, "Login");

        TextColumn<User> nameColumn = new TextColumn<User>() {
            @Override
            public String getValue(User user) {
                return user.getName();
            }
        };
        userTable.addColumn(nameColumn, "Name");

        TextColumn<User> surnameColumn = new TextColumn<User>() {
            @Override
            public String getValue(User user) {
                return user.getSurname();
            }
        };
        userTable.addColumn(surnameColumn, "Surname");

        TextColumn<User> emailColumn = new TextColumn<User>() {
            @Override
            public String getValue(User user) {
                return user.getEmail();
            }
        };
        userTable.addColumn(emailColumn, "Email");

        TextColumn<User> passwordColumn = new TextColumn<User>() {
            @Override
            public String getValue(User user) {
                return user.getPassword();
            }
        };
        userTable.addColumn(passwordColumn, "Password");

        TextColumn<User> roleColumn = new TextColumn<User>() {
            @Override
            public String getValue(User user) {
                return user.getRole();
            }
        };
        userTable.addColumn(roleColumn, "Role");

        DateCell dateCell = new DateCell();
        Column<User, Date> dateColumn = new Column<User, Date>(dateCell) {
            @Override
            public Date getValue(User user) {
                return user.getRegDate();
            }
        };
        userTable.addColumn(dateColumn, "Registration Date");

        userTable.setPageSize(10);
        userTable.setRowData(0, userList);
        SimplePager pager = new SimplePager();
        pager.setDisplay(userTable);

        AsyncDataProvider<User> provider = new AsyncDataProvider<User>()
        {
            @Override
            protected void onRangeChanged(HasData<User> display)
            {
                int start = display.getVisibleRange().getStart();
                int end = start + display.getVisibleRange().getLength();
                end = Math.min(end, userList.size());
                List<User> sub = userList.subList(start, end);
                updateRowData(start, sub);
            }
        };
        provider.addDataDisplay(userTable);
        provider.updateRowCount(userList.size(), true);

        tablePanel.add(userTable);
    }

    @Override
    public HasClickHandlers getAddButton() {
        return addButton;
    }

    @Override
    public HasClickHandlers getEditButton() {
        return editButton;
    }

    @Override
    public HasClickHandlers getDeleteButton() {
        return deleteButton;
    }

    @Override
    public List<Integer> getSelectedIds() {
        List<Integer> selectedRows = new ArrayList<>();

        for (User user : selectionModel.getSelectedSet()) {
            selectedRows.add(user.getId());
        }

        return selectedRows;
    }

    @Override
    public Widget asWidget() {
        return this;
    }
}