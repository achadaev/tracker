package com.example.tracker.server.service;

import com.example.tracker.server.dao.IProcedureDAO;
import com.example.tracker.server.dao.IProcedureTypeDAO;
import com.example.tracker.server.dao.IUserDAO;
import com.example.tracker.shared.model.*;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;
import java.util.*;

import static com.example.tracker.server.constant.ExceptionMessages.ACCESS_DENIED_MESSAGE;
import static com.example.tracker.server.constant.ExceptionMessages.NO_SUCH_PROCEDURE_MESSAGE;

@Component
public class ProcedureService {
    @Autowired
    private IUserDAO iUserDao;

    @Autowired
    private IProcedureDAO iProcedureDao;

    @Autowired
    private IProcedureTypeDAO iProcedureTypeDAO;

    private final static Logger LOG = LoggerFactory.getLogger(ProcedureService.class);

    public User getCurrentUser() {
        String login = UtilsService.getCurrentUsername();
        return iUserDao.getUserByName(login);
    }

    private boolean isAdmin() {
        return "admin".equals(getCurrentUser().getRole());
    }

    public List<Procedure> getUsersExpenses() {
        return iProcedureDao.getUsersExpenses(getCurrentUser().getId());
    }

    public List<Procedure> getUsersIncomes() {
        return iProcedureDao.getUsersIncomes(getCurrentUser().getId());
    }

    public Procedure getProcedureById(int id) {
        if (isAdmin()) {
            List<Procedure> procedureList = iProcedureDao.getAllExpenses();
            procedureList.addAll(iProcedureDao.getAllIncomes());
            for (Procedure procedure : procedureList) {
                if (procedure.getId() == id) {
                    return procedure;
                }
            }
            throw new NoSuchElementException(NO_SUCH_PROCEDURE_MESSAGE);
        } else {
            Procedure procedure = iProcedureDao.getProcedureById(getCurrentUser().getId(), id);
            if (procedure.getIsArchived() == 0) {
                return procedure;
            } else {
                throw new NoSuchElementException(NO_SUCH_PROCEDURE_MESSAGE);
            }
        }
    }

    public ReviewInfo getReview() throws AccessDeniedException {
        if (isAdmin()) {
            ReviewInfo result = new ReviewInfo();
            for (User user : getAllUsers()) {
                ReviewInfo temp = iProcedureDao.getReview(user.getId());
                result.setWeek(result.getWeek() + temp.getWeek());
                result.setMonth(result.getMonth() + temp.getMonth());
                result.setAmount(result.getAmount() + temp.getAmount());
            }
            return result;
        } else {
            return iProcedureDao.getReview(getCurrentUser().getId());
        }
    }

    public List<Procedure> getProceduresByTypeId(int id) throws AccessDeniedException {
        if (isAdmin()) {
            if (id == -100) {
                return iProcedureDao.getAllExpenses();
            } else if (id == 100) {
                return iProcedureDao.getAllIncomes();
            } else {
                List<Procedure> result = new ArrayList<>();
                for (User user : getAllUsers()) {
                    result.addAll(iProcedureDao.getProceduresByTypeId(user.getId(), id));
                }
                return result;
            }
        } else {
            if (id == -100) {
                return getUsersExpenses();
            } else if (id == 100) {
                return getUsersIncomes();
            } else {
                List<Procedure> result = new ArrayList<>();
                List<Procedure> procedures = iProcedureDao.getProceduresByTypeId(getCurrentUser().getId(), id);
                for (Procedure procedure : procedures) {
                    if (procedure.getIsArchived() == 0) {
                        result.add(procedure);
                    }
                }
                return result;
            }
        }
    }

    public List<Procedure> getProceduresByTypeId(int id, int userId) throws AccessDeniedException {
        if (isAdmin()) {
            if (userId == 0) {
                return getProceduresByTypeId(id);
            } else {
                if (id == -100) {
                    return iProcedureDao.getUsersExpenses(userId);
                } else if (id == 100) {
                    return iProcedureDao.getUsersIncomes(userId);
                } else {
                    return iProcedureDao.getProceduresByTypeId(userId, id);
                }
            }
        } else {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }
    }

