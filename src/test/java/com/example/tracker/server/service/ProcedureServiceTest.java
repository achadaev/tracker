package com.example.tracker.server.service;

import com.example.tracker.server.dao.IProcedureDAO;
import com.example.tracker.server.dao.IUserDAO;
import com.example.tracker.shared.model.Procedure;
import com.example.tracker.shared.model.User;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProcedureServiceTest {

    @Spy
    @InjectMocks
    private ProcedureService procedureService;

    @Mock
    IProcedureDAO iProcedureDAO;

    @Mock
    IUserDAO iUserDAO;

    private User user;

    @Before
    public void initUser() {
        user = new User();
        user.setId(1);
        user.setRole("admin");
        doReturn(user).when(procedureService).getCurrentUser();
    }

    @Test
    public void getUsersExpensesShouldNotReturnNull() {
        Assert.assertNotNull(procedureService.getCurrentUsersExpenses());
    }

    @Test(expected = NoSuchElementException.class)
    public void getExpenseByIdShouldThrowException() {
        doReturn(Lists.emptyList()).when(iProcedureDAO).getAllExpenses();
        procedureService.getProcedureById(1);
    }

    @Test
    public void getReviewShouldNotReturnNull() throws AccessDeniedException {
        Assert.assertNotNull(procedureService.getReview(true));
    }

    @Test
    public void getExpensesByTypeIdShouldNotReturnNull() throws AccessDeniedException {
        Assert.assertNotNull(procedureService.getProceduresByTypeId(1));
    }

    @Test
    public void getExpensesByDateShouldNotReturnNull() throws AccessDeniedException {
        user.setRole("user");
        Date start = new Date();
        Date end = start;

        Assert.assertNotNull(procedureService.getProceduresByDate(10 , start, end));
    }

    @Test
    public void updateExpenseShouldAddExpenseIfNotFound() {
        Procedure procedure = new Procedure(100, 100, -1, null, "Name", new Date(), 100, 0);
        procedureService.updateProcedure(procedure);
        verify(procedureService).addProcedure(procedure);
    }

    @Test(expected = AccessDeniedException.class)
    public void getUserByIdShouldThrowException() throws AccessDeniedException {
        user.setRole("user");
        procedureService.getUserById(2);
    }

    @Test(expected = AccessDeniedException.class)
    public void updateUserShouldThrowException() throws AccessDeniedException {
        user.setRole("user");
        procedureService.updateUser(new User());
    }

    @Test(expected = AccessDeniedException.class)
    public void getAllUsersShouldThrowException() throws AccessDeniedException {
        user.setRole("user");
        procedureService.getAllUsers();
    }

    @Test(expected = AccessDeniedException.class)
    public void deleteUsersShouldThrowException() throws AccessDeniedException {
        user.setRole("user");
        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(2);
        procedureService.deleteUsers(ids);
    }

    @Test
    public void getDatesBetweenShouldNotReturnNull() {
        doReturn(Lists.emptyList()).when(iProcedureDAO).getAllExpenses();
        Assert.assertNotNull(procedureService.getDatesBetween());
    }

    @Test
    public void getExpensesBetweenShouldNotReturnNull() {
        doReturn(Lists.emptyList()).when(procedureService).getDatesBetween();
        Assert.assertNotNull(procedureService.getExpensesBetween());
    }

}
