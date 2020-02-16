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
    private IProcedureDAO iProcedureDAO;

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
        return iProcedureDAO.getUsersExpenses(getCurrentUser().getId());
    }

    public List<Procedure> getUsersIncomes() {
        return iProcedureDAO.getUsersIncomes(getCurrentUser().getId());
    }

    public Procedure getProcedureById(int id) {
        if (isAdmin()) {
            List<Procedure> procedureList = iProcedureDAO.getAllExpenses();
            procedureList.addAll(iProcedureDAO.getAllIncomes());
            for (Procedure procedure : procedureList) {
                if (procedure.getId() == id) {
                    return procedure;
                }
            }
            throw new NoSuchElementException(NO_SUCH_PROCEDURE_MESSAGE);
        } else {
            Procedure procedure = iProcedureDAO.getProcedureById(getCurrentUser().getId(), id);
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
                ReviewInfo temp = getReview(user.getId());
                if (user.getId() == getCurrentUser().getId()) {
                    result.setMonthChange(temp.getMonthChange());
                }
                result.setAmount(result.getAmount() + temp.getAmount());
                result.setMonth(result.getMonth() + temp.getMonth());
                result.setWeek(result.getWeek() + temp.getWeek());
            }
            return result;
        } else {
            return getReview(getCurrentUser().getId());
        }
    }

    public ReviewInfo getReview(int userId) {
        ReviewInfo reviewInfo = new ReviewInfo();
        DateTime firstDayOfMonth = new DateTime(new Date()).dayOfMonth().withMinimumValue();
        DateTime firstDayOfWeek = new DateTime(new Date()).dayOfWeek().withMinimumValue();
        List<Procedure> tempList;

        // Month change calculating
        tempList = iProcedureDAO.getIncomesByDate(userId, firstDayOfMonth.toDate(), new Date());
        reviewInfo.setMonthChange(getTotalPrice(tempList));

        tempList = iProcedureDAO.getExpensesByDate(userId, firstDayOfMonth.toDate(), new Date());
        reviewInfo.setMonthChange(reviewInfo.getMonthChange() - getTotalPrice(tempList));

        // Amount calculating
        tempList = iProcedureDAO.getUsersExpenses(userId);
        reviewInfo.setAmount(getTotalPrice(tempList));

        // Month calculating
        tempList = iProcedureDAO.getExpensesByDate(userId, firstDayOfMonth.toDate(), new Date());
        reviewInfo.setMonth(getTotalPrice(tempList));

        // Week calculating
        tempList = iProcedureDAO.getExpensesByDate(userId, firstDayOfWeek.toDate(), new Date());
        reviewInfo.setWeek(getTotalPrice(tempList));

        return reviewInfo;
    }

    private double getTotalPrice(List<Procedure> procedureList) {
        double total = 0.0;
        for (Procedure procedure : procedureList) {
            total += procedure.getPrice();
        }

        return total;
    }

    public List<Procedure> getProceduresByTypeId(int id) throws AccessDeniedException {
        if (isAdmin()) {
            if (id == -100) {
                return iProcedureDAO.getAllExpenses();
            } else if (id == 100) {
                return iProcedureDAO.getAllIncomes();
            } else {
                List<Procedure> result = new ArrayList<>();
                for (User user : getAllUsers()) {
                    result.addAll(iProcedureDAO.getProceduresByTypeId(user.getId(), id));
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
                List<Procedure> procedures = iProcedureDAO.getProceduresByTypeId(getCurrentUser().getId(), id);
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
                    return iProcedureDAO.getUsersExpenses(userId);
                } else if (id == 100) {
                    return iProcedureDAO.getUsersIncomes(userId);
                } else {
                    return iProcedureDAO.getProceduresByTypeId(userId, id);
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
                    result.addAll(iProcedureDAO.getExpensesByDate(user.getId(), startDate, endDate));
                }
                return result;
            } else if (typeId == 100) {
                for (User user : getAllUsers()) {
                    result.addAll(iProcedureDAO.getIncomesByDate(user.getId(), startDate, endDate));
                }
                return result;
            } else {
                ProcedureType type = iProcedureTypeDAO.getTypeById(typeId);
                if (type.getKind() < 0) {
                    for (User user : getAllUsers()) {
                        result.addAll(iProcedureDAO.getExpensesByDateAndTypeId(user.getId(), typeId, startDate, endDate));
                    }
                } else {
                    for (User user : getAllUsers()) {
                        result.addAll(iProcedureDAO.getIncomesByDateAndTypeId(user.getId(), typeId, startDate, endDate));
                    }
                }
                return result;
            }
        } else {
            List<Procedure> procedures;
            if (typeId == -100) {
                procedures = iProcedureDAO.getExpensesByDate(getCurrentUser().getId(), startDate, endDate);
            } else if (typeId == 100) {
                procedures = iProcedureDAO.getIncomesByDate(getCurrentUser().getId(), startDate, endDate);
            } else {
                ProcedureType type = iProcedureTypeDAO.getTypeById(typeId);
                if (type.getKind() < 0) {
                    procedures = iProcedureDAO.getExpensesByDateAndTypeId(getCurrentUser().getId(), typeId, startDate, endDate);
                } else {
                    procedures = iProcedureDAO.getIncomesByDateAndTypeId(getCurrentUser().getId(), typeId, startDate, endDate);
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
                    return iProcedureDAO.getExpensesByDate(userId, startDate, endDate);
                } else if (typeId == 100) {
                    return iProcedureDAO.getIncomesByDate(userId, startDate, endDate);
                } else {
                    ProcedureType type = iProcedureTypeDAO.getTypeById(typeId);
                    if (type.getKind() < 0) {
                        return iProcedureDAO.getExpensesByDateAndTypeId(userId, typeId, startDate, endDate);
                    } else {
                        return iProcedureDAO.getIncomesByDateAndTypeId(userId, typeId, startDate, endDate);
                    }
                }
            }
        } else {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }
    }

    public Boolean addProcedure(Procedure procedure) {
        return iProcedureDAO.addProcedure(procedure, getCurrentUser().getId());
    }

    public Boolean updateProcedure(Procedure procedure) {
        List<Procedure> procedureList;
        if (isAdmin()) {
            if (procedure.getKind() < 0) {
                procedureList = iProcedureDAO.getAllExpenses();
            } else {
                procedureList = iProcedureDAO.getAllIncomes();
            }
        } else {
            if (procedure.getKind() < 0) {
                procedureList = iProcedureDAO.getUsersExpenses(getCurrentUser().getId());
            } else {
                procedureList = iProcedureDAO.getUsersIncomes(getCurrentUser().getId());
            }
        }
        for (Procedure temp : procedureList) {
            if (procedure.getId() == temp.getId()) {
                return iProcedureDAO.updateProcedure(procedure);
            }
        }
        return addProcedure(procedure);
    }

    public List<Procedure> archiveProcedures(List<Integer> ids) {
        if (isAdmin()) {
            iProcedureDAO.archiveProcedures(ids, getCurrentUser().getId());
            return iProcedureDAO.getAllExpenses();
        } else {
            return iProcedureDAO.archiveProcedures(ids, getCurrentUser().getId());
        }
    }

    public List<Procedure> deleteProcedures(List<Integer> ids) {
        if (isAdmin()) {
            iProcedureDAO.deleteProcedures(ids, getCurrentUser().getId());
            return iProcedureDAO.getAllExpenses();
        } else {
            return iProcedureDAO.deleteProcedures(ids, getCurrentUser().getId());
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
            procedureList = iProcedureDAO.getAllExpenses();
        } else {
            procedureList = getUsersExpenses();
        }
        if (!procedureList.isEmpty()) {
            procedureList.sort(Comparator.comparing(Procedure::getDate));
            DateTime firstDay = new DateTime(procedureList.get(0).getDate()).dayOfMonth().withMinimumValue();
            DateTime lastDay = new DateTime(procedureList.get(procedureList.size() - 1).getDate());
            List<SimpleDate> datesBetween = new ArrayList<>();

            while (firstDay.compareTo(lastDay) <= 0) {
                datesBetween.add(new SimpleDate(firstDay.toDate()));
                firstDay = firstDay.plusMonths(1).dayOfMonth().withMinimumValue();
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

    private double getMonthlyExpenses(int typeId, List<Procedure> procedureList) {
        double monthly = 0.0;
        for (Procedure procedure : procedureList) {
            if (procedure.getTypeId() == typeId) {
                monthly += procedure.getPrice();
            }
        }
        return monthly;
    }

    public Map<String, Double> getExpensesReviewByTypes() {
        Map<String, Double> result = new HashMap<>();
        List<Procedure> procedureList;

        if (isAdmin()) {
            procedureList = iProcedureDAO.getAllExpenses();
        } else {
            procedureList = getUsersExpenses();
        }
        for (Procedure procedure : procedureList) {
            for (ProcedureType type : iProcedureTypeDAO.getExpenseTypes()) {
                if (procedure.getTypeId() == type.getId()) {
                    result.merge(type.getName(), procedure.getPrice(), Double::sum);
                }
            }
        }

        return result;
    }

    public Map<String, Double> getIncomesReviewByTypes() {
        Map<String, Double> result = new HashMap<>();
        List<Procedure> procedureList;

        if (isAdmin()) {
            procedureList = iProcedureDAO.getAllIncomes();
        } else {
            procedureList = getUsersIncomes();
        }
        for (Procedure procedure : procedureList) {
            for (ProcedureType type : iProcedureTypeDAO.getIncomeTypes()) {
                if (procedure.getTypeId() == type.getId()) {
                    result.merge(type.getName(), procedure.getPrice(), Double::sum);
                }
            }
        }

        return result;
    }

    public List<Procedure> getSortedAndFilteredProcedures(int typeId, Date startDate, Date endDate, int startIndex,
                                                          int quantity, String column,
                                                          boolean isAscending) throws AccessDeniedException {
        List<Procedure> result;
        Date nullDate = new Date(0);
        if (startDate.equals(nullDate) && endDate.equals(nullDate)) {
            result = getProceduresByTypeId(typeId);
        } else {
            result = getProceduresByDate(typeId, startDate, endDate);
        }

        doSort(result, column);

        if (isAscending) {
            Collections.reverse(result);
        }
        int endIndex = Math.min(startIndex + quantity, result.size());
        return result.subList(startIndex, endIndex);
    }

    public List<Procedure> getSortedAndFilteredProcedures(int typeId, Date startDate, Date endDate, int startIndex,
                                                          int quantity, String column, boolean isAscending,
                                                          int userId) throws AccessDeniedException {
        if (userId == 0) {
            return getSortedAndFilteredProcedures(typeId, startDate, endDate, startIndex, quantity, column, isAscending);
        } else {
            List<Procedure> result;
            Date nullDate = new Date(0);
            if (startDate.equals(nullDate) && endDate.equals(nullDate)) {
                result = getProceduresByTypeId(typeId, userId);
            } else {
                result = getProceduresByDate(typeId, startDate, endDate, userId);
            }

            doSort(result, column);

            if (isAscending) {
                Collections.reverse(result);
            }
            int endIndex = Math.min(startIndex + quantity, result.size());
            return result.subList(startIndex, endIndex);
        }
    }

    private void doSort(List<Procedure> list, String column) {
        switch (column) {
            case "ID": {
                list.sort(Comparator.comparingInt(Procedure::getId));
                break;
            }
            case "Username": {
                list.sort((Comparator.comparing(Procedure::getUsername)));
                break;
            }
            case "Type": {
                list.sort((o1, o2) -> {
                    ProcedureType type1 = iProcedureTypeDAO.getTypeById(o1.getTypeId());
                    ProcedureType type2 = iProcedureTypeDAO.getTypeById(o2.getTypeId());
                    return type1.getName().compareTo(type2.getName());
                });
                break;
            }
            case "Name": {
                list.sort((Comparator.comparing(Procedure::getName)));
                break;
            }
            case "Date": {
                list.sort((Comparator.comparing(Procedure::getDate)));
                break;
            }
            case "Price": {
                list.sort(Comparator.comparingDouble(Procedure::getPrice));
                break;
            }
        }
    }

}
