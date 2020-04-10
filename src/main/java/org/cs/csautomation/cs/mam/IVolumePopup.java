
package org.cs.csautomation.cs.mam;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author priyanka.patil
 *
 */
public interface IVolumePopup {

    public void selectPopupDivMenu(WebDriverWait waitForReload,
            WebElement subMenuToBeSelected, WebDriver browserDriverInstance);

    public void enterValueInDialogueMamStudio(WebDriverWait waitForReload,
            String demoVolumeFolderName);

    public void askBoxWindowOperation(WebDriverWait waitForReload,
            boolean pressOkay, WebDriver browserDriverInstance);

    public void askBoxWindowOperationMamStudio(WebDriverWait waitForReload,
            boolean pressOkay, WebDriver browserDriverInstance);

    public WebElement getCreateNewFile();

    public WebElement getCsPopupDiv();

    public WebElement getTxtCsGuiModalDialogFormName();

    public WebElement getBtnCsGuiModalDialogOkButton();

    public WebElement getBtnCsGuiModalDialogCancelButton();

    public WebElement getCreateNewFolder();

    public WebElement getBtnExtendMam();

    public WebElement getBtnReplaceMam();

    public void enterValueInDialogue(WebDriverWait waitForReload,
            String userInputFolderName);

    public WebElement getCreateNewMamAttribute();

    public WebElement getObjectOption();

    public WebElement getEditOption();

    public WebElement getArchiveOption();

    public WebElement getProcessOption();

    public WebElement getDownloadAsZipOption();

    public WebElement getRestoreArchiveOption();

    public WebElement getDeleteVersionOption();

    public WebElement getDeleteOldVersionsOption();

    public WebElement getRestoreVersionOption();

    public WebElement getCsPopupRollbackVersion();

    public WebElement getCsPopupCreateLink();

    public WebElement getCsPopupRemoveLink();

    public WebElement getCsPopupDelete();

    public WebElement getDuplicateOption();

    public WebElement getCheckForDuplicatesOption();

    public WebElement getViewFolderAsEditor();

    public WebElement getToolsOption();

    public WebElement getClkRemove();

    public WebElement getClkAddAgain();

    public void enterValueInUserInputDialogue(WebDriverWait waitForReload,
            String userInputFolderName);

    public WebElement getClkUploadNewFile();
}
