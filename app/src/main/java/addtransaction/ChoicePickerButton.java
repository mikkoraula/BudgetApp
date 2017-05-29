package addtransaction;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ToggleButton;

/**
 * Created by Mikko on 20.5.2017.
 *
 * This is a normal Togglebutton class that has one String attribute that tells which button it is
 * in a multichoice picker
 */

public class ChoicePickerButton extends ToggleButton {
    private String buttonLabel;

    public ChoicePickerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public String getButtonLabel() {
        return buttonLabel;
    }

    public void setButtonLabel(String buttonLabel) {
        this.buttonLabel = buttonLabel;
    }
}
