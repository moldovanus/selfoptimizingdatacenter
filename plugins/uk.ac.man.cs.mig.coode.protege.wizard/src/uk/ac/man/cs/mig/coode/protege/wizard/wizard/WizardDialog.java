package uk.ac.man.cs.mig.coode.protege.wizard.wizard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Jan 6, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 * <p/>
 * A Wizard consists of a dialog containing
 * pages, that can be sequentially accessed using
 * previous and next buttons.  When the user is on the first
 * page, then the previous button is disabled.  When the user
 * is on the last page, the next button label is changed to
 * reflect this.  To use the <code>Wizard</code> call one of the constructors
 * and call <code>setPages(WizardPage [])</code> to set the pages that the
 * <code>Wizard</code> should use.
 *
 * @see WizardPage
 * @see uk.ac.man.cs.mig.coode.protege.wizard.wizard.WizardEventListener
 */
public class WizardDialog extends JDialog {

    private ArrayList listeners;

    private WizardPage[] pages; // An array containing the wizard pages
    private JPanel pagePanel; // A panel (with a CardLayout) to hold the pages

    private JPanel buttonPanel; // The panel holding the button panel
    private JPanel mainPanel; // A panel consisting of all the screen, not buttons

    private int returnValue = OPTION_CANCEL;

    public static final int OPTION_CANCEL = 0;
    public static final int OPTION_APPROVE = 1;

    private JButton nextButton; // Button that advances the displayed page by one page
    private JButton prevButton; // Button that retreats the displayed page by one page
    private JButton cancelButton; // Button that cancels the Wizard operation and closes the dialog

    private String nextText; // The label displayed on the nextButton
    private String prevText; // The label displayed on the prevButton
    private String finishText; // The label displayed on the nextButton when the wizard is on the last page
    private String cancelText; // The label displayed on the cancelButton

    private int currentPage; // The index of the page being displayed.

    private boolean ignoreImpendingButtonAction = false; // Sets a flag to ignore impending
    //actions from the prev, next and
    // cancel buttons

    private Object userObject;

    private Frame wizardOwner;

    // Key Handler Keys
    private int NEXT_KEY = KeyEvent.VK_ENTER;
    private int PREV_KEY = KeyEvent.VK_BACK_SPACE;

    /**
     * Constructs a <code>Wizard</code> with no owner frame, using the
     * specified title and the specified pages.
     *
     * @param title The title to display in the <code>Wizard</code> title bar.
     */
    public WizardDialog(String title) {
        this(null, title);
    }

    /**
     * Constructs a <code>Wizard</code> with the specified owner frame, title
     * and pages.
     *
     * @param owner The owner <code>Frame</code>.  The <code>Wizard</code> will be centered on
     *              this <code>Frame</code> and also minimse and maximise with the <code>Frame</code>.
     * @param title The title to be displayed in the <code>Wizard</code> title bar.
     */
    public WizardDialog(Frame owner, String title) {
        this(owner, title, "Prev", "Next", "Finish", "Cancel");
    }

    /**
     * Constructs a <code>Wizard</code> with the specified attributes.
     *
     * @param owner             The owner <code>Frame</code>.  The <code>Wizard</code> will be centered on
     *                          this <code>Frame</code> and also minimse and maximise with the <code>Frame</code>.
     * @param title             The title to be displayed in the <code>Wizard</code> title bar.
     * @param prevButtonLabel   The label to be used on the button that causes the previous <code>Wizard</code>
     *                          page to be displayed.
     * @param nextButtonLabel   The label to be used on the button that causes the next <code>Wizard</code>
     *                          page to be displayed.
     * @param finishButtonLabel The label to be displayed on the 'next button' when the last <code>Wizard</code>
     *                          page is displayed.
     * @param cancelButtonText  The label to be displayed on the Cancel button.
     */
    public WizardDialog(Frame owner, String title, String prevButtonLabel, String nextButtonLabel, String finishButtonLabel, String cancelButtonText) {
        super(owner, title, true);

        wizardOwner = owner;

        setModal(true);

        setResizable(false);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        listeners = new ArrayList();

        nextText = nextButtonLabel;

        prevText = prevButtonLabel;

        finishText = finishButtonLabel;

        cancelText = cancelButtonText;

        pagePanel = new JPanel();

        createUI();

        setFocusable(true);

        this.addKeyListener(new KeyAdapter() {
            /**
             * Invoked when a key has been released.
             */
            public void keyReleased(KeyEvent e) {
                handleKeyEvent(e);
            }
        });

    }

