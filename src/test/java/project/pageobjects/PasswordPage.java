package project.pageobjects;

import aquality.selenium.elements.interfaces.IButton;
import aquality.selenium.elements.interfaces.ITextBox;
import aquality.selenium.forms.Form;
import org.openqa.selenium.By;

/**
 * @author Pavel Romanov 09.03.2023
 */

public class PasswordPage extends Form {
    private static final String PASSWORD_PAGE_IDENTIFIER = "//*[contains(@class, 'VKIDPanel')]";
    private static final String PASSWORD_PAGE_IDENTIFIER_NAME = "password_input_block";
    private final ITextBox passwordTextBox = getElementFactory().getTextBox(By.xpath("//*[@type='password']"),
            "password_text_box");
    private final IButton submitBtn = getElementFactory().getButton(By.xpath("//*[@type='submit']"),
            "submit_btn");

    public PasswordPage() {
        super(By.xpath(PASSWORD_PAGE_IDENTIFIER), PASSWORD_PAGE_IDENTIFIER_NAME);
    }

    public boolean isPasswordPageDisplayed() {
        this.state().waitForDisplayed();
        return this.state().isExist();
    }

    public void enterPassword(String password) {
        passwordTextBox.clearAndType(password);
    }

    public void clickSubmitBtn() {
        submitBtn.click();
    }
}
