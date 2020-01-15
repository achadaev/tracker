import com.example.tracker.server.dao.IExpenseDAO;
import com.example.tracker.server.dao.IExpenseDAOImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class MainTest {

    @Test
    public void expensesShouldNotBeNull() {
        IExpenseDAO iExpenseDAO = null;
        try {
            iExpenseDAO = new IExpenseDAOImpl();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Assertions.assertNotNull(iExpenseDAO.getAllExpenses());
    }
}