    public List<Procedure> getProceduresByDate(int typeId, Date startDate, Date endDate) throws AccessDeniedException {
        List<Procedure> result = new ArrayList<>();

        if (isAdmin()) {
            if (typeId == -100) {
                for (User user : getAllUsers()) {
                    result.addAll(iProcedureDao.getExpensesByDate(user.getId(), startDate, endDate));
                }
                return result;
            } else if (typeId == 100) {
                for (User user : getAllUsers()) {
                    result.addAll(iProcedureDao.getIncomesByDate(user.getId(), startDate, endDate));
                }
                return result;
            } else {
                ProcedureType type = iProcedureTypeDAO.getTypeById(typeId);
                if (type.getKind() < 0) {
                    for (User user : getAllUsers()) {
                        result.addAll(iProcedureDao.getExpensesByDateAndTypeId(user.getId(), typeId, startDate, endDate));
                    }
                } else {
                    for (User user : getAllUsers()) {
                        result.addAll(iProcedureDao.getIncomesByDateAndTypeId(user.getId(), typeId, startDate, endDate));
                    }
                }
                return result;
            }
        } else {
            List<Procedure> procedures;
            if (typeId == -100) {
                procedures = iProcedureDao.getExpensesByDate(getCurrentUser().getId(), startDate, endDate);
            } else if (typeId == 100) {
                procedures = iProcedureDao.getIncomesByDate(getCurrentUser().getId(), startDate, endDate);
            } else {
                ProcedureType type = iProcedureTypeDAO.getTypeById(typeId);
                if (type.getKind() < 0) {
                    procedures = iProcedureDao.getExpensesByDateAndTypeId(getCurrentUser().getId(), typeId, startDate, endDate);
                } else {
                    procedures = iProcedureDao.getIncomesByDateAndTypeId(getCurrentUser().getId(), typeId, startDate, endDate);
                }
            }
            for (Procedure procedure : procedures) {
                if (procedure.getIsArchived() == 0) {
                    result.add(procedure);
                }
            }
            return result;
        }
    }

    public List<Procedure> getProceduresByDate(int typeId, Date startDate, Date endDate, int userId) throws AccessDeniedException {
        if (isAdmin()) {
            if (userId == 0) {
                return getProceduresByDate(typeId, startDate, endDate);
            } else {
                if (typeId == -100) {
                    return iProcedureDao.getExpensesByDate(userId, startDate, endDate);
                } else if (typeId == 100) {
                    return iProcedureDao.getIncomesByDate(userId, startDate, endDate);
                } else {
                    ProcedureType type = iProcedureTypeDAO.getTypeById(typeId);
                    if (type.getKind() < 0) {
                        return iProcedureDao.getExpensesByDateAndTypeId(userId, typeId, startDate, endDate);
                    } else {
                        return iProcedureDao.getIncomesByDateAndTypeId(userId, typeId, startDate, endDate);
                    }
                }
            }
        } else {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }
    }

    public Boolean addProcedure(Procedure procedure) {
        return iProcedureDao.addProcedure(procedure, getCurrentUser().getId());
    }

    public Boolean updateProcedure(Procedure procedure) {
        List<Procedure> procedureList;
        if (isAdmin()) {
            if (procedure.getKind() < 0) {
                procedureList = iProcedureDao.getAllExpenses();
            } else {
                procedureList = iProcedureDao.getAllIncomes();
            }
        } else {
            if (procedure.getKind() < 0) {
                procedureList = iProcedureDao.getUsersExpenses(getCurrentUser().getId());
            } else {
                procedureList = iProcedureDao.getUsersIncomes(getCurrentUser().getId());
            }
        }
        for (Procedure temp : procedureList) {
            if (procedure.getId() == temp.getId()) {
                return iProcedureDao.updateProcedure(procedure);
            }
        }
        return addProcedure(procedure);
    }

    public List<Procedure> archiveProcedures(List<Integer> ids) {
        if (isAdmin()) {
            iProcedureDao.archiveProcedures(ids, getCurrentUser().getId());
            //TODO same
            return iProcedureDao.getAllExpenses();
        } else {
            return iProcedureDao.archiveProcedures(ids, getCurrentUser().getId());
        }
    }

    public List<Procedure> deleteProcedures(List<Integer> ids) {
        if (isAdmin()) {
            iProcedureDao.deleteProcedures(ids, getCurrentUser().getId());
            return iProcedureDao.getAllExpenses();
        } else {
            return iProcedureDao.deleteProcedures(ids, getCurrentUser().getId());
        }
    }

    public User getUserById(int id) throws AccessDeniedException {
        if (isAdmin() || id == getCurrentUser().getId()) {
            return iUserDao.getUserById(id);
        } else {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }
    }

    public Boolean updateUser(User user) throws AccessDeniedException {
        if (isAdmin()) {
            for (User temp : getAllUsers()) {
                if (temp.getId() == user.getId()) {
                    return iUserDao.updateUser(user);
                }
            }
            return iUserDao.addUser(user);
        } else if (user.getId() == getCurrentUser().getId()) {
            return iUserDao.updateUser(user);
        } else {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }
    }

    public Boolean updatePassword(User user) throws AccessDeniedException {
        if (isAdmin()) {
            for (User temp : getAllUsers()) {
                if (temp.getId() == user.getId()) {
                    return iUserDao.updatePassword(user);
                }
            }
            return false;
        } else if (user.getId() == getCurrentUser().getId()) {
            return iUserDao.updatePassword(user);
        } else {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }
    }

    public List<User> getAllUsers() throws AccessDeniedException {
        if (isAdmin()) {
            return iUserDao.getAllUsers();
        } else {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }
    }

    public List<User> archiveUsers(List<Integer> ids) throws AccessDeniedException {
        if (isAdmin()) {
            iUserDao.archiveUsers(ids);
            return iUserDao.getAllUsers();
        } else {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }
    }

    public List<User> deleteUsers(List<Integer> ids) throws AccessDeniedException {
        if (isAdmin()) {
            return iUserDao.deleteUsers(ids);
        } else {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }
    }

