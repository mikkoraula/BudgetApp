package payment.history;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mikko.budgetapplication.R;

/**
 * Created by Mikko on 2.8.2016.
 */
public class MonthlyTabFragment extends Fragment {
    private String tabName = "";
    private TextView textView;

    // newInstance constructor for creating fragment with arguments
    public static MonthlyTabFragment newInstance(String monthTitle) {
        MonthlyTabFragment monthlyTabFragment = new MonthlyTabFragment();
        Bundle monthArgs = new Bundle();
        monthArgs.putString("someMonthTitle", monthTitle);
        monthlyTabFragment.setArguments(monthArgs);
        return monthlyTabFragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabName = getArguments().getString("someMonthTitle");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.monthly_tab_fragment, container, false);
        textView = (TextView) view.findViewById(R.id.monthly_tab_fragment_text_view);
        textView.setText(tabName);
        return view;
    }
}