    /**
     * Handles key events
     *
     * @param e The KeyEvent
     */
    public void handleKeyEvent(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == NEXT_KEY) {
            if (nextButton.isEnabled() == true) {
                nextButtonPressed();
            }

        }
        else if (keyCode == PREV_KEY) {
            if (prevButton.isEnabled() == true) {
                prevButtonPressed();
            }
        }
        else if (keyCode == KeyEvent.VK_ESCAPE) {
            cancelButtonPressed();
        }

    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void createUI() {
        buttonPanel = createButtonPanel();

        pagePanel = new JPanel();
        pagePanel.setLayout(new CardLayout());

        mainPanel = new JPanel(new BorderLayout(6, 6));
        mainPanel.setBorder(new EmptyBorder(6, 6, 6, 6));
        mainPanel.add(pagePanel, BorderLayout.CENTER);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        nextButton = new JButton(new AbstractAction(nextText) {
            public void actionPerformed(ActionEvent e) {
                nextButtonPressed();
            }
        });

        prevButton = new JButton(new AbstractAction(prevText) {
            public void actionPerformed(ActionEvent e) {
                prevButtonPressed();
            }
        });

        cancelButton = new JButton(new AbstractAction(cancelText) {
            public void actionPerformed(ActionEvent e) {
                cancelButtonPressed();
            }

        });

        buttonPanel.add(prevButton);

        buttonPanel.add(nextButton);

        buttonPanel.add(cancelButton);

        return buttonPanel;
    }

    /**
     * Sets the pages that the Wizard should use.
     *
     * @param pages An array containing the <code>WizardPages</code>
     */
    public void setPages(WizardPage[] pages) {
        if (pages == null) {
            throw new NullPointerException("setPages parameter must not be null");
        }

        this.pages = new WizardPage[pages.length];

        for (int i = 0; i < pages.length; i++) {
            this.pages[i] = pages[i];

            // Set this wizard as the page's wizard
            this.pages[i].setWizard(this);

            // Notify the page that the owner wizard
            // has been set
            this.pages[i].wizardSet(this);
        }

        currentPage = 0;

        pagePanel.removeAll();

        for (int i = 0; i < pages.length; i++) {
            pagePanel.add(pages[i], pages[i].getName());
        }

        // not need - this will be called when firstPage() called
        // firePageChangedEvent();

        pack();

        setButtonState();

        setWizardLocation();
    }

    /**
     * Centres the <code>Wizard</code> on it's parent,
     * or the screen if the parent is <code>null</code>.
     */
    protected void setWizardLocation() {
        int ownerCentreX;

        int ownerCentreY;

        if (wizardOwner != null) {
            ownerCentreX = (int) wizardOwner.getBounds().getCenterX();

            ownerCentreY = (int) wizardOwner.getBounds().getCenterY();
        }
        else {
            ownerCentreX = Toolkit.getDefaultToolkit().getScreenSize().width / 2;

            ownerCentreY = Toolkit.getDefaultToolkit().getScreenSize().height / 2;
        }

        setLocation(ownerCentreX - getSize().width / 2, ownerCentreY - getSize().height / 2);
    }

    /**
     * Causes the Wizard to be shown.  This method should be used
     * rather than the <code>show()</code> method in <code>JDialog</code>
     * so that the return value may be examined to inidcate whether the user
     * cancelled the Wizard or not.  Before calling <code>showWizard()</code> some
     * pages <i>must</i> be added to the <code>Wizard</code> using the <code>setPages</code> method.
     *
     * @return <code>OPTION_APPROVE</code> if the user did not press cancel, or
     *         <code>OPTION_CANCEL</code> if the user pressed the cancel button.
     */
    public int showWizard() {
        if (pages == null) {
            throw new NullPointerException("The Wizard does not contain any pages.\n" +
                    "Add some pages to the Wizard using addPages before calling" +
                    " show Wizard.");
        }

        if (pages.length == 0) {
            throw new IndexOutOfBoundsException("The Wizard must contain at least one page.\n" +
                    "Add some pages to the Wizard using addPages before calling" +
                    " showWizard.");

        }

        firstPage();

        super.setVisible(true);

        return returnValue;
    }

    public boolean getNextButtonEnabled() {
        return nextButton.isEnabled();
    }

    public boolean getPrevButtonEnabled() {
        return prevButton.isEnabled();
    }