    public Boolean updateType(ProcedureType type) throws AccessDeniedException {
        if (isAdmin()) {
            for (ProcedureType temp : iProcedureTypeDAO.getTypes()) {
                if (temp.getId() == type.getId()) {
                    return iProcedureTypeDAO.updateType(type);
                }
            }
            return iProcedureTypeDAO.addType(type);
        } else {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }
    }

    public List<SimpleDate> getDatesBetween() {
        List<Procedure> procedureList;
        if (isAdmin()) {
            procedureList = iProcedureDao.getAllExpenses();
        } else {
            procedureList = getUsersExpenses();
        }
        if (!procedureList.isEmpty()) {
            Collections.sort(procedureList, (Comparator.comparing(Procedure::getDate)));
            DateTime first = new DateTime(procedureList.get(0).getDate()).dayOfMonth().withMinimumValue();
            DateTime last = new DateTime(procedureList.get(procedureList.size() - 1).getDate());
            List<SimpleDate> datesBetween = new ArrayList<>();

            while (first.compareTo(last) <= 0) {
                datesBetween.add(new SimpleDate(first.toDate()));
                first = first.plusMonths(1).dayOfMonth().withMinimumValue();
            }

            return datesBetween;
        }
        return new ArrayList<>();
    }

    public List<MonthlyExpense> getExpensesBetween() {
        Calendar calendar = Calendar.getInstance();
        List<SimpleDate> dates = getDatesBetween();
        List<MonthlyExpense> expensesBetween = new ArrayList<>();

        if (dates.size() > 1) {
            DateTime first = new DateTime(dates.get(0).getDate());
            DateTime last = new DateTime(dates.get(dates.size() - 1).getDate());
            calendar.setTime(first.toDate());

            Date firstDay;
            Date lastDay;

            while (calendar.getTime().compareTo(last.toDate()) <= 0) {
                calendar.set(Calendar.DATE, 1);
                firstDay = calendar.getTime();
                calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                lastDay = calendar.getTime();
                List<Double> monthlyExpenses = new ArrayList<>();

                for (ProcedureType type : iProcedureTypeDAO.getExpenseTypes()) {
                    try {
                        monthlyExpenses.add(getMonthlyExpenses(type.getId(), getProceduresByDate(type.getId(), firstDay, lastDay)));
                    } catch (AccessDeniedException e) {
                        e.printStackTrace();
                    }
                }

                expensesBetween.add(new MonthlyExpense(monthlyExpenses));
                calendar.add(Calendar.MONTH, 1);
                calendar.set(Calendar.DATE, 1);
            }

            return expensesBetween;

        } else if (dates.size() == 1) {
            calendar.setTime(new DateTime(dates.get(0).getDate()).toDate());
            List<Double> monthlyExpenses = new ArrayList<>();

            for (ProcedureType type : iProcedureTypeDAO.getExpenseTypes()) {
                monthlyExpenses.add(getMonthlyExpenses(type.getId(), getUsersExpenses()));
            }

            expensesBetween.add(new MonthlyExpense(monthlyExpenses));

            return expensesBetween;

        } else {
            return new ArrayList<>();
        }
    }

    private Double getMonthlyExpenses(int typeId, List<Procedure> procedureList) {
        double monthly = 0.0;
        for (Procedure procedure : procedureList) {
            if (procedure.getTypeId() == typeId) {
                monthly += procedure.getPrice();
            }
        }
        return monthly;
    }

    public List<Procedure> getSortedAndFilteredProcedures(int typeId, Date startDate, Date endDate, int startIndex,
                                                          int quantity, boolean isAscending) throws AccessDeniedException {
        List<Procedure> result;
        Date nullDate = new Date(0);
        if (startDate.equals(nullDate) && endDate.equals(nullDate)) {
            result = getProceduresByTypeId(typeId);
        } else {
            result = getProceduresByDate(typeId, startDate, endDate);
        }
        result.sort(Comparator.comparingDouble(Procedure::getPrice));
        if (isAscending) {
            Collections.reverse(result);
        }
        int endIndex = Math.min(startIndex + quantity, result.size());
        return result.subList(startIndex, endIndex);
    }

    public List<Procedure> getSortedAndFilteredProcedures(int typeId, Date startDate, Date endDate, int startIndex,
                                                          int quantity, boolean isAscending, int userId) throws AccessDeniedException {
        if (userId == 0) {
            return getSortedAndFilteredProcedures(typeId, startDate, endDate, startIndex, quantity, isAscending);
        } else {
            List<Procedure> result;
            Date nullDate = new Date(0);
            if (startDate.equals(nullDate) && endDate.equals(nullDate)) {
                result = getProceduresByTypeId(typeId, userId);
            } else {
                result = getProceduresByDate(typeId, startDate, endDate, userId);
            }
            result.sort(Comparator.comparingDouble(Procedure::getPrice));
            if (isAscending) {
                Collections.reverse(result);
            }
            int endIndex = Math.min(startIndex + quantity, result.size());
            return result.subList(startIndex, endIndex);
        }
    }

}
