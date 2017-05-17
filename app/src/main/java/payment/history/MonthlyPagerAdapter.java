package payment.history;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.example.mikko.budgetapplication.DateHandler;

import java.util.ArrayList;

import data.Transaction;
import data.TransactionData;

/**
 * Created by Mikko on 2.8.2016.
 */
public class MonthlyPagerAdapter extends FragmentStatePagerAdapter {
    private int numberOfTabs;

    private ArrayList<Transaction> payments;
    private ArrayList<Transaction> incomes;

    private MonthlyTabFragment currentMonthlyTabFragment;

    public MonthlyPagerAdapter(FragmentManager fm, int numberOfTabs, ArrayList<Transaction> payments, ArrayList<Transaction> incomes) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
        this.payments = payments;
        this.incomes = incomes;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (currentMonthlyTabFragment != object) {
            currentMonthlyTabFragment = ((MonthlyTabFragment) object);
            //System.out.println("monthlypageradapter got a current Tab Name: " + currentMonthlyTabFragment.getTabName());
        }
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {

        for (int i = numberOfTabs; i >= 0; i--) {
            if (position == i) {
                String title;
                int monthIndex = DateHandler.getMonthFromCurrentMonth(i);
                int year = DateHandler.getYearFromCurrentMonth(i);

                // prepare this month's transactions
                ArrayList<Transaction> monthPayments;
                ArrayList<Transaction> monthIncomes;
                monthPayments = getMonthsTransactions(payments, monthIndex, year);
                monthIncomes = getMonthsTransactions(incomes, monthIndex, year);


                // do the title of the tab
                title = DateHandler.months[monthIndex];
                title += " ";
                title += year;

                TransactionData monthPaymentData = new TransactionData();
                monthPaymentData.setTransactionList(monthPayments);
                TransactionData monthIncomeData = new TransactionData();
                monthIncomeData.setTransactionList(monthIncomes);

                // instantiate the fragment
                MonthlyTabFragment monthlyTabFragment =  MonthlyTabFragment.newInstance(title, monthPaymentData, monthIncomeData);
                //monthlyTabFragment.setTransactions(monthPayments, monthIncomes);
                return monthlyTabFragment;
            }
        }
        // just in case math breaks
        return null;
    }

    public ArrayList<Transaction> getMonthsTransactions(ArrayList<Transaction> transactions, int month, int year) {
        ArrayList<Transaction> monthTransactionList = new ArrayList<>();
        for (Transaction transaction : transactions) {
            long transactionAge = transaction.getDateInMilliseconds();

            // first check the year
            if (DateHandler.getYear(transactionAge) == year) {
                // then the month
                if (DateHandler.getMonth(transactionAge) == month) {

                    // if both are ok, then add transaction to new list
                    monthTransactionList.add(transaction);
                }
            }
        }
        return monthTransactionList;
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