    /**
     * Called when the button that requests that the next
     * page be displayed is pressed.
     */
    public void nextButtonPressed() {
        // If we're on the last page
        // then set the return value as
        // approve and close the dialog

        if (currentPage == getNumberOfPages() - 1) {
            fireFinishButtonPressedEvent();

            if (ignoreImpendingButtonAction == false) {
                returnValue = OPTION_APPROVE;

                dispose();
            }
            else {
                ignoreImpendingButtonAction = false;
            }
        }
        else {
            fireNextButtonPressedEvent();

            if (ignoreImpendingButtonAction == false) {
                nextPage();
            }
            else {
                ignoreImpendingButtonAction = false;
            }
        }
    }


    /**
     * Called when the button that requests that the previous
     * page be displayed.
     */
    public void prevButtonPressed() {
        firePrevButtonPressedEvent();

        if (ignoreImpendingButtonAction == false) {
            prevPage();
        }
        else {
            ignoreImpendingButtonAction = false;
        }
    }

    /**
     * Called when the cancel button is pressed.
     */
    public void cancelButtonPressed() {
        fireCancelButtonPressedEvent();

        if (ignoreImpendingButtonAction == false) {
            returnValue = OPTION_CANCEL;

            dispose();
        }
        else {
            ignoreImpendingButtonAction = true;
        }
    }


    /**
     * Returns the number of pages that the Wizard isShown.
     *
     * @return The number of pages
     */
    public int getNumberOfPages() {
        return pages.length;
    }

    protected CardLayout getPagePanelLayout() {
        return (CardLayout) (pagePanel.getLayout());
    }


    /**
     * Programmatically sets the Wizard to display the
     * first page.
     */
    public void firstPage() {
        currentPage = 0;

        getPagePanelLayout().first(pagePanel);

        setButtonState();

        firePageChangedEvent();
    }

    /**
     * Programmatically advances the Wizard to display the next
     * page.  If the Wizard is on the last page then nothing
     * happens
     */
    public void nextPage() {
        currentPage++;

        if (currentPage > pages.length - 1) {
            currentPage = pages.length - 1;
        }
        else {
            getPagePanelLayout().next(pagePanel);

            setButtonState();

            firePageChangedEvent();
        }

    }

    /**
     * Programmatically causes the Wizard to display
     * the previous page.  If the Wizard is already
     * on the first page then nothing happens.
     */
    public void prevPage() {
        currentPage--;

        if (currentPage < 0) {
            currentPage = 0;
        }
        else {
            getPagePanelLayout().previous(pagePanel);

            setButtonState();

            firePageChangedEvent();
        }
    }

    /**
     * Refreshes the state of the <code>Wizard</code> buttons.
     * i.e. ensures that the correct button states (enabled/disabled) and
     * correct button labels are displayed based on the currently displayed page.
     */
    protected void setButtonState() {
        if (currentPage == 0) {
            setPrevButtonEnabled(false);
        }
        else {
            setPrevButtonEnabled(true);
        }

        if (currentPage == getNumberOfPages() - 1) {
            nextButton.setText(finishText);
        }
        else {
            nextButton.setText(nextText);
        }
    }


    /**
     * Gets the label displayed on the button that
     * causes the previous page to be displayed.
     *
     * @return The label text.
     */
    public String getPrevButtonLabel() {
        return prevText;
    }

    /**
     * Sets the label displayed on the button that causes
     * the previous page to be displayed.
     *
     * @param label
     */
    public void setPrevButtonLabel(String label) {
        prevText = label;

        prevButton.setText(prevText);
    }

    /**
     * Gets the label displayed on the button that
     * causes the next page to be displayed.
     *
     * @return The label text.
     */
    public String getNextButtonLabel() {
        return nextText;
    }

    /**
     * Sets the label displayed on the button that causes
     * the next page to be displayed.
     *
     * @param label The text to be used for the label.
     */
    public void setNextButtonLabel(String label) {
        nextText = label;

        setButtonState();
    }

    /**
     * Gets the label displayed on the button that
     * causes the next page to be shown, when the Wizard
     * is on the last page. (For example 'Finish')
     *
     * @return The label text.
     */
    public String getFinishButtonLabel() {
        return finishText;
    }

    /**
     * Sets the label displayed on the button that causes
     * the next page to be shown when the Wizard is on the
     * last page.
     *
     * @param label The text to be used for the label.
     */
    public void setFinishButtonLabel(String label) {
        finishText = label;

        setButtonState();
    }

