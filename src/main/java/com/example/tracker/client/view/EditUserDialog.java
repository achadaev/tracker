package com.example.tracker.client.view;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.presenter.EditUserPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.form.error.BasicEditorError;
import org.gwtbootstrap3.client.ui.form.validator.Validator;
import org.gwtbootstrap3.extras.select.client.ui.Select;
import org.gwtbootstrap3.client.ui.TextBox;

import java.util.ArrayList;
import java.util.List;

import static com.example.tracker.client.constant.PathConstants.MANAGE_PROFILES_PATH;
import static com.example.tracker.client.constant.PathConstants.PROFILE_PATH;
import static com.example.tracker.client.constant.WidgetConstants.INCORRECT_PASSWORD_PATTERN_ERR;
import static com.example.tracker.client.constant.WidgetConstants.PASSWORD_PATTERN;

public class EditUserDialog extends Composite implements EditUserPresenter.Display {
    interface EditUserViewUiBinder extends UiBinder<HTMLPanel, EditUserDialog> {
    }

    @UiField
    Form editForm;
    @UiField
    Form changeForm;

    @UiField
    FormGroup addPasswordGroup;
    @UiField
    FormGroup changePasswordGroup;
    @UiField
    FormGroup roleSelectionGroup;
    @UiField
    FormGroup roleGroup;

    @UiField
    Modal editModal;
    @UiField
    TextBox login;
    @UiField
    TextBox name;
    @UiField
    TextBox surname;
    @UiField
    TextBox email;
    @UiField
    Input password;
    @UiField
    Button changePasswordButton;
    @UiField
    Lead role;
    @UiField
    Select roleSelection;
    @UiField
    Lead regDate;
    @UiField
    Button saveButton;
    @UiField
    Button cancelButton;

    @UiField
    Input newPassword;
    @UiField
    Input confirmPassword;
    @UiField
    Button doChangePasswordButton;
    @UiField
    Modal changeModal;

    private static EditUserViewUiBinder ourUiBinder = GWT.create(EditUserViewUiBinder.class);

    public EditUserDialog(boolean isNewUser) {
        initWidget(ourUiBinder.createAndBindUi(this));

        if (isNewUser) {
            changePasswordGroup.removeFromParent();
        } else {
            addPasswordGroup.removeFromParent();
        }
        if (ExpensesGWTController.isAdmin()) {
            roleGroup.removeFromParent();
        } else {
            roleSelectionGroup.removeFromParent();
        }

        password.addValidator(new Validator<String>() {
            @Override
            public int getPriority() {
                return 0;
            }

            @Override
            public List<EditorError> validate(Editor<String> editor, String s) {
                List<EditorError> result = new ArrayList<>();
                String value = s == null ? "" : s;
                if (!value.matches(PASSWORD_PATTERN)) {
                    result.add(new BasicEditorError(password, s, INCORRECT_PASSWORD_PATTERN_ERR));
                }
                return result;
            }
        });

        newPassword.addValidator(new Validator<String>() {
            @Override
            public int getPriority() {
                return 0;
            }

            @Override
            public List<EditorError> validate(Editor<String> editor, String s) {
                List<EditorError> result = new ArrayList<>();
                String value = s == null ? "" : s;
                if (!value.matches(PASSWORD_PATTERN)) {
                    result.add(new BasicEditorError(newPassword, s, INCORRECT_PASSWORD_PATTERN_ERR));
                }
                return result;
            }
        });

        confirmPassword.addValidator(new Validator<String>() {
            @Override
            public int getPriority() {
                return 0;
            }

            @Override
            public List<EditorError> validate(Editor<String> editor, String s) {
                List<EditorError> result = new ArrayList<>();
                if (!confirmPassword.getValue().equals(newPassword.getValue())) {
                    result.add(new BasicEditorError(confirmPassword, s, "Passwords doesn't matches"));
                }
                return result;
            }
        });
    }

    @Override
    public HasClickHandlers getCancelButton() {
        return cancelButton;
    }

    @Override
    public Form getEditForm() {
        return editForm;
    }

    @Override
    public Form getChangeForm() {
        return changeForm;
    }

    @Override
    public void showEditModal() {
        editModal.show();
    }

    @Override
    public void hideEditModal() {
        editModal.hide();
        if (ExpensesGWTController.isAdmin()) {
            Window.Location.replace(GWT.getHostPageBaseURL() + "#" + MANAGE_PROFILES_PATH);
        } else {
            Window.Location.replace(GWT.getHostPageBaseURL() + "#" + PROFILE_PATH);
        }
    }

    @Override
    public void showChangeModal() {
        changeModal.show();
    }

    @Override
    public void hideChangeModal() {
        changeModal.hide();
    }

    @Override
    public HasClickHandlers getSaveButton() {
        return saveButton;
    }

    @Override
    public TextBox getLogin() {
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
    public HasValue<String> getNewPassword() {
        return newPassword;
    }

    @Override
    public HasValue<String> getConfirmPassword() {
        return confirmPassword;
    }

    @Override
    public Button getDoChangePasswordButton() {
        return doChangePasswordButton;
    }

    @Override
    public HasClickHandlers getChangePasswordButton() {
        return changePasswordButton;
    }

    @Override
    public Lead getRole() {
        return role;
    }

    @Override
    public Select getRoleSelection() {
        return roleSelection;
    }

    @Override
    public Lead getRegDate() {
        return regDate;
    }
}