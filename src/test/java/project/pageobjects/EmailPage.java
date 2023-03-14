package project.pageobjects;

import aquality.selenium.elements.interfaces.IButton;
import aquality.selenium.elements.interfaces.ITextBox;
import aquality.selenium.forms.Form;
import org.openqa.selenium.By;

/**
 * @author Pavel Romanov 09.03.2023
 */

public class EmailPage extends Form {
    private static final String MAIN_PAGE_IDENTIFIER = "//*[@id = 'index_login']";
    private static final String MAIN_PAGE_IDENTIFIER_NAME = "login_input_block";

    private final ITextBox emailTextBox = getElementFactory().getTextBox(By.xpath("//*[@id = 'index_email']"),
            "email_text_box");
    private final IButton signInBtn = getElementFactory().getButton(By.xpath("//*[contains(@class, 'signInButton')]"),
            "sign_in_btn");

    public EmailPage() {
        super(By.xpath(MAIN_PAGE_IDENTIFIER), MAIN_PAGE_IDENTIFIER_NAME);
    }

    public boolean isEmailPageDisplayed() {
        return this.state().isExist();
    }

    public void enterEmail(String email) {
        emailTextBox.clearAndType(email);
    }

    public void clickSignInBtn() {
        signInBtn.click();
    }
}