    /**
     * Enables/Disables the button that causes the
     * Wizard to display the next page.
     *
     * @param b <code>true</code> to enable the button, <code>false</code>
     *          to disable the button.
     */
    public void setNextButtonEnabled(boolean b) {
        nextButton.setEnabled(b);
    }

    /**
     * Enables/Disbales the button that causes the Wizard to
     * display the previous page.
     *
     * @param b <code>true</code> to enable the button, <code>false</code>
     *          to disable the button.
     */
    public void setPrevButtonEnabled(boolean b) {
        prevButton.setEnabled(b);
    }

    public void setCancelButtonEnabled(boolean b) {
        cancelButton.setEnabled(b);
    }

    /**
     * Causes the specified page to be shown.
     *
     * @param name The name of the page to be shown.
     */
    public void setCurrentPage(String name) {
        for (int i = 0; i < pages.length; i++) {
            if (pages[i].getName().equals(name)) {
                currentPage = i;

                getPagePanelLayout().show(pagePanel, name);

                firePageChangedEvent();

                setButtonState();
            }
        }

    }

    /**
     * Gets the page currently being displayed.
     *
     * @return The <code>WizardPage</code> that is
     *         currently being displayed.
     */
    public WizardPage getCurrentPage() {
        return pages[currentPage];
    }

    /**
     * Adds a listener to the <code>Wizard</code>
     *
     * @param lsnr The listener to be added.
     */
    public void addWizardListener(WizardEventListener lsnr) {
        listeners.add(lsnr);
    }

    /**
     * Removes a previously added listener.
     *
     * @param lsnr The listener to be removed.
     */
    public void removeListener(WizardEventListener lsnr) {
        listeners.remove(lsnr);
    }

    /**
     * Causes the next button action to be ignored.
     */
    public void ignoreImpendingButtonAction() {
        ignoreImpendingButtonAction = true;
    }

    /**
     * Allows an <code>Object</code> to be associated
     * with the <code>Wizard</code>
     *
     * @param obj The <code>Object</code> to be associated
     *            with the <code>Wizard</code>
     */
    public void setUserObject(Object obj) {
        this.userObject = obj;
    }

    /**
     * Gets the user object that can be associated with the
     * <code>Wizard.</code>
     *
     * @return The <code>Wizard</code> user object that has
     *         been set with the <code>setUserObject</code> method,
     *         or <code>null</code> if no user object has been set.
     */
    public Object getUserObject() {
        return userObject;
    }

    /**
     * Gets a page contained in the <code>Wizard</code> with the
     * specified name.
     *
     * @param pageName The name of the page.
     * @return The <code>WizardPage</code> that has the specified name,
     *         or <code>null</code> if the <code>Wizard</code> does not
     *         contain a page with the specified name.
     */
    public WizardPage getPage(String pageName) {
        WizardPage page = null;

        for (int i = 0; i < pages.length; i++) {
            if (pages[i].getName().equals(pageName)) {
                page = pages[i];

                break;
            }
        }

        return page;
    }


    protected void fireNextButtonPressedEvent() {
        WizardEvent evt = new WizardEvent(this);

        for (int i = 0; i < listeners.size(); i++) {
            (((WizardEventListener) listeners.get(i))).nextPressed(evt);
        }
    }

    protected void firePrevButtonPressedEvent() {
        WizardEvent evt = new WizardEvent(this);

        for (int i = 0; i < listeners.size(); i++) {
            (((WizardEventListener) listeners.get(i))).prevPressed(evt);
        }
    }

    protected void firePageChangedEvent() {
        WizardEvent evt = new WizardEvent(this);

        for (int i = 0; i < listeners.size(); i++) {
            (((WizardEventListener) listeners.get(i))).pageChanged(evt);
        }

        pages[currentPage].pageSelected(this);
    }

    protected void fireFinishButtonPressedEvent() {
        WizardEvent evt = new WizardEvent(this);

        for (int i = 0; i < listeners.size(); i++) {
            (((WizardEventListener) listeners.get(i))).finishPressed(evt);
        }
    }

    protected void fireCancelButtonPressedEvent() {
        WizardEvent evt = new WizardEvent(this);

        for (int i = 0; i < listeners.size(); i++) {
            (((WizardEventListener) listeners.get(i))).cancelPressed(evt);
        }
    }
}
